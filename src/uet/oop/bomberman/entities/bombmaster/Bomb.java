package uet.oop.bomberman.entities.bombmaster;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.Map;

import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.itemmaster.*;
import uet.oop.bomberman.entities.stillobjectmaster.*;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static uet.oop.bomberman.GameController.audioController;
import static uet.oop.bomberman.GameController.itemsList;


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
                audioController.playParallel(AudioController.AudioName.EXPLODING, 1);
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
            flameAroundTop.add(new FlameAround(x, y - i, FlameAround.FlameType.VERTICAL, map));
            flameAroundDown.add(new FlameAround(x, y + i, FlameAround.FlameType.VERTICAL, map));
            flameAroundLeft.add(new FlameAround(x - i, y, FlameAround.FlameType.HORIZON, map));
            flameAroundRight.add(new FlameAround(x + i, y, FlameAround.FlameType.HORIZON, map));
        }
        flameAroundTop.add(new FlameAround(x, y - Bomber.BOMB_RADIUS, FlameAround.FlameType.TOP, map));
        flameAroundDown.add(new FlameAround(x, y + Bomber.BOMB_RADIUS, FlameAround.FlameType.DOWN, map));
        flameAroundLeft.add(new FlameAround(x - Bomber.BOMB_RADIUS, y, FlameAround.FlameType.LEFT, map));
        flameAroundRight.add(new FlameAround(x + Bomber.BOMB_RADIUS, y, FlameAround.FlameType.RIGHT, map));
    }

    boolean checkUp = true;
    public void destroyUp() {
        for (int i = 0; i < flameAroundTop.size(); i++) {
            int xTile = flameAroundTop.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                checkUp = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) y / Sprite.SCALED_SIZE - (double) flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundTop.size() - 1; j >= distance; j--) flameAroundTop.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                checkUp = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    if (!setItem(xTile, yTile)){
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }else {
                        itemsList.add(addItem(xTile, yTile));
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertMapToGraph();
                    distance = (double) y / Sprite.SCALED_SIZE - (double) flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundTop.size() - 1; j >= distance; j--) flameAroundTop.remove(j);
                }
                return;
            }
        }
    }

    boolean checkDown = true;
    public void destroyDown() {
        for (int i = 0; i < flameAroundDown.size(); i++) {
            int xTile = flameAroundDown.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                checkDown = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = flameAroundDown.size() - 1; j >= distance; j--) flameAroundDown.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                checkDown = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    if (!setItem(xTile, yTile)){
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }else {
                        itemsList.add(addItem(xTile, yTile));
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertMapToGraph();
                    distance = (double) flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = flameAroundDown.size() - 1; j >= distance; j--) flameAroundDown.remove(j);
                }
                return;
            }
        }
    }

    boolean checkLeft = true;
    public void destroyLeft() {
        for (int i = 0; i < flameAroundLeft.size(); i++) {
            int xTile = flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundLeft.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                checkLeft = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) x / Sprite.SCALED_SIZE - (double) flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundLeft.size() - 1; j >= distance; j--) flameAroundLeft.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                checkLeft = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    if (!setItem(xTile, yTile)){
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }else {
                        itemsList.add(addItem(xTile, yTile));
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertMapToGraph();
                    distance = (double) x / Sprite.SCALED_SIZE - (double) flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundLeft.size() - 1; j >= distance; j--) flameAroundLeft.remove(j);
                }
                return;
            }
        }
    }

    boolean checkRight = true;
    public void destroyRight() {
        for (int i = 0; i < flameAroundRight.size(); i++) {
            int xTile = flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundRight.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                checkRight = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = flameAroundRight.size() - 1; j >= distance; j--) flameAroundRight.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                checkRight = false;
                if (bombStatus == BombStatus.EXPLODED) {
                    if (!setItem(xTile, yTile)){
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }else {
                        itemsList.add(addItem(xTile, yTile));
                        map.replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
                    }
                    map.convertMapToGraph();
                    distance = (double) flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = flameAroundRight.size() - 1; j >= distance; j--) flameAroundRight.remove(j);
                }
                return;
            }
        }

    }
    public Entity addItem(int xTile, int yTile){
        switch (map.getItem(xTile, yTile)){
            case SpeedItem.code:
                 return new SpeedItem(xTile, yTile, Sprite.powerup_speed.getFxImage());
            case FlameItem.code:
                return new FlameItem(xTile, yTile, Sprite.powerup_flames.getFxImage());
            case BombItem.code:
                return new BombItem(xTile, yTile, Sprite.powerup_bombs.getFxImage());
        }
        return null;
    }
    public boolean setItem(int xTile, int yTile){
        switch (map.getItem(xTile, yTile)){
            case SpeedItem.code:
                map.replace(yTile, xTile, new SpeedItem(xTile, yTile, Sprite.powerup_speed.getFxImage()));
                return true;
            case FlameItem.code:
                map.replace(yTile, xTile, new FlameItem(xTile, yTile, Sprite.powerup_flames.getFxImage()));
                return true;
            case BombItem.code:
                map.replace(yTile, xTile, new BombItem(xTile, yTile, Sprite.powerup_bombs.getFxImage()));
                return true;
        }
        return false;
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
                    || (xTile == xBombTile && yTile + i == yBombTile && checkDown)
                    || (xTile == xBombTile && yTile - i == yBombTile && checkUp)
                    || (xTile == xBombTile + i && yTile == yBombTile && checkRight)
                    || (xTile == xBombTile - i && yTile == yBombTile && checkLeft)) return true;
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
            flameAroundTop.forEach(Entity::update);
            flameAroundDown.forEach(Entity::update);
            flameAroundLeft.forEach(Entity::update);
            flameAroundRight.forEach(Entity::update);
            flameAroundCenter.update();
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

        }
    }
}