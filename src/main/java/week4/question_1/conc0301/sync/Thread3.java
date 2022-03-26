
package week4.question_1.conc0301.sync;

public class Thread3 {
    class Inner {
        private void m4t1() {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : Inner.m4t1()=" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }

        private void m4t2() {
            int i = 5;
            while (i-- > 0) {
                System.out.println(Thread.currentThread().getName() + " : Inner.m4t2()=" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    private void m4t1(Inner inner) {
        synchronized (inner) { //使用对象锁
            inner.m4t1();
        }
    }

    private void m4t2(Inner inner) {
        inner.m4t2();
    }

    public static void main(String[] args) {
        /**
         * 这个示例中，创建2个线程 都执行 myt3 对象的方法，
         * 线程t1 执行 myt3对象同步方法 m4t1 对象锁是 inner 对象
         * 线程t2 执行 myt3对象普调方法 m4t2
         * 2个线程不存在资源竞争问题，因此会交替执行
         */
        final Thread3 myt3 = new Thread3();
        final Inner inner = myt3.new Inner();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                myt3.m4t1(inner);
            }
        }, "t1");
        Thread t2 = new Thread(new Runnable() {
            public void run() {
                myt3.m4t2(inner);
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
