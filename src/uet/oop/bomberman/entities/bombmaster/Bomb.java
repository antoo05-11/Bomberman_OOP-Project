package uet.oop.bomberman.entities.bombmaster;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.Map;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobjectmaster.Brick;
import uet.oop.bomberman.entities.stillobjectmaster.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static uet.oop.bomberman.entities.Bomber.itemsList;


public class Bomb extends Entity {
    public enum BombStatus {
        NotExplodedYet,
        EXPLODED,
        DISAPPEAR
    }

    private Map map;
    int waitForExplodingTime = 2;
    int indexOfSprite = 0;
    protected BombStatus bombStatus;
    protected Entity flameAroundCenter;
    protected List<Entity> flameAroundTop = new ArrayList<>();
    protected List<Entity> flameAroundDown = new ArrayList<>();
    protected List<Entity> flameAroundLeft = new ArrayList<>();
    protected List<Entity> flameAroundRight = new ArrayList<>();

    boolean checkRight, checkLeft, checkUp, checkDown;
    /**
     * Timer for counting 3 seconds before exploding.
     */
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        int i = 0;

        @Override
        public void run() {
            i++;
            if ((waitForExplodingTime - i) <= 0) {
                bombStatus = BombStatus.EXPLODED;
                timer.cancel();
                indexOfSprite = 0;
            }
        }
    };

    /**
     * Constructor for Bomb, run timer and add all flame sprite around.
     */
    public Bomb(int x, int y, Image img, Map map) {
        super(x, y, img);
        this.map = map;
        timer.schedule(task, 0, 1000);
        bombStatus = BombStatus.NotExplodedYet;
        flameAroundCenter = new FlameAround(x, y, FlameAround.FlameType.CENTER, map);
        for (int i = 1; i < Bomber.BOMB_RADIUS; i++) {
            Entity flameTop = new FlameAround(x, y - i, FlameAround.FlameType.VERTICAL, map);
            Entity flameDown = new FlameAround(x, y + i, FlameAround.FlameType.VERTICAL, map);
            Entity flameLeft = new FlameAround(x - i, y, FlameAround.FlameType.HORIZON, map);
            Entity flameRight = new FlameAround(x + i, y, FlameAround.FlameType.HORIZON, map);
            flameAroundTop.add(flameTop);
            flameAroundDown.add(flameDown);
            flameAroundLeft.add(flameLeft);
            flameAroundRight.add(flameRight);
        }
        Entity flameTopLast = new FlameAround(x, y - Bomber.BOMB_RADIUS, FlameAround.FlameType.TOP, map);
        Entity flameDownLast = new FlameAround(x, y + Bomber.BOMB_RADIUS, FlameAround.FlameType.DOWN, map);
        Entity flameLeftLast = new FlameAround(x - Bomber.BOMB_RADIUS, y, FlameAround.FlameType.LEFT, map);
        Entity flameRightLast = new FlameAround(x + Bomber.BOMB_RADIUS, y, FlameAround.FlameType.RIGHT, map);
        flameAroundTop.add(flameTopLast);
        flameAroundDown.add(flameDownLast);
        flameAroundLeft.add(flameLeftLast);
        flameAroundRight.add(flameRightLast);
    }

    public void destroyUp() {
        for (int i = 0; i < flameAroundTop.size(); i++) {
            int xTile = flameAroundTop.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameController.mapList.get(GameController.LEVEL)
                    .getEntityAt(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) y / Sprite.SCALED_SIZE - (double) flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundTop.size() - 1; j >= distance - 1; j--) flameAroundTop.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == BombStatus.EXPLODED) {
                    itemsList.add(GameController.mapList.get(GameController.LEVEL).randomItem(yTile, xTile));
                    distance = (double) y / Sprite.SCALED_SIZE - (double) flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundTop.size() - 1; j >= distance - 1; j--) flameAroundTop.remove(j);
                }
                return;
            }
        }
    }

    public void destroyDown() {
        for (int i = 0; i < flameAroundDown.size(); i++) {
            int xTile = flameAroundDown.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameController.mapList.get(GameController.LEVEL)
                    .getEntityAt(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = flameAroundDown.size() - 1; j >= distance - 1; j--) flameAroundDown.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == BombStatus.EXPLODED) {
                    itemsList.add(GameController.mapList.get(GameController.LEVEL).randomItem(yTile, xTile));
                    distance = (double) flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = flameAroundDown.size() - 1; j >= distance - 1; j--) flameAroundDown.remove(j);
                }
                return;
            }
        }
    }

    public void destroyLeft() {
        for (int i = 0; i < flameAroundLeft.size(); i++) {
            int xTile = flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundLeft.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameController.mapList.get(GameController.LEVEL)
                    .getEntityAt(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) x / Sprite.SCALED_SIZE - (double) flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundLeft.size() - 1; j >= distance - 1; j--) flameAroundLeft.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == BombStatus.EXPLODED) {
                    itemsList.add(GameController.mapList.get(GameController.LEVEL).randomItem(yTile, xTile));
                    distance = (double) x / Sprite.SCALED_SIZE - (double) flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundLeft.size() - 1; j >= distance - 1; j--) flameAroundLeft.remove(j);
                }
                return;
            }
        }
    }

    public void destroyRight() {
        for (int i = 0; i < flameAroundRight.size(); i++) {
            int xTile = flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundRight.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = GameController.mapList.get(GameController.LEVEL)
                    .getEntityAt(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = flameAroundRight.size() - 1; j >= distance - 1; j--) flameAroundRight.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == BombStatus.EXPLODED) {
                    itemsList.add(GameController.mapList.get(GameController.LEVEL).randomItem(yTile, xTile));
                    distance = (double) flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = flameAroundRight.size() - 1; j >= distance - 1; j--) flameAroundRight.remove(j);
                }
                return;
            }
        }

    }

    public BombStatus getBombStatus() {
        return bombStatus;
    }

    public void setSprite(Image img) {
        this.img = img;
    }

    /**
     * Check if a point in Pixel is inside bombList.
     */
    public boolean insideBombRange_Pixel(int xPos, int yPos) {
        int xTile = xPos / Sprite.SCALED_SIZE;
        int yTile = yPos / Sprite.SCALED_SIZE;
        int xBombTile = x / Sprite.SCALED_SIZE;
        int yBombTile = y / Sprite.SCALED_SIZE;
        for (int i = 1; i <= Bomber.BOMB_RADIUS; i++) {
            if ((xTile == xBombTile && yTile == yBombTile)
                    || (xTile == xBombTile && yTile + i == yBombTile)
                    || (xTile == xBombTile && yTile - i == yBombTile)
                    || (xTile == xBombTile + i && yTile == yBombTile)
                    || (xTile == xBombTile - i && yTile == yBombTile)) return true;
        }
        return false;
    }

    @Override
    public void render(GraphicsContext gc) {
        destroyLeft();
        destroyRight();
        destroyDown();
        destroyUp();
        if (bombStatus == BombStatus.NotExplodedYet) {
            super.render(gc);
        }
        if (bombStatus == BombStatus.EXPLODED) {
            flameAroundTop.forEach(g -> g.render(gc));
            flameAroundDown.forEach(g -> g.render(gc));
            flameAroundLeft.forEach(g -> g.render(gc));
            flameAroundRight.forEach(g -> g.render(gc));
            flameAroundCenter.render(gc);
        }
    }

    @Override
    public void update() {
        destroyLeft();
        destroyRight();
        destroyDown();
        destroyUp();
        if (bombStatus == BombStatus.NotExplodedYet) {
            indexOfSprite = (indexOfSprite < 1000) ? indexOfSprite + 1 : 0;
            setSprite(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, indexOfSprite, 30).getFxImage());

        }
        if (bombStatus == BombStatus.EXPLODED) {
            bombStatus = ((FlameAround) flameAroundCenter).getStatus();
            flameAroundTop.forEach(Entity::update);
            flameAroundDown.forEach(Entity::update);
            flameAroundLeft.forEach(Entity::update);
            flameAroundRight.forEach(Entity::update);
            flameAroundCenter.update();
        }
    }
}