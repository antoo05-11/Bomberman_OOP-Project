package uet.oop.bomberman.entities.movingobject;

import javafx.scene.input.KeyCode;
import uet.oop.bomberman.map_graph.CollisionManager;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobject.Brick;
import uet.oop.bomberman.entities.stillobject.Portal;
import uet.oop.bomberman.entities.movingobject.enemies.Enemy;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.entities.stillobject.item.BombItem;
import uet.oop.bomberman.entities.stillobject.item.FlameItem;
import uet.oop.bomberman.entities.stillobject.item.Item;
import uet.oop.bomberman.entities.stillobject.item.SpeedItem;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

import static uet.oop.bomberman.GameController.*;

public class Bomber extends MovingObject {
    public static final List<Class> cannotPassEntityList = Arrays.asList(new Class[]{Wall.class, Brick.class});
    public static final int MAX_LIVES = 3;
    int numOfLives = 3;
    public static int speed = 3;
    private int maxBomb = 3;
    public int bombRadius = 1;

    public int getBombRadius() {
        return bombRadius;
    }

    Map<KeyCode, Boolean> keyCodeMap = new HashMap<>();

    public void setNumOfLives(int numOfLives) {
        this.numOfLives = numOfLives;
    }

    public int getNumOfLives() {
        return numOfLives;
    }

    /**
     * Bomber size
     */
    public static final int WIDTH = Sprite.SCALED_SIZE * 20 / 32;
    public static final int HEIGHT = Sprite.SCALED_SIZE * 28 / 32;
    private static final int FIX_SIZE = Sprite.SCALED_SIZE / 2;
    /**
     * Direction check and bombed check.
     */
    private boolean bombed = false;
    private KeyCode latestDirectKey = KeyCode.RIGHT;
    private final List<Entity> movingEntitiesList;

    /**
     * Constructor of bomber.
     */
    public Bomber(int x, int y, CollisionManager collisionManager) {
        super(x, y, null, collisionManager);
        movingEntitiesList = map.getMovingEntitiesList();
    }

    /**
     * Save key code.
     */
    public void saveKeyEvent(KeyCode keyCode, boolean isPress) {
        if (isPress && keyCode.isArrowKey()) {
            switch (keyCode) {
                case DOWN:
                    keyCodeMap.put(KeyCode.DOWN, true);
                    break;
                case LEFT:
                    keyCodeMap.put(KeyCode.LEFT, true);
                    break;
                case RIGHT:
                    keyCodeMap.put(KeyCode.RIGHT, true);
                    break;
                case UP:
                    keyCodeMap.put(KeyCode.UP, true);
                    break;
            }
            latestDirectKey = keyCode;
        }
        if (!isPress && keyCode.isArrowKey()) {
            if (keyCodeMap.size() == 2 && keyCodeMap.containsValue(false)) keyCodeMap.clear();
            switch (keyCode) {
                case DOWN:
                    keyCodeMap.remove(KeyCode.DOWN);
                    break;
                case LEFT:
                    keyCodeMap.remove(KeyCode.LEFT);
                    break;
                case RIGHT:
                    keyCodeMap.remove(KeyCode.RIGHT);
                    break;
                case UP:
                    keyCodeMap.remove(KeyCode.UP);
                    break;
            }

            indexOfSprite = 0;
        }
        if (keyCode == KeyCode.SPACE) {
            bombed = isPress;
        }
    }

    /**
     * Update bomber status.
     */
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

    /**
     * Set bomb for bomber.
     */
    private void setBomb() {
        if (bombed) {
            map.setBomb(x + Bomber.WIDTH / 2,
                    y + Bomber.HEIGHT / 2);
            bombed = false;
        }
    }

