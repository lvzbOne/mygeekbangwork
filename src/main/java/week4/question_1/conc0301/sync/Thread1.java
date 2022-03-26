
package week4.question_1.conc0301.sync;

public class Thread1 implements Runnable {

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " synchronized loop " + i);
            }
        }
    }

    public static void main(String[] args) {
        /**
         *  新增2个线程 线程执行体都是t1，由于线程执行体里有对象锁this
         *  所以当其中一个线程执行时，获取了t1的对象锁，另一个线程无法进入线程执行体等待
         *  线程执行完毕后，释放t1的对象锁，另一个线程就能获得t1的对象锁，开始执行
         *  因此，会按顺序打印20次不会有交替的情况，哪个线程先打印不确定（操作系统调用的随机性），
         */
        Thread1 t1 = new Thread1();
        Thread ta = new Thread(t1, "A");
        Thread tb = new Thread(t1, "B");
        ta.start();
        tb.start();
    }
}
