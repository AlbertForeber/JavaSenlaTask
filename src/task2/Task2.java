package task2;


public class Task2 {
    public static void main(String[] args) throws InterruptedException {
        SyncName syncName = new SyncName();

        new Thread(() -> {
            try {
                syncName.sayMyName();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(250);

        new Thread(() -> {
            try {
                syncName.sayMyName();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
