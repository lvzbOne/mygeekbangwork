# 上手JDK 动态代理

> 前言：最近在学习spring框架知识 spring apo 是基于代理模式， jdk 1.3开始支持动态代理，这里练习一下原生的jdk动态代理，加深印象。示例仅供个人参考。
>
> 在 Java 的 `java.lang.reflect` 包下提供了 一个 Proxy类和 一个`InvocationHandler`接口，通过这 2个东西就能生成 JDK动态代理对象。需要注意: JDK动态代理只能为接口创建动态代理

## 一、示例

这里示例一个代理工厂，通过代理工厂获取代理对象，实现对代理对象的原有方法增强。通过这种代理模式，可以起到解耦的作用[原生的对象并没有做任何改动，借助代理对象做了增强，否则需要在原生的类上进行显示嵌入要增强的行为]
这里**预留2个问题思考：

1. 动态代理 `Proxy.newProxyInstance`(类加载器,要代理对象的实现所有接口,`InvocationHandler`的实例)，三个入参的作用，尤其是类加载器。
2. `InvocationHandler`的实例中invoke方法中第一个参数`Object `对象是指代什么？**

```java
/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/3/29
 */
public interface Dog {
    void info();

    void run();
}
```

```java
/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/3/29
 */
public class GunDog implements Dog {
    @Override
    public void info() {
        System.out.println("我是一只猎狗");
    }

    @Override
    public void run() {
        System.out.println("我跑的飞快！");
    }
}

```

```java
/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/3/29
 */
public class DogUtil {
    public void before() {
        System.out.println("========= 前置处理方法 ==========");
    }

    public void after(){
        System.out.println("========= 后置处理方法 ==========");
    }
}
```

```java
/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/3/29
 */
public class MyInvocationHandler implements InvocationHandler {
    private Object target;

    /**
    * 设置主调
    */
    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final DogUtil util = new DogUtil();
        util.before();
        Object result = method.invoke(target, args);
        /// 如果没传入主调进来，直接用proxy，会抛异常,好像这个proxy参数没有起到作用...
//        Object result = method.invoke(proxy, args);
        util.after();
        return result;
    }
}
```

```java
/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/3/29
 */
public class ProxyFactory {

    public static Object getProxy(Object o) {
        MyInvocationHandler handler = new MyInvocationHandler();
        handler.setTarget(o);
        // 传入三个参数：类加载器、代理对象实现的所有接口、InvocationHandler的实例对象
        return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
    }
}
```

```java
/**
 * @author lvzb31988
 * @description: TODO
 * @date 2022/3/29
 */
public class ProxyTest {
    public static void main(String[] args) {
        // jdk8及以前的版本 输出代理对象的字节码文件到本地磁盘，在项目根目录的com.sun.proxy下
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        /// jdk8以后的版本
        // System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        Dog target = new GunDog();
        // 获取target对象的代理对象，该对象也实现了target实现的所有接口
        Dog proxyInstance = (Dog) ProxyFactory.getProxy(target);

        System.out.println("代理对象的运行时类的Class对象：" + proxyInstance.getClass());
        System.out.println("原生对象的运行时类的Class对象：" + target.getClass());
        
        proxyInstance.run();
        proxyInstance.info();
    }
}
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/d80aa23701be4db0ae4c827e7222d83d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHZRaUZlbg==,size_20,color_FFFFFF,t_70,g_se,x_16)

## 二、代理对象字节码反编译后分析

> 从反编译后的结果来看：代理类是个final修饰类，继承了Proxy，同样实现了被代理对象的接口（这就是前面传入被代理对象实现的所有接口），还含有一个入参是`InvocationHandler`类型的构造函数用来生成代理对象
> 通过代理对象的方法来看，从接口继承过来重写的方法是调用父类`InvocationHandler`实例的invoke方法，入参是[当前代理对象,方法名,和方法入参]
> 从这也就能解释前面2个预留的问题了：
> 1. jdk动态代理会生成一个代理对象的字节码文件，需要通过传入的类加载器，加载到JVM内存中;
> 2. `invoke`方法的第一个Object 类型的入参最终就是代理对象本身，但是我们希望的是利用被代理对象的方法之前之后做些什么进行增强，所以要传入自己的主调，**需要注意的是：如果直接用代理对象作为主调就会方法调用死循环抛出异常 原因从反编译的字节码就能分析出来（代理对象调用父类`InvocationHandler`实例的`invoke`方法，`invoke`方法又通过反射调用代理对象的对应方法，然后就循环内嵌调用出不来了）**，感兴趣的可以自己试试。

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.sun.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import week5.question_1.proxy02.Dog;

public final class $Proxy0 extends Proxy implements Dog {
    private static Method m1;
    private static Method m3;
    private static Method m4;
    private static Method m2;
    private static Method m0;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final void run() throws  {
        try {
            super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final void info() throws  {
        try {
            super.h.invoke(this, m4, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final int hashCode() throws  {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m3 = Class.forName("week5.question_1.proxy02.Dog").getMethod("run");
            m4 = Class.forName("week5.question_1.proxy02.Dog").getMethod("info");
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}

```

参考内容：
[https://www.cnblogs.com/akaneblog/p/6720513.html](https://www.cnblogs.com/akaneblog/p/6720513.html)
