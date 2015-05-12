package br.com.marcosoft.apropriator.util;

import java.util.Random;


public class Cipher {
    private static int INTERLACE_OFFSET = 3;

    public static String interlace(String base, String str, int offset) {
        String ret="";
        for (int i=0,j=0; i<base.length(); i++) {
            ret += base.charAt(i);
            if ((i+1) % offset == 0) {
                ret += str.charAt(j++);
            }
        }
        return ret;
    }

    public static String uninterlace(String str, int offset) {
        String ret="";
        for (int i=offset; i<str.length(); i+=offset+1) {
            ret += str.charAt(i);
        }
        return ret;
    }

    public static String cript(String str) {
        final String shift = shift(str, 1);
        final String base = randomString(INTERLACE_OFFSET * str.length());
        final String interlace = interlace(base, shift, INTERLACE_OFFSET);
        return interlace;
    }

    public static String randomString(int len) {
        final Random random = new Random();
        String ret="";
        for (int i=0; i<len; i++) {
            ret += (char) (random.nextInt(95) + ' ');
        }
        return ret;
    }

    public static String uncript(String str) {
        final String striped = uninterlace(str, INTERLACE_OFFSET);
        return shift(striped, -1);
    }

    private static String shift(String str, int sinal) {
        String ret="";
        int offset = 1;
        for (int i=0; i<str.length(); i++) {
            ret += shift(str.charAt(i), offset * sinal);
            offset = offset % INTERLACE_OFFSET + 1;
        }
        return ret;
    }

    static char shift(char ch, int offset) {
        return (char) (ch + offset);
    }
}
