package task2;
import java.util.ArrayList;


public class TestBoquet {
    public static void main(String[] args) {
        ArrayList<Flower> boquet = new ArrayList<>();
        boquet.add(new Rose());
        boquet.add(new Tulip());
        boquet.add(new Lily());

        double sumPrice = 0;
        for (Flower j : boquet) {
            sumPrice += j.getPrice();
        }

        System.out.printf("%f суммарная стоимость", sumPrice);
    }

}
