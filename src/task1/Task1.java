package task1;

public class Task1 {
    public static void main(String[] args) throws InterruptedException {
        boolean launch = true;
        SyncOne syncOne = new SyncOne();

        Thread thread = new Thread(() -> {
            try {
                syncOne.method();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread2 = new Thread(syncOne::secondMethod);

        System.out.println("I've not started yet... So I'm " + thread.getState());
        thread.start();
        System.out.println("I've started, now I'm " + thread.getState());
        Thread.sleep(1000);
        System.out.println("I'm waiting with timer, I'm " + thread.getState());
        Thread.sleep(2000);
        System.out.println("I'm waiting without timer, I'm " + thread.getState());
        Thread.sleep(1000);

        syncOne.resume();
        Thread.sleep(500);

        thread2.start();

        Thread.sleep(700);


        System.out.println("Oh no, I'm " + thread.getState());
        syncOne.setMayFinish(true);




        Thread.sleep(1000);
        System.out.println("Work is finished, I'm " + thread.getState());

    }
}
