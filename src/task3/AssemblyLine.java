package task3;

public class AssemblyLine implements IAssemblyLine {
    @Override
    public IProduct assembleProduct(IProduct laptop) {
        ILineStep motherboardBuilder = new MotherboardLineStep();
        ILineStep screenBuilder = new ScreenLineStep();
        ILineStep frameBuilder = new FrameLineStep();

        laptop.installFirstPart(motherboardBuilder.buildProductPart());
        laptop.installSecondPart(screenBuilder.buildProductPart());
        laptop.installThirdPart(frameBuilder.buildProductPart());

        return laptop;
    }
}
