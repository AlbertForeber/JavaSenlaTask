package task3;

public class Laptop implements IProduct {
    Motherboard motherboard;
    Screen screen;
    Frame frame;

    @Override
    public void installFirstPart(IProductPart part) {
        motherboard = (Motherboard) part;
    }

    @Override
    public void installSecondPart(IProductPart part) {
        screen = (Screen) part;
    }

    @Override
    public void installThirdPart(IProductPart part) {
        frame = (Frame) part;
    }

    @Override
    public String toString() {
        return String.format("Laptop(motherboard: %s, screen: %s, frame: %s)", motherboard, screen, frame);
    }
}
