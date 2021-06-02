package com.example.lqasocialmedia.util;

public class CommonUtils {
    public static String API_ROOT = "https://58588563e98f.ngrok.io";
    public static String getImageFullPath(String path) {
        return API_ROOT + "/images/" + path;
    }
}
