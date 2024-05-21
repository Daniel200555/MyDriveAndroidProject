package com.example.mydrive.redgex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check {

    public static boolean checkPassword(String text) {
        String rangeLengthRegex = "^.{6,20}$";
        Pattern rangePattern = Pattern.compile(rangeLengthRegex);
        return matchesPattern(rangePattern, text);
    }

    public static boolean matchesPattern(Pattern pattern, String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

}
