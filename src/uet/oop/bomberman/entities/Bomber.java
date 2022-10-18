package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.entities.bombmaster.Bomb;
import uet.oop.bomberman.entities.enemiesmaster.Enemy;
import uet.oop.bomberman.entities.itemmaster.Item;
import uet.oop.bomberman.entities.itemmaster.SpeedItem;
import uet.oop.bomberman.entities.stillobjectmaster.Brick;
import uet.oop.bomberman.entities.stillobjectmaster.Grass;
import uet.oop.bomberman.entities.stillobjectmaster.StillObjects;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import static uet.oop.bomberman.GameController.*;

public class Bomber extends Entity {
    /**
     * Bomber status
     */
    enum BomberStatus {
        ALIVE,
        DEAD
    }

    BomberStatus bomberStatus;
    /**
     * Bomber size
     */
    public static final int HEIGHT = 30;
    public static final int WIDTH = 20;
    /**
     * Direction check and bombed check.
     */
    public boolean goLeft = false;
    public boolean goRight = false;
    public boolean goUp = false;
    public boolean goDown = false;
    private boolean bombed = false;
    private KeyCode latestDirectKey = KeyCode.RIGHT;

    public List<Entity> bombsList = new LinkedList<>();

    Entity newBomb;
    CollisionManager collisionManager;

    int indexOfSprite = 0;
    public static int SPEED = 2;
    public static int MAX_BOMB = 3;
    public static int BOMB_RADIUS = 3;

    void reset() {
        SPEED = 2;
        BOMB_RADIUS = 1;
        MAX_BOMB = 3;
        bomberStatus = BomberStatus.ALIVE;
        setSprite(Sprite.player_right.getFxImage());
    }

    public Bomber(int x, int y, CollisionManager collisionManager) {
        super(x, y, null);
        reset();
        this.collisionManager = collisionManager;
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
            latestDirectKey = keyCode;
        }
        if (keyCode == KeyCode.SPACE) {
            bombed = isPress;
        }
    }

    private void updateBomberStatus() {

        /**
         * Died by bomb.
         */

        for (Entity i : bombsList) {
            if (((Bomb) i).insideBombRange_Pixel(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2)
                    && ((Bomb) i).getBombStatus() == Bomb.BombStatus.EXPLODED) {
                bomberStatus = BomberStatus.DEAD;
                indexOfSprite = 0;
                break;
            }
        }

        /**
         * Died because of colliding with oneal and balloom.
         */
        for (int i = 1; i < GameController.entities.get(GameController.LEVEL).size(); i++) {
            if (((Enemy) GameController.entities.get(GameController.LEVEL).get(i)).collideBomber(x, y))
                bomberStatus = BomberStatus.DEAD;
        }
    }

    private void setBomb() {
        if (bombed) {
            if (bombsList.size() < MAX_BOMB) {
                newBomb = new Bomb((x + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE,
                        (y + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE,
                        Sprite.bomb.getFxImage(), collisionManager.getMap());
                boolean checkRepeated = false;
                for (Entity i : bombsList) {
                    if (i.x == newBomb.x && i.y == newBomb.y) {
                        checkRepeated = true;
                        break;
                    }
                }
                if (!checkRepeated)
                    bombsList.add(newBomb);
                bombed = false;
            }
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
            if (!collisionManager.collide(x, y, "DOWN")) y += SPEED;
            else {
                if (!goUp && !goLeft && !goRight) {
                    if ((x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - x < 11
                            && !(collisionManager.downRight instanceof StillObjects)) {
                        x = (x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (x + WIDTH - ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < 11
                            && !(collisionManager.downLeft instanceof StillObjects)) {
                        x = ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - WIDTH - 1;
                    }
                }
            }
        }
        if (goLeft) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_left,
                    Sprite.player_left_1,
                    Sprite.player_left_2, indexOfSprite, 20).getFxImage()
            );
            if (!collisionManager.collide(x, y, "LEFT")) x -= SPEED;
            else {
                if (!goUp && !goDown && !goRight) {
                    if ((y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - y < 11
                            && !(collisionManager.downLeft instanceof StillObjects)) {
                        y = (y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (y + HEIGHT - ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < 11
                            && !(collisionManager.topLeft instanceof StillObjects)) {
                        y = ((y + 30) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - HEIGHT - 1;
                    }
                }
            }
        }
        if (goUp) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_up,
                    Sprite.player_up_1,
                    Sprite.player_up_2, indexOfSprite, 20).getFxImage());
            if (!collisionManager.collide(x, y, "UP")) y -= SPEED;
            else {
                if (!goDown && !goLeft && !goRight) {
                    if ((x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - x < 11
                            && !(collisionManager.topRight instanceof StillObjects)) {
                        x = (x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (x + WIDTH - ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < 11
                            && !(collisionManager.topLeft instanceof StillObjects)) {
                        x = ((x + 20) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - WIDTH - 1;
                    }
                }
            }
        }
        if (goRight) {
            setSprite(Sprite.movingSprite(
                    Sprite.player_right,
                    Sprite.player_right_1,
                    Sprite.player_right_2, indexOfSprite, 20).getFxImage()
            );
            if (!collisionManager.collide(x, y, "RIGHT")) x += SPEED;
            else {
                if (!goUp && !goLeft && !goDown) {
                    if ((y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - y < 11
                            && !(collisionManager.downRight instanceof StillObjects)) {
                        y = (y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (y + HEIGHT - ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < 11
                            && !(collisionManager.topRight instanceof StillObjects)) {
                        y = ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - HEIGHT - 1;
                    }
                }
            }
        }
    }

    private void updateBombsList() {
        bombsList.forEach(Entity::update);
        if (!bombsList.isEmpty())
            if (((Bomb) bombsList.get(0)).getBombStatus() == Bomb.BombStatus.DISAPPEAR) {
                bombsList.remove(0);
            }
    }

    private void updateItemsList() {
        for (Entity i : itemsList) {
            if (((Item) i).insideItem_Pixel(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2)) {
                audioController.playParallel(AudioController.AudioName.EAT_ITEM, 1);
                i.update();
                itemsList.remove(i);

                break;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (bomberStatus == BomberStatus.ALIVE) {
            for (Entity i : bombsList) {
                i.render(gc);
            }
            for (Entity i : itemsList) {
                i.render(gc);
            }
            super.render(gc);
        }
        if (bomberStatus == BomberStatus.DEAD) {
            super.render(gc);
        }
    }

    @Override
    public void update() {
        if (bomberStatus == BomberStatus.ALIVE) {
            moving();
            setBomb();
            updateBombsList();
            updateItemsList();
            updateBomberStatus();
        }
        if (bomberStatus == BomberStatus.DEAD) {
            indexOfSprite++;
            setSprite(Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, indexOfSprite, 20).getFxImage());
            if (indexOfSprite == 20) {
                GameController.gameStatus = GameStatus.GAME_LOSE;
            }
            itemsList.clear();
            SPEED = 2;
            MAX_BOMB = 1;
            BOMB_RADIUS = 1;
        }
    }
}
