package biz.movia.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
   This is a FileOutputStream that will call this.getFD().sync() after 'writesThreshold' writes have been done.
 */
public class SyncedFileOutputStream extends FileOutputStream {

    private final int writesThreshold;
    private int writesCount;

    public SyncedFileOutputStream(File file, int writesThreshold) throws FileNotFoundException {
        super(file);
        this.writesThreshold = writesThreshold;
    }

    public SyncedFileOutputStream(File file, boolean append, int writesThreshold) throws FileNotFoundException {
        super(file, append);
        this.writesThreshold = writesThreshold;
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        syncIfNeeded();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void write(byte b[]) throws IOException {
        super.write(b);
        syncIfNeeded();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void write(byte b[], int off, int len) throws IOException {
        super.write(b, off, len);
        syncIfNeeded();
    }

    private void syncIfNeeded() throws IOException {
        writesCount++;
        if (writesCount >= writesThreshold) {
            super.flush();
            super.getFD().sync();
            writesCount = 0;
        }
    }
}
