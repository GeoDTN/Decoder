import biz.movia.utils.ByteUtil;

import java.util.Arrays;

public class Urmet {

    public String date;
    public String time;

    static private String bcdCharsString = "0123456789 .-#*!";
    static private char[] bcdChars = bcdCharsString.toCharArray();


    public Urmet decode(byte[] dataBytes) throws InvalidData {

        int[] data = ByteUtil.toUnsignedInt(dataBytes);

        if (data.length < 2)
            throw new InvalidData("data must have at least two bytes");

        boolean extendedVocabulary = (data[0] >> 7) == 1;

        boolean len16bit = ((data[0] >> 6) & 1) == 1; // if true, "len" is 16 bits, if false, 8 bits.

        int id = data[0] & 0b111111;

        int lenLow = data[1];

        if (len16bit && data.length < 3)
            throw new InvalidData("data must have at least three bytes");

        int length;

        int headerLength;

        if (len16bit) {

            int lenHi = data[2];

            length = (lenHi << 8) | lenLow;

            headerLength = 3;

        } else {

            length = data[1];

            headerLength = 2;

        }

        if (headerLength+length > dataBytes.length)
            throw new InvalidData("Not enough bytes in dataBytes");

        byte[] content = Arrays.copyOfRange(dataBytes, headerLength, headerLength+length);


        return processContent(extendedVocabulary, id, content);

    }

    private Urmet processContent(boolean extended, int id, byte[] content) {
        if ( ! extended )
            return processBasicContent(id, content);
        else
            return processExtendedContent(id, content);
    }

    private Urmet processExtendedContent(int id, byte[] content) {
       return null;
    }

    private Urmet processBasicContent(int id, byte[] content) {


        return null;
    }


    public class InvalidData extends Exception {
        public InvalidData(String message) {
            super(message);
        }
    }


    String decodeBCD(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte bb : data) {
            int b = Byte.toUnsignedInt(bb); // b = bb & 0xff
            stringBuilder.append( bcdChars[ b >> 4 ] );
            stringBuilder.append( bcdChars[ b & 0x0f ] );
        }
        return stringBuilder.toString();
    }
}
