package biz.movia.utils;

import java.io.IOException;
import java.util.logging.*;

/*
   Wraps the Logger class
 */
@SuppressWarnings("unused")
public class Log {

    public static class BetterLevel extends Level {

        BetterLevel(String name, int code) {
            super(name, code);
        }

        public static final BetterLevel FATAL = new BetterLevel("FATAL", 980);
        public static final BetterLevel ERROR = new BetterLevel("ERROR", 950);
        public static final BetterLevel WARNING = new BetterLevel("WARNING", 900);
        public static final BetterLevel INFO    = new BetterLevel("INFO", 800);
        public static final BetterLevel DEBUG   = new BetterLevel("DEBUG", 200);
        public static final BetterLevel VERBOSE = new BetterLevel("VERBOSE", 100);
    }

    private final Logger log;

    public Log(Class c) {
        log = Logger.getLogger(c.getName());
    }

    public void verbose(String text) {
        log.log(Log.BetterLevel.VERBOSE, text);
    }

    public void verbose(String text, Throwable tr) {
        log.log(Log.BetterLevel.VERBOSE, text, tr);
    }


    public void debug(String text) {
        log.log(Log.BetterLevel.DEBUG, text);
    }

    public void debug(String text, Throwable tr) {
        log.log(Log.BetterLevel.DEBUG, text, tr);
    }

    public void info(String text) {
        log.log(Log.BetterLevel.INFO, text);
    }

    public void info(String text, Throwable tr) {
        log.log(Log.BetterLevel.INFO, text, tr);
    }



    public void warning(String text) {
        log.log(Log.BetterLevel.WARNING, text);
    }

    public void warning(String text, Throwable tr) {
        log.log(Log.BetterLevel.WARNING, text, tr);
    }

    public void error(String text) {
        log.log(Log.BetterLevel.ERROR, text);
    }

    public void error(String text, Throwable tr) {
        log.log(Log.BetterLevel.ERROR, text, tr);
    }

    public void fatal(String text) {
        log.log(Log.BetterLevel.FATAL, text);
    }

    public void fatal(String text, Throwable tr) {
        log.log(Log.BetterLevel.FATAL, text, tr);
    }

    /*
    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }*/


    /*
     * Set 'packageName' to your application's package name. For example,
     * "org.project.myapp".
     * Then use
     *     private final static Log log = new Log(MyClass.class);
     * in your class MyClass.
     *
     * If 'logpath' is not null, logs will be appended to the file with path 'logpath'.
     *
     * If 'stdout' is true, logs to stdout too.
     *
     * If 'debug' is true, debug logging level will be enabled.
     */
    @SuppressWarnings("SameParameterValue")
    public static void setLogging(String packageName, String logpath, boolean stdout, boolean debug) throws IOException {

        // See SimpleFormatter.format() documentation
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tD %1$tT: %4$s: %5$s %6$s%n");

        // Remove default ConsoleHandler
        Logger rootLogger = Logger.getLogger("");
        for(Handler handler : rootLogger.getHandlers()) {
            if (handler.getClass().equals(java.util.logging.ConsoleHandler.class))
                rootLogger.removeHandler(handler);
        }

        Logger appLogger = Logger.getLogger(packageName);

        // log to file 'logPath'
        Handler fh = null;
        if(logpath != null) {
            fh = new FileHandler(logpath, 200*1000*1000, 100, true);
            fh.setFormatter(new SimpleFormatter());
            appLogger.addHandler(fh);
        }

        StreamHandler sh = null;
        if (stdout) {
            // log also to stdout and flush every time
            sh = new StreamHandler(System.out, new SimpleFormatter()) {
                @Override
                public synchronized void publish(final LogRecord record) {
                    super.publish(record);
                    flush();
                }
            };
            appLogger.addHandler(sh);
        }

        // don't use Java's defaults loggers
        // appLogger.setUseParentHandlers(false);

        if (debug) {
            if (fh != null)
                fh.setLevel(Log.BetterLevel.DEBUG);
            if (sh != null)
                sh.setLevel(Log.BetterLevel.DEBUG);
            appLogger.setLevel(Log.BetterLevel.DEBUG);
        }

    }

    public static void close(String packageName) {
        Logger appLogger = Logger.getLogger(packageName);

        for(Handler handler : appLogger.getHandlers()) {
            handler.close();
        }
    }

    public static void flush(String packageName) {
        Logger appLogger = Logger.getLogger(packageName);

        for(Handler handler : appLogger.getHandlers())
            handler.flush();
    }

    /* From https://stackoverflow.com/questions/13825403/java-how-to-get-logger-to-work-in-shutdown-hook */
    public static class MyLogManager extends LogManager {
        static MyLogManager instance;
        public MyLogManager() { instance = this; }
        @Override public void reset() { /* don't reset yet. */ }
        private void reset0() { super.reset(); }
        public static void resetFinally() { instance.reset0(); }
    }

    /* From https://stackoverflow.com/questions/13825403/java-how-to-get-logger-to-work-in-shutdown-hook
     * Call this function before any Log method is used. At the end of the program, call closeLoggingWithShutdownHook().
     * If you use new Log() in static initializations, then, in order to call this method before them, put the following
     * code
     *      static {
     *          Log.initLoggingWithShutdownHook()
     *      }
     * at the very beginning of your main class.
     *
     * Log.close() is not called by closeLoggingWithShutdownHook(). If you want to call it, you need to call them both.
     */
    public static void initLoggingWithShutdownHook() {
        System.setProperty("java.util.logging.manager", MyLogManager.class.getName());
    }

    /* Note: Log.close() is not called by this method. If you want to call it, you need to call them both. */
    public static void closeLoggingWithShutdownHook() {
        MyLogManager.resetFinally();
    }


}
