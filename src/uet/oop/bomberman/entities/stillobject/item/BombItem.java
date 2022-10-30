package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.map_graph.Map;

public class BombItem extends Item{
    public static final int code = 1;
    private int bonus = 1;
    public BombItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        Map.MAX_BOMB += bonus = 1;
    }
}
