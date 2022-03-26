
package week4.question_1.conc0302.atomic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncCount {

    private int num = 0;

    /**
     * 公平锁，等待时间最久的优先分配，不公平锁就是随机分配，默认是不公平的
     */
    private Lock lock = new ReentrantLock(true);

    public int add() {
        lock.lock();
        try {
            return num++;
        } finally {
            lock.unlock();
        }
    }

    public int getNum() {
        return num;
    }
}
