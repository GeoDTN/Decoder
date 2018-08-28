package biz.movia.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class RetentiveInputStream extends InputStream {

    private ByteBuffer lastReadBuffer = ByteBuffer.allocate(1024);

    private int readBytes = 0;

    private final InputStream is;

    public RetentiveInputStream(InputStream is) {
        this.is = is;
    }

    @Override
    public int read() throws IOException {

        int ret = is.read();

        if(ret == -1)
            return ret;

        if ( lastReadBuffer.position() >= lastReadBuffer.capacity() )
            lastReadBuffer = enlargeBuffer(lastReadBuffer, 1024);

        lastReadBuffer.put((byte)ret);

        readBytes+=1;

        return ret;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int ret = is.read(b, off, len);

        if(ret < 0)
            return ret;

        if ( lastReadBuffer.position() + ret > lastReadBuffer.capacity() )
            lastReadBuffer = enlargeBuffer(lastReadBuffer, ret+1024);

        lastReadBuffer.put(b, off, ret);

        readBytes += ret;

        return ret;
    }

    public byte[] getLastReadBytes() {
        byte[] buf = new byte[readBytes];

        int curPosition = lastReadBuffer.position();
        lastReadBuffer.position(0);

        lastReadBuffer.get(buf);

        lastReadBuffer.position(curPosition);

        return buf;
    }

    public void clearLastReadBuffer() {
        lastReadBuffer.clear();
        readBytes = 0;
    }

    private ByteBuffer enlargeBuffer(ByteBuffer oldBuffer, int additionalSize) {
        ByteBuffer newBuffer = ByteBuffer.allocate(oldBuffer.capacity() + additionalSize);
        int lastPosition = oldBuffer.position();
        oldBuffer.position(0);
        newBuffer.put(oldBuffer);
        newBuffer.position(lastPosition);
        oldBuffer.position(lastPosition);
        return newBuffer;
    }

}
