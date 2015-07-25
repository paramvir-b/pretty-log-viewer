package com.rokoder.app.prettylogviewer;

import java.io.*;

public class FileProcessor {
    String fileName;

    public FileProcessor(String fileName) {
        this.fileName = fileName;
    }

    public void process() {

        try {
            FileReader fr = new FileReader(fileName);

            process(fr);
            fr.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found : " + fileName, e);
        } catch (IOException e) {
            throw new IllegalStateException("IO failed", e);
        }


    }

    public void process(Reader reader) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(reader);
            LineProcessor lp = new LineProcessor();

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
