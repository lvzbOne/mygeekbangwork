package week5.question_1.proxy02;

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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final DogUtil util = new DogUtil();
        util.before();
        Object result = method.invoke(target, args);
        /// 如果没传入主调进来，会抛异常，这是为什么呢？？？
//        Object result = method.invoke(proxy, args);
        util.after();
        return result;
    }
}
