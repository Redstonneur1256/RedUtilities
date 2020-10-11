package fr.redstonneur1256.redutilities.timer;

class SubTimer {

    private final long runs;
    private final long executionTime;
    private final Runnable consumer;
    private long lastRunTime;
    private int count;
    private double average;

    SubTimer(long runs, Runnable consumer) {
        this.runs = runs;
        this.executionTime = 1_000_000_000L / runs;
        this.consumer = consumer;
    }

    public void init() {
        this.lastRunTime = System.nanoTime();
    }

    public boolean execute() {
        if(lastRunTime + executionTime < System.nanoTime()) {
            lastRunTime += executionTime;
            long start = System.nanoTime();
            consumer.run();
            long end = System.nanoTime();
            count++;

            average += (end - start);
            average /= 2;
            return true;
        }
        return false;
    }

    public int getCount() {
        int value = count;
        count = 0;
        return value;
    }

    public long getRuns() {
        return runs;
    }

}