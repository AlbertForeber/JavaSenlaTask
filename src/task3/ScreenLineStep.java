package task3;

public class ScreenLineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        Screen screen = new Screen();
        screen.setResolutionX(1920);
        screen.setResolutionY(1080);
        return screen;
    }
}
