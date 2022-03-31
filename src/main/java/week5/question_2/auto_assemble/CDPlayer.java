package week5.question_2.auto_assemble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 激光唱片 属于媒体播放一类
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
@Component
public class CDPlayer implements MediaPlayer {

    @Autowired
    private CompactDisc cd;

    public CDPlayer(CompactDisc cd) {
        this.cd = cd;
    }

    @Override
    public String play() {
        return cd.play();
    }

}
