package biz.movia.utils;

// Uses ComputeIfTimePassed to issue a Log.warning() if enough time passed or a Log.info() otherwise
public class LogIfTimePassed {

    private final ComputeIfTimePassed computeIfTimePassed;
    private final Log log;
    private final String prefixMessage;

    private static final String ONE_LOG_PER_YEAR_PREFIX_MSG = ">> 1 l/year <<";
    private static final String ONE_LOG_PER_DAY_PREFIX_MSG = ">> 1 l/day <<";
    private static final String ONE_LOG_PER_HOUR_PREFIX_MSG = ">> 1 l/hour <<";
    private static final String ONE_LOG_PER_10_MINUTES_PREFIX_MSG = ">> 1 l/10min <<";
    private static final String ONE_LOG_PER_MINUTE_PREFIX_MSG = ">> 1 l/min <<";

    public LogIfTimePassed(Log log, long delta_ms, String prefixMessage) {
        computeIfTimePassed = new ComputeIfTimePassed(delta_ms);
        this.log = log;
        this.prefixMessage = prefixMessage == null ? "" : prefixMessage+" ";
    }

    // Uses ComputeIfTimePassed to issue a Log.warning() if enough time passed or a Log.info() otherwise
    public void warning(String text, Exception e) {
        if ( ! computeIfTimePassed.compute(x -> log.warning(prefixMessage+text, e)) )
            log.info(text, e);
    }

    // Uses ComputeIfTimePassed to issue a Log.warning() if enough time passed or a Log.info() otherwise
    public void warning(String text) {
        if ( ! computeIfTimePassed.compute(x -> log.warning(prefixMessage+text)) )
            log.info(text);
    }

    public static LogIfTimePassed oneLogPerYear(Log log) {
        return new LogIfTimePassed(log, 12*30*24*60*60*1000L, ONE_LOG_PER_YEAR_PREFIX_MSG);
    }

    public static LogIfTimePassed oneLogPerDay(Log log) {
        return new LogIfTimePassed(log, 1000*60*60*24, ONE_LOG_PER_DAY_PREFIX_MSG);
    }

    public static LogIfTimePassed oneLogPerHour(Log log) {
        return new LogIfTimePassed(log, 1000*60*60, ONE_LOG_PER_HOUR_PREFIX_MSG);
    }

    public static LogIfTimePassed oneLogPer10Minutes(Log log) {
        return new LogIfTimePassed(log, 1000*60*10, ONE_LOG_PER_10_MINUTES_PREFIX_MSG);
    }

    public static LogIfTimePassed oneLogPerMinute(Log log) {
        return new LogIfTimePassed(log, 1000*60, ONE_LOG_PER_MINUTE_PREFIX_MSG);
    }


    // Uses ComputeIfTimePassed to issue a Log.warning() if enough time passed. Unlike the warning() method, it does not
    // issue a Log.info() otherwise (it stays quiet).
    public void warnOrBeQuiet(String text, Exception e) {
        computeIfTimePassed.compute(x -> log.warning(prefixMessage+text, e));
    }

    // Uses ComputeIfTimePassed to issue a Log.warning() if enough time passed. Unlike the warning() method, it does not
    // issue a Log.info() otherwise (it stays quiet).
    public void warnOrBeQuiet(String text) {
        computeIfTimePassed.compute(x -> log.warning(prefixMessage+text));
    }


}
