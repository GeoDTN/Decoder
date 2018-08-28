package biz.movia.utils;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class ComputeIfTimePassed {

    private final long delta_ms;
    private long last_time;

    public ComputeIfTimePassed(long millisec, long initialTime) {
        this.delta_ms = millisec;
        this.last_time = initialTime;
    }

    public ComputeIfTimePassed(long millisec) {
        this(millisec, 0);
    }

    // null is returned if not enough time passed, otherwise the function is
    // executed and its return value is returned
    public <T, R> R compute(T t, Function<T,R> function) {
        long now = System.currentTimeMillis();
        if (now-this.last_time >= this.delta_ms) {
            R r = function.apply(t);
            this.last_time = now;
            return r;
        }
        return null;
    }

    // nothing is done if not enough time passed, otherwise the consumer is
    // executed, in this case true is returned
    public <T> boolean compute(T t, Consumer<T> consumer) {
        Boolean ret = compute(t, x -> { consumer.accept(x); return Boolean.TRUE; });
        return ret ==  Boolean.TRUE;
    }

    // nothing is done if not enough time passed, otherwise the consumer is
    // executed, in this case true is returned
    public <T> boolean compute(Consumer<T> consumer) {
        return compute(null, consumer);
    }

}
