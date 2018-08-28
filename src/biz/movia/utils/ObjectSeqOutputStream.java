package biz.movia.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/*
   Serializes and writes a sequence of objects. When the stream is closed, an instance of EndOfFileSignal is also
   written to mark the end of the stream.
   Use ObjectSeqInputStream to read and deserialize the written objects.
 */
public class ObjectSeqOutputStream implements AutoCloseable {
    private final ObjectOutputStream oos;

    public ObjectSeqOutputStream(OutputStream os) throws IOException {
        this.oos = new ObjectOutputStream(os);
    }

    public void writeObject(Object obj) throws IOException {
        oos.writeObject(obj);
    }

    public void close() throws IOException {
        oos.writeObject(new EndOfFileSignal());
        oos.close();
    }
}
