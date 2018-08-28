package biz.movia.utils;

import java.nio.ByteBuffer;

public class ByteBufferUtil {

    // put the remaining bytes from src into dst. If this would overflow dst, put as much
    // as you can. In other words, put only dst.remaining() bytes.
    public static void putButDoNotOverflow(ByteBuffer src, ByteBuffer dst) {

        if (src.remaining() > dst.remaining()) { // first check if 'dst' has enough room
            // No. It has not. So, put as much as you can but not more. This is accomplished as follow:
            // to lower src.remaining() to dst.remaining() lower the limit of src. Use a readonly copy of src to avoid
            // changing the limit of src.
            int surplus = src.remaining() - dst.remaining();
            src = src.asReadOnlyBuffer();
            src.limit(src.limit()-surplus);
            // now src.remaning() is equal to dst.remaining()
        }

        dst.put(src.slice());

    }

}
