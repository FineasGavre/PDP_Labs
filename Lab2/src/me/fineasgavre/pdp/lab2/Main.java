package me.fineasgavre.pdp.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static Integer partialResult = null;

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition readyToSendProductCondition = lock.newCondition();
    private static final Condition readyToReceiveProductCondition = lock.newCondition();

    public static void main(String[] args) {
        var vector1 = new ArrayList<>(List.of(1, 2, 3, 4));
        var vector2 = new ArrayList<>(List.of(1, 2, 3, 4));

        // Check if vector sizes are the same
        if (vector1.size() != vector2.size()) {
            throw new RuntimeException("Vector sizes ");
        }

        var producer = new Thread(() -> {
            var size = vector1.size();

            for (int i = 0; i < size; i++) {
                lock.lock();

                try {
                    while (partialResult != null) {
                        readyToSendProductCondition.await();
                    }

                    partialResult = vector1.get(i) * vector2.get(i);

                    logger.log(Level.INFO, "[PRODUCER] Sending Partial Product: {0}.", partialResult);

                    readyToReceiveProductCondition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        });

        var consumer = new Thread(() -> {
            var size = vector1.size();
            var totalSum = 0;

            for (int i = 0; i < size; i++) {
                try {
                    lock.lock();

                    while (partialResult == null) {
                        readyToReceiveProductCondition.await();
                    }

                    logger.log(Level.INFO, "[CONSUMER] Received Partial Sum: {0}.", partialResult);

                    totalSum += partialResult;
                    partialResult = null;
                    readyToSendProductCondition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }

            logger.log(Level.INFO, "[CONSUMER] Total Sum: {0}.", totalSum);
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
