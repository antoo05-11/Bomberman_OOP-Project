package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Pair;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movingobject.enemies.Enemy;
import uet.oop.bomberman.entities.movingobject.enemies.Oneal;
import uet.oop.bomberman.map_graph.Map;
import uet.oop.bomberman.scenemaster.SceneController;

import java.util.*;
import java.util.List;

public class GameController {
    /**
     * Game status control
     */
    public enum GameStatus {
        GAME_LOBBY,
        GAME_PLAYING,
        WIN_ALL,
        GAME_LOSE,
        GAME_PAUSE
    }

    public static GameStatus gameStatus = GameStatus.GAME_LOBBY;

    public static Canvas playingCanvas = new Canvas(SceneController.SCREEN_WIDTH, SceneController.SCREEN_HEIGHT - 30);
    private final GraphicsContext gc = playingCanvas.getGraphicsContext2D();

    /**
     * Constructor.
     */
    public GameController(Stage stage) {
        this.stage = stage;
        loadMap();
    }

    /**
     * Map control.
     */
    public final static List<uet.oop.bomberman.map_graph.Map> mapList = new ArrayList<>();
    public static int LEVEL = 0;
    public static final int MAX_LEVEL = 3;

    private void loadMap() {
        for (int i = 0; i <= MAX_LEVEL; i++) {
            entities.add(new ArrayList<>());
            mapList.add(new Map(i));
        }
    }

    /**
     * Scene control.
     */
    private Stage stage;

    /**
     * Timer for scenes.
     */
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            render();
            update();
        }
    };

    boolean isReset = true;

    /**
     * Game characters, bombs and items.
     */
    public static List<List<Entity>> entities = new ArrayList<>();
    public static List<Entity> bombsList = new LinkedList<>();
    public static List<Entity> itemsList = new ArrayList<>();

    /**
     * Audio controller.
     */
    public static AudioController audioController = new AudioController();

    /**
     * Run game engine.
     */
    public void run() {
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI_fxml/LobbyScene.fxml")))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.show();
        timer.start();
    }

    /**
     * Update entities list (moving entities).
     */
    private void updateEntitiesList() {
        for (int i = entities.get(LEVEL).size() - 1; i >= 0; i--) {
            //Remove enemies died by bomb out of list.
            if (entities.get(LEVEL).get(i) instanceof Enemy) {
                if (((Enemy) entities.get(LEVEL).get(i)).getEnemyStatus() == Enemy.EnemyStatus.DEAD) {
                    //If an oneal is killed, set all other oneal status to NOT_CONNECTED.
                    if (entities.get(LEVEL).get(i) instanceof Oneal) {
                        for (Entity k : entities.get(LEVEL)) {
                            if (k instanceof Oneal) ((Oneal) k).setOnealStatus(Oneal.OnealStatus.NOT_CONNECTED);
                        }
                    }
                    entities.get(LEVEL).remove(i);
                }
            }
        }

        //This priority queue save (index of vertice, distance to bomber) and then get index of min distance.
        Queue<Pair> dis = new PriorityQueue<>(Comparator.comparingDouble(o -> (int) o.getValue()));
        for (int i = 1; i < entities.get(LEVEL).size(); i++) {
            if (entities.get(LEVEL).get(i) instanceof Oneal) {
                if (((Oneal) entities.get(LEVEL).get(i)).getOnealStatus() == Oneal.OnealStatus.CONNECTED) {
                    dis.add(new Pair(i, ((Oneal) entities.get(LEVEL).get(i)).getDistanceToBomber()));
                }
            }
        }
        //If existing an oneal chasing bomber, all other oneals will run random move and has INVALID status.
        if (!dis.isEmpty())
            for (Entity j : entities.get(LEVEL)) {
                if (!j.equals(entities.get(LEVEL).get((Integer) dis.peek().getKey())) && j instanceof Oneal) {
                    ((Oneal) j).setOnealStatus(Oneal.OnealStatus.INVALID);
                }
            }
    }

    /**
     * dx_gc and dy_gc is displacement of graphic context gc.
     * These values are set along with the position of Bomber.
     */
    public static int dx_gc = 0, dy_gc = 0;

    /**
     * If xPixel of bomber < SCREEN_WIDTH/2, gc render (xPixel,yPixel) as usual.
     * If SCREEN_WIDTH/2 < xPixel of bomber < MAP_WIDTH - SCREEN_WIDTH/2, gc decrease position of image by xPixel-SCREEN_WIDTH/2.
     * If xPixel of bomber > MAP_WIDTH - SCREEN_WIDTH/2, gc  decrease position of image by MAP_WIDTH - SCREEN_WIDTH.
     * All operations do same for y_pos rendering of gc.
     */
    public void updateMapCamera() {
        int bomber_xPixel = entities.get(LEVEL).get(0).getX();
        int bomber_yPixel = entities.get(LEVEL).get(0).getY();

        if (bomber_xPixel < SceneController.SCREEN_WIDTH / 2) {
            dx_gc = 0;
        } else if (bomber_xPixel < mapList.get(LEVEL).getWidthPixel() - SceneController.SCREEN_WIDTH / 2) {
            dx_gc = bomber_xPixel - SceneController.SCREEN_WIDTH / 2;
        } else {
            dx_gc = mapList.get(LEVEL).getWidthPixel() - SceneController.SCREEN_WIDTH;
        }
        if (bomber_yPixel < (SceneController.SCREEN_HEIGHT - 30) / 2) {
            dy_gc = 0;
        } else if (bomber_yPixel < mapList.get(LEVEL).getHeightPixel() - (SceneController.SCREEN_HEIGHT - 30) / 2) {
            dy_gc = bomber_yPixel - (SceneController.SCREEN_HEIGHT - 30) / 2;
        } else {
            dy_gc = mapList.get(LEVEL).getHeightPixel() - (SceneController.SCREEN_HEIGHT - 30);
        }
    }

    /**
     * Update all specs of game, set scenes.
     */
    public void update() {
        if (gameStatus == GameStatus.GAME_PLAYING) {
            isReset = false;
            audioController.playAlone(AudioController.AudioName.PLAYING, -1);
            entities.get(LEVEL).forEach(Entity::update);
            //Update all list.
            updateMapCamera();
            updateEntitiesList();
            //updateBombsList();
            //updateItemsList();

        } else if (gameStatus == GameStatus.GAME_LOBBY) {
            reset();
        } else if (gameStatus == GameStatus.GAME_LOSE) {
            reset(); //Reset all game specs before go out.
            gameStatus = GameStatus.GAME_LOBBY;
        }
    }

    private void render() {
        if (gameStatus == GameStatus.GAME_PLAYING) {
            gc.clearRect(0, 0, playingCanvas.getWidth(), playingCanvas.getHeight());
            mapList.get(LEVEL).mapRender(gc);
            entities.get(LEVEL).forEach(g -> g.render(gc));
        } else if (gameStatus == GameStatus.GAME_LOBBY) {

        }
    }

    private void reset() {
        if (!isReset) {
            try {
                stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/UI_fxml/LobbyScene.fxml")))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stage.show();
            for (int i = 0; i <= LEVEL; i++) {
                mapList.get(LEVEL).reset(); //Reset played map in map list.
            }
        }
        isReset = true;
        LEVEL = 0;
        //Only when resetting(go to lobby scene), lobby music is started
        audioController.playAlone(AudioController.AudioName.LOBBY, -1);
    }
}
