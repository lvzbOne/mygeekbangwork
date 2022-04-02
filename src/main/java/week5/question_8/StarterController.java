package week5.question_8;

import com.example.schoolstarter.bean.School;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 自定义starter 调用测试
 * 浏览器：localhost:8080/hello
 * 服务器会调用自定义starter注入进来的bean执行ding() 方法
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/4/2
 */
@RestController
public class StarterController {
    @Resource
    private School school;

    @RequestMapping("/hello")
    public String hello() {
        school.ding();
        return "hello 看日志";
    }
}
