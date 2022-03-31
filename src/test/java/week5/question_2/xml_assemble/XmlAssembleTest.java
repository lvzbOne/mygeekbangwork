package week5.question_2.xml_assemble;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.context.XmlServletWebServerApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * xml 装配bean 单元测试
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
class XmlAssembleTest {

    private static final String XML_FILE_Path = "bean.xml";

    @Test
    void xmlAssembleTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(XML_FILE_Path);
        Student student01 = (Student) context.getBean("student01");
        Student student02 = (Student) context.getBean("student02");

        student01.print();
        student02.print();

        Klass klass = (Klass) context.getBean("klass");
        List<Student> studentList = klass.getStudents();

        System.out.println("=========== 以下是Klass Students 内容 =============");
        // 把forEach 换成 peek试试，不会打印
        studentList.stream().forEach(item -> {
            item.print();
        });


    }
}