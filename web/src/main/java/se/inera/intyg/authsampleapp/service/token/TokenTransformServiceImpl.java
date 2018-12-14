package se.inera.intyg.authsampleapp.service.token;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Iterator;

@Component
public class TokenTransformServiceImpl implements TokenTransformService {

    @Override
    public String extractUsingRawStringMatcher(String samlMessage) {
        int firstIndex = samlMessage.indexOf("<saml2:Assertion");
        int lastIndex = samlMessage.lastIndexOf("</saml2:Assertion>") + "</saml2:Assertion>".length();
        return samlMessage.substring(firstIndex, lastIndex);
    }

    @Override
    public String extractAndEncode(String samlMessage) {

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document dDoc = builder.parse(new ByteArrayInputStream(samlMessage.getBytes(Charset.forName("UTF-8"))));
            XPath xPath = XPathFactory.newInstance().newXPath();

            NamespaceContext namespaceContext = new NamespaceContext() {
                public String getNamespaceURI(String prefix) {
                    switch (prefix) {
                    case "saml2":
                        return "urn:oasis:names:tc:SAML:2.0:assertion";
                    case "saml2p":
                        return "urn:oasis:names:tc:SAML:2.0:protocol";
                    case "ds":
                        return "http://www.w3.org/2000/09/xmldsig#";
                    default:

                    }
                    System.err.println("Unrecognized namespaceprefix: " + prefix);
                    return null;
                }

                public Iterator<String> getPrefixes(String val) {
                    System.err.println("getPrefixes: " + val);
                    return null;
                }

                public String getPrefix(String uri) {
                    System.err.println("getPrefix: " + uri);
                    return null;
                }
            };

            xPath.setNamespaceContext(namespaceContext);
            Node node = (Node) xPath.evaluate("//saml2:Assertion", dDoc, XPathConstants.NODE);
            String assertionXml = nodeToString(node);
            return base64Encode(assertionXml);
            //return Base64.getEncoder().withoutPadding().encodeToString(assertionXml.getBytes(Charset.forName("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "no");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    @Override
    public String base64Encode(String messageStr) {
        return org.opensaml.xml.util.Base64.encodeBytes(messageStr.getBytes(Charset.forName("UTF-8")), 8);
    }
}
