import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 为了测试方便 ，定义的class直接在 默认包下，可将编译生成的class 分别放到 main方法中 指定的目录，用 java 命令行的方式来执行测试
 *
 */
public class MyClassLoader extends ClassLoader {
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        MyClassLoader classLoader1 = new MyClassLoader("loader1");
        classLoader1.setPath("D:\\myapp\\serverlib\\");

        MyClassLoader classLoader2 = new MyClassLoader(classLoader1, "loader2");
        classLoader2.setPath("D:\\myapp\\clientlib\\");

        MyClassLoader classLoader3 = new MyClassLoader(null, "loader3"); // 指定 null，则代表 以
                                                                         // 根类加载器作为父加载器
        classLoader3.setPath("D:\\myapp\\otherlib\\");

        test(classLoader2);
        test(classLoader3);
    }
    
    private static void test(ClassLoader loader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = loader.loadClass("Sample"); // 统一都测试加载 Sample类
        Object object = clazz.newInstance(); // 实例化，触发初始化（可以注释此行，对比测试下）
    }
    
    private String name;

    private String path;

    private final String suffix = ".class";

    public MyClassLoader(String name) {
        // 默认则是 “系统类”加载器作为父加载器
        this.name = name;
    }

    public MyClassLoader(ClassLoader parent, String name) {
        super(parent); // 显示指定类为当前classLoader的父加载器
        this.name = name;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, loadClassData(name), 0, data.length);
    }

    private byte[] loadClassData(String name) throws ClassNotFoundException {
        byte[] data = null;
        this.name = this.name.replace(".", "\\");

        try (InputStream is = new FileInputStream(new File(path + name + suffix));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();) {

            int ch = 0;
            while ((ch = is.read()) != -1) {
                baos.write(ch);
            }
            data = baos.toByteArray();
            return data;
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ClassNotFoundException(name);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return name;
    }
}
