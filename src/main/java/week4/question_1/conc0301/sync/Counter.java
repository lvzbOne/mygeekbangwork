package week4.question_1.conc0301.sync;

public class Counter {

    private int sum = 0;
    private Object lock = new Object();

    public void incr() {
        synchronized (lock) {
            sum = sum + 1;
        }
    }

    public int getSum() {
        return sum;
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         *本示例是
         * 主线程从0每次加1到10_0000;
         * 2个副线程 分别加 500000次
         * 由于在写操作是同步的，有对象锁控制，因此每次读操作都是正确的值，
         * 所以单线程的累加和主线程单线程累加的最终结果一致
         */
        int loop = 10_0000;

        // test single thread
        Counter counter = new Counter();
        for (int i = 0; i < loop; i++) {
            counter.incr();
        }

        System.out.println("single thread: " + counter.getSum());

        // test multiple threads
        final Counter counter2 = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < loop / 2; i++) {
                counter2.incr();
            }
        });
        t1.start();
        t2.start();
        Thread.sleep(1000);

        System.out.println("multiple threads: " + counter2.getSum());


    }
}
