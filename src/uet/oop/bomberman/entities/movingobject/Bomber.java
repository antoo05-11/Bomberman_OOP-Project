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

import static uet.oop.bomberman.GameController.*;

public class Bomber extends MovingObject {
    public void setNumOfLives(int numOfLives) {
        this.numOfLives = numOfLives;
    }

    public int getNumOfLives() {
        return numOfLives;
    }

    /**
     * Bomber status
     */
    enum BomberStatus {
        ALIVE,
        DEAD
    }

    int numOfLives = 3;
    BomberStatus bomberStatus;
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

    //public List<Entity> bombsList = new LinkedList<>();

    Entity newBomb;
    CollisionManager collisionManager;

    int indexOfSprite = 0;
    public static int SPEED = 2;
    public static int MAX_BOMB = 3;
    public static int BOMB_RADIUS = 1;
    protected int life = 3;

    void reset() {
        SPEED = 3;
        BOMB_RADIUS = 1;
        MAX_BOMB = 3;
        bomberStatus = BomberStatus.ALIVE;
        setSprite(Sprite.player_right.getFxImage());
    }

    public Bomber(int x, int y, CollisionManager collisionManager) {
        super(x, y, null);
        reset();
        this.collisionManager = collisionManager;
        this.life = 3;
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
            if (((Enemy) GameController.entities.get(GameController.LEVEL).get(i)).collideBomber(x, y)) {
                bomberStatus = BomberStatus.DEAD;
                indexOfSprite = 0;
                break;
            }
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
                    if (i.getX() == newBomb.getX() && i.getY() == newBomb.getY()) {
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
            setSprite(Sprite.movingSprite(
                    Sprite.player_left,
                    Sprite.player_left_1,
                    Sprite.player_left_2, indexOfSprite, 20).getFxImage()
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
            setSprite(Sprite.movingSprite(
                    Sprite.player_up,
                    Sprite.player_up_1,
                    Sprite.player_up_2, indexOfSprite, 20).getFxImage());
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
            setSprite(Sprite.movingSprite(
                    Sprite.player_right,
                    Sprite.player_right_1,
                    Sprite.player_right_2, indexOfSprite, 20).getFxImage()
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

    private void updateBombsList() {
        bombsList.forEach(Entity::update);
        if (!bombsList.isEmpty())
            if (((Bomb) bombsList.get(0)).getBombStatus() == Bomb.BombStatus.DISAPPEAR) {
                bombsList.remove(0);
            }
    }

    private void updateItemsList() {
        int Bomber_xPixel = entities.get(LEVEL).get(0).getX();
        int Bomber_yPixel = entities.get(LEVEL).get(0).getY();
        for (Entity i : itemsList) {
            if (((Item) i).insideItem_Pixel(Bomber_xPixel + Bomber.WIDTH / 2, Bomber_yPixel + Bomber.HEIGHT / 2)) {
                audioController.playParallel(AudioController.AudioName.EAT_ITEM, 1);
                i.update();
                itemsList.remove(i);
                break;
            }
        }
    }

    private void updatePortal() {
        if (mapList.get(LEVEL).getEntityAt(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2) instanceof Portal) {
            if (LEVEL == MAX_LEVEL) {
                System.out.println("WIN");
            } else if (entities.get(LEVEL).size() == 1) {
                reset();
                itemsList.clear();
                bombsList.clear();
                LEVEL++;
                gameStatus = GameStatus.WIN_ONE;
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
            updateBomberStatus();
            updateBombsList();
            updateItemsList();
            updatePortal();
        }
        if (bomberStatus == BomberStatus.DEAD) {
            indexOfSprite++;
            setSprite(Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2,
                    Sprite.player_dead3, indexOfSprite, 20).getFxImage());
            if (indexOfSprite == 20) {
                numOfLives--;
                if (numOfLives > 0) {
                    GameController.gameStatus = GameStatus.LOAD_CURRENT_LEVEL;
                } else
                    GameController.gameStatus = GameStatus.GAME_LOSE;
            }
            itemsList.clear();
        }
    }
}
