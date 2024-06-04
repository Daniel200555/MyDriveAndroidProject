package com.example.mydrive.format;

public class Format {

    public String formatFile(String name) {
        return switch (name) {
            case ".jpg", ".jpeg", ".png" -> "PICTURE";
            case ".mp4", ".mov", ".avi" -> "VIDEO";
            case ".mp3" -> "AUDIO";
            case ".txt" -> "DOCUMENT";
            default -> "NULL";
        };
    }

    public String getType(String name, char from) {
        int num = name.length();
        int temp = 0;
        String result = "";
        for (int i = 0; i < num; i++) {
            if (name.charAt(i) == from) {
                temp = i;
            }
        }
        for (int i = temp; i < num; i++)
            result += name.charAt(i);
        return result;
    }

    public boolean isFile(String name) {
        int num = name.length();
        for (int i = 0; i < num; i++) {
            if (name.charAt(i) == '.') {
                return true;
            }
        }
        return false;
    }

}
