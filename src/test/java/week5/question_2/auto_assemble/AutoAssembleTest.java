package week5.question_2.auto_assemble;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * 自动扫描装配 单元测试
 * junit5 新增注解 @ExtendWith(SpringExtension.class)
 * 可以替代原来的 @RunWith(SpringJUnit4ClassRunner.class) 与之类似（这个我也是猜测的，现在都用这个）？？
 * 灵感来源：https://blog.csdn.net/csdn_am/article/details/79757097
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
// @SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CDPlayerConfig.class})
class AutoAssembleTest {
    @Autowired
    private CompactDisc player;

    @Autowired
    private MediaPlayer mediaPlayer;

    @Test
    public void player() {
        System.out.println(mediaPlayer.play());
    }
}