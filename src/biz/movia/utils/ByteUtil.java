package biz.movia.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;

public class ByteUtil {

    public static class NotASCIIException extends Exception {}

    private static boolean isAscii(byte b) {
        return b >= 0; // b <= 127 is implicit since b is a signed integer of 8 bits.
    }

    private static boolean isAscii(byte[] B) {

        for (byte b : B)
            if ( ! isAscii(b) )
                return false;

        return true;
    }

    /*
       Converts the byte array B to an ASCII string and throws an exception
       if some bytes are not ASCII characters.
     */
    public static String toASCIIString(byte[] B) throws NotASCIIException {

        if ( ! isAscii(B) )
            throw new NotASCIIException();

        return new String(B, StandardCharsets.US_ASCII);
    }

    public static String toHex(byte[] B) {
        StringBuilder ret = new StringBuilder();

        for (byte b : B)
            ret.append(String.format("%02x", Byte.toUnsignedInt(b)));

        return ret.toString();
    }

    public static byte[] decodeHex(String hexString) throws ParseException, OddLengthStringException {

        if (hexString.length() % 2 != 0) throw new OddLengthStringException();

        int strLength = hexString.length();

        byte[] ret = new byte[strLength/2];

        for (int i = 0; i < strLength; i+=2) {
            String byteStr = hexString.substring(i, i+2);
            try {
                ret[i / 2] = (byte) Integer.parseInt(byteStr, 16);
            } catch(NumberFormatException e) {
                throw new ParseException(String.format("\"%s\" is not an hex string", byteStr), i);
            }
        }

        return ret;
    }

    public static int[] toUnsignedInt(byte[] data) {
        return toUnsignedInt(data, 0, data.length);
    }

    @SuppressWarnings("SameParameterValue")
    public static int[] toUnsignedInt(byte[] data, int offset, int len) {

        if (data == null) {
            throw new NullPointerException();
        } else if ((offset < 0) || (offset >= data.length) || (len < 0) ||
                ((offset + len) > data.length) || ((offset + len) < 0)) {
            throw new IndexOutOfBoundsException();
        }

        int[] ret = new int[len];

        for(int i=offset; i<offset+len; i++)
            ret[i] = Byte.toUnsignedInt(data[i]);

        return ret;
    }

    public static int getUint8(ByteBuffer bb) throws BufferUnderflowException {
        return Byte.toUnsignedInt(bb.get());
    }

    public static int getUint16(ByteBuffer bb) throws BufferUnderflowException {
        return Short.toUnsignedInt(bb.getShort());
    }

    public static byte[] concat(byte[] A, byte[] B) {
        byte[] AB = new byte[A.length+B.length];
        System.arraycopy(A, 0, AB, 0, A.length);
        System.arraycopy(B, 0, AB, A.length, B.length);
        return AB;
    }

    public static class OddLengthStringException extends Exception { }

    public static boolean contains(byte[] array, byte[] subarray) {
        if (array.length < subarray.length)
            return false;
        for (int i=0; i+subarray.length-1<array.length; i++) {
            boolean notMatching = false;
            for (int j = 0; j < subarray.length; j++) {
                if (array[i + j] != subarray[j]) {
                    notMatching = true;
                    break;
                }
            }
            if (!notMatching)
                return true;
        }

        return false;
    }

    /* The same of String.indexOf but for byte arrays. Returns -1 if the subarray is not found. */
    public static int indexOf(byte[] array, byte[] subarray) {
        if (array.length < subarray.length)
            return -1;

        int ret = -1;
        for (int i=0; i+subarray.length-1<array.length; i++) {
            boolean notMatching = false;
            for (int j=0; j<subarray.length; j++) {
                if (array[i+j] != subarray[j]) {
                    notMatching = true;
                    break;
                }

            }
            if (notMatching)
                continue;
            else {
                ret = i;
                break;
            }
        }

        return ret;
    }

    public static byte[] byteArrayFromInputStream(InputStream is) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = is.read(data)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

}
