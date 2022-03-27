package week4.question_2;

import java.util.concurrent.locks.Lock;

/**
 * 方法6：synchronized 对象锁+wait notifyAll()
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/26
 */
public class Idea6 {
    public static void main(String[] args) throws InterruptedException {
        Object oo = new Integer(1);
        CalTask target = new CalTask(oo);
        Thread t = new Thread(target, "线程一");
        t.start();

        System.out.println(target.getResult());
        synchronized (oo) {
            // 主线主动阻塞释放锁
            oo.wait();
        }
        // 24157817
        System.out.println(target.getResult());

    }

    private static class CalTask implements Runnable {
        private int result;
        private Object oo;

        public CalTask(Object oo) {
            this.oo = oo;
        }

        public int getResult() {
            return result;
        }

        @Override
        public void run() {
            synchronized (oo) {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = Homework.sum();
                // 唤醒主线程
                oo.notify();
            }
        }
    }
}
