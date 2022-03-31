package week5.question_2.config_assemble;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 注解装配bean 单元测试
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
class ArchitectureTest {

    @Test
    public void testConfigAssemble() {

        // AnnotationConfigApplicationContext 注解配置应用程序上下文,显示的加载配置类（该类要用@Configuration修饰）
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ArchitectureConfig.class);
        // 注意 getBean("这里填写的是被@Bean修饰的方法名 就能返回该 Bean,建议命名规则的返回的bean 的首字母小写")
        final Aaaa instance = (Aaaa) context.getBean("aaaa");

        TileRoofedHouse tileRoofedHouse = (TileRoofedHouse) context.getBean("tileRoofedHouse");
        TileHouse tileHouse = tileRoofedHouse.getTileHouse();
        BrickHouse brickHouse = tileRoofedHouse.getBrickHouse();

        tileHouse.info();
        brickHouse.info();


        Architecture architecture = (Architecture) context.getBean("architecture");
        architecture.info();
    }
}