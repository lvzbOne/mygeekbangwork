package week5.question_8;

import com.example.schoolstarter.bean.School;
import com.example.schoolstarter.config.AutoConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.RestController;
import week5.question_2.auto_assemble.CDPlayerConfig;
import week5.question_2.config_assemble.ArchitectureConfig;
import week5.question_2.config_assemble.BrickHouse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 这个单元测试 自定义start有点问题,在week1里启动主函数，页面访问 localhost:8080/hello 就能答打印一条日志
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/2
 */
@SpringBootTest(classes = AutoConfig.class)
class AutoConfigTest {

    @Autowired
    private School school;

    @Test
    void autoConfigTest() {
        System.out.println("=================");
        school.ding();
    }

}