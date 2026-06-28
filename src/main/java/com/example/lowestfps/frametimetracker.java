package com.example.lowestfps;

import java.util.ArrayDeque;
import java.util.Deque;

public class FrameTimeTracker {
    private static class Frame {
        final long timestamp;
        final double durationMs;

        Frame(long timestamp, double durationMs) {
            this.timestamp = timestamp;
            this.durationMs = durationMs;
        }
    }

    private static final Deque<Frame> window = new ArrayDeque<>();
    private static double absoluteMaxFrameTime = 0.0;
    private static double lastFrameTimeMs = 16.67; // Fallback to 60fps
    private static long lastFrameNano = 0;

    public static void recordFrame() {
        long nowNano = System.nanoTime();
        if (lastFrameNano != 0) {
            long durationNano = nowNano - lastFrameNano;
            double durationMs = durationNano / 1_000_000.0;
            addFrame(durationMs);
        }
        lastFrameNano = nowNano;
    }

    private static synchronized void addFrame(double durationMs) {
        lastFrameTimeMs = durationMs;
        long nowMs = System.currentTimeMillis();
        window.addLast(new Frame(nowMs, durationMs));
        if (durationMs > absoluteMaxFrameTime) {
            absoluteMaxFrameTime = durationMs;
        }
        cleanup(nowMs);
    }

    private static void cleanup(long nowMs) {
        long limitMs = nowMs - (long) (Config.windowSizeSeconds * 1000.0);
        while (!window.isEmpty() && window.peekFirst().timestamp < limitMs) {
            window.removeFirst();
        }
    }

    public static synchronized double getMaxFrameTimeMs() {
        cleanup(System.currentTimeMillis());
        double max = 0.0;
        for (Frame f : window) {
            if (f.durationMs > max) {
                max = f.durationMs;
            }
        }
        return max;
    }

    public static double getAbsoluteMaxFrameTimeMs() {
        return absoluteMaxFrameTime;
    }

    public static double getLastFrameTimeMs() {
        return lastFrameTimeMs;
    }

    public static synchronized void reset() {
        absoluteMaxFrameTime = 0.0;
        lastFrameTimeMs = 16.67;
        window.clear();
        lastFrameNano = 0;
    }
}
