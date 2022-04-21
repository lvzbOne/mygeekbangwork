package week4.question_2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 方法五：通过 ReentrantLock 锁，先让子线程持有锁执行，子线程执行完毕释放锁后，主线程再持有锁执行输出
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/26
 */
public class Idea5 {
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        CalTask target = new CalTask(lock);
        Thread t = new Thread(target, "线程一");
        t.start();
        // 主线程阻塞让步 子线程持有锁执行
        Thread.sleep(1000L);
        System.out.println("不一定能保证获取到值：" + target.getResult());

        lock.lock();
        try {
            // 24157817
            System.out.println("主线程会等待子线程等待2秒后保证能获取到值：" + target.getResult());
        } finally {
            lock.unlock();
        }
    }

    private static class CalTask implements Runnable {
        private int result;
        private Lock lock;

        public CalTask(Lock lock) {
            this.lock = lock;
        }

        public int getResult() {
            return result;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = Homework.sum();
            } finally {
                lock.unlock();
            }
        }
    }
}
