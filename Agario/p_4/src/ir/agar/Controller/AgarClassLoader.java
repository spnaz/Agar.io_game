package ir.agar.Controller;


public class AgarClassLoader extends ClassLoader {
    public AgarClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    public AgarClassLoader() {
    }

    public Class loadDynamicClass(String name, byte[] bin) {
        try {
            return defineClass(name, bin, 0, bin.length);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("errrrror");
            return null;
        }
    }
}
