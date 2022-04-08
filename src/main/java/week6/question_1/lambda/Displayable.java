package week6.question_1.lambda;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/6
 */
@FunctionalInterface
public interface Displayable {
    /**
     * 执行
     */
    void display();

    default int add(int a, int b) {
        return a + b;
    }
}
