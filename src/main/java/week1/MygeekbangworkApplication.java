package week1;

import com.example.schoolstarter.bean.School;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 主入口
 *
 * @author 起凤
 */
@SpringBootApplication
//@ImportResource(locations = {"classpath:bean.xml"})
//@ComponentScan(nameGenerator = AnnotationBeanNameGenerator.class)
public class MygeekbangworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MygeekbangworkApplication.class, args);
    }

}
