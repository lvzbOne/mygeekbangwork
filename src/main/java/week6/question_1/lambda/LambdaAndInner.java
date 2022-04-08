package week6.question_1.lambda;

/**
 * 区别匿名内部类与lambda表达式的不同
 * 匿名内部类可以调用接口的默认方法，lambda表达式内不行
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/6
 */
public class LambdaAndInner {
    private int age = 12;
    private static String name = "lambda与匿名内部类的区别";

    public void test() {
        String book = "GoF23设计模式";
        Displayable dis = () -> {
            // book 就成为了 隐式的final,后面不允许更改
            System.out.println("book局部变量为：" + book);
            System.out.println("外部类的age实例变量为：" + age);
            System.out.println("外部类的name实例变量为：" + name);
            // lambda 表达式内部无法调用接口的default方法
            // System.out.println(add(1, 2));
        };
        // 打开下面这行 lambda 表达式内的book就会编译报错。
        /// book = "a";
        dis.display();
        // 只能用生成后的对象调用default方法
        System.out.println(dis.add(1, 2));
    }

    public static void main(String[] args) {
        LambdaAndInner inner = new LambdaAndInner();
        inner.test();

        String book = "数据结构与算法之美";
        Displayable anonymityImpl = new Displayable() {
            @Override
            public void display() {
                // book 就成为了 隐式的final,后面不允许更改
                System.out.println("推荐一本学习数据结构与算法基础知识的好书：" + book);
                // 匿名实现接口里就可以直接调用接口的default方法
                System.out.println("default add: " + add(1, 2));
            }
        };
        // 打开这行 匿名实现内的book就会编译报错。
        /// book = "码出高效";
        anonymityImpl.display();
    }
}
