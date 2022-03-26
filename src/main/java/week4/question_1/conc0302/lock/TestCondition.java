package week4.question_1.conc0302.lock;

import java.util.Random;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/24
 */
public class TestCondition {
    public static void main(String[] args) {
        ConditionDemo data = new ConditionDemo();
        Random random = new Random();
        final int SIZE = 1000;

        new Thread(() -> {
            while (true) {
                try {
                    data.put(random.nextInt(SIZE));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "生产者线程-").start();

        new Thread(() -> {
            while (true) {
                try {
                    data.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "消费者线程-").start();
    }
}
