import utils.ByteUtil;
//import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Urmet {

    private static final int ID_DATE = 7;
    private static final int ID_TIME = 8;
    private static final int ID_CALLED_PARTY_NUMBER = 12;
    private static final int ID_CALLING_PARTY_NUMBER = 11;
    private static final int ID_CONNECTED_PARTY_NUMBER = 13;
    private static final int ID_SELECTED_NUMBER = 17;
    private static final int ID_CALL_DIRECTION = 18;
    private static final int ID_LOAD_BEARING_SERVICE = 19;
    private static final int ID_ADDITIONAL_SERVICE = 20;
    private static final int ID_ADDITIONAL_SERVICE_OPERATION = 21;
    private static final int ID_CALL_RESULT = 22;
    private static final int ID_SHORT_MESSAGE = 23;
    private static final int ID_USER_To_USER_MESSAGE = 24;



    public String date;
    public String time;
    public String calledPartyNumber;
    public String callingPartyNumber;
    public String connectedPartyNumber;
    public String selectedNumber;
    public int callDirection;
    public String loadBearingService;
    public String additionalService;
    public String additionalServiceOperation;
    public String callResult;
    public String shortMessage;
    public String userToUserMessage;


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
        else if (id == ID_CALLING_PARTY_NUMBER)
            processCallingPartyNumber(content);
        else if (id == ID_CONNECTED_PARTY_NUMBER)
            processConnectedPartyNumber(content);
        else if (id == ID_SELECTED_NUMBER)
            processSelectedNumber(content);
        else if (id == ID_CALL_DIRECTION)
            processCallDirection(content);
        else if (id == ID_LOAD_BEARING_SERVICE)
            processLoadBearingService(content);
        else if (id == ID_ADDITIONAL_SERVICE)
            processAdditionalService(content);
        else if (id == ID_ADDITIONAL_SERVICE_OPERATION)
            processAdditionalServiceOperation(content);
        else if (id == ID_CALL_RESULT)
            processCallResult(content);
        else if (id == ID_SHORT_MESSAGE)
            processShortMessage(content);
        else if (id == ID_USER_To_USER_MESSAGE)
            processUserToUserMessage(content);


    }



    private void processDate(byte[] content) {
        this.date = decodeBCD(content);
    }
    private void processTime(byte[] content) {
        this.time = decodeBCD(content);
    }
    private void processCalledPartyNumber(byte[] content) {
        this.calledPartyNumber = decodeBCD(content);
    }
    private void processCallingPartyNumber(byte[] content) {
        this.callingPartyNumber = decodeBCD(content);
    }

    private void processConnectedPartyNumber(byte[] content) {
        this.connectedPartyNumber = decodeBCD(content);
    }
    private void processSelectedNumber(byte[] content) {
        this.selectedNumber = decodeBCD(content);
    }
    private void processCallDirection(byte[] content) {
        this.callDirection = Byte.toUnsignedInt(content[0]);
    }
    private void processLoadBearingService(byte[] content) {
        this.loadBearingService = decodeBCD(content);
    }
    private void processAdditionalService(byte[] content) {
        this.additionalService = decodeBCD(content);
    }
    private void processAdditionalServiceOperation(byte[] content) {
        this.additionalServiceOperation = decodeBCD(content);
    }
    private void processCallResult(byte[] content) {
        this.callResult = decodeBCD(content);
    }
    private void processShortMessage(byte[] content) {
        this.shortMessage = new String(content, StandardCharsets.US_ASCII);
    }
    private void processUserToUserMessage(byte[] content) {
        this.userToUserMessage = new String(content, StandardCharsets.US_ASCII);
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
                ", calledPartyNumber =" + calledPartyNumber + '\n' +
                ", callingPartyNumber =" + callingPartyNumber + '\n' +
                ", connectedPartyNumber =" + connectedPartyNumber + '\n' +
                ", selectedNumber  =" + selectedNumber+ '\n' +
                ", callDirection   =" + callDirection+ '\n' +
                ", loadBearingService =" + loadBearingService+ '\n' +
                ", additionalService =" + additionalService+ '\n' +
                ", additionalServiceOperation =" + additionalServiceOperation+ '\n' +
                ", callResult =" + callResult+ '\n' +
                ", shortMessage =" + shortMessage+ '\n' +
                ", userToUserMessage =" + userToUserMessage+ '\n' +
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
