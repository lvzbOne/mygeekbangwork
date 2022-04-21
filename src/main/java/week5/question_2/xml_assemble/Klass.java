package week5.question_2.xml_assemble;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component // 测试XML时注释掉@Component
public class Klass {

    List<Student> students;

    public void dong() {
        System.out.println(this.getStudents());
    }

}
