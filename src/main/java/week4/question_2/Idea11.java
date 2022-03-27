package week4.question_2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 方法11： CyclicBarrier
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/27
 */
public class Idea11 {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        AtomicInteger result = new AtomicInteger(-99);
        CyclicBarrier cyc = new CyclicBarrier(1, () -> {
            result.set(Homework.sum());
        });
        // -99
        System.out.println(result.get());
        cyc.await();
        // 24157817
        System.out.println(result.get());
    }
}
