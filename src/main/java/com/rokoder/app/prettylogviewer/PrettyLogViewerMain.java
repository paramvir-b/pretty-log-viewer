package com.rokoder.app.prettylogviewer;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class PrettyLogViewerMain {

    private final String fileName;

    public PrettyLogViewerMain() {
        fileName = null;
    }

    public PrettyLogViewerMain(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String args[]) {

        if (args.length == 0) {
            PrettyLogViewerMain hlm = new PrettyLogViewerMain();
            hlm.process();
            return;
        }

        ArgumentParser parser = ArgumentParsers.newArgumentParser("pretty-log-viewer").
                defaultHelp(true).description("Convert embedded json/xml objects into human readable form");
        parser.addArgument("-f").help("Log file name");
        try {
            Namespace ns = parser.parseArgs(args);
            String inputFileName = ns.getString("f");

            PrettyLogViewerMain hlm = new PrettyLogViewerMain(inputFileName);
            hlm.process();
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }

    private Scanner createScanner() {
        Scanner sin = null;

        try {
            if (fileName == null || fileName.isEmpty()) {
                sin = new Scanner(System.in);
            } else {
                sin = new Scanner(new File(fileName));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return sin;
    }

    public void process() {
        Scanner sin = createScanner();
        Writer writer = new OutputStreamWriter(System.out);

        LogProcessor fp = new LogProcessor(sin, writer);
        fp.process();
    }

}
