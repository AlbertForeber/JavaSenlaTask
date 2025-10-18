package task3;

public class Test {
    public static void main(String[] args) {
        AssemblyLine assemblyLine = new AssemblyLine();
        Laptop laptop = new Laptop();
        assemblyLine.assembleProduct(laptop);

        System.out.println(laptop);
    }
}
