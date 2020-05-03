package com.natlight.mobilenewsapp.utils;

public class ImageUtils {
    public static String getImageName(String title) {
        return title.replace(' ', '_').toLowerCase();
    }
}
