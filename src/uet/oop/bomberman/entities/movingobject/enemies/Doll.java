package uet.oop.bomberman.entities.movingobject.enemies;

import uet.oop.bomberman.map_graph.CollisionManager;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Arrays;
import java.util.List;

public class Doll extends Enemy implements MediumEnemy {
    public static final List<Class> cannotPassEntityList = Arrays.asList(new Class[] {Wall.class, Bomb.class});

    /**
     * Constructor of Doll.
     */
    public Doll(int xUnit, int yUnit, CollisionManager collisionManager) {
        super(xUnit, yUnit, Sprite.doll_right1.getFxImage(), collisionManager);
    }

    @Override
    public void loadSprite() {
        leftSprites = new Sprite[3];
        rightSprites = new Sprite[3];
        deadSprites = new Sprite[1];
        leftSprites[0] = Sprite.doll_left1;
        leftSprites[1] = Sprite.doll_left2;
        leftSprites[2] = Sprite.doll_left3;
        rightSprites[0] = Sprite.doll_right1;
        rightSprites[1] = Sprite.doll_right2;
        rightSprites[2] = Sprite.doll_right3;
        deadSprites[0] = Sprite.doll_dead;
    }

    /**
     * This is move.
     */
    @Override
    public void move() {
        randomMovingWhenCollidingWithWall();
    }

    @Override
    public List<Class> getCannotPassEntityList() {
        return cannotPassEntityList;
    }
}
