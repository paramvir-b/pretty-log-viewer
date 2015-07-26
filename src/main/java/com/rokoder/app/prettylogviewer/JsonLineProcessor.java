package com.rokoder.app.prettylogviewer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by havexz on 7/25/15.
 */
public class JsonLineProcessor implements LineProcessor {
    private static final Pattern JSON_LINE_PATTERN = Pattern.compile("\\{.*\\}");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public boolean canProcess(String lineStr) {
        return JSON_LINE_PATTERN.matcher(lineStr).find();
    }

    private String convertToPrettyStr(String inputStr) {
        Object obj = gson.fromJson(inputStr, Object.class);
        String jsonStr = gson.toJson(obj);
        return jsonStr;
    }

    @Override
    public List<String> processLine(String lineStr) {
        Matcher matcher = JSON_LINE_PATTERN.matcher(lineStr);
        ArrayList<String> outStrList = new ArrayList<String>();
        while (matcher.find()) {
            try {
                String startStr = lineStr.substring(0, matcher.start());
                outStrList.add(startStr);
                String foundStr = matcher.group();

                String prettyStr = convertToPrettyStr(matcher.group());
                outStrList.add(prettyStr);
                String endStr = lineStr.substring(matcher.end());
                if (endStr.length() > 0) {
                    outStrList.add(endStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        return outStrList;
    }
}
