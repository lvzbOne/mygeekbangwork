package week4.question_2;

import java.util.concurrent.*;

/**
 * 方法10： 线程池+带返回值的task
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/27
 */
public class Idea10 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ExecutorService pool = Executors.newSingleThreadExecutor();

        final Future<Integer> future = pool.submit(() -> {
            Thread.sleep(2000L);
            return Homework.sum();
        });

        final Integer result = future.get();
        // 24157817
        System.out.println(result);
        pool.shutdown();
    }
}
