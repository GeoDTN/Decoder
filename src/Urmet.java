import biz.movia.utils.ByteUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Urmet {

    private static final int ID_DATE = 7;
    private static final int ID_TIME = 8;
    private static final int ID_CALLED_PARTY_NUMBER = 12;

    public String date;
    public String time;
    public String calledPartyNumber;

    static private String bcdCharsString = "0123456789 .-#*!";
    static private char[] bcdChars = bcdCharsString.toCharArray();


    public static Urmet decode(byte[] dataBytes) throws InvalidData {

        Urmet urmet = new Urmet();

        for(int offset = 0; offset < dataBytes.length;) {
            offset += urmet.decodeBlock(Arrays.copyOfRange(dataBytes, offset, dataBytes.length));
        }

        return urmet;
    }

    // returns the block length
    public int decodeBlock(byte[] dataBytes) throws InvalidData {


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

        processContent(extendedVocabulary, id, content);

        return headerLength+length;
    }

    private void processContent(boolean extended, int id, byte[] content) {
        if ( ! extended )
            processBasicContent(id, content);
        else
            processExtendedContent(id, content);
    }

    private void  processExtendedContent(int id, byte[] content) {
    }

    private void processBasicContent(int id, byte[] content) {


        if (id == ID_DATE)
            processDate(content);
        else if(id == ID_TIME)
            processTime(content);
        else if (id == ID_CALLED_PARTY_NUMBER)
            processCalledPartyNumber(content);

    }

    private void processCalledPartyNumber(byte[] content) {
        this.calledPartyNumber = decodeBCD(content);
    }

    private void processTime(byte[] content) {
        this.time = decodeBCD(content);
    }

    private void processDate(byte[] content) {
        this.date = decodeBCD(content);
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

    @Override
    public String toString() {
        return "Urmet{" +
                "date='" + date + '\'' + '\n' +
                ", time='" + time + '\'' + '\n' +
                ", calledPartyNumber " + calledPartyNumber + '\n' +
                '}';
    }

    public static void main(String[] args) throws IOException{
        for(String file : args) {

            System.out.println("Decoding "+file);
            try {
                Urmet urmet = Urmet.decode(Files.readAllBytes(Paths.get(file)));
                System.out.println(urmet);
            } catch (InvalidData invalidData) {
                invalidData.printStackTrace();
            }
            System.out.println();

        }
    }
}
