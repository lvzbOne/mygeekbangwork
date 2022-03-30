package week5.question_1.proxy01;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/29
 */
public class ProxyTest {
    public static void main(String[] args) {
        final MyInvocationHandler handler = new MyInvocationHandler();
        handler.setTarget(new Object());

        // Person 接口没有真正的实现类，这种写死的参数也能生成代理对象,
        Person person = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class[]{Person.class}, handler);
        person.sayHello();
        person.walk();

        A instance = (A) Proxy.newProxyInstance(A.class.getClassLoader(), new Class[]{A.class}, handler);
        instance.aaa();
    }
}
