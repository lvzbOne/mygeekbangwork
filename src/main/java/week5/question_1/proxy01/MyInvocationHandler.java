package week5.question_1.proxy01;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/29
 */
public class MyInvocationHandler implements InvocationHandler {
    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * @param o       调用方法的对象
     * @param method  调用的方法
     * @param objects 方法的入参
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        System.out.println("------- 正在执行的方法 ------" + method);
        if (null != objects) {
            System.out.println("开始打印调用方法的入参");
            for (Object obj : objects) {
                System.out.println(obj);
            }
        } else {
            System.out.println(">>>>>> 调用的方法没有入参 <<<<<<<");
        }
        /// return method.invoke(target, objects);
        return null;
    }
}
