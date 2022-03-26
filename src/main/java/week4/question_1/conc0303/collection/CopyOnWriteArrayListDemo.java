package week4.question_1.conc0303.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 本示例未看懂
         * 1. why Vector 也不安全
         * 2. CopyOnWriteArrayList 的读输出100个数，线程删除后剩余50个为什么都是奇数的
         */

        // ArrayList，LinkedList，Vector不安全，运行报错
        // why Vector 也不安全
//        List<Integer> list = new ArrayList<Integer>();
//        List<Integer> list = new LinkedList<>();
//        List<Integer> list = new Vector<>();

        // 只有CopyOnWriteArrayList 安全，不报错
        List<Integer> list = new CopyOnWriteArrayList();

        for (int i = 0; i < 100; i++) {
            list.add(i);
        }

        T1 t1 = new T1(list);
        T2 t2 = new T2(list);
        t1.start();
        t2.start();

        Thread.sleep(5000);
        System.out.println("最后多少个？" + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    public static class T1 extends Thread {
        private List<Integer> list;

        public T1(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (Integer i : list) {
                System.out.println(i);
            }
        }
    }

    public static class T2 extends Thread {
        private List<Integer> list;

        public T2(List<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                list.remove(i);
            }
        }
    }

}
