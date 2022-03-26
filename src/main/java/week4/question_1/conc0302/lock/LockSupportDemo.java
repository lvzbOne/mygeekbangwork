package week4.question_1.conc0302.lock;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

    public static Object u = new Object();
    static ChangeObjectThread t1 = new ChangeObjectThread("t1");
    static ChangeObjectThread t2 = new ChangeObjectThread("t2");

    public static class ChangeObjectThread extends Thread {
        public ChangeObjectThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (u) {
                System.out.println("in " + getName());
                LockSupport.park();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("被中断了");
                }
                System.out.println("继续执行");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * 本示例使用 LockSupport.park() LockSupport.unpark() 进行线程间的通信
         * 启动2个线程
         *  t1 线程开始执行，获取u对象锁，打印一句 in t1 然后调用 LockSupport.park()进入阻塞，
         *     此时主线程阻塞10秒，启动t2线程(t2线程由于u对象锁被t1获取，只能等待t1)，再阻塞主线程3秒执行中断t1线程操作,t1被唤醒执行完毕释放u对象锁。
         *     t2获取u对象锁进入线程执行体开始执行，执行到LockSupport.park()开始阻塞，此时主线程执行 LockSupport.unpark(t2) 唤醒t2
         *     t1.join(); 主线程阻塞等待t1执行完毕再继续执行主线程，t2.join();主线程阻塞等待t2执行完毕再继续执行主线程.
         */
        t1.start();
        Thread.sleep(1000L);
        t2.start();
        Thread.sleep(3000L);
        t1.interrupt();
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
    }
}