    /**
     * Make bomber move.
     */
    private void moving() {
        if (keyCodeMap.size() > 0) indexOfSprite++;
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
        if (keyCodeMap.containsKey(KeyCode.RIGHT)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_right,
                    Sprite.player_right_1,
                    Sprite.player_right_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(this, x, y, "RIGHT", speed)) {
                x += speed;
                if (keyCodeMap.containsKey(KeyCode.UP))
                    if (!keyCodeMap.get(KeyCode.UP)) keyCodeMap.remove(KeyCode.UP);
                if (keyCodeMap.containsKey(KeyCode.DOWN))
                    if (!keyCodeMap.get(KeyCode.DOWN)) keyCodeMap.remove(KeyCode.DOWN);
            } else {
                if (keyCodeMap.size() == 1) {
                    if ((y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - y < FIX_SIZE
                            && collisionManager.downRightCorner == null) {
                        keyCodeMap.put(KeyCode.DOWN, false);
                    } else if (y + HEIGHT - ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && collisionManager.topRightCorner == null) {
                        keyCodeMap.put(KeyCode.UP, false);
                    }
                }
            }
        }
        if (keyCodeMap.containsKey(KeyCode.RIGHT) && !keyCodeMap.get(KeyCode.RIGHT)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_right,
                    Sprite.player_right_1,
                    Sprite.player_right_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(this, x, y, "RIGHT", 1)) {
                x += 1;
            }
        }
        if (keyCodeMap.containsKey(KeyCode.LEFT) && keyCodeMap.get(KeyCode.LEFT)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_left,
                    Sprite.player_left_1,
                    Sprite.player_left_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(this, x, y, "LEFT", speed)) {
                x -= speed;
                if (keyCodeMap.containsKey(KeyCode.UP))
                    if (!keyCodeMap.get(KeyCode.UP)) keyCodeMap.remove(KeyCode.UP);
                if (keyCodeMap.containsKey(KeyCode.DOWN))
                    if (!keyCodeMap.get(KeyCode.DOWN)) keyCodeMap.remove(KeyCode.DOWN);

            } else {
                if (keyCodeMap.size() == 1) {
                    if ((y / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - y < FIX_SIZE
                            && collisionManager.downLeftCorner == null) {
                        keyCodeMap.put(KeyCode.DOWN, false);
                    } else if (y + HEIGHT - ((y + HEIGHT) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && collisionManager.topLeftCorner == null) {
                        keyCodeMap.put(KeyCode.UP, false);
                    }
                }
            }
        }
        if (keyCodeMap.containsKey(KeyCode.LEFT) && !keyCodeMap.get(KeyCode.LEFT)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_left,
                    Sprite.player_left_1,
                    Sprite.player_left_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(this, x, y, "LEFT", 1)) {
                x -= 1;
            }
        }
        if (keyCodeMap.containsKey(KeyCode.UP)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_up,
                    Sprite.player_up_1,
                    Sprite.player_up_2, indexOfSprite, 20));
            if (!collisionManager.collide(this, x, y, "UP", speed)) {
                y -= speed;
                if (keyCodeMap.containsKey(KeyCode.RIGHT))
                    if (!keyCodeMap.get(KeyCode.RIGHT)) keyCodeMap.remove(KeyCode.RIGHT);
                if (keyCodeMap.containsKey(KeyCode.LEFT))
                    if (!keyCodeMap.get(KeyCode.LEFT)) keyCodeMap.remove(KeyCode.LEFT);
            } else {
                if (keyCodeMap.size() == 1) {
                    if ((x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - x < FIX_SIZE
                            && collisionManager.topRightCorner == null) {
                        keyCodeMap.put(KeyCode.RIGHT, false);
                    } else if (x + WIDTH - ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && collisionManager.topLeftCorner == null) {
                        keyCodeMap.put(KeyCode.LEFT, false);
                    }
                }
            }
        }
        if (keyCodeMap.containsKey(KeyCode.UP) && !keyCodeMap.get(KeyCode.UP)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_up,
                    Sprite.player_up_1,
                    Sprite.player_up_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(this, x, y, "UP", 1)) {
                y -= 1;
            }
        }
        if (keyCodeMap.containsKey(KeyCode.DOWN)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_down,
                    Sprite.player_down_1,
                    Sprite.player_down_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(this, x, y, "DOWN", speed)) {
                y += speed;
                if (keyCodeMap.containsKey(KeyCode.RIGHT))
                    if (!keyCodeMap.get(KeyCode.RIGHT)) keyCodeMap.remove(KeyCode.RIGHT);
                if (keyCodeMap.containsKey(KeyCode.LEFT))
                    if (!keyCodeMap.get(KeyCode.LEFT)) keyCodeMap.remove(KeyCode.LEFT);
            } else {
                if (keyCodeMap.size() == 1) {
                    if ((x / Sprite.SCALED_SIZE + 1) * Sprite.SCALED_SIZE - x < FIX_SIZE
                            && collisionManager.downRightCorner == null) {
                        keyCodeMap.put(KeyCode.RIGHT, false);
                    } else if (x + WIDTH - ((x + WIDTH) / Sprite.SCALED_SIZE) * Sprite.SCALED_SIZE < FIX_SIZE
                            && collisionManager.downLeftCorner == null) {
                        keyCodeMap.put(KeyCode.LEFT, false);
                    }
                }
            }
        }
        if (keyCodeMap.containsKey(KeyCode.DOWN) && !keyCodeMap.get(KeyCode.DOWN)) {
            setImg(Sprite.movingSprite(
                    Sprite.player_down,
                    Sprite.player_down_1,
                    Sprite.player_down_2, indexOfSprite, 20)
            );
            if (!collisionManager.collide(this, x, y, "DOWN", 1)) {
                y += 1;
            }
        }

    }

    /**
     * Update item list.
     */
    private void eatItemAndPortal() {
        int xTile = (x + Bomber.WIDTH / 2) / Sprite.SCALED_SIZE;
        int yTile = (y + Bomber.HEIGHT / 2) / Sprite.SCALED_SIZE;
        Entity item = map.getItem(xTile, yTile);
        if (item instanceof Item) {
            audioController.playParallel(AudioController.AudioName.EAT_ITEM, 1);
            item.update();
            if (item instanceof FlameItem) {
                bombRadius++;
            }
            if (item instanceof SpeedItem) {
                speed++;
            }
            if (item instanceof BombItem) {
                maxBomb++;
            }
            map.removeItem(xTile, yTile);
        }

        if (item instanceof Portal) {
            if (movingEntitiesList.size() == 1) {
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


    public int getMaxBomb() {
        return maxBomb;
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
        if (objectStatus == MovingObjectStatus.ALIVE) {
            moving();
            setBomb();
            updateBomberStatus();
            map.updateBombArrayList();
            eatItemAndPortal();

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

    @Override
    public List<Class> getCannotPassEntityList() {
        return cannotPassEntityList;
    }
}
