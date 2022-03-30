package week5.question_1.proxy02;

/**
 * @author 起凤
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
        Dog proxyInstance = (Dog) ProxyFactory.getProxy(target);

        System.out.println("代理对象的运行时类的Class对象：" + proxyInstance.getClass());
        System.out.println("原生对象的运行时类的Class对象：" + target.getClass());

        proxyInstance.run();
        proxyInstance.info();

//        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

    }
}
