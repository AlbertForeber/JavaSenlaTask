package task2;
import java.util.ArrayList;


public class TestBouquet {
    public static void main(String[] args) {
        ArrayList<Flower> bouquet = new ArrayList<>();
        bouquet.add(new Rose());
        bouquet.add(new Tulip());
        bouquet.add(new Lily());

        double sumPrice = 0;
        for (Flower j : bouquet) {
            sumPrice += j.getPrice();
        }

        System.out.printf("%f суммарная стоимость", sumPrice);
    }

}
