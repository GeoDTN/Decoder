package biz.movia.utils;

public class StringUtil {

    /* Repeats a string n times.
       repeat("abc", 3) = "abcabcabc";
     */
    public static String repeat(String s, int n) {
        StringBuilder finalStr = new StringBuilder();
        for (int i=0; i<n; i++)
            finalStr.append(s);
        return finalStr.toString();
    }
}
