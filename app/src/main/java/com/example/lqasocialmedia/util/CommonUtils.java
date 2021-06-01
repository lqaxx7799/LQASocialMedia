package com.example.lqasocialmedia.util;

public class CommonUtils {
    public static String API_ROOT = "https://92d0d4827dcb.ngrok.io";
    public static String getImageFullPath(String path) {
        return API_ROOT + "/images/" + path;
    }
}
