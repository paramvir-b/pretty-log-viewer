package com.rokoder.app.prettylogviewer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogProcessor {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final Scanner inScanner;
    private final Writer outWriter;
    private final List<LineProcessor> lineProcessorList = new ArrayList<LineProcessor>();

    public LogProcessor(Scanner inScanner, Writer outWriter) {
        this.inScanner = inScanner;
        this.outWriter = outWriter;

        lineProcessorList.add(new JsonLineProcessor());
        lineProcessorList.add(new XMLLineProcessor());
    }

    public LogProcessor(Scanner inScanner, Writer outWriter, LineProcessor[] lineProcessorArray) {
        this.inScanner = inScanner;
        this.outWriter = outWriter;

        for (LineProcessor lp : lineProcessorArray) {
            lineProcessorList.add(lp);
        }
    }


    private void write(String str) {
        try {
            outWriter.write(str);
            outWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("IOException occurred", e);
        }

    }

    private void writeLine(String str) {
        write(str);
        write(LINE_SEPARATOR);
    }

    private void writeLineList(List<String> strList) {
        for (String str : strList) {
            writeLine(str);
        }
    }


    public void process() {

        while (inScanner.hasNextLine()) {
            String lineStr = inScanner.nextLine();
            boolean isFound = false;
            List<String> processedStrList = null;
            try {
                for (LineProcessor lp : lineProcessorList) {
                    if (!isFound && lp.canProcess(lineStr)) {
                        processedStrList = lp.processLine(lineStr);
                        isFound = true;
                    }
                }
            } catch (RuntimeException e) {
                StackTraceElement[] stackTrace = e.getStackTrace();
                for (StackTraceElement ste : stackTrace) {
                    writeLine("PLV-EXP[" + ste + "]PLV-EXP");
                }

                // WE IGNORE THE EXCEPTION AND OUTPUT THE STRING AS IS
                isFound = false;
            }

            if (isFound) {
                writeLineList(processedStrList);
            } else {
                writeLine(lineStr);
            }

        }
    }
}
