package task3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Task3 {
    private static volatile boolean isProducing = true;
    private static volatile boolean isReceiving = true;
    public static void main(String[] args) throws InterruptedException {
        // Ограничим четырьмя.
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(4);


        Thread producer = new Thread(() -> {
            try {
                while (true) {
                    if (isProducing)
                        blockingQueue.put(3);

                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        producer.setDaemon(true);
        producer.start();


        Thread receiver = new Thread(() -> {
            try {
                while (true)
                    if (isReceiving)
                        System.out.println(blockingQueue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        receiver.setDaemon(true);
        receiver.start();


        Thread.sleep(3000);
        isProducing = false;
        System.out.println("When there are no produced elements Receiver is " + receiver.getState());
        isReceiving = false;
        Thread.sleep(3000);
        isProducing = true;
        Thread.sleep(6000);
        System.out.println("When queue is full Producer is " + producer.getState());

    }
}
