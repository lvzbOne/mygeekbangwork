package week4.question_1.conc0302.atomic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class LongDemo {

    public static void main(String[] args) {
        /**
         * 该示例 展示 AtomicLong 和 LongAdder原子自增
         * 相比是上个写同步保护的自增，性能更优
         * 上个例子 主线程阻塞5秒结束时，子线程还没运行完，就读，所以值num值是中途就打印了，侧面展示里同步机制的写性能有点慢
         * 本例子  主线程组设1秒结束时，自增就到头了，返回预期结果。
         */
        final AtomicLong atomicLong = new AtomicLong();
        final LongAdder longAdder = new LongAdder();

        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10000; j++) {
                        atomicLong.getAndIncrement();
                        longAdder.increment();
                    }
                }
            }).start();
        }

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("atomicLong=" + atomicLong.get());
        System.out.println("longAdder =" + longAdder.sum());

    }
}
