package week4.question_1.conc0301;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadMain {

    public static void main(String[] args) {
        /**
         * 该示例展示1：
         * 线程A
         * 线程B: 阻塞500 毫秒 打印
         *       "线程组中活动线程的数量:" + Thread.currentThread().getThreadGroup().activeCount();
         *       "线程标识符:" + currentThread.getId();
         *       "线程优先级:" + currentThread.getPriority());
         *       "线程状态:" + currentThread.getState());
         *       "所属的线程组:" + currentThread.getThreadGroup());
         *       "是否处于活跃状态:" + currentThread.isAlive());
         *       "是否为守护线程:" + currentThread.isDaemon());
         *
         * 线程C：定制的有返回值的线程 ThreadC implements Callable<String> 重写 call 方法
         *       封装进入FutureTask对象 FutureTask 继承了Runnable 和 Future 所以可以放入Thread对象中
         *       调用FutureTask对象的get方法时主线程会阻塞，等待ThreadC线程执行完毕后返回结果再执行
         */
        ThreadA threadA = new ThreadA();
        threadA.start();
        System.out.println("这是主线程：");

        ThreadB threadB = new ThreadB();
        new Thread(threadB).start();
        System.out.println("这是主线程：");

        ThreadC threadC = new ThreadC();
        FutureTask<String> futureTask = new FutureTask<>(threadC);
        new Thread(futureTask).start();
        System.out.println("这是主线程:begin!");
        try {
            System.out.println("得到的返回结果是:" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("这是主线程:end!");
    }

}
