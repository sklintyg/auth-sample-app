# auth-sample-app
Example application for connecting to Inera IdP and exchanging a SAML certificate for an OAuth token

# placeholder
client_id=<placeholder>
client_secret=<placeholder>

rp identifier är samma som <placeholder> dvs client_id


1. SAML-autentisering går mot https://idp.ineratest.org/saml med SP som förregistrerats som SP och RP.
2. SAML AuthnReq Skall ha med två stycken extra audience restrictions:

    <rp identifier>
    https://idp.ineratest.org:443/oidc
    
    
3. Får SAML-biljett, plockar ut <Assertion>-delen, base64-kodar.

4. POST:ar
client_id=<client_id>&
client_secret=<client_secret>&
assertion=<base64-kodad-assertion>&
grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Asaml2-bearer 
  
till 
  
   https://idp.ineratest.org:443/oidc/token 
  
med 

   Content-Type application/x-www-form-urlencoded

5. Får tillbaka JWS på format 
  {
"access_token" : "asdfgh",
"refresh_token" : "zxcvbn",
"scope" : "",
"token_type" : "Bearer",
"expires_in" : 3600
}

6. Autentiserar användaren mot Webcert med access_token
7. Webcert kontrollerar signatur av JWS mot publik nyckel (https://idp.ineratest.org:443/oidc/jwks.json)?
8. Webcert anropar

    https://idp.ineratest.org:443/oidc/token/introspect 
    
med <token> för att verifiera dess äkthet. Denna endpoint kräver ej clientId/secret.

9. Webcert kontrollerar svaret och om OK fortsätter auktorisering och inloggning.

Klart
