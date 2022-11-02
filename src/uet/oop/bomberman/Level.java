package uet.oop.bomberman;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.map_graph.Map;
import uet.oop.bomberman.scenemaster.SceneController;

import java.util.concurrent.atomic.AtomicInteger;

public class Level {
    public static int MAX_TIME = 180;
    private final AtomicInteger levelCode = new AtomicInteger(0);
    private Map gameMap;
    private int levelPoint;
    AtomicInteger gamePoint;
    public static Canvas playingCanvas = new Canvas(SceneController.SCREEN_WIDTH,
            SceneController.SCREEN_HEIGHT - 30);
    private GraphicsContext gc = playingCanvas.getGraphicsContext2D();

    public Level(AtomicInteger gamePoint) {
        this.gamePoint = gamePoint;
        loadLevel(levelCode.get());
    }
    public void loadLevel(int levelCode) {
        this.levelCode.set(levelCode);
        gameMap = new Map(this);
    }
    public AtomicInteger getLevelCode() {
        return levelCode;
    }
    public void update() {
        gameMap.getMovingEntitiesList().forEach(Entity::update);
        gameMap.updateMapCamera();
        gameMap.updateEntitiesList();
    }
    public void render() {
        gc.clearRect(0, 0, playingCanvas.getWidth(), playingCanvas.getHeight());
        gameMap.render(gc);
    }
    public void reset() {
        gamePoint.addAndGet(-levelPoint);
        levelPoint = 0;
        int numOfLives = gameMap.getBomberNumOfLives();
        loadLevel(levelCode.get());
        gameMap.setBomberNumOfLives(numOfLives);
    }
    public void plusPoint(int rewardPoint) {
        levelPoint += rewardPoint;
        gamePoint.addAndGet(rewardPoint);
    }

    public int bonusRewardPoint() {
        return levelCode.get() * 100;
    }

    public Map getGameMap() {
        return gameMap;
    }
}
