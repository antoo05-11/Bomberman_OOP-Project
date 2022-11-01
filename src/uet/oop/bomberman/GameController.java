package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.map_graph.Map;
import uet.oop.bomberman.scenemaster.LobbyController;
import uet.oop.bomberman.scenemaster.PlayingController;
import uet.oop.bomberman.scenemaster.SceneController;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController {
    /**
     * Game status control.
     */
    public enum GameStatus {
        GAME_LOBBY,
        GAME_START,
        GAME_PLAYING,
        LOAD_CURRENT_LEVEL,
        WIN_ONE,
        WIN_ALL,
        GAME_LOSE,
        GAME_PAUSE,
        GAME_UNPAUSE
    }

    private Stage stage;

    private int levelPoint = 0;
    private int gamePoint = 0;
    private String username = "";

    public void setUsername(String username) {
        this.username = username;
    }

    public void plusPoint(int rewardPoint) {
        gamePoint += rewardPoint;
        levelPoint += rewardPoint;
    }

    public static GameStatus gameStatus;

    public static Canvas playingCanvas = new Canvas(SceneController.SCREEN_WIDTH,
            SceneController.SCREEN_HEIGHT - 30);
    private final GraphicsContext gc = playingCanvas.getGraphicsContext2D();
    public final static List<Map> mapList = new ArrayList<>();
    public static int LEVEL = 0;
    public static final int MAX_LEVEL = 3;

    private LobbyController lobbyController;
    private PlayingController playingController;
    private Scene lobbyScene;
    private Scene playingScene;

    /**
     * AudioController can be used anywhere to play any audio if needed.
     * See how to play audio at {@link AudioController}.
     */
    public static AudioController audioController = new AudioController();
    public static final int MAX_TIME = 180; // Max time for each level is 180 seconds.

    /**
     * Constructor with available stage.
     */
    public GameController(Stage stage) {
        this.stage = stage;
        loadMap();
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Load all map, only used when constructing game controller.
     */
    private void loadMap() {
        for (int i = 0; i <= MAX_LEVEL; i++) {
            mapList.add(new Map(i, this));
        }
    }


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
     * Run game engine.
     */
    public void run() {

        FXMLLoader fxmlLoader1 = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/UI_fxml/LobbyScene.fxml")));
        FXMLLoader fxmlLoader2 = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/UI_fxml/PlayingScene.fxml")));
        try {
            lobbyScene = new Scene(fxmlLoader1.load());
            playingScene = new Scene(fxmlLoader2.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lobbyController = (fxmlLoader1).getController();
        playingController = (fxmlLoader2).getController();

        lobbyController.setPlayingScene(playingScene);
        playingController.setLobbyScene(lobbyScene);

        lobbyController.setGameController(this);
        playingController.setGameController(this);

        gameStatus = GameStatus.GAME_LOBBY;
        stage.setScene(lobbyScene);
        stage.show();

        timer.start();
    }


    /**
     * Update all specs of game, set scenes.
     */
    public void update() {
        lobbyController.updateStatus();
        playingController.updateStatus();
        audioController.run();
    }

    /**
     * Render into playing canvas by gc.
     */
    private void render() {
        if (gameStatus == GameStatus.GAME_PLAYING) {
            gc.clearRect(0, 0, playingCanvas.getWidth(), playingCanvas.getHeight());
            mapList.get(LEVEL).render(gc);
        }
    }

    /**
     * Reset level point.
     */
    public void resetLevelPoint() {
        levelPoint = 0;
    }

    /**
     * Reset current level after died.
     */
    public void resetCurrentLevel() {
        gamePoint -= levelPoint;
        levelPoint = 0;
        int numOfLives = mapList.get(LEVEL).getBomberNumOfLives();
        mapList.get(LEVEL).reset();
        mapList.get(LEVEL).setBomberNumOfLives(numOfLives);
    }

    /**
     * Reset all level.
     */
    public void resetAllLevel() {
        gamePoint = 0;
        levelPoint = 0;
        for (int i = 0; i <= LEVEL; i++) {
            mapList.get(LEVEL).reset();
        }
        LEVEL = 0;
    }

    /**
     * Get username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get game points.
     */
    public int getGamePoint() {
        return gamePoint;
    }

    public Map getCurrentMap() {
        return mapList.get(LEVEL);
    }
}
