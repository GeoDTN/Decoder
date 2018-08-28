package biz.movia.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

/*
   Reads a sequence of objects previously serialized with ObjectSeqOutputStream.
   This class can be instantiated with a given type if all the objects have the same type. In this case,
   readTypedObject() must be used.
   The methods readTypedObject() and readObject() return an ObjectOrEOF object oof.
   oof.eof is true if the EndOfFileSignal has been reached, and no more objects are present
   in the stream. Afterwards, no more reads shall be attempted. If oof.eof is false, oof.object
   contains the object read from the stream.

   Note well, if you want to use readTypedObject(), you must use the ObjectSeqInputStream(InputStream in, Class<T> type)
   constructor, where type is the class object of your class. For example, if your class is MyObject, its class object
   is given by MyObject.class
 */
public class ObjectSeqInputStream<T> implements AutoCloseable {

    private final ObjectInputStream ois;
    private final Class<T> type;

    public ObjectSeqInputStream(InputStream in, Class<T> type) throws IOException {
        this.ois = new ObjectInputStream(in);
        this.type = Objects.requireNonNull(type);
    }

    public ObjectSeqInputStream(InputStream in) throws IOException {
        this.ois = new ObjectInputStream(in);
        this.type = null;
    }

    public ObjectOrEOF<T> readTypedObject() throws IOException, ClassNotFoundException, ClassCastCheckedException {

        if (type == null) throw new NullPointerException(); // No type has been specified during construction.
        // Use the ObjectSeqInputStream(InputStream, Class<T>) constructor.

        ObjectOrEOF<Object> objOrEOF = readObject();

        if (objOrEOF.eof)
            return new ObjectOrEOF<>(null, true);

        if (objOrEOF.object == null || type.isInstance(objOrEOF.object))
            return new ObjectOrEOF<>(type.cast(objOrEOF.object), false);

        throw new ClassCastCheckedException();
    }

    @SuppressWarnings("WeakerAccess")
    public ObjectOrEOF<Object> readObject() throws IOException, ClassNotFoundException {
        Object obj = ois.readObject();
        if (obj instanceof EndOfFileSignal)
            return new ObjectOrEOF<>(null, true);
        else
            return new ObjectOrEOF<>(obj, false);
    }

    /*
       this.eof is true if an instance of EndOfFileSignal has been read from the stream,
       and thus no more objects are present in the stream.
       Otherwise, this.object contains the object read from the stream.
     */
    public static class ObjectOrEOF<T> {
        public final T object;
        public final boolean eof;

        ObjectOrEOF(T object, boolean eof) {
            this.object = object;
            this.eof = eof;
        }
    }

    public void close() throws IOException {
        ois.close();
    }

}
