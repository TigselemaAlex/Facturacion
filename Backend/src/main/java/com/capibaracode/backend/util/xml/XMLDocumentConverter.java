package com.capibaracode.backend.util.xml;

import jakarta.persistence.AttributeConverter;
import org.w3c.dom.Document;

public class XMLDocumentConverter implements AttributeConverter<Document, String> {

    @Override
    public String convertToDatabaseColumn(Document document) {
        return document != null ? XMLUtils.documentToString(document) : null;
    }

    @Override
    public Document convertToEntityAttribute(String xmlString) {
        return xmlString != null ? XMLUtils.parseXML(xmlString) : null;
    }
}
