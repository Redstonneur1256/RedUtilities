package fr.redstonneur1256.redutilities.timer;

import fr.redstonneur1256.redutilities.Utils;

import java.util.HashMap;
import java.util.Map;

public class Timer {

    private HashMap<String, SubTimer> timers;
    private Runnable onEnd;
    private boolean running = false;

    Timer(HashMap<String, SubTimer> timers, Runnable onEnd) {
        this.timers = timers;
        this.onEnd = onEnd;
    }

    public void start() {
        start0();
    }

    public void startAsync() {
        new Thread(() -> {
            try {
                start0();
            }catch(Exception ignored) {
            }
        }).start();
    }

    private void start0() {
        if(running)
            throw new IllegalStateException("Loop is already running");

        running = true;

        timers.values().forEach(SubTimer::init);

        long info = System.currentTimeMillis();

        while(running) {
            boolean dontSleep = false;
            for(SubTimer timer : timers.values()) {
                if(!timer.execute())
                    dontSleep = true;
            }
            if(!dontSleep) {
                Utils.sleep(1);
            }
            if(info + 1000 < System.currentTimeMillis()) {
                info += 1000;
                StringBuilder infoBuilder = new StringBuilder();
                for(Map.Entry<String, SubTimer> entry : timers.entrySet()) {
                    SubTimer timer = entry.getValue();
                    String name = entry.getKey();
                    int count = timer.getCount();
                    if(count != timer.getRuns())
                        infoBuilder.append(" | ").append(name).append(" ").append(count);
                }
                String infoMessage = infoBuilder.toString().replaceFirst(" \\| ", "");
                if(!infoMessage.isEmpty())
                    System.out.println(infoMessage);
            }
        }

        if(this.onEnd != null)
            this.onEnd.run();
    }

    public void stop() {
        running = false;
    }

}
