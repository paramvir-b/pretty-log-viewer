package com.rokoder.app.prettylogviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
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

//        LineProcessorOld lp = new LineProcessorOld();

        while (inScanner.hasNextLine()) {
            String lineStr = inScanner.nextLine();
            boolean isFound = false;
            List<String> processedStrList = null;
            for (LineProcessor lp : lineProcessorList) {
                if (!isFound && lp.canProcess(lineStr)) {
                    processedStrList = lp.processLine(lineStr);
                    isFound = true;
                }
            }

            if (isFound) {
                writeLineList(processedStrList);
            } else {
                writeLine(lineStr);
            }

        }
    }

    public void process(Reader reader) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(reader);
            LineProcessorOld lp = new LineProcessorOld();

            String lineStr = null;
            while ((lineStr = br.readLine()) != null) {
                String processedLineStr = lp.processLine(lineStr);
                System.out.print(processedLineStr);
            }

            br.close();
        } catch (IOException e) {
            throw new IllegalStateException("IO failed", e);
        }

    }
}
