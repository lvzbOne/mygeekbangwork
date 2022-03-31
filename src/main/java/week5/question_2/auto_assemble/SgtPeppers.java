package week5.question_2.auto_assemble;


import org.springframework.stereotype.Component;

/**
 * 辣椒中士 属于碟片一类
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
@Component
public class SgtPeppers implements CompactDisc {

    private String title = "原来最遥远的不是距离，而是昨天";
    private String artist = "邓紫棋";

    @Override
    public String play() {
        return "Playing " + title + " by " + artist;
    }

}
