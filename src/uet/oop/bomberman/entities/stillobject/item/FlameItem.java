package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Map;

public class FlameItem extends Item{
    public static final int code = 2;
    private final int bonus = 1;

    /**
     * Constructor of flame item.
     */
    public FlameItem(int xUnit, int yUnit, Map map) {
        super(xUnit, yUnit, Sprite.powerup_flames.getFxImage(), map);
    }

}
