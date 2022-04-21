package week4.question_1.conc0301;

public class DaemonThread {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 本示例展示：
         * 启动一个线程  test-thread-1 设置成守护线程（后台线程），
         * 如果前台线程全部结束的话，后台线程也会结束（但是结束没有那么迅速可能会有点延迟）
         * 因此这里是不会打印出那句话的
         *
         */
        Runnable task = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread t = Thread.currentThread();
            System.out.println("当前线程:" + t.getName());
        };
        Thread thread = new Thread(task);
        thread.setName("test-thread-1");
        thread.setDaemon(true);
        thread.start();

        //Thread.sleep(5500);
    }


}
