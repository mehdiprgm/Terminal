package com.zen.lib.terminal.event;

import com.zen.lib.terminal.driver.TerminalDriver;
import com.zen.lib.terminal.driver.TerminalWindowSize;

/**
 * Provides a mechanism to listen for terminal window resize events.
 *
 * <p>This class allows subscribing to changes in the terminal dimensions by polling
 * the current window size at a specified interval.</p>
 *
 * @author Mehdi Lavasani (zenDEv2)
 * @version 1.0
 * @since 21
 */
public final class TerminalResizeListener {

    static {
        System.loadLibrary("zenterminal");
    }

    private TerminalResizeListener() {
    }

    /**
     * Functional interface for handling terminal resize events.
     */
    @FunctionalInterface
    public interface OnResizeListener {
        /**
         * Invoked when the terminal window size changes.
         *
         * @param oldSize the dimensions of the terminal before the resize
         * @param newSize the current dimensions of the terminal after the resize
         */
        void onResize(TerminalWindowSize oldSize, TerminalWindowSize newSize);
    }

    /**
     * Subscribes a listener to terminal resize events with a default polling interval.
     *
     * <p>The default polling interval is 200 milliseconds.</p>
     *
     * @param listener the callback to be executed on resize
     * @return a subscription object to manage the listener lifecycle
     */
    public static ResizeSubscription addListener(OnResizeListener listener) {
        return addListener(listener, 200);
    }

    /**
     * Subscribes a listener to terminal resize events with a custom polling interval.
     *
     * @param listener the callback to be executed on resize
     * @param pollMillis the interval in milliseconds between size checks
     * @return a subscription object to manage the listener lifecycle
     */
    public static ResizeSubscription addListener(OnResizeListener listener, long pollMillis) {
        ResizeSubscription subscription = new ResizeSubscription(listener, pollMillis);
        subscription.start();
        return subscription;
    }

    /**
     * Represents an active subscription to terminal resize events.
     *
     * <p>Provides methods to stop the monitoring thread and check the current status
     * of the subscription.</p>
     */
    public static final class ResizeSubscription {
        private final OnResizeListener listener;
        private final long pollMillis;

        private volatile boolean running;
        private Thread thread;

        private ResizeSubscription(OnResizeListener listener, long pollMillis) {
            this.listener = listener;
            this.pollMillis = pollMillis;
        }

        private void start() {
            if (running) {
                return;
            }

            running = true;
            thread = new Thread(() -> {
                TerminalWindowSize lastSize = TerminalDriver.getTerminalWindowSize();

                while (running && !Thread.currentThread().isInterrupted()) {
                    TerminalWindowSize currentSize = TerminalDriver.getTerminalWindowSize();

                    if (currentSize.width() != lastSize.width()
                            || currentSize.height() != lastSize.height()) {
                        listener.onResize(lastSize, currentSize);
                        lastSize = currentSize;
                    }

                    try {
                        Thread.sleep(pollMillis);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });

            thread.setDaemon(true);
            thread.start();
        }

        /**
         * Stops the resize monitoring for this subscription.
         */
        public void stop() {
            running = false;

            if (thread != null) {
                thread.interrupt();
            }
        }

        /**
         * Checks if the resize monitoring is currently active.
         *
         * @return {@code true} if the listener is still polling, {@code false} otherwise
         */
        public boolean isRunning() {
            return running;
        }
    }
}