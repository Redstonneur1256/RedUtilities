package fr.redstonneur1256.redutilities.graphics.swing;

import fr.redstonneur1256.redutilities.Utils;

import javax.swing.*;

public class FrameAnimator {

    public static final int slow;
    public static final int normal;
    public static final int fast;
    private static final Runnable nothing;
    static {
        slow = 20;
        normal = 10;
        fast = 5;
        nothing = () -> {};
    }

    public static void fadeIn(JFrame frame) {
        fadeIn(frame, nothing);
    }

    public static void fadeInt(JFrame frame, int speed) {
        fadeIn(frame, speed, nothing);
    }

    public static void fadeIn(JFrame frame, Runnable callback) {
        fadeIn(frame, normal, callback);
    }

    public static void fadeIn(JFrame frame, int speed, Runnable callback) {
        fade(frame, speed, true, callback);
    }

    public static void fadeOut(JFrame frame) {
        fadeOut(frame, nothing);
    }

    public static void fadeOut(JFrame frame, int speed) {
        fadeOut(frame, speed, nothing);
    }

    public static void fadeOut(JFrame frame, Runnable callback) {
        fadeOut(frame, normal, callback);
    }

    public static void fadeOut(JFrame frame, int speed, Runnable callback) {
        fade(frame, speed, false, callback);
    }

    private static void fade(JFrame frame, int speed, boolean inverted, Runnable callback) {
        Thread thread = new Thread(() -> {
            for(int i = 0; i < 100; i++) {
                float opacity = i / 100.0F;
                frame.setOpacity(inverted ? opacity : 1 - opacity);
                Utils.sleep(speed);
            }
            callback.run();
        }, "WindowFade");
        thread.setDaemon(true);
        thread.start();
    }

}

