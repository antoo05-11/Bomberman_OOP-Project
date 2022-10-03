package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.entities.bombmaster.Bomb;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Direction.DIRECTION;

import java.util.LinkedList;
import java.util.List;

public class Bomber extends Entity {
    /**
     * Direction check and bombed check.
     */
    private CollisionManager collisionManager;
    boolean goLeft = false;
    boolean goRight = false;
    boolean goUp = false;
    boolean goDown = false;
    boolean isRunning = false;

    private KeyCode latestDirectKey = KeyCode.RIGHT;
    boolean bombed = false;
    public List<Entity> bombsList = new LinkedList<>();
    Entity newBomb;


    private int speed = 2;
    int indexOfSprite = 0;


    public Bomber(int x, int y, Image img, CollisionManager collisionManager) {
        super(x, y, img);
        this.collisionManager = collisionManager;
    }

    public void setSprite(Image img) {
        this.img = img;
    }

    public void saveKeyEvent(KeyCode keyCode, boolean isPress) {
        if (keyCode.isArrowKey()) {
            switch (keyCode) {
                case DOWN:
                    goDown = isPress;
                    if (collisionManager.collide(x, y, "DOWN")) goDown = false;
                    break;
                case LEFT:
                    goLeft = isPress;
                    if (collisionManager.collide(x, y, "LEFT")) goLeft = false;
                    break;
                case RIGHT:
                    goRight = isPress;
                    if (collisionManager.collide(x, y, "RIGHT")) goRight = false;
                    break;
                case UP:
                    goUp = isPress;
                    if (collisionManager.collide(x, y, "UP")) goUp = false;
                    break;
            }
            if (!isPress) {
                indexOfSprite = 0;
            }
            latestDirectKey = keyCode;
        }
        if (keyCode == KeyCode.SPACE) {
            bombed = isPress;
        }
    }

    private void setBomb() {
        if (bombed) {
            newBomb = new Bomb((x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE,
                    (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE,
                    Sprite.bomb.getFxImage());
            newBomb.setGraphicsContext(gc);
            bombsList.add(newBomb);
            bombed = false;
        }
    }

    private void moving() {
        if (goUp || goDown || goRight || goLeft) indexOfSprite++;
        else {
            switch (latestDirectKey) {
                case LEFT:
                    setSprite(Sprite.player_left.getFxImage());
                    break;
                case RIGHT:
                    setSprite(Sprite.player_right.getFxImage());
                    break;
                case UP:
                    setSprite(Sprite.player_up.getFxImage());
                    break;
                case DOWN:
                    setSprite(Sprite.player_down.getFxImage());
                    break;
            }
        }
        if (goDown) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_down,
                    Sprite.player_down_1,
                    Sprite.player_down_2, indexOfSprite, 20).getFxImage()
            );
            y += speed;
        }
        if (goLeft) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_left,
                    Sprite.player_left_1,
                    Sprite.player_left_2, indexOfSprite, 20).getFxImage()
            );
            x -= speed;
        }
        if (goUp) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_up,
                    Sprite.player_up_1,
                    Sprite.player_up_2, indexOfSprite, 20).getFxImage());
            y -= speed;

        }
        if (goRight) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_right,
                    Sprite.player_right_1,
                    Sprite.player_right_2, indexOfSprite, 20).getFxImage()
            );
            x += speed;
        }
    }

    private void updateAndRenderBombsList() {
        bombsList.forEach(Entity::update);
        if (!bombsList.isEmpty())
            if (((Bomb) bombsList.get(0)).getBombStatus() == Bomb.BombStatus.DISAPPEAR) {
                bombsList.remove(0);
            }
        for (Entity i : bombsList) {
            if (((Bomb) i).getBombStatus() == Bomb.BombStatus.NotExplodedYet) ((Bomb) i).render(gc);
        }

    }

    @Override
    public void update() {
        moving();
        setBomb();
        updateAndRenderBombsList();
    }
}
