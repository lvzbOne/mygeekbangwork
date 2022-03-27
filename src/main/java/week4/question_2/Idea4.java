package week4.question_2;

/**
 * 方法四：通过 synchronized 对象锁，先让子线程持有对象锁执行，子线程执行完毕释放锁后，主线程再持有对象锁执行输出
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/26
 */
public class Idea4 {
    public static void main(String[] args) throws InterruptedException {
        Object oo = new Integer(1);
        CalTask target = new CalTask(oo);
        Thread t = new Thread(target, "线程一");
        t.start();
        // 主线程阻塞让步子线程持有oo对象锁执行
        Thread.sleep(1000L);
        synchronized (oo) {
            // 24157817
            System.out.println("异步计算结果为：" + target.getResult());
        }
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
                result = Homework.sum();
            }
        }
    }
}
