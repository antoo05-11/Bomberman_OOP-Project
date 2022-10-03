package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.entities.bombmaster.Bomb;
import uet.oop.bomberman.graphics.Sprite;

import java.util.LinkedList;
import java.util.List;

public class Bomber extends Entity {
    private KeyCode keyCode;
    /**
     * Direction check and bombed check.
     */
    boolean goLeft = false;
    boolean goRight = false;
    boolean goUp = false;
    boolean goDown = false;
    boolean bombed = false;
    public List<Entity> bombsList = new LinkedList<>();
    Entity newBomb;

    int indexOfSprite = 0;

    private static final int SPEED = 2;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
    }

    public void setSprite(Image img) {
        this.img = img;
    }

    public void saveKeyEvent(KeyCode keyCode, boolean isPress) {
        if (keyCode.isArrowKey()) {
            switch (keyCode) {
                case DOWN:
                    goDown = isPress;
                    break;
                case LEFT:
                    goLeft = isPress;
                    break;
                case RIGHT:
                    goRight = isPress;
                    break;
                case UP:
                    goUp = isPress;
                    break;
            }
            if (!isPress) {
                indexOfSprite = 0;
            }
        }
        if (keyCode == KeyCode.SPACE) {
            bombed = isPress;
        }
    }

    private void setBomb() {
        if (bombed) {
            newBomb = new Bomb(x / Sprite.SCALED_SIZE,
                    y / Sprite.SCALED_SIZE,
                    Sprite.bomb.getFxImage());
            newBomb.setGraphicsContext(gc);
            bombsList.add(newBomb);
            bombed = false;
        }
    }

    private void moving() {
        if (goUp || goDown || goRight || goLeft) indexOfSprite++;
        if (goDown) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_down,
                    Sprite.player_down_1,
                    Sprite.player_down_2, indexOfSprite, 20).getFxImage()
            );
            y += SPEED;

        }
        if (goLeft) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_left,
                    Sprite.player_left_1,
                    Sprite.player_left_2, indexOfSprite, 20).getFxImage()
            );
            x -= SPEED;
        }
        if (goUp) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_up,
                    Sprite.player_up_1,
                    Sprite.player_up_2, indexOfSprite, 20).getFxImage());
            y -= SPEED;
        }
        if (goRight) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_right,
                    Sprite.player_right_1,
                    Sprite.player_right_2, indexOfSprite, 20).getFxImage()
            );
            x += SPEED;
        }
    }

    @Override
    public void update() {
        moving();
        setBomb();
        bombsList.forEach(Entity::update);
        if(!bombsList.isEmpty())
        if(((Bomb)bombsList.get(0)).getBombStatus() == Bomb.BombStatus.DISAPPEAR) {
            bombsList.remove(0);
        }
    }
}
