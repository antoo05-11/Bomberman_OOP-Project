package uet.oop.bomberman.entities.movingobject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.entities.CanBePassedThrough;
import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobject.Portal;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.entities.movingobject.enemies.Enemy;
import uet.oop.bomberman.entities.stillobject.item.Item;
import uet.oop.bomberman.graphics.Sprite;

import java.util.LinkedList;
import java.util.List;

import static uet.oop.bomberman.GameController.*;

public class Bomber extends MovingObject {
    public static final int MAX_LIVES = 3;

    public void setNumOfLives(int numOfLives) {
        this.numOfLives = numOfLives;
    }

    public int getNumOfLives() {
        return numOfLives;
    }

    /**
     * Bomber status
     */


    int numOfLives = 3;


    /**
     * Bomber size
     */

    public static final int HEIGHT = Sprite.SCALED_SIZE * 30 / 32;
    public static final int WIDTH = Sprite.SCALED_SIZE * 20 / 32;
    private static final int FIX_SIZE = Sprite.SCALED_SIZE * 11 / 32;
    /**
     * Direction check and bombed check.
     */
    public boolean goLeft = false;
    public boolean goRight = false;
    public boolean goUp = false;
    public boolean goDown = false;
    private boolean bombed = false;
    private KeyCode latestDirectKey = KeyCode.RIGHT;

    private final List<Entity> movingEntitiesList;
    public List<Entity> bombsList;


    CollisionManager collisionManager;


    public static int SPEED = 2;
    public static int BOMB_RADIUS = 1;

    void reset() {
        SPEED = 3;
        BOMB_RADIUS = 1;
        objectStatus = MovingObjectStatus.ALIVE;
        setImg(Sprite.player_right);
    }

    public Bomber(int x, int y, CollisionManager collisionManager) {
        super(x, y, null);
        reset();
        this.collisionManager = collisionManager;
        bombsList = collisionManager.getMap().getBombsList();
        movingEntitiesList = collisionManager.getMap().getMovingEntitiesList();
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
        /*
          Died because of colliding with oneal and balloom.
         */
        for (int i = 1; i < movingEntitiesList.size(); i++) {
            if (((Enemy) movingEntitiesList.get(i)).collideBomber(x, y)) {
                setObjectStatus(MovingObjectStatus.MORIBUND);
                break;
            }
        }
    }

    private void setBomb() {
        if (bombed) {
            collisionManager.getMap().setBomb(x + Bomber.WIDTH / 2,
                    y + Bomber.HEIGHT / 2);
            bombed = false;
        }
    }

