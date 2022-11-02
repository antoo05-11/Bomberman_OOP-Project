package uet.oop.bomberman.entities.stillobject;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Map;

public class Portal extends StillObject {

    /**
     * Constructor of portal.
     */
    public Portal(int xUnit, int yUnit, Map map) {
        super(xUnit, yUnit, Sprite.portal.getFxImage(), map);
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
    }
}
