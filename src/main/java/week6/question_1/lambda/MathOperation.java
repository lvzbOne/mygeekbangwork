package week6.question_1.lambda;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/4/7
 */
public interface MathOperation<T> {
    /**
     * 灵活的数学运算函数
     *
     * @param a
     * @param b
     * @return
     */
    T operation(int a, int b);
}
