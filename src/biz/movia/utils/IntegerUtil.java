package biz.movia.utils;

import java.util.ArrayList;

public class IntegerUtil {

    public static ArrayList<Integer> asArrayList(int[] I) {
        ArrayList<Integer> L = new ArrayList<>(I.length);
        for (int i : I)
            L.add(i);
        return L;
    }

}
