
package week4.question_1.conc0301;

import java.io.IOException;

public class RunnerMain {

    public static void main(String[] args) throws IOException {

        /**
         *本实例
         */
        Runner1 runner1 = new Runner1();
        Thread thread1 = new Thread(runner1);

        Runner2 runner2 = new Runner2();
        Thread thread2 = new Thread(runner2);

        thread1.start();
        thread2.start();

        thread2.interrupt();  // i = true

        // 为什么返回4？？？ Thread[Monitor Ctrl-Break,5,main] 这个线程是哪里来的？？
        System.out.println("当前线程组下存活的线程数量：" + Thread.activeCount());
        // 列出当前线程组
        Thread.currentThread().getThreadGroup().list();
        // 返回以此线程组为祖先的活动线程组的数量
        System.out.println("当前线程组的父亲线程组下存活的线程组数量：" + Thread.currentThread().getThreadGroup().getParent().activeGroupCount());
//        Thread.currentThread().getThreadGroup().getParent().list();


    }
}
