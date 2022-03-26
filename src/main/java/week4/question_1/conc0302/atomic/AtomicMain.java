
package week4.question_1.conc0302.atomic;

public class AtomicMain {

    public static void main(String[] args) {
        /**
         * 该示例 SyncCount 对象内的一个add方法是加锁写保护的,而且是公平锁（等待久的先获取锁）
         * 主线程结束时，100个子线程还没运行完，就读，所以值num值是中途就打印了，侧面展示里同步机制的写性能有点慢
         */
        final SyncCount count = new SyncCount();
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        count.add();
                    }
                }
            }).start();
        }

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("num=" + count.getNum());
    }

}
