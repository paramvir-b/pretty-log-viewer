package com.rokoder.app.prettylogviewer;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class JsonLineProcessorTest {

    @Test
    public void testJson() throws IOException {
        LineProcessor lp = new JsonLineProcessor();

        String inputStr = "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] {hello : \"basic\"}";
        List<String> processedLineList = lp.processLine(inputStr);

        Assert.assertEquals("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] ",
                processedLineList.get(0));
        Assert.assertEquals("{\n" +
                "  \"hello\": \"basic\"\n" +
                "}", processedLineList.get(1));

        processedLineList = lp.processLine(
                "2014-01-31 19:53:40,375 INFO [main] t [HumanLogMainTest.java:48] {hello : \"basic\"} some message ");
        Assert.assertEquals("2014-01-31 19:53:40,375 INFO [main] t [HumanLogMainTest.java:48] ",
                processedLineList.get(0));
        Assert.assertEquals("{\n" +
                "  \"hello\": \"basic\"\n" +
                "}", processedLineList.get(1));
        Assert.assertEquals(" some message ", processedLineList.get(2));

    }

}
