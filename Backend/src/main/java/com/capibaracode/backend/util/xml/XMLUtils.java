package com.capibaracode.backend.util.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.ElementProxy;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collections;

public class XMLUtils {

    private static Path p12 = Paths.get("xml").toAbsolutePath().normalize().resolve("test.p12");
    private static String p12Password = "2006Andrea";

    public static XmlMapper xmlMapper = new XmlMapper();

    public static String createXML(InvoiceXMLModel xmlData, String accesKey) {
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            Path xmlPath = Paths.get("xml").toAbsolutePath().normalize();
            if (!Files.exists(xmlPath)) {
                Files.createDirectory(xmlPath);
            }
            Path xmlPathXml = xmlPath.resolve(accesKey + ".xml");
            FileOutputStream fos = new FileOutputStream(xmlPathXml.toString());

            StringWriter writer = new StringWriter();
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            xmlMapper.writeValue(writer, xmlData);
            fos.write(writer.toString().getBytes());
            fos.close();
            //signature(xmlPathXml, accesKey);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);

            String xmlContent = writer.toString();
            byte[] base64Bytes = Base64.getEncoder().encode(xmlContent.getBytes());
            String content = constructSOAPRequest(new String(base64Bytes));

            HttpEntity<String> requestEntity = new HttpEntity<>(content, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl", requestEntity, String.class);

            System.out.println("Response Status Code: " + response.getStatusCodeValue());
            System.out.println("Response Body: " + response.getBody());
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String constructSOAPRequest(String base64EncodedXML) {
        // ... (construct your SOAP request with the Base64 encoded XML here)
        String soapRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ec=\"http://ec.gob.sri.ws.recepcion\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <ec:validarComprobante>\n" +
                "            <xml>\n" +
                                base64EncodedXML +
                "            </xml>\n" +
                "        </ec:validarComprobante>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        return soapRequest;
    }

    private static void signature(Path xmlPath, String accesKey) throws Exception {

        // Initialize the XML Security library
        org.apache.xml.security.Init.init();

        //Security.addProvider(new BouncyCastleProvider());

        // Load the .p12 file
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(p12.toString()), p12Password.toCharArray());

        // Get the private key and certificate from the key store
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, p12Password.toCharArray());
        X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

        // Load the XML document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(xmlPath.toFile()));

        // Create the XMLSignature element
        ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "");
        XMLSignature sig = new XMLSignature(doc, "", XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256);
        Element root = doc.getDocumentElement();
        doc.getDocumentElement().appendChild(sig.getElement());

        // Create the Transforms and add them to the signature
        Transforms transforms = new Transforms(doc);
        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
        sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

        // Load the signing key and certificate into the signature
        sig.addKeyInfo(cert.getPublicKey());
        sig.addKeyInfo(cert);
        sig.sign(privateKey);

        // Sign the XML data
        //byte[] signedData = signData(xmlData, privateKey, cert);

        // Save the signed data to a file
        FileOutputStream signedFileOutputStream = new FileOutputStream(xmlPath.toString());
        org.apache.xml.security.utils.XMLUtils.outputDOMc14nWithComments(doc, signedFileOutputStream);
        signedFileOutputStream.close();




    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    private static String getXMLContent(String xmlFilePath) {
        String content = null;

        try {
            Path path = Paths.get(xmlFilePath);
            byte[] bytes = Files.readAllBytes(path);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public static void addDSNamespacePrefix(String xmlFilePath) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(xmlFilePath));

        NodeList nodes = doc.getElementsByTagNameNS(Constants.SignatureSpecNS, "*");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            String localName = element.getLocalName();
            element.setPrefix("ds");
            element.setNodeValue("ds:" + localName);
        }

        // Save the modified XML to the same file
        FileOutputStream fos = new FileOutputStream(xmlFilePath);
        org.apache.xml.security.utils.XMLUtils.outputDOMc14nWithComments(doc, fos);
        fos.close();
    }

    private static byte[] signData(byte[] xmlData, PrivateKey privateKey, X509Certificate cert) throws CertificateEncodingException, CMSException, OperatorCreationException, IOException {
        // Create the CMS signed data generator
        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        // Prepare the certificate chain
        X509CertificateHolder certHolder = new JcaX509CertificateHolder(cert);
        Store<X509CertificateHolder> certs = new JcaCertStore(Collections.singletonList(certHolder));
        generator.addCertificates(certs);

        // Prepare the private key and signer info
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);
        X509CertificateHolder[] certChain = {certHolder};
        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().build()).build(contentSigner, cert));


        // Create the CMS signed data
        CMSTypedData cmsData = new CMSProcessableByteArray(xmlData);
        CMSSignedData signedData = generator.generate(cmsData, true);

        // Return the signed data
        return signedData.getEncoded();
    }

    public static Document parseXML(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier excepción aquí
        }
        return null;
    }

    public static String documentToString(Document document) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar cualquier excepción aquí
        }
        return null;
    }

}
