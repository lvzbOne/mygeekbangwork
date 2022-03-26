package week4.question_1.conc0303.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CopyOnWriteArrayListDemo1 {
    private static final int THREAD_POOL_MAX_NUM = 10;
    //    private List<String> mList = new ArrayList<String>();  // ArrayList 无法运行
    private List<String> mList = new CopyOnWriteArrayList<>();

    public static void main(String args[]) throws InterruptedException {
        /**
         * 本示例 展示
         *  1. ArrayList是线程不安全的
         *  2. CopyOnWriteArrayList 是线程安全的，但是如何保证的呢？
         *  3. 10个写线程，线程执行体里就只新增一个，为什么打印的结果里会有add 多次的结果出现？？？
         *
         *  4. 线程池里的线程复用情况 例如线程 pool-1-thread-3 先是执行的读操作，结束后，被回收用于写操作
         */
        new CopyOnWriteArrayListDemo1().start();
    }

    private void initData() {
        for (int i = 0; i <= THREAD_POOL_MAX_NUM; i++) {
            this.mList.add("...... Line " + (i + 1) + " ......");
        }
    }

    private void start() throws InterruptedException {
        initData();
        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_MAX_NUM);
        // TODO: 线程池的大小是10个固定线程数，但是添加了20个线程，线程池的策略是什么样的？？
        for (int i = 0; i < THREAD_POOL_MAX_NUM; i++) {
            service.execute(new ListReader(this.mList));
            service.execute(new ListWriter(this.mList, i));
        }
        service.shutdown();
        Thread.sleep(5000);
        System.out.println("是否阻塞？？？？");
        for (int i = 0; i < mList.size(); i++) {
            System.out.println(i + ":" + mList.get(i));
        }
    }

    private void start1() {
        for (int i = 0; i <= THREAD_POOL_MAX_NUM; i++) {
            this.mList.add("...... Line " + (i + 1) + " ......");
        }

        for (int i = 0; i < THREAD_POOL_MAX_NUM; i++) {
            new Thread(new ListReader(this.mList)).start();
            new Thread(new ListWriter(this.mList, i)).start();
        }
    }

    private class ListReader implements Runnable {
        private List<String> mList;

        public ListReader(List<String> list) {
            this.mList = list;
        }

        @Override
        public void run() {
//            System.out.println("[id:" + Thread.currentThread().getId() + "] " + Thread.currentThread().getName()+"读操作");
            if (this.mList != null) {
                for (String str : this.mList) {
                    System.out.println("[id:" + Thread.currentThread().getId() + "] " + Thread.currentThread().getName() + " : " + str);
                }
            }
        }
    }

    private class ListWriter implements Runnable {
        private List<String> mList;
        private int mIndex;

        public ListWriter(List<String> list, int index) {
            this.mList = list;
            this.mIndex = index;
        }

        @Override
        public void run() {
            System.out.println("加操作===[id:" + Thread.currentThread().getId() + "]...... add {index: " + mIndex + ", seiz:" + mList.size() + "} ......");
            if (this.mList != null) {
                //this.mList.remove(this.mIndex);
                this.mList.add("[id:" + Thread.currentThread().getId() + "]...... add {index: " + mIndex + ", seiz:" + mList.size() + "} ......");
            }
        }
    }
}