package com.example.restaurantsoftware.util;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ImageUtils {
    public static String encodeToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}