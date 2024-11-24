package org.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utils {

  public static String generateSHA265(String text) {
    String result = "";

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(text.getBytes());
      StringBuilder hexString = new StringBuilder();
      for (byte b : hashBytes) {
        hexString.append(String.format("%02x", b));
      }
      result = hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return result;
  }
}
