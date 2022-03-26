package week4.question_1.conc0301.sync;

public class TestSetGet {

    public static void main(String[] args) throws Exception {
        /**
         * 本示例展示的是一个 多线程读写的操作
         *  SetGet 有2个同步方法，一个读，一个写
         *  主线程进行写操作时先获取了 s对象的对象锁，然后进行打印 setting V的值，
         *  t线程等待，主线程再持有s对象的对象锁阻塞1秒，进行赋值后再进行打印
         *  t线程等主线程执行set方法完毕后，才获取s 的对象锁，进行处理读操作，和阻塞
         *  但是不影响主线程的最后一步打印
         *
         *
         */
        final SetGet s = new SetGet();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    s.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        long start = System.currentTimeMillis();
        s.set(10);
        System.out.println(" ... " + (System.currentTimeMillis() - start));

    }


    public static class SetGet {

        int a = 0;

        public synchronized void set(int v) throws Exception {
            System.out.println(Thread.currentThread().getName() + " setting " + v);
            Thread.sleep(1000);
            a = v;
            System.out.println(Thread.currentThread().getName() + " set " + v);
        }

        public synchronized int get() throws Exception {
            System.out.println(Thread.currentThread().getName() + " getting ");
            Thread.sleep(10000);
            System.out.println(Thread.currentThread().getName() + " get a:" + a);
            return a;
        }

    }
}
