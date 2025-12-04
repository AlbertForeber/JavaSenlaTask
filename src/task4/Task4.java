package task4;

public class Task4 {
    public static void main(String[] args) {
        TimeService timeService = new TimeService(2);
        timeService.start();

        Runtime.getRuntime().addShutdownHook(new Thread(timeService::stop));
        while (true) {}


    }
}
