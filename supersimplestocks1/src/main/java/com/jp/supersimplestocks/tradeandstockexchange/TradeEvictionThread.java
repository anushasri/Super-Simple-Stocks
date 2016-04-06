package com.jp.supersimplestocks.tradeandstockexchange;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


class TradeEvictionThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeEvictionThread.class);
    public static final String WORKERNAME = "workername";
    private String evictionThreadName;
    private Thread evictionThread;

    TradeEvictionThread(String name) {
        this.evictionThreadName = name;
    }

    /**
     * This method starts the  trade eviction thread to
     * avoid memory leak
     */
     final void start() {

        evictionThread = new Thread(evictionThreadName) {

            @Override
            public void run() {
                MDC.put(WORKERNAME, evictionThreadName);

                while (!Thread.interrupted()) {
                    TradesQueue.INSTANCE.evictTrades();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        evictionThread.setPriority(5);
        evictionThread.start();
    }

    /**
     * This method interrupts the thread for specific number of attempts
     * before throwing run time exception
     */
    final void stop() {
        LOGGER.info("Stopping evictionThread ...");
        int attempts = 0;
        while (null != evictionThread && evictionThread.isAlive()) {
            if (attempts > 0) {
                LOGGER.info("Could not interrupt or join  the evictionThread so attempting again");
            }
            evictionThread.interrupt();
            try {
                evictionThread.join(1000);
            } catch (InterruptedException ex) {
                LOGGER.error("Error shutting down evictionThread");
                throw new RuntimeException("Error shutting down evictionThread", ex);
            }
            attempts++;
            if (attempts == 5) {
                LOGGER.error("could nt interrupt or join the evictionThread after 5 attemps");
                throw new RuntimeException("could nt interrupt or join the evictionThread after 5 attemps");
            }
        }
        LOGGER.info("Stopped evictionThread thread");
    }
}
