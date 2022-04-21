package week5.question_1.proxy02;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/29
 */
public class DogUtil {
    public void before() {
        System.out.println("========= 前置处理方法 ==========");
    }

    public void after() {
        System.out.println("========= 后置处理方法 ==========");
    }
}
