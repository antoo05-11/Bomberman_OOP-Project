package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import uet.oop.bomberman.graphics.Sprite;

import java.security.Key;
import java.util.ArrayList;
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
            if (isPress) {
                indexOfSprite++;
                //if (indexOfSprite > 2) indexOfSprite = 0;
                switch (keyCode) {
                    case DOWN:
                        goDown = true;
                        setSprite(Sprite.movingSprite(
                                Sprite.player_down,
                                Sprite.player_down_1,
                                Sprite.player_down_2, indexOfSprite, 10).getFxImage()
                        );
                        break;
                    case LEFT:
                        goLeft = true;
                        setSprite(Sprite.movingSprite(
                                Sprite.player_left,
                                Sprite.player_left_1,
                                Sprite.player_left_2, indexOfSprite, 10).getFxImage()
                        );
                        break;
                    case RIGHT:
                        goRight = true;
                        setSprite(Sprite.movingSprite(
                                Sprite.player_right,
                                Sprite.player_right_1,
                                Sprite.player_right_2, indexOfSprite, 10).getFxImage()
                        );
                        break;
                    case UP:
                        goUp = true;
                        setSprite(Sprite.movingSprite(
                                Sprite.player_up,
                                Sprite.player_up_1,
                                Sprite.player_up_2, indexOfSprite, 10).getFxImage());
                        break;
                }
            } else {
                switch (keyCode) {
                    case DOWN:
                        goDown = false;
                        break;
                    case LEFT:
                        goLeft = false;
                        break;
                    case RIGHT:
                        goRight = false;
                        break;
                    case UP:
                        goUp = false;
                        break;
                }
                indexOfSprite = 0;
            }
        }
        if (keyCode == KeyCode.SPACE) {
            bombed = isPress;
        }
    }

    private void setBomb() {
        if (bombed) {
            newBomb = new Bomb(x / Sprite.SCALED_SIZE, y/ Sprite.SCALED_SIZE, Sprite.bomb.getFxImage());
            bombsList.add(newBomb);
            bombed = false;
        }
    }


    private void moving() {
        if (goRight) x += SPEED;
        if (goLeft) x -= SPEED;
        if (goUp) y -= SPEED;
        if (goDown) y += SPEED;
    }

    @Override
    public void update() {
        moving();
        setBomb();
        bombsList.forEach(Entity::update);
    }
}
