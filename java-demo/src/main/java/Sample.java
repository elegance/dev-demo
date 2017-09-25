
public class Sample {
    public static int v1 = 1;

    public Sample() {
        System.out.println("Sample is loaded by: " + this.getClass().getClassLoader() + "[" + (++v1) + "]");
        new Dog();
    }
}
