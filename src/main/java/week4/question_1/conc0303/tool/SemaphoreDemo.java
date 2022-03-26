package week4.question_1.conc0303.tool;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args) {
        /**
         * 本示例
         * 工人1占用一个机器在生产...
         * 工人0占用一个机器在生产...
         * 工人1释放出机器
         * 工人0释放出机器
         * 工人2占用一个机器在生产...
         * 工人3占用一个机器在生产...
         * 工人2释放出机器
         * 工人3释放出机器
         * 工人4占用一个机器在生产...
         * 工人5占用一个机器在生产...
         * 工人5释放出机器
         * 工人4释放出机器
         * 工人6占用一个机器在生产...
         * 工人7占用一个机器在生产...
         * 工人7释放出机器
         * 工人6释放出机器
         *
         * Process finished with exit code 0
         * 信号量用来限流，同时最多只有 new Semaphore(2) 入参个进程能操作
         *  semaphore.acquire(); 和
         *  semaphore.release(); 之间的资源
         */
        int N = 8;            //工人数
        Semaphore semaphore = new Semaphore(2); //机器数目
        for (int i = 0; i < N; i++)
            new Worker(i, semaphore).start();
    }

    static class Worker extends Thread {
        private int num;
        private Semaphore semaphore;

        public Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();  // 在子线程里控制资源占用
                System.out.println("工人" + this.num + "占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人" + this.num + "释放出机器");
                semaphore.release();   // 在子线程里控制释放资源占用
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}