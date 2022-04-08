package week6.question_1.lambda;

/**
 * 问候服务
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/7
 */
public interface GreetingService {
    /**
     * 打招呼
     *
     * @param message 招呼
     */
    void sayMessage(String message);

    /**
     * defaultSay 方法
     *
     * @param str
     */
    default void defaultSay(String str) {
        System.out.println(str);
    }
}
