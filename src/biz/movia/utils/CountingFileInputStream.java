package biz.movia.utils;

import org.apache.commons.io.input.CountingInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CountingFileInputStream extends CountingInputStream {

    private final long fileLength;

    public CountingFileInputStream(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
        fileLength = file.length();
    }

    public CountingFileInputStream(String filename) throws FileNotFoundException {
        this(filename != null ? new File(filename) : null);
    }

    /* Returns the number of bytes not yet read from the file */
    public long remaining() {
        return fileLength - getByteCount();
    }

}
