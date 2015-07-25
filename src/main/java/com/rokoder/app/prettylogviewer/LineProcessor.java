package com.rokoder.app.prettylogviewer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by havexz on 2/8/14.
 */
public class LineProcessor {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processXml(String xmlLineStr) {

        try {
            Document document = parseXmlFile(xmlLineStr);

            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            System.out.println(out.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private interface Callback {
        String convertToPrettyStr(String inputStr);
    }

    private String patternProcessor(String lineStr, Pattern pattern, Callback cb) {

        Matcher matcher = pattern.matcher(lineStr);
//        System.out.println(lineStr);
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        while (matcher.find()) {
            try {
                String startStr = lineStr.substring(0, matcher.start());
                sb.append(startStr);
                sb.append(LINE_SEPARATOR);
                String foundStr = matcher.group();

                String prettyStr = cb.convertToPrettyStr(matcher.group());
                sb.append(prettyStr);
                String endStr = lineStr.substring(matcher.end());
                if (endStr.length() > 0) {
                    sb.append(endStr);
                    sb.append(LINE_SEPARATOR);
                }
                found = true;
            } catch(Exception e) {
                e.printStackTrace();
                found = false;
                break;
            }
        }

        if (!found) {
            return null;
        }

        return sb.toString();
    }

    public String processLine(String lineStr) {
        boolean foundFlag = false;
        // Find json block
        Pattern jsonPattern = Pattern.compile("\\{.*\\}");

        StringBuilder sb = new StringBuilder();
        String outputStr;
        outputStr = patternProcessor(lineStr, jsonPattern, new Callback() {
            @Override
            public String convertToPrettyStr(String inputStr) {
                final Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Object obj = gson.fromJson(inputStr, Object.class);
                String jsonStr = gson.toJson(obj) + LINE_SEPARATOR;
                return jsonStr;
            }
        });

        if (outputStr != null) {
            sb.append(outputStr);
            outputStr = null;
            foundFlag = true;
        }

        Pattern xmlPattern = Pattern.compile("\\<.*\\>");

        outputStr = patternProcessor(lineStr, xmlPattern, new Callback() {
            @Override
            public String convertToPrettyStr(String inputStr) {
                try {
                    Document document = parseXmlFile(inputStr);

                    OutputFormat format = new OutputFormat(document);
                    format.setLineWidth(65);
                    format.setIndenting(true);
                    format.setIndent(2);
                    Writer out = new StringWriter();
                    XMLSerializer serializer = new XMLSerializer(out, format);
                    serializer.serialize(document);
                    return out.toString();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if (outputStr != null) {
            sb.append(outputStr);
            foundFlag = true;
        }
        if (!foundFlag) {
            sb.append(lineStr);
            sb.append(LINE_SEPARATOR);
        }

        return sb.toString();
    }

}
