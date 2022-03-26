package week4.question_1.conc0303.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamParallelDemo {
    public static void main(String[] args) {
        /**
         * 本示例显示
         * java8 并行流的使用  list.stream().parallel()
         */
        List<Integer> list = new ArrayList<>();
        // TODO：IntStream.range(1, 10000)这种用法是？？？？
        IntStream.range(1, 10000).forEach(i -> list.add(i));
        BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue(10000);
        List<Long> longList = list.stream().parallel()
                .map(i -> i.longValue())
                .sorted()
                .collect(Collectors.toList());
//      // 串行，单线程
//      longList.stream().forEach(
        // 并行，默认使用CPU * 2个线程
        longList.stream().forEach(
                i -> {
                    try {
                        blockingQueue.put(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println("blockingQueue:" + blockingQueue.toString());
    }


}
