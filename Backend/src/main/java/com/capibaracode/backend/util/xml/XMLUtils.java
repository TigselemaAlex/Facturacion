package com.capibaracode.backend.util.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtils {

    public static XmlMapper xmlMapper = new XmlMapper();
    public static String createXML(InvoiceXMLModel xmlData, String accesKey){
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            /*Path xmlPath = Paths.get("xml").toAbsolutePath().normalize();
            if(!Files.exists(xmlPath)){
                Files.createDirectory(xmlPath);
            }
            Path xmlPathXml = xmlPath.resolve(accesKey + ".xml");
            FileOutputStream fos = new FileOutputStream(xmlPathXml.toString());*/
            StringWriter writer = new StringWriter();
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            xmlMapper.writeValue(writer, xmlData);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
