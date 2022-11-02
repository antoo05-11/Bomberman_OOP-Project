package uet.oop.bomberman.entities.movingobject.enemies;

import uet.oop.bomberman.map_graph.CollisionManager;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Arrays;
import java.util.List;

public class Kondoria extends Enemy implements MediumEnemy {
    public static final List<Class> cannotPassEntityList = Arrays.asList(new Class[]{Bomb.class});
    private int timeTrace = 0;

    @Override
    public void loadSprite() {
        leftSprites = new Sprite[3];
        rightSprites = new Sprite[3];
        deadSprites = new Sprite[1];
        leftSprites[0] = Sprite.kondoria_left1;
        leftSprites[1] = Sprite.kondoria_left2;
        leftSprites[2] = Sprite.kondoria_left3;
        rightSprites[0] = Sprite.kondoria_right1;
        rightSprites[1] = Sprite.kondoria_right2;
        rightSprites[2] = Sprite.kondoria_right3;
        deadSprites[0] = Sprite.kondoria_dead;
    }

    /**
     * Constructor for Kondoria.
     */
    public Kondoria(int xUnit, int yUnit, CollisionManager collisionManager) {
        super(xUnit, yUnit, Sprite.kondoria_right1.getFxImage(), collisionManager);
    }

    @Override
    public void move() {
        randomMovingWhenCollidingWithWall();
    };

    @Override
    public List<Class> getCannotPassEntityList() {
        return cannotPassEntityList;
    }
}
