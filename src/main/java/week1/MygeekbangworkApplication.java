package week1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


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
