package com.rokoder.app.prettylogviewer;

import java.util.List;

/**
 * Created by havexz on 7/25/15.
 */
public interface LineProcessor {

    boolean canProcess(String lineStr);

    List<String> processLine(String lineStr);
}
