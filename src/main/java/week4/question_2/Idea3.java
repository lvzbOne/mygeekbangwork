package week4.question_2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方式三：Thread.yield() 主线程一直让步，直到子线程返回
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/26
 */
public class Idea3 {
    public static void main(String[] args) {
        AtomicInteger result = new AtomicInteger(-99);
        Thread t = new Thread(() -> {
            result.set(Homework.sum());
        }, "定制的线程");
        t.start();

        while (-99 == result.get()) {
            Thread.yield();
        }
        // 24157817
        System.out.println("异步计算结果为：" + result.get());
    }

}
