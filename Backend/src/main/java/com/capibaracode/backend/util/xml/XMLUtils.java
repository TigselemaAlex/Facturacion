package com.capibaracode.backend.util.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLUtils {

    public static XmlMapper xmlMapper = new XmlMapper();
    public static void createXML(InvoiceXMLModel xmlData, String accesKey){
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            Path xmlPath = Paths.get("xml").toAbsolutePath().normalize();
            if(!Files.exists(xmlPath)){
                Files.createDirectory(xmlPath);
            }
            Path xmlPathXml = xmlPath.resolve(accesKey + ".xml");
            FileOutputStream fos = new FileOutputStream(xmlPathXml.toString());
            StringWriter writer = new StringWriter();
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            xmlMapper.writeValue(writer, xmlData);
            String xml = writer.toString();
            fos.write(xml.getBytes());
            fos.close();
            System.out.println(xml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
