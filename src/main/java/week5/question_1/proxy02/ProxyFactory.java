package week5.question_1.proxy02;

import java.lang.reflect.Proxy;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/29
 */
public class ProxyFactory {

    public static Object getProxy(Object o) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        MyInvocationHandler handler = new MyInvocationHandler();
        handler.setTarget(o);
//        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
       // System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        return Proxy.newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), handler);
    }
}
