package fr.redstonneur1256.redutilities.timer;

import java.util.HashMap;

public class TimerBuilder {

    private HashMap<String, SubTimer> timers = new HashMap<>();
    private Runnable onEnd;

    public static TimerBuilder of() {
        return new TimerBuilder();
    }

    public TimerBuilder add(String name, Runnable runnable, int times) {
        timers.put(name, new SubTimer(times, runnable));
        return this;
    }

    public TimerBuilder end(Runnable onEnd) {
        this.onEnd = onEnd;
        return this;
    }

    public Timer go() {
        return new Timer(timers, onEnd);
    }
}
