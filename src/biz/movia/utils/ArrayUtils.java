package biz.movia.utils;

public class ArrayUtils {

    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) >= 0;
    }

    public static <T> int indexOf(T[] array, T value) {
        for(int i = 0; i<array.length; i++) {
            if(array[i].equals(value))
                return i;
        }
        return -1;
    }
}
