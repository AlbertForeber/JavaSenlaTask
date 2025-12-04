package task4;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeService {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "Scheduled-Thread");
        t.setDaemon(true);
        return t;
    });
    private final int delay;
    private ScheduledFuture<?> task;

    public TimeService(int n) {
        this.delay = n;
    }

    public void start() {
        task = scheduler.scheduleWithFixedDelay(
                () -> System.out.println("DEBUG >> System time: " + LocalTime.now()),
                0,
                delay,
                TimeUnit.SECONDS
                );
    }

    public void stop() {
        if (task != null) {
            task.cancel(true);
        }

        scheduler.shutdown();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            // Выбрасывается, если был прерван поток запустивший этот метод
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
