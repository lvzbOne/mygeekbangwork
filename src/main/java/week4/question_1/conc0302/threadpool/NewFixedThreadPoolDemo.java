
package week4.question_1.conc0302.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewFixedThreadPoolDemo {

    public static void main(String[] args) {
        /**
         * 本示例演示了通过 Executors创建一个固定16个线程的线程池，execute()提交一个无返回值的线程
         *
         */
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        for (int i = 0; i < 100; i++) {
            final int no = i;
            executorService.execute(() -> {
                try {
                    System.out.println("start:" + no);
                    Thread.sleep(1000L);
                    System.out.println("end:" + no);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }

}
