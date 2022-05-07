package week1;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/6
 */
public class XlassLoader extends ClassLoader {
    private static final int X = 255;

    private String fullPath;
    private String fileNameWithSuffix;
    private Boolean needDecode = false;

    public XlassLoader() {
    }

    public XlassLoader(String fullPath, String fileNameWithSuffix) {
        this.fileNameWithSuffix = fileNameWithSuffix;
        this.fullPath = fullPath;
    }


    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public void setNeedDecode(Boolean needDecode) {
        this.needDecode = needDecode;
    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] byteArray;
        Class<?> clazz = null;
        try (InputStream inputStream = new FileInputStream(fullPath)) {
            int length = inputStream.available();
            byteArray = new byte[length];
            inputStream.read(byteArray);
            if (needDecode) {
                byteArray = decode(byteArray, length);
            }
            clazz = defineClass(name, byteArray, 0, byteArray.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public void createEncodeFile(String prefix, String suffix, String path) throws IOException {
        File hello = this.createFile("Hello", ".xlass", path);

        try (FileInputStream fin = new FileInputStream(path + fileNameWithSuffix);
             FileOutputStream fos = new FileOutputStream(hello)) {
            ///hello.deleteOnExit();
            ///int length = fin.available();
            byte[] byteArray = new byte[64];
            int hasRead = 0;
            while ((hasRead = fin.read(byteArray)) > 0) {
                // 编码 255-byte
                byte[] bytes = encode(byteArray, hasRead);
                fos.write(bytes, 0, hasRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createFile(String prefix, String suffix, String path) throws IOException {
        File file = new File(path + prefix + suffix);
        if (file.exists()) {
            file.delete();
        }
        /// File newFile = File.createTempFile(prefix, suffix, new File(path));// 创建临时文件，前缀+随机数+后缀
        file.createNewFile();
        return file;
    }

    public byte[] encode(byte[] bytes, int length) {
        byte[] newBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            newBytes[i] = (byte) (X - bytes[i]);
        }
        return newBytes;
    }

    public byte[] decode(byte[] bytes, int length) {
        byte[] newBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            newBytes[i] = (byte) (X - bytes[i]);
        }
        return newBytes;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        String fullPath = "C:/Users/起凤/Desktop/temp/demo/jvm0104/HelloByteCode.class";
        String path = "C:/Users/起凤/Desktop/temp/demo/jvm0104/";
        String fileNameWithSuffix = "HelloByteCode.class";
        String prefix = "Hello";
        String suffix = ".xlass";
        String signature = "demo.jvm0104.HelloByteCode";


        XlassLoader xlassLoader = new XlassLoader(fullPath, fileNameWithSuffix);
        Class<?> clazz = xlassLoader.loadClass(signature);
        Method main = clazz.getMethod("main", String[].class);
        main.invoke(null, (Object) null);
        // clazz.newInstance();

        System.out.println("======================= 分割线 ======================");

        // 将HelloByteCode.class编码生成 Hello.xlass,然后用自定义类加载器解码加载执行main方法
        xlassLoader.createEncodeFile(prefix, suffix, path);
        String fullPath1 = "C:/Users/起凤/Desktop/temp/demo/jvm0104/Hello.xlass";
        XlassLoader loader = new XlassLoader();
        loader.setFullPath(fullPath1);
        loader.setNeedDecode(true);
        Class<?> helloClass = loader.loadClass(signature);
        main = helloClass.getMethod("main", String[].class);
        main.invoke(null, (Object) null);
    }
}
