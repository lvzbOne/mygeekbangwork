package week5.question_2.config_assemble;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 建筑配置类
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
@Configuration
public class ArchitectureConfig {

    @Bean
    public Aaaa aaaa() {
        return new Aaaa();
    }

    @Bean
    public BrickHouse brickHouse() {
        return new BrickHouse();
    }

    @Bean
    public TileHouse tileHouse() {
        return new TileHouse();
    }

    @Bean
    public TileRoofedHouse tileRoofedHouse() {
        return new TileRoofedHouse(tileHouse(), brickHouse());
    }

    @Bean
    public Architecture villa() {
        return new Villa();
    }

    @Bean
    public Architecture architecture() {
        return new Villa();
    }

}
