package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Map;

public class BombItem extends Item {

    /**
     * Constructor of bomb item.
     */
    public BombItem(int xUnit, int yUnit, Map map) {
        super(xUnit, yUnit, Sprite.powerup_bombs.getFxImage(), map);
    }

}
