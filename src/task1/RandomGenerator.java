package task1;

import java.util.Arrays;
import java.util.Random;


class RandomGenerator {
    public static void main(String[] args) {
        Random random = new Random();
        int number = random.nextInt(100, 999);
        int[] numbers = {number / 100, number / 10 % 10, number % 10};
        Arrays.sort(numbers);
        System.out.printf("%d %d\n", number, numbers[2]);
    }
}
