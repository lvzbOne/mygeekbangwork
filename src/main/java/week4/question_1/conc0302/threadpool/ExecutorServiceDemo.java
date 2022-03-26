
package week4.question_1.conc0302.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorServiceDemo {

    public static void main(String[] args) {
        /**
         * 本示例演示了通过 Executors创建一个定时的的线程池，提交一个线程，执行打印一句话
         * 因为线程池没有关闭，所以一直阻塞？？
         */
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(16);
        try {
            String str = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "I am a task, which submited by the so called laoda, and run by those anonymous workers";
                }
            }).get();

            System.out.println("str=" + str);
            // executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
