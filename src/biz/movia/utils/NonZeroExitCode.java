package biz.movia.utils;

/* Signals that an externally run program terminated with a non zero exit code */
public class NonZeroExitCode extends Exception {

    @SuppressWarnings("WeakerAccess")
    public final int exitCode;

    public NonZeroExitCode(int exitCode) {
        super("Non zero exit code "+exitCode);
        this.exitCode = exitCode;
    }
}
