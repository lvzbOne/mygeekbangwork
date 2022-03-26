
package week4.question_1.conc0302.lock;

public class ReentrantLockDemo {

    public static void main(String[] args) {
        /**
         * Thread-0 get begin
         * Thread-0 get end
         * Thread-1 get begin
         * Thread-1 get end
         * Thread-2 put begin
         * Thread-2 put end
         * Thread-3 put begin
         * Thread-3 put end
         *
         * ReentrantLock 读写互斥
         *
         */
        final Count count = new Count();

        for (int i = 0; i < 2; i++) {
            new Thread() {
                @Override
                public void run() {
                    count.get();
                }
            }.start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread() {
                @Override
                public void run() {
                    count.put();
                }
            }.start();
        }
    }
}
