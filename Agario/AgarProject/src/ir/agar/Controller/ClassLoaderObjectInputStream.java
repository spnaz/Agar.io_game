package ir.agar.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.concurrent.ConcurrentHashMap;


public class ClassLoaderObjectInputStream extends ObjectInputStream {
    private ConcurrentHashMap<String, Class> classes = new ConcurrentHashMap<>();
    private AgarClassLoader classLoader = new AgarClassLoader();

    public ClassLoaderObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public ClassLoaderObjectInputStream() throws IOException, SecurityException {
    }

    public void loadClass(String className, byte[] bin) {
        classes.put(className, classLoader.loadDynamicClass(className, bin));
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        if (classes.containsKey(objectStreamClass.getName()))
            return classes.get(objectStreamClass.getName());

        return super.resolveClass(objectStreamClass);
    }
}
