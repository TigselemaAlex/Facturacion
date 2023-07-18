package com.capibaracode.backend.util.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLUtils {

    public static XmlMapper xmlMapper = new XmlMapper();
    public static void createXML(InvoiceXMLModel xmlData, String accesKey){
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String xml = xmlMapper.writeValueAsString(xmlData);
            Path xmlPath = Paths.get("xml").toAbsolutePath().normalize().resolve(accesKey + ".xml");
            FileOutputStream fos = new FileOutputStream(xmlPath.toString());
            fos.write(xml.getBytes());
            fos.close();
            System.out.println(xml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
