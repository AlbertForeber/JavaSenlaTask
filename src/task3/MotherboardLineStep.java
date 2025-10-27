package task3;

public class MotherboardLineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        Motherboard motherboard = new Motherboard();
        motherboard.setBrand("Brand1");
        motherboard.setModel("Model1");
        return motherboard;
    }
}
