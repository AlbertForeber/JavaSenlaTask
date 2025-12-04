package task1;

public class SyncOne {
    private final Object lock = new Object();
    private int waitTime = 2000;
    private boolean locked = true;

    // Важно, эта переменная обязательно volatile
    // Иначе кэшируется процессором и изменения не видны.
    private volatile boolean mayFinish = false;

    public void method() throws InterruptedException {
        synchronized (lock) {
            Thread currentThread = Thread.currentThread();

            lock.wait(waitTime);

            while (locked) {
                lock.wait();
            }

            lock.wait(1000);

            System.out.println("I waited, so now I'm " + currentThread.getState());

        }
    }

    public void secondMethod() {
        synchronized (lock) {

            // Cимуляция занятого потока
            while (!mayFinish) {
                // Спасало ранее от кэширования так как захватывает монитор System.out
                // Заставляет поток сбросить все кэшированные значения и синхронизировать с изменениями
                // System.out.print("");
            }
        }
    }

    public void resume() {
        synchronized (lock) {
            locked = false;
            lock.notify();
        }
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setMayFinish(boolean mayFinish) {
        this.mayFinish = mayFinish;
    }
}
