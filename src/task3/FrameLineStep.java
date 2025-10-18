package task3;

public class FrameLineStep implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        Frame frame = new Frame();
        frame.setColor("black");
        return frame;
    }
}
