package week6.question_1.stream;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream 流 很像集合的性质但是他只产生新流不对原数据进行任何改动
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/8
 */
public class StreamTest {

    public static <T> void show(String str, Stream<T> stream) {
        final int SIZE = 10;
        List<T> firstElement = stream
                .limit(SIZE + 1)
                .collect(Collectors.toList());

        System.out.println("str : " + str);
        for (int i = 0; i < firstElement.size(); i++) {
            if (i > 0) System.out.print(",");
            if (i < SIZE) System.out.println(firstElement.get(i));
            else System.out.println("...");
        }
    }

    public static Stream<String> letters(String s) {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            stringList.add(s.substring(i, i + 1));
        }
        return stringList.stream();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("=============================== Stream 的示例 初步认识流的概念 Stream.of(),Stream.generate(),Stream.iterate(),Files.lines() ==================================");
        Path path = Paths.get("D:\\ideaProjects\\jikeshijian\\mygeekbangwork\\src\\main\\resources\\hikari.properties");
        String content = new String(Files.readAllBytes(path));

        String words = "hello world I am lvzb 31988 quick qiao Qli QQ";
        Stream<String> stringStream = Stream.of(words.split(" "));
        show(words, stringStream);

        Stream<String> empty = Stream.empty();
        show("silence", empty);

        // 这个很诡异 Stream.generate()产生无限的顺序流,不加limit截断进行收集就完了收集不了....
        Stream<String> generate = Stream.generate(() -> "Echo");
        show("echos", generate);

        // 这个很诡异 Stream.generate()产生无限的顺序流,不加limit截断进行收集就完了收集不了....
        Stream<Double> d = Stream.generate(Math::random);
        show("randoms", d);

        // 这个很诡异 Stream.iterate()产生无限的顺序流
        Stream<BigInteger> iterate = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
        show("integers", iterate);

        // TODO:正则表达式如何使用？？？
        Stream<String> splitAsStream = Pattern.compile("\\PL+").splitAsStream(content);
        show("wordsAnotherWay", splitAsStream);

        // Files.lines 产生一个流,指定文件中的行
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            show("path lines", lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=============================== Stream 的示例 filter,map,flatMap ==================================");
        List<String> wordList = Arrays.asList(content.split("\\PL+"));
        // 获取单词长度大于12的元素的流
        Stream<String> filterList = wordList.stream().filter(item -> item.length() > 12);
        // 获取list首字母小写的流
        Stream<String> lowCaseStream = wordList.stream().map(String::toLowerCase);
        // 获取只有首字母的流
        Stream<String> firstStream = wordList.stream().map(s -> s.substring(0, 1));

        // letters()返回的是 Stream<String>类型,该流经过映射后就是 Stream<Stream<String>>
        // 例如：letters("boat")的返回值流是["b","o","a","t"]
        // wordList.stream().map(w -> letters(w)) 之后返回的就是[["b","o","a","t"]]
        Stream<Stream<String>> streamStream = wordList.stream().map(w -> letters(w));

        // TODO 平铺有点特殊
        // wordList.stream().map(w -> letters(w)) 假设之后返回的是[["y","o","u"],["b","o","a","t"]]
        // wordList.stream().flatMap(w -> letters(w)) 之后返回的就是["y","o","u","b","o","a","t"]
        Stream<String> flatMap = wordList.stream().flatMap(w -> letters(w));

        System.out.println("=============================== Stream 的示例 抽取子流和连接流 limit,skip,concat ==================================");
        // 生成一个无限流对象截断前100个数据返回，如果流元素小于100则流结束后返回，大于100截断返回
        Stream<Double> limit = Stream.generate(Math::random).limit(100);

        // skip 产生一个流，它的元素是当前流中除了前n个元素之外的所有元素
        Stream<String> skip = Stream.of(words.split(" ")).skip(1);

        // Stream.concat(Stream1,Stream2) 合并2个流，注意第一个流不能是无线的否则第二个流无法合并了
        Stream<String> concat = Stream.concat(letters("hello"), letters("hi"));

        System.out.println("=============================== Stream 的示例 流转换 distinct,sorted,peek ==================================");
        // 去重
        Stream<String> distinct = Stream.of("merrily", "merrily", "merrily", "gently").distinct();

        // TODO Comparator.comparing(String::length) 好像是比较的第一个值是0还是啥？？？？要注意
        Stream<String> sorted = Stream.of(words.split(" ")).sorted(Comparator.comparing(String::length));

        // peek 用于调试，可验证无限流是惰性处理的,调试可设置断点查看
        Object[] objects = Stream.iterate(1.0, p -> p * 2).peek(e -> {
            System.out.println("Fetching " + e);
        }).limit(20).toArray();

        System.out.println("=============================== Stream 的示例 终结操作 count,max,min,findFirst,findAny,anyMatch,allMatch,noneMatch ==================================");
        // TODO Optional
        Optional<String> max = Stream.of(words.split(" ")).max(String::compareToIgnoreCase);
        System.out.println("largest " + max.orElse(""));

        // 找到第一个以字母Q开头的单词，前提是存在这样的单词
        Optional<String> first = Stream.of(words.split(" ")).filter(s -> s.startsWith("Q")).findFirst();

        // 并行流查找配合Any很有效(快速度)
        Optional<String> startsWithQ = Stream.of(words.split(" ")).parallel().filter(s -> s.startsWith("Q")).findAny();

        // 并行查找是否存在匹配任意一个匹配(快速度)
        boolean b1 = Stream.of(words.split(" ")).parallel().anyMatch(s -> s.startsWith("Q"));

        // 并行查找是否存在全部匹配(快速度)
        boolean b2 = Stream.of(words.split(" ")).parallel().allMatch(s -> s.startsWith("Q"));

        // 并行查找是否全不匹配任意一个匹配(快速度)
        boolean b3 = Stream.of(words.split(" ")).parallel().noneMatch(s -> s.startsWith("Q"));

    }
}
