package com.rokoder.app.prettylogviewer;

// FIXME REMOVE THIS CODE OTHER VERSION OF API WORKS ON MOST PLATFOMRS
//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLLineProcessor implements LineProcessor {

    private static final Pattern XML_LINE_PATTERN = Pattern.compile("<([a-zA-Z0-9_]+).*?</\\1>");

    @Override
    public boolean canProcess(String lineStr) {
        return XML_LINE_PATTERN.matcher(lineStr).find();
    }

    @Override
    public List<String> processLine(String lineStr) {
        Matcher matcher = XML_LINE_PATTERN.matcher(lineStr);
        ArrayList<String> outStrList = new ArrayList<String>();
        int startIdx = 0;
        int endIdx = lineStr.length() - 1;
        while (matcher.find()) {
            String startStr = lineStr.substring(startIdx, matcher.start());
            startIdx += matcher.end();
            endIdx = matcher.end();
            outStrList.add(startStr);

            String prettyStr = convertToPrettyStr(matcher.group());
            outStrList.add(prettyStr);
        }

        String endStr = lineStr.substring(endIdx);
        if (endStr.length() > 0) {
            outStrList.add(endStr);
        }


        return outStrList;
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Parsing failed for xml: " + in, e);
        } catch (SAXException e) {
            throw new RuntimeException("Parsing failed for xml: " + in, e);
        } catch (IOException e) {
            throw new RuntimeException("Parsing failed for xml: " + in, e);
        }
    }

    // FIXME REMOVE THIS CODE OTHER VERSION OF API WORKS ON MOST PLATFOMRS
//    private String convertToPrettyStrV2(String inputStr) {
//        try {
//            Document document = parseXmlFile(inputStr);
//
//            OutputFormat format = new OutputFormat(document);
//            format.setLineWidth(65);
//            format.setIndenting(true);
//            format.setIndent(2);
//            Writer out = new StringWriter();
//            XMLSerializer serializer = new XMLSerializer(out, format);
//            serializer.serialize(document);
//            return out.toString();
//        } catch (IOException e) {
//            throw new RuntimeException("Parsing failed for xml: " + inputStr, e);
//        }
//
//    }
//
    private String convertToPrettyStr(String inputStr) {
        try {
            Document document = parseXmlFile(inputStr);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            xmlString = xmlString.substring(0, xmlString.lastIndexOf('\n'));
            return xmlString;
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException("Parsing failed for xml: " + inputStr, e);
        } catch (TransformerException e) {
            throw new RuntimeException("Parsing failed for xml: " + inputStr, e);
        }
    }
}
