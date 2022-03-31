package week5.question_2.config_assemble;

import org.springframework.stereotype.Component;

/**
 * 砖头房子
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */

public class BrickHouse implements Architecture{
    @Override
    public void info() {
        System.out.println("我是砖房子！");
    }
}
