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
import java.util.List;

/**
 * Created by havexz on 1/31/14.
 */
@RunWith(JUnit4.class)
public class PrettyLogViewerMainTest {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PrettyLogViewerMainTest.class);

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
    public void test1() throws IOException {
        LOGGER.info("Hello");
// TODO SEE IF WE CAN GENERATE LOG AND PASS IT TO TEST CASE
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
        PrettyLogViewerMain.main(new String[]{"-f", "src/test/resources/test_data/in_d1.log"});
    }
}

