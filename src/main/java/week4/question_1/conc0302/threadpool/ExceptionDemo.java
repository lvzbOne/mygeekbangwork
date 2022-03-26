package week4.question_1.conc0302.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExceptionDemo {

    public static void main(String[] args) {
        /**
         * 本示例演示了通过 Executors创建一个固定线程数为1个的线程池，提交一个线程，执行抛出一个异常
         * call 的抛出异常可以被catch 住
         */
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try {
            Future<Double> future = executorService.submit(() -> {
                throw new RuntimeException("executorService.submit()");
            });

            double b = future.get();
            System.out.println(b);

        } catch (Exception ex) {
            System.out.println("catch submit");
            ex.printStackTrace();
        }
//
//        try {
//            executorService.execute(() -> {
//                  throw new RuntimeException("executorService.execute()");
//                });
//        } catch (Exception ex) {
//            System.out.println("catch execute");
//            ex.printStackTrace();
//        }
//
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }

}
