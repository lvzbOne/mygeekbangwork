
package week4.question_1.conc0301.sync;

public class Thread2 {

    public void m4t1() {
        synchronized (this) {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public synchronized void m4t2() {
        int i = 5;
        while (i-- > 0) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
            }
        }
    }

    public static void main(String[] args) {
        /**
         *  新增2个线程 线程执行体都是调用 myt2 的对象，由于线程执行体里有对象锁this
         *  所以当其中一个线程执行时，获取了 myt2 的对象锁，另一个线程需要等待直到获取到 myt2 的对象锁 才能进入线程执行体
         *  等待其中一个线程执行完毕后，释放t1的对象锁，另一个线程就能获得t1的对象锁，开始执行
         *  因此，会按顺序打印[要么t1先打印完，再打开始打印t2,或者反过来]不会有交替的情况，哪个线程先打印不确定（操作系统调用的随机性），
         */

        final Thread2 myt2 = new Thread2();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                myt2.m4t1();
            }
        }, "t1");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                myt2.m4t2();
            }
        }, "t2");
        t2.start();
        t1.start();
    }

}
