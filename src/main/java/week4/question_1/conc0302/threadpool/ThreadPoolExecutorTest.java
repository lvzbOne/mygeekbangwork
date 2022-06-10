package week4.question_1.conc0302.threadpool;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/6/6
 */
@Slf4j
public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        int coreSize = Runtime.getRuntime().availableProcessors();
        int maxSize = Runtime.getRuntime().availableProcessors() * 2;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(500);

        log.warn("coreSize: {}, maxSize:{}", coreSize, maxSize);
        // 默认拒绝策略是 ThreadPoolExecutor.AbortPolicy: 丢弃任务并抛出 RejectedExecutionException异常
        // 这里更能成    ThreadPoolExecutor.CallerRunsPolicy 由调用线程（提交任务的线程）处理该任务
        // 阿里巴巴规范手册里推荐的就是这种方式
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(coreSize, maxSize, 1000, TimeUnit.MILLISECONDS, workQueue, new CustomThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 1000; i++) {
            final int n = i;
            threadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "" + n);
            });
        }
        threadPool.shutdown();
    }
}

class CustomThreadFactory implements ThreadFactory {
    private AtomicInteger serial = new AtomicInteger(0);

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("customThread-" + serial);
        return t;
    }
}
