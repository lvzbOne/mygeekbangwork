
package week4.question_1.conc0302.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewSingleThreadExecutorDemo {

    public static void main(String[] args) {
        /**
         * 本示例演示了通过 Executors创建一个固定1个线程的线程池，execute()提交一个无返回值的线程
         *
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            final int no = i;
            executorService.execute(() -> {
                System.out.println("start:" + no);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end:" + no);
            });
        }
        executorService.shutdown();
        System.out.println("Main Thread End!");
    }

}
