package com.rokoder.app.prettylogviewer;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class XMLLineProcessorTest {

    @Test
    public void testXml() throws IOException {
        LineProcessor lp = new XMLLineProcessor();

        List<String> processedLineList;

        processedLineList = lp.processLine(
                "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] <hello><child>basic</child></hello>");
        Assert.assertEquals("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] ",
                processedLineList.get(0));
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<hello>\n" +
                "  <child>basic</child>\n" +
                "</hello>", processedLineList.get(1));

        processedLineList = lp.processLine(
                "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] <hello><child>basic</child></hello> some message");
        Assert.assertEquals("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] ",
                processedLineList.get(0));
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<hello>\n" +
                "  <child>basic</child>\n" +
                "</hello>", processedLineList.get(1));
        Assert.assertEquals(" some message", processedLineList.get(2));
    }

}
