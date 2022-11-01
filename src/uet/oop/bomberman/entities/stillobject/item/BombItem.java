package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.map_graph.Map;

public class BombItem extends Item {
    public static final int code = 1;
    private int bonus = 1;

    /**
     * Constructor of bomb item.
     */
    public BombItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * Update bomb item.
     */
    @Override
    public void update() {
        Map.MAX_BOMB += bonus = 1;
    }
}
