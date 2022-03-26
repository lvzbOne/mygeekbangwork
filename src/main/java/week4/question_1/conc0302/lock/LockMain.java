
package week4.question_1.conc0302.lock;

public class LockMain {

    public static void main(String[] args) {
        /**
         * 本示例是一个死锁的示例：
         * count对象是一个有2个锁的对象，且含有2个方法
         * add ： 先用lock1对象锁，持有lock1对象锁阻塞1秒钟，再用lock2对象锁锁住加一操作
         * lockMethod ： 先用lock2对象锁，持有lock2对象锁阻塞1秒钟，再用lock1对象锁锁住加一操作
         *
         * 线程A 是一个调用 add 方法对count对象资源加一的定制任务
         * 线程B 也是是一个调用 lockMethod 方法对count对象资源加一的定制任务
         *
         * 当A，B线程启动后，当A线程持有 lock1对象锁,阻塞1秒钟未释放lock1锁,cpu切换到 线程B,
         *                B线程持有lock2对象锁阻塞1秒钟未释放lock2锁,cpu切换到线程A,
         *                此时A线程阻塞恢复,等待获取lock2锁进行下一步操作但是lock2锁被 B线程持有未被释放,
         *                线程B阻塞恢复时,等待lock1锁进行下一步操作但是lock1锁被线程A持有
         *                这种：线程A持有lock1锁等待获取lock2锁，线程B持有lock2锁等待获取lock1锁，相互等待造成死锁
         *
         *
         */
        Count3 count3 = new Count3();
        ThreadA threadA = new ThreadA(count3);
        threadA.setName("线程A");
        threadA.start();

        ThreadB threadB = new ThreadB(count3);
        threadB.setName("线程B");
        threadB.start();

    }

}
