package com.bravos2k5.bravosshop.utils;

import java.util.Random;

public class RandomUtils {

    public final static Random random = new Random();

    public static String randomString(int length) {
        char[] s = "qwertyuiopasdfghjklzxcvbnm1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(s.length) - 1;
            stringBuilder.append(s[index]);
        }
        return stringBuilder.toString();
    }

}
