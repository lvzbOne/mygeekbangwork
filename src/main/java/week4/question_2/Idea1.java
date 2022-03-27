package week4.question_2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 方法一：
 * 用定制的Call 作为有返回值的线程体，调用get 主线程阻塞等待子线程返回 获取方法返回值
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/26
 */
public class Idea1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(new CalTask());
        final Thread t = new Thread(task, "join 线程");
        t.start();
        // 24157817
        System.out.println("异步计算结果为：" + task.get());
    }

    public static class CalTask implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            return Homework.sum();
        }
    }
}
