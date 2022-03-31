package week5.question_2.config_assemble;

import org.springframework.stereotype.Component;

/**
 * 别墅房子
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */

public class Villa implements Architecture {
    @Override
    public void info() {
        System.out.println("我是别墅房子！");
    }
}
