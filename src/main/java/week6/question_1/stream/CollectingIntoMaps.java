package week6.question_1.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/9
 */
public class CollectingIntoMaps {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {
        private int id;
        private String name;

    }

    public static Stream<Person> people() {
        return Stream.of(new Person(1001, "Peter"), new Person(1002, "Paul"), new Person(1003, "Mary"));
    }

    public static void main(String[] args) {
        System.out.println("============================================== 收集到映射表中 ==============================================");
        // TODO Collectors.toMap 操作都可以换成 Collectors.toConcurrentMap 表示并行收集，并行时元素就不再是按照流中的顺序收集

        Map<Integer, String> idToName = people().collect(Collectors.toMap(Person::getId, Person::getName));
        System.out.println("idToName" + idToName);

        // TODO:Function.identity() 指代Person本身
        Map<Integer, Person> idToPerson = people().collect(Collectors.toMap(Person::getId, Function.identity()));
        System.out.println("idToPerson" + idToPerson.getClass().getName() + idToPerson);

        // 当出现key冲突时进行抛出异常
        idToPerson = people().collect(
                Collectors.toMap(Person::getId, Function.identity(), (existingValue, newValue) -> {
                    throw new IllegalArgumentException();
                }, TreeMap::new));
        System.out.println("idToPerson" + idToPerson.getClass().getName() + idToPerson);

        // 当出现key冲突时value新值,覆盖旧值
        Stream<Locale> locals = Stream.of(Locale.getAvailableLocales());
        Map<String, String> languageNames = locals.collect(
                Collectors.toMap(Locale::getDisplayLanguage, l -> l.getDisplayLanguage(l), (k1, k2) -> k2));
        System.out.println("languageNames " + languageNames);

        // TODO：复杂一点的映射 而且流如果被使用过一次后，再次使用需要重新加载，如果吧下面 ”locals = Stream.of(Locale.getAvailableLocales());“ 注释掉执行就会报错
        locals = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryLanguageSets = locals.collect(Collectors.toMap(Locale::getDisplayCountry, l -> Collections.singleton(l.getDisplayLanguage()), (k1, k2) -> {
            Set<String> union = new HashSet<>(k1);
            union.addAll(k2);
            return union;
        }));
        System.out.println("countryLanguageSet: " + countryLanguageSets);
    }
}
