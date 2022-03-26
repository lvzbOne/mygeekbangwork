package week4.question_1.conc0303.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SyncListDemo {

    public static void main(String[] args) {
        /**
         * 本示例显示了：
         * 1. Arrays.asList(1,2,3,4,5,6,7,8,8) 获取得到的list 可以进行修改内容，但是不能添加内容[UnsupportedOperationException] 查看源码发现是一个final 数组
         * 2. Arrays.toString(char[]) ,
         *    Collections.shuffle(List<T>): 打乱list数组 实现机制是？？？
         *    Collections.unmodifiableList(list1) ： list 不能被修改,也不能新增 如何做到的？？？
         * 3.
         */

        List list0 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 8);
        list0.set(8, 9);        // 可以修改内容，不能变动元素数量
        // list0.add(10);   // will throw an error

        List list = new ArrayList(); // 正常List，可以操作
        list.addAll(list0);

        List list1 = Collections.synchronizedList(list);

        // 多线程操作
        // to do something

        System.out.println(Arrays.toString(list1.toArray()));
        // 数组打乱
        Collections.shuffle(list1);


        System.out.println(Arrays.toString(list1.toArray()));


        // 假如不再修改

        List list2 = Collections.unmodifiableList(list1);

        System.out.println(list2.getClass());

        /// list2.set(8, 10); // 这里会报错,上一步已经设置成不可修改的了

        System.out.println(Arrays.toString(list2.toArray()));

        /// list2.add(11); // 这里会报错,上一步已经设置成不可修改的了

        System.out.println(Arrays.toString(list2.toArray()));
    }

}
