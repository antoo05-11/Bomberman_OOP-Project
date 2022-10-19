package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.util.Pair;
import uet.oop.bomberman.audiomaster.Audio;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bombmaster.Bomb;
import uet.oop.bomberman.entities.enemiesmaster.Balloom;
import uet.oop.bomberman.entities.enemiesmaster.Enemy;
import uet.oop.bomberman.entities.enemiesmaster.Oneal;
import uet.oop.bomberman.entities.itemmaster.Item;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.scenemaster.LobbyScene;
import uet.oop.bomberman.scenemaster.PlayScene;
import uet.oop.bomberman.scenemaster.RootScene;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.*;

public class GameController {
    /**
     * Game status control
     */
    public enum GameStatus {
        GAME_LOBBY,
        GAME_PLAYING,
        WIN_ALL,
        GAME_LOSE
    }

    public static GameStatus gameStatus = GameStatus.GAME_LOBBY;

    /**
     * Constructor.
     */
    public GameController(Stage stage) throws LineUnavailableException {
        this.stage = stage;
        loadMap();
    }

    /**
     * Map control.
     */
    public final static List<Map> mapList = new ArrayList<>();
    public static int LEVEL = 0;
    private static final int MAX_LEVEL = 0;

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
    private LobbyScene lobbyScene = new LobbyScene();
    private PlayScene playScene = new PlayScene();

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

    /**
     * Game characters, bombs and items.
     */
    public static List<List<Entity>> entities = new ArrayList<>();
    public static List<Entity> bombsList = new ArrayList<>();
    public static List<Entity> itemsList = new ArrayList<>();

    /**
     * Audio controller.
     */

    public static AudioController audioController = new AudioController();

    /**
     * Run game engine.
     */
    public void run() {
        stage.setScene(lobbyScene.getScene());
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

        if (bomber_xPixel < PlayScene.getWidthPixelPlayingFrame() / 2) {
            dx_gc = 0;
        } else if (bomber_xPixel < mapList.get(LEVEL).getWidthPixel() - PlayScene.getWidthPixelPlayingFrame() / 2) {
            dx_gc = bomber_xPixel - PlayScene.getWidthPixelPlayingFrame() / 2;
        } else {
            dx_gc = mapList.get(LEVEL).getWidthPixel() - PlayScene.getWidthPixelPlayingFrame();
        }
        if (bomber_yPixel < PlayScene.getHeightPixelPlayingFrame() / 2) {
            dy_gc = 0;
        } else if (bomber_yPixel < mapList.get(LEVEL).getHeightPixel() -PlayScene.getHeightPixelPlayingFrame() / 2) {
            dy_gc = bomber_yPixel -PlayScene.getHeightPixelPlayingFrame()/ 2;
        } else {
            dy_gc = mapList.get(LEVEL).getHeightPixel() - PlayScene.getHeightPixelPlayingFrame();
        }
    }

    /**
     * Update all specs of game, set scenes.
     */
    public void update() {

        if (gameStatus == GameStatus.GAME_PLAYING) {
            if (!stage.getScene().equals(playScene.getScene())) {
                stage.setScene(playScene.getScene());
                audioController.playAlone(AudioController.AudioName.PLAYING, -1);
            }
            entities.get(LEVEL).forEach(Entity::update);
            //Update all list.
            updateMapCamera();
            updateEntitiesList();
            //updateBombsList();
            //updateItemsList();

        } else if (gameStatus == GameStatus.GAME_LOBBY) {
            if (!stage.getScene().equals(lobbyScene.getScene())) {
                stage.setScene(lobbyScene.getScene());
            }
            audioController.playAlone(AudioController.AudioName.LOBBY, -1);
        } else if (gameStatus == GameStatus.GAME_LOSE) {
            reset(); //Reset all game specs before go out.
            gameStatus = GameStatus.GAME_LOBBY;
        }
    }

    public void render() {
        if (gameStatus == GameStatus.GAME_PLAYING) {
            playScene.getGc().clearRect(0, 0, playScene.getCanvas().getWidth(), playScene.getCanvas().getHeight());
            mapList.get(LEVEL).mapRender(playScene.getGc());
            entities.get(LEVEL).forEach(g -> g.render(playScene.getGc()));
        } else if (gameStatus == GameStatus.GAME_LOBBY) {

        }
    }

    private void reset() {
        playScene = new PlayScene();
        lobbyScene = new LobbyScene();
        for (int i = 0; i <= LEVEL; i++) {
            mapList.get(LEVEL).reset(); //Reset played map in map list.
        }
    }
}
