package com.rokoder.app.prettylogviewer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.OutputStreamAppender;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.io.PrintStream;

/**
 * Created by havexz on 1/31/14.
 */
@RunWith(JUnit4.class)
public class HumanLogMainTest {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HumanLogMainTest.class);

    private static Logger createLoggerFor(String string, String file) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
        fileAppender.setFile(file);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

        Logger logger = (Logger) LoggerFactory.getLogger(string);
        logger.addAppender(fileAppender);

        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }

    private static Logger createLoggerFor(String string, PipedOutputStream pipeOut) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();

        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();

        OutputStreamAppender<ILoggingEvent> osa = new OutputStreamAppender<ILoggingEvent>();
        osa.setName("h");
        osa.setEncoder(ple);
        osa.setOutputStream(new PrintStream(pipeOut));
        osa.setContext(lc);
        osa.start();
        Logger logger = (Logger) LoggerFactory.getLogger(string);
        logger.addAppender(osa);

        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false); /* set to true if root should log too */

        return logger;
    }

    @Test
    public void testJson() throws IOException {
        LOGGER.info("Hello");

        //HumanLogMain.main(new String[]{"-f", "src/test/resources/test_data/f1.log"});
        LineProcessor lp = new LineProcessor();

        String inputStr = "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] {hello : \"basic\"}";
        String processedLine = lp.processLine(inputStr);

        LOGGER.info(processedLine);
        Assert.assertEquals("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] \n" +
                "{\n" +
                "  \"hello\": \"basic\"\n" +
                "}\n", processedLine);

        processedLine = lp.processLine(
                "2014-01-31 19:53:40,375 INFO [main] t [HumanLogMainTest.java:48] some message {hello : \"basic\"}");
        Assert.assertEquals("2014-01-31 19:53:40,375 INFO [main] t [HumanLogMainTest.java:48] some message \n" +
                "{\n" +
                "  \"hello\": \"basic\"\n" +
                "}\n", processedLine);

    }

    @Test
    public void testXml() throws IOException {
        LOGGER.info("Hello");

        //HumanLogMain.main(new String[]{"-f", "src/test/resources/test_data/f1.log"});
        LineProcessor lp = new LineProcessor();

        String processedLine;

        processedLine = lp.processLine(
                "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] <hello><child>basic</child></hello>");
        Assert.assertEquals("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] \n" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<hello>\n" +
                "  <child>basic</child>\n" +
                "</hello>\n", processedLine);

        processedLine = lp.processLine(
                "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] <hello>basic</hello>");

        processedLine = lp.processLine(
                "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] <hello>\"basic\"</hello>");

    }

    @Test
    public void testXmlJson() {
        LineProcessor lp = new LineProcessor();

        String processedLine;

        //processedLine = lp.processLine("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] before <xml name=\"hello\"><child>basic</child></xml><xml name=\"hello\"><child>basic</child><child2>{\"array\":[1,2,3],\"boolean\":true,\"null\":null,\"number\":123,\"object\":{\"a\":\"b\",\"c\":\"d\",\"e\":\"f\"},\"string\":\"Hello World\"}</child2></xml> after2");
    }

    @Test
    public void test1() throws IOException {
        LOGGER.info("Hello");

        //HumanLogMain.main(new String[]{"-f", "src/test/resources/test_data/f1.log"});
        LineProcessor lp = new LineProcessor();

        String processedLine = lp.processLine(
                "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] {hello : \"basic\"}");

        Assert.assertEquals("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] \n" +
                "{\n" +
                "  \"hello\": \"basic\"\n" +
                "}\n", processedLine);

        processedLine = lp.processLine(
                "2014-01-31 19:53:40,375 INFO [main] t [HumanLogMainTest.java:48] some message {hello : \"basic\"}");
        Assert.assertEquals("2014-01-31 19:53:40,375 INFO [main] t [HumanLogMainTest.java:48] some message \n" +
                "{\n" +
                "  \"hello\": \"basic\"\n" +
                "}\n", processedLine);

        processedLine = lp.processLine(
                "2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] <hello><child>basic</child></hello>");
        Assert.assertEquals("2014-01-31 19:53:10,545 INFO [main] t [HumanLogMainTest.java:47] \n" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<hello>\n" +
                "  <child>basic</child>\n" +
                "</hello>\n", processedLine);

//        Pipe  dOutputStream pipeOut = new PipedOutputStream();
//
//        Logger logger1 = createLoggerFor("t", pipeOut);
//        logger1.info("{hello : \"basic\"}");
//        logger1.info("some message");

//        OutputStreamAppender<ILoggingEvent> appender = (OutputStreamAppender<ILoggingEvent>) logger1.getAppender("h");
//        InputStreamReader isr = new InputStreamReader(appender.getOutputStream());

//        PipedInputStream pipeIn = new PipedInputStream(pipeOut);
//        System.setOut(new PrintStream(pipeOut));
//
//        PushbackReader pr = new PushbackReader(new InputStreamReader(pipeIn));
//
//        int ch = -1;
//        while ((ch = pr.process()) != -1) {
//            LOGGER.info("ch = " + ch);
//        }


    }

    @Test
    public void testBasic() {
        HumanLogMain.main(new String[]{"-f", "src/test/resources/test_data/f1.log"});
    }
}

