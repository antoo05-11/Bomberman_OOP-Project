package uet.oop.bomberman.entities.stillobject.bomb;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.util.Duration;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.movingobject.MovingObject;
import uet.oop.bomberman.entities.stillobject.*;
import uet.oop.bomberman.entities.stillobject.item.Item;
import uet.oop.bomberman.map_graph.Map;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobject.item.BombItem;
import uet.oop.bomberman.entities.stillobject.item.FlameItem;
import uet.oop.bomberman.entities.stillobject.item.SpeedItem;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;

import static uet.oop.bomberman.GameController.*;
import static uet.oop.bomberman.GameController.GameStatus.GAME_LOBBY;
import static uet.oop.bomberman.GameController.GameStatus.WIN_ONE;


public class Bomb extends StillObject {
    public enum BombStatus {
        NotExplodedYet,
        EXPLODED,
        DISAPPEAR
    }

    int waitForExplodingTime = 2; //2 seconds before exploding.
    int indexOfSprite = 0;
    protected BombStatus bombStatus;
    protected Entity flameAroundCenter;
    protected List<Entity> flameAroundTop = new ArrayList<>();
    protected List<Entity> flameAroundDown = new ArrayList<>();
    protected List<Entity> flameAroundLeft = new ArrayList<>();
    protected List<Entity> flameAroundRight = new ArrayList<>();
    protected List<Entity> bricksDestroyed = new ArrayList<>();
    Bomber bomber;
    /**
     * Timer and task for counting 3 seconds before exploding.
     */
    Timeline timerForExploding = new Timeline();
    private int timerCounter = 2;


    /**
     * Constructor for Bomb, run timer and add all flame sprite around.
     */
    public Bomb(int x, int y, Map map) {
        super(x, y, Sprite.bomb.getFxImage(), map);
        bomber = (Bomber) map.getMovingEntitiesList().get(0);
        bombStatus = BombStatus.NotExplodedYet;

        timerForExploding.getKeyFrames().addAll(new KeyFrame(Duration.seconds(0),
                event -> {
                    timerCounter--;
                    if (timerCounter <= 0) {
                        bombStatus = BombStatus.EXPLODED;
                        indexOfSprite = 0;
                /*
                  All flame near bomb switch to EXPLODED status.
                 */
                        for (Entity flame : flameAroundDown) {
                            ((FlameAround) flame).setFlameStatus(BombStatus.EXPLODED);
                        }
                        for (Entity flame : flameAroundLeft) {
                            ((FlameAround) flame).setFlameStatus(BombStatus.EXPLODED);
                        }
                        for (Entity flame : flameAroundRight) {
                            ((FlameAround) flame).setFlameStatus(BombStatus.EXPLODED);
                        }
                        for (Entity flame : flameAroundTop) {
                            ((FlameAround) flame).setFlameStatus(BombStatus.EXPLODED);
                        }
                        ((FlameAround) flameAroundCenter).setFlameStatus(BombStatus.EXPLODED);

                        audioController.playParallel(AudioController.AudioName.EXPLODING, 1);
                        indexOfSprite = 0;

                        timerForExploding.stop();
                    }
                }), new KeyFrame(Duration.seconds(1)));
        timerForExploding.setCycleCount(-1);
        timerForExploding.playFromStart();

        flameAroundCenter = new FlameAround(x, y, FlameAround.FlameType.CENTER, map);
        for (int i = 1; i < bomber.getBombRadius(); i++) {
            flameAroundTop.add(new FlameAround(x, y - i, FlameAround.FlameType.VERTICAL, map));
            flameAroundDown.add(new FlameAround(x, y + i, FlameAround.FlameType.VERTICAL, map));
            flameAroundLeft.add(new FlameAround(x - i, y, FlameAround.FlameType.HORIZON, map));
            flameAroundRight.add(new FlameAround(x + i, y, FlameAround.FlameType.HORIZON, map));
        }
        flameAroundTop.add(new FlameAround(x, y - bomber.getBombRadius(), FlameAround.FlameType.TOP, map));
        flameAroundDown.add(new FlameAround(x, y + bomber.getBombRadius(), FlameAround.FlameType.DOWN, map));
        flameAroundLeft.add(new FlameAround(x - bomber.getBombRadius(), y, FlameAround.FlameType.LEFT, map));
        flameAroundRight.add(new FlameAround(x + bomber.getBombRadius(), y, FlameAround.FlameType.RIGHT, map));
    }

