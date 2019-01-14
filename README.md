# auth-sample-app
Example application for connecting to Inera IdP and exchanging a SAML certificate for an OAuth token.

Based on Spring Framework 4 with Spring Security and the Spring SAML Extension

This application demonstrates the following:

### 1. Perform a SAML-based authentication vs Inera IdP with the requisite audience restrictions.
See _web/src/main/java/se/inera/intyg/authsampleapp/auth/SampleWebSSOProfileImpl.java_

The custom WebSSOProfile class adds extra audience restrictions, e.g:

     <saml2:AudienceRestriction>
         <saml2:Audience>https://idp.ineratest.org:443/saml</saml2:Audience>
         <saml2:Audience>https://url.to.the.serviceprovider</saml2:Audience>
         <saml2:Audience>journalsystemet</saml2:Audience> 
     </saml2:AudienceRestriction>
     
- https://idp.ineratest.org:443/saml: This is the EntityID of the IDP
- https://url.to.the.serviceprovider: This is the EntityID of the SP
- journalsystemet: This is the OIDC client_id for the journaling system

The the "journalsystemet" (or rather the actual value) client_id is assigned by the IdP to the SP.

### 2. Save the SAMLResponse and stores it as Base64 on the user session.
Since the RF7522 exchange requires the SP to present a SAML assertion issued by the IdP, we need to extract the SAMLResponse from the SAML authentication described above.

In the demo application, this is handled by the class _/web/src/main/java/se/inera/intyg/authsampleapp/auth/SampleSAMLAuthenticationProvider.java_ which subclasses the default SAMLAuthenticationProvider.

It is very important to extract and store the SAMLResponse "as-is" since even the slightest modification (type of line breaks, spacing, charset) may result in signature validation failing in the exchange. Therefore, the demo application takes the entire SAMLResponse Base64 and puts it into a byte array on the user object:

    ... omitted for brevity ...
    // Read the SAMLResponse from the InboundMessageTransport parameter value.
    byte[] samlResponseBase64 = ((HTTPInTransport) context.getInboundMessageTransport()).getParameterValue("SAMLResponse")
                            .getBytes(Charset.forName("UTF-8"));
    
    // Attach to principal
    ((UserModel) principal.getPrincipal()).setSamlAuthentication(samlResponseBase64);
    ... omitted for brevity ...

From here on, the SAMLResponse is available on our user principal, which we'll need in the next section.

### 3. Extract the <saml2:Assertion>
Again, we need to be really careful when extracting the Assertion element. For example, transforming the SAMLResponse to plain text, loading it into a DOM, using XPath to extract the Assertion and finally writing it to a new string, will almost certainly mess up line-breaks, spacing or formatting in such a way that signature validation won't pass.

In the demo application, we're using a brute-force approach where we search the SAMLResponse byte array for the index of the known sequence of bytes for "<saml2:Assertion>" and  "</saml2:Assertion>". Given these two indicies, the full Assertion element can be extracted from the SAMLResponse with tampering with any contents.

This is handled in the service class _/web/src/main/java/se/inera/intyg/authsampleapp/service/token/TokenTransformServiceImpl.java_

### 4. Send the Token Exchange request
To exchange our SAML assertion extracted above for an OAuth token using RFC7522, we need to execute a POST against the token exchange endpoint.

The demo application configures this URL in a property:

    token.exchange.endpoint.url=https://idp.ineratest.org/oidc/token
    
Your actual URL may vary depending on which Inera IdP your application needs to use.

The exchange request is a POST having the following characteristics:

**Content-Type:** application/x-www-form-urlencoded
**Four form parameters:**

client_id=<client id, e.g. same as "journalsystemet">
client_secret=<the password for "journalsystemet">
assertion=<the SAML assertion, Base64 encoded>
grant_type=urn:ietf:params:oauth:grant-type:saml2-bearer

**IMPORTANT!!!!** 
In addition to being base64-encoded, the Assertion must _also_ be URL-encoded _after_ being Base64-encoded. Do NOT confuse this with base64url-encoding which is something different.

The problem here _may_ be that some HTTP/REST-clients _automatically_ performs URL-encoding of form parameters if the _application/x-www-form-urlencoded_ Content-Type header is detected on the request.

The Spring RestTemplate _does_ this encoding by default as does POSTMan if you're trying this stuff offline. 
On the other hand, curl does not url-encode automatically unless parameters are specified using --data-urlencode. There's other REST-clients that does NOT perform automatic URL-encoding given the header such as the "Advanced REST client" (ARC) Chrome application.   
  
The code for this is handled in this class: _/web/src/main/java/se/inera/intyg/authsampleapp/service/token/TokenExchangeServiceImpl.java_:

    byte[] samlAssertion = tokenTransformService.extractSamlAssertion(samlResponse);
    byte[] samlAssertionAsBase64 = Base64.getEncoder().encode(samlAssertion);
    String assertion = new String(samlAssertionAsBase64, Charset.forName("UTF-8"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add(CLIENT_ID, clientId);
    map.add(CLIENT_SECRET, clientSecret);
    map.add(GRANT_TYPE, BEARER_VALUE);
    map.add(ASSERTION, assertion);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    ResponseEntity<String> response = restTemplate.postForEntity(tokenExchangeEndpointUrl, request, String.class);
    return response.getBody();
    

### 5. Handle the response.
The response from the token exchange is a JSON document containing the actual access_token we're going to use for authenticating the application against Webcert:

    {
        "access_token" : "asdfghgskgfddfhd....",
        "refresh_token" : "zxcvbn............",
        "scope" : "",
        "token_type" : "Bearer",
        "expires_in" : 3600
    }
In the _/web/src/main/java/se/inera/intyg/authsampleapp/web/controller/UserController.java_, we're storing the access_token and the refresh_token in the session.

The way to proceed for submitting the access_token to Webcert from here may vary. The overall objective is to read the Set-Cookie: SESSION=..... from the HTTP response from Webcert, that the browser can store and use for subsequent calls to Webcert.

##### 5.1 FORM POST
The example application POSTs a standard HTML form to _https://path.to.webcert/oauth/token_ with content-type _application/x-www-form-urlencoded_ with the _access_token_ along with the already known "integration parameters" as form parameters.

TODO show form code

TBD In the demo application, we're submitting the intygs-id (the resource we want to access in Webcert) using a form parameter while the URL is https://path.to.webcert/oauth/token

##### 5.2 Authorization bearer header / XHR
Given that the demo application / journaling system are running on another domain than Webcert, we need to pay attention to Cross-Origin Resource Sharing (CORS) and whether SESSION cookies set on a CORS-enabled XHR request to another domain may be rejected by the browser. 

Please refer to https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS?redirectlocale=en-US&redirectslug=HTTP_access_control#Requests_with_credentials on how to use XHR with credentials in a cross-domain scenario.

At this time, we recommend **not** pursuing this route since we havn't been able to test this properly in a cross-domain scenario. Using XHR + Bearer on the same domain works fine though, but that should be of limited use for systems integrating with Webcert.

Another variant is to let Webcert listen to /visa/intyg/{intygsId}/oauth or similar where the intygs-id is part of the PATH rather than specified as a form parameter.



### 6. Refresh token
Once the access_token has expired, a new one can be obtained using the refresh_token.