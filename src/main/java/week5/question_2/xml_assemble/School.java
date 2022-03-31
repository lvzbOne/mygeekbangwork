package week5.question_2.xml_assemble;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
public class School implements ISchool {

    // Resource
    @Autowired(required = true) //primary
    private Klass class1;

    @Resource(name = "student100")
    private Student student100;

    @Override
    public void ding() {
        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);
    }
}