    /**
     * Destroy the brick above.
     */
    private void destroyUp() {
        for (int i = 0; i < flameAroundTop.size(); i++) {
            int xTile = flameAroundTop.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) y / Sprite.SCALED_SIZE - (double) flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundTop.size() - 1; j >= distance; j--) flameAroundTop.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {

                if (bombStatus == BombStatus.EXPLODED) {
                    destroyBrick(xTile, yTile, (Brick) nearTile);
                    distance = (double) y / Sprite.SCALED_SIZE - (double) flameAroundTop.get(i).getY() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundTop.size() - 1; j >= distance; j--) flameAroundTop.remove(j);
                }
                return;
            }
        }
    }

    /**
     * Destroy brick and add item, portal.
     *
     * @param xTile    int
     * @param yTile    int
     * @param nearTile Brick
     */
    private void destroyBrick(int xTile, int yTile, Brick nearTile) {
        nearTile.destroyBrick(xTile, yTile);
        map.removeInMapInfo(xTile, yTile);
    }

    /**
     * Destroy the brick below.
     */
    private void destroyDown() {
        for (int i = 0; i < flameAroundDown.size(); i++) {
            int xTile = flameAroundDown.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(xTile * Sprite.SCALED_SIZE, yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = flameAroundDown.size() - 1; j >= distance; j--) flameAroundDown.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == BombStatus.EXPLODED) {
                    destroyBrick(xTile, yTile, (Brick) nearTile);
                    distance = (double) flameAroundDown.get(i).getY() / Sprite.SCALED_SIZE - (double) y / Sprite.SCALED_SIZE;
                    for (int j = flameAroundDown.size() - 1; j >= distance; j--) flameAroundDown.remove(j);
                }
                return;
            }
        }
    }

    /**
     * Destroy the brick left.
     */
    private void destroyLeft() {
        for (int i = 0; i < flameAroundLeft.size(); i++) {
            int xTile = flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundLeft.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) x / Sprite.SCALED_SIZE - (double) flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundLeft.size() - 1; j >= distance; j--) flameAroundLeft.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                //((Brick) nearTile).destroyBrick(xTile, yTile);
                if (bombStatus == BombStatus.EXPLODED) {
                    destroyBrick(xTile, yTile, (Brick) nearTile);
                    distance = (double) x / Sprite.SCALED_SIZE - (double) flameAroundLeft.get(i).getX() / Sprite.SCALED_SIZE;
                    for (int j = flameAroundLeft.size() - 1; j >= distance; j--) flameAroundLeft.remove(j);
                }
                return;
            }
        }
    }

    /**
     * Destroy the brick right.
     */
    public void destroyRight() {
        for (int i = 0; i < flameAroundRight.size(); i++) {
            int xTile = flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE;
            int yTile = flameAroundRight.get(i).getY() / Sprite.SCALED_SIZE;

            double distance;
            Entity nearTile = map
                    .getEntityAt(Math.max(xTile * Sprite.SCALED_SIZE, 0), yTile * Sprite.SCALED_SIZE);

            if (nearTile instanceof Wall) {
                if (bombStatus == BombStatus.EXPLODED) {
                    distance = (double) flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = flameAroundRight.size() - 1; j >= distance; j--) flameAroundRight.remove(j);
                }
                return;
            } else if (nearTile instanceof Brick) {
                if (bombStatus == BombStatus.EXPLODED) {
                    destroyBrick(xTile, yTile, (Brick) nearTile);
                    distance = (double) flameAroundRight.get(i).getX() / Sprite.SCALED_SIZE - (double) x / Sprite.SCALED_SIZE;
                    for (int j = flameAroundRight.size() - 1; j >= distance; j--) flameAroundRight.remove(j);
                }
                return;
            }
        }

    }


    public BombStatus getBombStatus() {
        return bombStatus;
    }

    /**
     * Check if a point in Pixel is inside bombList.
     *
     * @param xPos is x pos of bomber.
     * @param yPos is y pos of bomber.
     * @return true if bomber is inside bomb's exploding range.
     */
    public boolean insideBombRange_Pixel(int xPos, int yPos) {
        int xTile = xPos / Sprite.SCALED_SIZE;
        int yTile = yPos / Sprite.SCALED_SIZE;
        int xFlameTile;
        int yFlameTile;
        if (bombStatus == BombStatus.EXPLODED) {
            for (Entity i : flameAroundLeft) {
                xFlameTile = i.getX() / Sprite.SCALED_SIZE;
                yFlameTile = i.getY() / Sprite.SCALED_SIZE;
                if (xFlameTile == xTile && yFlameTile == yTile) return true;
            }
            for (Entity i : flameAroundTop) {
                xFlameTile = i.getX() / Sprite.SCALED_SIZE;
                yFlameTile = i.getY() / Sprite.SCALED_SIZE;
                if (xFlameTile == xTile && yFlameTile == yTile) return true;
            }
            for (Entity i : flameAroundRight) {
                xFlameTile = i.getX() / Sprite.SCALED_SIZE;
                yFlameTile = i.getY() / Sprite.SCALED_SIZE;
                if (xFlameTile == xTile && yFlameTile == yTile) return true;
            }
            for (Entity i : flameAroundDown) {
                xFlameTile = i.getX() / Sprite.SCALED_SIZE;
                yFlameTile = i.getY() / Sprite.SCALED_SIZE;
                if (xFlameTile == xTile && yFlameTile == yTile) return true;
            }
            xFlameTile = flameAroundCenter.getX() / Sprite.SCALED_SIZE;
            yFlameTile = flameAroundCenter.getY() / Sprite.SCALED_SIZE;
            return xFlameTile == xTile && yFlameTile == yTile;
        }
        return false;
    }

    /**
     * This is render.
     */
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
            for (Entity brick : bricksDestroyed) {
                brick.render(gc);
            }
        }
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
        bricksDestroyed.forEach(Entity::update);
        flameAroundTop.forEach(Entity::update);
        flameAroundDown.forEach(Entity::update);
        flameAroundLeft.forEach(Entity::update);
        flameAroundRight.forEach(Entity::update);
        flameAroundCenter.update();

        destroyLeft();
        destroyRight();
        destroyDown();
        destroyUp();

        if (bombStatus == BombStatus.NotExplodedYet) {
            setImg(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, ++indexOfSprite, 20));
        }
        if (bombStatus == BombStatus.EXPLODED) {
            bombStatus = ((FlameAround) flameAroundCenter).getStatus();
            /*
              Check if moving objects inside bomb range.
             */
            for (int i = map.getMovingEntitiesList().size() - 1; i >= 0; i--) {
                int xBomberPos = map.getMovingEntitiesList().get(i).getX();
                int yBomberPos = map.getMovingEntitiesList().get(i).getY();
                if (insideBombRange_Pixel(xBomberPos + Sprite.SCALED_SIZE / 2, yBomberPos + Sprite.SCALED_SIZE / 2)) {
                    ((MovingObject) map.getMovingEntitiesList().get(i)).setObjectStatus(MovingObject.MovingObjectStatus.MORIBUND);
                }
            }
        }
    }
}