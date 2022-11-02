package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Map;

public class SpeedItem extends Item {
    /**
     * Constructor of speed item.
     */
    public SpeedItem(int xUnit, int yUnit, Map map) {
        super(xUnit, yUnit, Sprite.powerup_speed.getFxImage(), map);
    }
}
