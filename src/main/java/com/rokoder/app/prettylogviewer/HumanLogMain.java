package com.rokoder.app.prettylogviewer;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Created by havexz on 1/30/14.
 */
public class HumanLogMain {

    String fileName;

    public HumanLogMain(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String args[]) {

        ArgumentParser parser = ArgumentParsers.newArgumentParser("human-log").
                defaultHelp(true).description("Convert embedded json/xml objects into human readable form");
        parser.addArgument("-f").help("Log file name");
        try {
            Namespace ns = parser.parseArgs(args);
            String fileName = ns.getString("f");
            System.out.println(fileName + "");
            HumanLogMain hlm = new HumanLogMain(fileName);
            hlm.process();
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }

    public void process() {
        FileProcessor fp = new FileProcessor(fileName);
        fp.process();
    }

}
