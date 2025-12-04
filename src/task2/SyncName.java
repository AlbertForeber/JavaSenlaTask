package task2;

public class SyncName {
    private final Object lock = new Object();
    private volatile boolean keepTelling = true;

    public void sayMyName() throws InterruptedException {
        synchronized (lock) {
            while (keepTelling) {
                System.out.println(
                        "I'm " +
                                Thread.currentThread().getName()
                );
                lock.wait(500);
            }
        }
    }

    public void setKeepTelling(boolean keepTelling) {
        this.keepTelling = keepTelling;
    }
}
