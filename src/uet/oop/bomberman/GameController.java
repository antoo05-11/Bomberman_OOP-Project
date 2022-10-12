package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemiesmaster.Balloom;
import uet.oop.bomberman.entities.itemmaster.Item;
import uet.oop.bomberman.scenemaster.LobbyScene;
import uet.oop.bomberman.scenemaster.PlayScene;

import java.util.ArrayList;
import java.util.List;

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
    public GameController(Stage stage) {
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
     * Game characters.
     */
    public static List<List<Entity>> entities = new ArrayList<>();


    /**
     * Run game engine.
     */
    public void run() {
        stage.setScene(lobbyScene.getScene());
        stage.show();
        timer.start();
    }

    public void update() {
        if (gameStatus == GameStatus.GAME_PLAYING) {
            if (!stage.getScene().equals(playScene)) stage.setScene(playScene.getScene());
            entities.get(LEVEL).forEach(g -> g.render(playScene.getGc()));
            entities.get(LEVEL).forEach(Entity::update);
        } else if (gameStatus == GameStatus.GAME_LOBBY) {
            if (!stage.getScene().equals(lobbyScene)) {
                reset(); //Reset all game specs before go out.
                stage.setScene(lobbyScene.getScene());
            }
        }
    }

    public void render() {
        if (gameStatus == GameStatus.GAME_PLAYING) {
            playScene.getGc().clearRect(0, 0, playScene.getCanvas().getWidth(), playScene.getCanvas().getHeight());
            mapList.get(LEVEL).mapRender(playScene.getGc());
        } else if (gameStatus == GameStatus.GAME_LOBBY) {

        }
    }

    private void reset() {
        for (int i = 0; i <= LEVEL; i++) {
            mapList.get(LEVEL).reset(); //Reset played map in map list.
        }
    }
}
