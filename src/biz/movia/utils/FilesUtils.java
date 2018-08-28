package biz.movia.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class FilesUtils {

    /* Moves the file 'from' to file 'to', replacing the 'to' file if it already exists. */
    @SuppressWarnings("WeakerAccess")
    public static void move(File from, File to) throws IOException {
        Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /* Moves the file 'from' to directory 'toDir' */
    public static void moveToDir(File from, File toDir) throws IOException {
        move(from, new File(toDir, from.getName()));
    }

    /* Like the move() method, however, it performs an intermediate move:
         mv from to.tmp
         mv to.tmp to
       In this way, if 'from' and 'to' are in different partitions, the first move will perform a copy. The .tmp
       extension will tell external processes to ignore the file while the copy is in progress. The final move,
       will make available the file. So, the end result is that twos moves behaves as a single move even if
       'from' an 'to are on different partitions.
    */
    private static void moveWithTmpIntermediate(File from, File to) throws IOException {
        File toTmp = new File(to.getPath()+".tmp");
        move(from, toTmp);
        move(toTmp, to);
    }

    /* Like the moveToDir() method, but with an intermediate move, see moveWithTmpIntermediate() */
    public static void moveToDirWithTmpIntermediate(File from, File toDir) throws IOException {
        moveWithTmpIntermediate(from, new File(toDir, from.getName()));
    }

    /* Creates the directory 'dir' if it does not exist */
    public static void createDir(File dir) throws IOException {
        if (!dir.exists())
            if (!dir.mkdirs())
                throw new IOException("Cannot create directory " + dir.getAbsolutePath());
    }


    /* Returns true if the two files have the same content. Warning: the comparison is inefficient: all the files'
       content is loaded in memory
     */
    public static boolean inefficientComparison(File file1, File file2) throws IOException {
        byte[] buf1 = Files.readAllBytes(file1.toPath());
        byte[] buf2 = Files.readAllBytes(file2.toPath());
        return Arrays.equals(buf1, buf2);
    }

    /* It replaces the extension of the file 'fileName' with the extension 'newExtension'. It returns the filename
       with the new extension.
       Assumption: fileName must have an extension, for example, "song.mp3". An extension, is a final substring
       beginning with a dot (".") and not containing any dots.
     */
    public static String replaceExtension(String fileName, String newExtension) {
        return fileName.replaceAll("\\.[^.]*$", "."+newExtension);
    }
}
