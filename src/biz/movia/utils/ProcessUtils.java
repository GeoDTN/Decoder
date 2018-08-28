package biz.movia.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/* This class wraps ProcessBuilder of Java. */
public class ProcessUtils {

    final private ProcessBuilder processBuilder;
    private File logFile;
    private File errLogFile;
    private boolean deleteLogFilesOnSuccess = true;

    public ProcessUtils(String... command) {
        processBuilder = new ProcessBuilder(command);
    }

    private void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public void setLogFile(File logFilePrefix, String suffix) {
        setLogFile(new File(logFilePrefix.getPath()+"."+suffix));
    }

    public void setErrLogFile(File logFilePrefix, String suffix) {
        this.errLogFile = new File(logFilePrefix.getPath()+"."+suffix);
    }

    public void setDeleteLogFilesOnSuccess(boolean value) {
        this.deleteLogFilesOnSuccess = value;
    }

    public File getErrLogFile() {
        return errLogFile;
    }

    public void setWorkingDir(File dir) {
        processBuilder.directory(dir);
    }

    public String getErrLogFileContent() throws IOException {
        return new String(Files.readAllBytes(getErrLogFile().toPath()), StandardCharsets.UTF_8);
    }

    /*
        It runs the process, writes the standard output and error to the log file (see setLogFile()).
        If the exit-value is zero (success), the log file is delete. Otherwise, the log file is kept and
        the exception NonZeroExitCode is raised.

        Note: before calling this method, setLogFile() must be called.
     */
    public void runAndLogOnError() throws IOException, InterruptedException, NonZeroExitCode {

        if (logFile == null) throw new NullPointerException("setLogFile() must be used before calling runAndLogOnError()");

        if (errLogFile == null)
            processBuilder.redirectErrorStream(true);
        else
            processBuilder.redirectError(errLogFile);
        processBuilder.redirectOutput(logFile);

        Process process = processBuilder.start();

        int exitValue = process.waitFor();

        if (exitValue == 0) { // ok. The program run without errors. Delete the log files.
            if (deleteLogFilesOnSuccess)
                deleteLogFiles();
        } else
            throw new NonZeroExitCode(exitValue);

    }

    public void deleteLogFiles() {
        if (logFile != null)
            //noinspection ResultOfMethodCallIgnored
            logFile.delete();
        if (errLogFile != null)
            //noinspection ResultOfMethodCallIgnored
            errLogFile.delete();
    }

}
