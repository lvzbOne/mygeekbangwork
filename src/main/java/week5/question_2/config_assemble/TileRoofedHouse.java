package week5.question_2.config_assemble;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 砖瓦房
 *
 * @author 起凤
 * @description: TODO
 * @date 2022/3/31
 */
@Data

public class TileRoofedHouse  implements Architecture{
    private TileHouse tileHouse;
    private BrickHouse brickHouse;

    public TileRoofedHouse(TileHouse tileHouse, BrickHouse brickHouse) {
        this.tileHouse = tileHouse;
        this.brickHouse = brickHouse;
    }

    @Override
    public void info() {
        System.out.println("我是砖瓦房子！");
    }
}
