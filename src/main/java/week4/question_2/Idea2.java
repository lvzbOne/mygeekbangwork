package week4.question_2;

/**
 * 方式二：主线程里调用 t.join，主线程阻塞等待线程 t 执行结束后返回继续执行主线程
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/26
 */
public class Idea2 {
    public static void main(String[] args) throws InterruptedException {
        CalTask target = new CalTask();
        System.out.println(target.getResult());

        Thread t = new Thread(target, "定制的线程");
        t.start();
        // 主线程里调用 t.join，主线程阻塞等待线程 t 执行结束后返回继续执行主线程
        t.join();
        // 24157817
        System.out.println("异步计算结果为：" + target.getResult());

    }

    private static class CalTask implements Runnable {
        private int result;

        public int getResult() {
            return result;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result = Homework.sum();
        }
    }
}
