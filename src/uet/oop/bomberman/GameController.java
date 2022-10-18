package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
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
import uet.oop.bomberman.scenemaster.LobbyScene;
import uet.oop.bomberman.scenemaster.PlayScene;

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
     * Update entities list include Oneal, balloom
     */
    public void updateEntities() {
        for (int i = entities.get(LEVEL).size() - 1; i >= 0; i--) {
            //Remove enemies died by bomb out of list.
            if (entities.get(LEVEL).get(i) instanceof Enemy) {
                if (((Enemy) entities.get(LEVEL).get(i)).getEnemyStatus() == Enemy.EnemyStatus.DEAD) {
                    if (entities.get(LEVEL).get(i) instanceof Oneal) {
                        for (Entity k : entities.get(LEVEL)) {
                            if (k instanceof Oneal) ((Oneal) k).setOnealStatus(Oneal.OnealStatus.NOT_CONNECTED);
                        }
                    }
                    entities.get(LEVEL).remove(i);
                }
            }
        }

        //If existing an oneal chasing bomber, all other oneals will run random move and has INVALID status
        Queue<Pair> dis = new PriorityQueue<>(Comparator.comparingDouble(o -> (int) o.getValue()));

        for (int i = 1; i < entities.get(LEVEL).size(); i++) {
            if (entities.get(LEVEL).get(i) instanceof Oneal) {
                if (((Oneal) entities.get(LEVEL).get(i)).getOnealStatus() == Oneal.OnealStatus.CONNECTED) {
                    dis.add(new Pair(i, ((Oneal) entities.get(LEVEL).get(i)).getDistanceToBomber()));
                }
            }
        }

        if (!dis.isEmpty())
            for (Entity j : entities.get(LEVEL)) {
                if (!j.equals(entities.get(LEVEL).get((Integer) dis.peek().getKey())) && j instanceof Oneal) {
                    ((Oneal) j).setOnealStatus(Oneal.OnealStatus.INVALID);
                }
            }

    }

    public void update() {
        if (gameStatus == GameStatus.GAME_PLAYING) {
            if (!stage.getScene().equals(playScene.getScene())) {
                stage.setScene(playScene.getScene());
                audioController.playAlone(AudioController.AudioName.PLAYING, -1);
            }
            entities.get(LEVEL).forEach(Entity::update);
            updateEntities();
        } else if (gameStatus == GameStatus.GAME_LOBBY) {
            if (!stage.getScene().equals(lobbyScene.getScene())) {
                stage.setScene(lobbyScene.getScene());
            }
            audioController.playAlone(AudioController.AudioName.LOBBY, -1);
        }
        else if(gameStatus == GameStatus.GAME_LOSE) {
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
