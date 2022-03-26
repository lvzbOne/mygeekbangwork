package week4.question_1.conc0301.op;

public class Join {

    public static void main(String[] args) {
        /**
         * join这个示例是想让我们体验下：
         * 1. 主线程持有 oo对象锁时，线程thread1一直等待主线程释放无法进如线程执行体，当i=20时主线程调用 wait()阻塞，释放对象锁，
         *    此时线程thread1就能获得oo对象锁，就能进入循环打印，当thread1线程执行完时，如果没有进行oo对象的notify操作，主线程就一直阻塞等待唤醒。
         *
         * 2. 如果把主线程的oo.wait(0) 换成 thread1.join(),
         *    那么 当i=20时主线程持有 oo对象锁阻塞等待子线程执行完毕他再继续执行，
         *    但是因为主线程没有释放oo对象锁，导致子线程执行时获取不到oo的对象锁一直等待主线程释放，就进不了线程体，造成相互等待然后就是死锁了
         *
         */
        Object oo = new Object();

        MyThread thread1 = new MyThread("thread1 -- ");
        //oo = thread1;
        thread1.setOo(oo);
        thread1.start();

        synchronized (oo) {  // 这里用oo或thread1/this
            for (int i = 0; i < 100; i++) {
                if (i == 20) {
                    try {
                        oo.wait(0);
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " -- " + i);
            }
        }
    }

}

class MyThread extends Thread {

    private String name;
    private Object oo;

    public void setOo(Object oo) {
        this.oo = oo;
    }

    public MyThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (oo) { // 这里用oo或this，效果不同
            for (int i = 0; i < 100; i++) {
                System.out.println(name + i);
            }
            // 放synchronized 内这里就正常
            oo.notify();
        }
        // 放synchronized 外这里就抛异常
        // oo.notify();
    }

}