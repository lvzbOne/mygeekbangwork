# 第一周作业概览

>  2.（必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。

### class字节码减去5加密解密作业实现实操

> 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 main方法，此文件内容是一个 HelloByteCode.class 文件所有字节（x=255-x）处理后的文件。
> 加密解密的原理很简单： [加密解密的一种简单方式](https://blog.csdn.net/LvQiFen/article/details/123211588)
> 
> - [HelloByteCode.class 字节码文件]()
> - [Hello.xlass 编码文件]()

### （一）HelloByteCode.class 反编译的内容

```java
package demo.jvm0104;

public class HelloByteCode {
    public HelloByteCode() {
    }

    public static void main(String[] var0) {
        new HelloByteCode();
        System.out.println("Hello World[你好，世界]!");
    }
}
```

### （二）具体实现

```java
package org.geekbang.a_jvm.类加载器;

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
```

### （三）测试结果

![在这里插入图片描述](https://img-blog.csdnimg.cn/3242313a590346b5acd8430737d70f72.png)


## 笔记
- getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
- getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
- getResourceAsStream读取的文件路径只局限与工程的源文件夹中，包括在工程src根目录下，以及类包里面任何位置，但是如果配置文件路径是在除了源文件夹之外的其他文件夹中时，该方法是用不了的。
## 参考资料
- [Java中getResourceAsStream的用法小结](https://developer.aliyun.com/article/43489)
- [getDeclaredMethod()和getMethod()的区别](https://blog.csdn.net/ozwarld/article/details/8277359)