package com.rokoder.app.prettylogviewer;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LogProcessorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogProcessorTest.class);

    @Test
    public void testBasic() throws IOException {

        StringWriter sw = new StringWriter();
        File inFile = new File("src/test/resources/test_data/in_d1.log");

        Scanner sin = new Scanner(inFile);

        LogProcessor lp = new LogProcessor(sin, sw);
        lp.process();

        List<String> expStringList = Files.readLines(new File("src/test/resources/test_data/out_d1_exp.log"),
                Charset.defaultCharset());
        List<String> actStringList = Arrays.asList(sw.toString().split("\n"));

        Assert.assertEquals(expStringList.size(), actStringList.size());

        for (int i = 0; i < actStringList.size(); i++) {
            String actStr = actStringList.get(i);
            String expStr = expStringList.get(i);
//            LOGGER.info("\nexpStr={}\nactStr={}", expStr, actStr);

            Assert.assertEquals(expStr, actStr);
        }
    }
}
