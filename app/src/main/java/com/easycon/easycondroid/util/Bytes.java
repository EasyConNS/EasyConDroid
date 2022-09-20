package com.easycon.easycondroid.util;

public class Bytes {
    public static String prettyPrint(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