    private void moving() {
        if (goUp || goDown || goRight || goLeft) indexOfSprite++;
        else {
            switch (latestDirectKey) {
                case LEFT:
                    setImg(Sprite.player_left);
                    break;
                case RIGHT:
                    setImg(Sprite.player_right);
                    break;
                case UP:
                    setImg(Sprite.player_up);
                    break;
                case DOWN:
                    setImg(Sprite.player_down);
                    break;
            }
        }
        if (goDown) {
            setImg(Sprite.movingSprite(
                    Sprite.player_down,
                    Sprite.player_down_1,
                    Sprite.player_down_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(x, y, "DOWN", SPEED)) y += SPEED;
            else {
                if (!goUp && !goLeft && !goRight) {
                    if ((x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - x < FIX_SIZE
                            && !(collisionManager.downRight instanceof CannotBePassedThrough)) {
                        x = (x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (x + WIDTH - ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && !(collisionManager.downLeft instanceof CannotBePassedThrough)) {
                        x = ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - WIDTH - 1;
                    }
                }
            }
        }
        if (goLeft) {
            setImg(Sprite.movingSprite(
                    Sprite.player_left,
                    Sprite.player_left_1,
                    Sprite.player_left_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(x, y, "LEFT", SPEED)) x -= SPEED;
            else {
                if (!goUp && !goDown && !goRight) {
                    if ((y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - y < FIX_SIZE
                            && !(collisionManager.downLeft instanceof CannotBePassedThrough)) {
                        y = (y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (y + HEIGHT - ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && !(collisionManager.topLeft instanceof CannotBePassedThrough)) {
                        y = ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - HEIGHT - 1;
                    }
                }
            }
        }
        if (goUp) {
            setImg(Sprite.movingSprite(
                    Sprite.player_up,
                    Sprite.player_up_1,
                    Sprite.player_up_2, indexOfSprite, 20));
            if (!collisionManager.collide(x, y, "UP", SPEED)) y -= SPEED;
            else {
                if (!goDown && !goLeft && !goRight) {
                    if ((x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - x < FIX_SIZE
                            && !(collisionManager.topRight instanceof CannotBePassedThrough)) {
                        x = (x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (x + WIDTH - ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && !(collisionManager.topLeft instanceof CannotBePassedThrough)) {
                        x = ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - WIDTH - 1;
                    }
                }
            }
        }
        if (goRight) {
            setImg(Sprite.movingSprite(
                    Sprite.player_right,
                    Sprite.player_right_1,
                    Sprite.player_right_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(x, y, "RIGHT", SPEED)) {
                x += SPEED;
            } else {
                if (!goUp && !goLeft && !goDown) {
                    if ((y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - y < FIX_SIZE
                            && !(collisionManager.downRight instanceof CannotBePassedThrough)) {
                        y = (y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE;
                    } else if (y + HEIGHT - ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && !(collisionManager.topRight instanceof CannotBePassedThrough)) {
                        y = ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE - HEIGHT - 1;
                    }
                }
            }
        }
    }


    private void updateItemsList() {

        int Bomber_xPixel = movingEntitiesList.get(0).getX();
        int Bomber_yPixel = movingEntitiesList.get(0).getY();

        Entity checkItem = mapList.get(LEVEL).getEntityAt(Bomber_xPixel + Bomber.WIDTH / 2, Bomber_yPixel + Bomber.HEIGHT / 2);
        if (checkItem instanceof Item) {
            audioController.playParallel(AudioController.AudioName.EAT_ITEM, 1);
            (checkItem).update();
            int columnPos = (Bomber_xPixel + Bomber.WIDTH / 2) / Sprite.SCALED_SIZE;
            int rowPos = (Bomber_yPixel + Bomber.HEIGHT / 2) / Sprite.SCALED_SIZE;
            mapList.get(LEVEL).replace(rowPos, columnPos, null);
        }
    }

    private void updatePortal() {
        if (collisionManager.getMap().getEntityAt(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2) instanceof Portal) {
            if (movingEntitiesList.size() == 1) {
                reset();
                bombsList.clear();
                switch (latestDirectKey) {
                    case LEFT:
                        setImg(Sprite.player_left);
                        break;
                    case RIGHT:
                        setImg(Sprite.player_right);
                        break;
                    case UP:
                        setImg(Sprite.player_up);
                        break;
                    case DOWN:
                        setImg(Sprite.player_down);
                        break;
                }
                gameStatus = GameStatus.WIN_ONE;
            }
        }
    }


    @Override
    public void update() {
        if (objectStatus == MovingObjectStatus.ALIVE) {
            moving();
            setBomb();
            updateBomberStatus();
            collisionManager.getMap().updateBombsList();
            updateItemsList();
            updatePortal();
        }
        if (objectStatus == MovingObjectStatus.MORIBUND) {
            indexOfSprite++;
            setImg(Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2,
                    Sprite.player_dead3, indexOfSprite, 20));
            if (indexOfSprite == 20) {
                setObjectStatus(MovingObjectStatus.DEAD);
                numOfLives--;
                if (numOfLives > 0) {
                    GameController.gameStatus = GameStatus.LOAD_CURRENT_LEVEL;
                } else
                    GameController.gameStatus = GameStatus.GAME_LOSE;
            }
        }
    }
}
