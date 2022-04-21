package week6.question_1.stream;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/9
 */
public class OptionalTest {
    public static void main(String[] args) {
        System.out.println("=============================== Optional 的示例 Optional.of,Optional.empty,option.orElse,option.orElseGet,option.ifPresent ==================================");
        // TODO 有效的使用 Optional 的关键策略：值不存在的情况下有可替代物，值存在的情况下才会使用该值
        // TODO 策略一：值不存在的情况下，有默认值 orElse、orElseGet、orElseThrow
        // TODO 策略二：只有在值存在的情况下，才消费该值 ifPresent

        List<String> resultList = new ArrayList<>();
        String words = "hello world I am lvzb 31988 quick qiao Qli QQ";

        // 创建一个Optional对象
        Optional<String> optionalWords = Optional.of(words);
        Optional<String> empty1 = Optional.empty();

        // 包装值为空时的替代值
        String o = empty1.orElse("111");
        show(o);
        // 包装值为空时调用get方法获取替代值
        String s = empty1.orElseGet(() -> Locale.getDefault().getDisplayName());
        show(s);
        // 包装值为空抛出异常类型
        //String s1 = empty1.orElseThrow(RuntimeException::new);
        //show(s1);

        /// 不返回任何值 ifPresent(Consumer<? super T> consumer) 存在时进行消费包装值
        optionalWords.ifPresent(v -> System.out.println(v));
        optionalWords.map(v -> v.substring(1, 3)).ifPresent(str -> System.out.println(str));
        // TODO 产生的option对象具有三种值之一 存在时则包装true或false,不存在则包装null
        optionalWords.map(resultList::add).ifPresent(r -> System.out.println(r));

        Optional<String> first = Arrays.asList(words.split(" ")).stream().filter(e -> e.contains("q")).findFirst();
        first.ifPresent(e -> System.out.println(e));

        System.out.println(inverse(4.0).flatMap(OptionalTest::squareRoot));

        System.out.println("=================================== 结果收集 forEach,forEachOrdered,toArray,collect,Collectors.toList，Collectors.toSet,Collectors.toCollection,Collectors.join =============================================");
        Arrays.asList(words.split(" ")).stream().forEach(System.out::println);
        // 并行流打印会导致任意打印
        Arrays.asList(words.split(" ")).parallelStream().forEach(System.out::println);
        // 并行流forEachOrdered 按顺序打印会导致丧失并行流的优势
        Arrays.asList(words.split(" ")).parallelStream().forEachOrdered(System.out::println);

        // 流的结果收集到数组，流无法运行时动态创建泛型数组，所以不加参数的情况下就是Object[]
        Object[] s1 = Arrays.asList(words.split(" ")).parallelStream().toArray();
        //  流的结果收集到数组，流无法运行时动态创建泛型数组，所以加String的构造器引用
        String[] s2 = Arrays.asList(words.split(" ")).parallelStream().toArray(String[]::new);

        // 收集为list
        List<String> collect = Arrays.asList(words.split(" ")).stream().collect(Collectors.toList());
        // 收集为set
        Set<String> set = Arrays.asList(words.split(" ")).stream().collect(Collectors.toSet());
        // 指定收集结果的集合
        LinkedList<String> linkedList = Arrays.asList(words.split(" ")).stream().parallel().collect(Collectors.toCollection(LinkedList::new));

        // 流元素连接成字符串
        String joinStr = Arrays.asList(words.split(" ")).stream().collect(Collectors.joining());
        // 流元素连接成字符串并且逗号分隔
        String commaStr = Arrays.asList(words.split(" ")).stream().collect(Collectors.joining(","));
        // 流元素全部映射成字符串再进行逗号分隔
        String mapStr = Arrays.asList(words.split(" ")).stream().map(Object::toString).collect(Collectors.joining(","));

        // 流元素结果数进行汇总统计，支持有 summarizingInt,summarizingDouble,summarizingInt,summarizingLong中的莫一种，可统计最大，最小，平均值，总和，数量等。
        IntSummaryStatistics summary = Arrays.asList(words.split(" ")).stream().collect(Collectors.summarizingInt(String::length));
        System.out.println(summary);
        System.out.println(summary.getAverage());
        System.out.println(summary.getMax());

        // 产生用于获取当前流中各个元素的迭代器。这是一种终结操作
        Iterator<Integer> integerIterator = Stream.iterate(0, n -> n++).limit(10).iterator();
        while (integerIterator.hasNext()) {
            System.out.println(integerIterator.next());
        }
        Integer[] integers = Stream.iterate(0, n -> n + 1).limit(10).toArray(Integer[]::new);

    }

    private static Optional<Double> squareRoot(Double x) {
        // Math.sqrt()开平方
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }

    private static Optional<Double> inverse(Double d) {
        return d == 0 ? Optional.empty() : Optional.of(1 / d);
    }

    private static void show(String s) {
        System.out.println(s);
    }
}
