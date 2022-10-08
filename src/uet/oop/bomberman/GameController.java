package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.scenemaster.LobbyScene;
import uet.oop.bomberman.scenemaster.PlayScene;
import uet.oop.bomberman.scenemaster.RootScene;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    /**
     * Game status control
     */
    enum GameStatus {
        GAME_LOBBY,
        GAME_PLAYING,
        WIN_ALL,
        LOSE
    }

    GameStatus gameStatus = GameStatus.GAME_LOBBY;

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
    private List<Map> mapList = new ArrayList<>();
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
    AnimationTimer lobbyTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {

        }
    };
    AnimationTimer playTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            render(playScene);
            update(playScene);
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
        stage.setScene(playScene.getScene());
        stage.show();
        playTimer.start();
    }

    public void update(RootScene scene) {
        if (scene instanceof PlayScene) {
            entities.get(LEVEL).forEach(g -> g.render(scene.getGc()));
            entities.get(LEVEL).forEach(Entity::update);
        } else if (scene instanceof LobbyScene) {

        }
    }

    public void render(RootScene scene) {
        if (scene instanceof PlayScene) {
            scene.getGc().clearRect(0, 0, scene.getCanvas().getWidth(), scene.getCanvas().getHeight());
            mapList.get(LEVEL).mapRender(scene.getGc());
        } else if (scene instanceof LobbyScene) {

        }
    }
}
