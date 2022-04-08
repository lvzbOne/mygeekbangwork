package week6.question_1.lambda;

import lombok.*;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/7
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class LambdaTest {
    private String message;


    private static <T> T operator(int a, int b, MathOperation<T> mathOperation) {
        return mathOperation.operation(a, b);
    }

    private static void print(int x) {
        System.out.println(x + 3);
    }

    private static void sayHello(String str) {
        System.out.println(str);
    }

    private void sayHi(String str) {
        System.out.println(str);
    }


    public static void main(String[] args) {
        System.out.println("================== lambda 的示例1 =================");
        MathOperation add = (int a, int b) -> {
            return a + b;
        };
        // 只有一句时，可省略类型 return 和花括号
        MathOperation sub = (a, b) -> a - b;
        MathOperation multi = (a, b) -> a * b;
        MathOperation divide = (a, b) -> a / b;
        // 不加<Double> 也行
        MathOperation<Double> power = (a, b) -> Math.pow(a, b);

        System.out.println("10 + 5 = " + operator(10, 5, add));
        System.out.println("10 - 5 = " + operator(10, 5, sub));
        System.out.println("10 * 5 = " + operator(10, 5, multi));
        System.out.println("10 / 5 = " + operator(10, 5, divide));
        System.out.println("10^5 = " + operator(10, 5, power));

        System.out.println("================== lambda 的示例2 =================");
        GreetingService g1 = (message) -> System.out.println(message);
        // 入参只有一个时可省略括号
        GreetingService g2 = message -> System.out.println(message);
        // 函数引用,大学学C++时 函数引用也是 " :: " 表示的
        // 这种写法过于简略了对于不了解这块基础知识的人可读性不是很友好，多用几次熟悉了就好
        GreetingService g3 = System.out::println;
        // 还能在lambda表达式内掉用其它的类方法
        GreetingService g4 = message -> sayHello(message);
        // g4的简化方法引用方式(静态方法引用)
        GreetingService g5 = LambdaTest::sayHello;
        // 这么看g6是不是很懵？实际上它就是new了个LambdaTest对象，不会返回,因为GreetingService里的抽象方法sayMessage是无返回值的
        GreetingService g6 = message -> new LambdaTest(message);
        // g7是g6的简化 lambda的构造器引用方式,这里等同于g6
        GreetingService g7 = LambdaTest::new;
        // g8实例方法引用
        GreetingService g8 = new LambdaTest()::sayHi;

        // Function<T, R> 是个函数式接口，只有一个 R apply(T t)抽象方法,接收一个类型 T,返回一个类型 R
        // 这里就是接受一个String 对象 返回一个LambdaTest对象
        Function<String, LambdaTest> function1 = message -> new LambdaTest(message);
        Function<String, LambdaTest> function2 = LambdaTest::new;


        g1.sayMessage("g1 Hello World!");
        g2.sayMessage("g2 Hello World!");
        g3.sayMessage("g3 Hello World!");
        g4.sayMessage("g4 Hello World!");
        g5.sayMessage("g5 Hello World!");
        // 不会打印
        g6.sayMessage("g6 Hello World!");
        // 不会打印
        g7.sayMessage("g7 Hello World!");
        g8.sayMessage("g8 Hello World!");

        System.out.println("LambdaTest 对象： " + function1.apply("function1 Hello World!"));
        System.out.println("LambdaTest 对象： " + function2.apply("function2 Hello World!"));


        System.out.println("================== lambda 的示例3 =================");
        // forEach(Consumer<? super T> action) 因为是整形数组 这里 T 是Integer 类型
        // Consumer<? super T> action 是个函数式接口 只有一个抽象方法 void accept(T t);
        Arrays.asList(1, 2, 3, 4).forEach(LambdaTest::print);
        System.out.println("----------------------------------");
        Arrays.asList(1, 2, 3, 4).forEach(item -> {
            System.out.println(item + 3);
        });
    }
}
