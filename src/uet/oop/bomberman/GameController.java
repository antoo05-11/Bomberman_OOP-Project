package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import uet.oop.bomberman.audiomaster.AudioController;
import uet.oop.bomberman.scenemaster.LobbyController;
import uet.oop.bomberman.scenemaster.PlayingController;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static uet.oop.bomberman.GameController.GameStatus.GAME_LOBBY;
import static uet.oop.bomberman.GameController.GameStatus.GAME_PLAYING;

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
    private String username = "";

    public void setUsername(String username) {
        this.username = username;
    }

    public void plusPoint(int rewardPoint) {
        gamePoint.addAndGet(rewardPoint);
    }

    public static GameStatus gameStatus;

    public static int LEVEL = 0;
    public static final int MAX_LEVEL = 4;

    int currentLevelCode = 0;

    private LobbyController lobbyController;
    private PlayingController playingController;

    private AtomicInteger gamePoint = new AtomicInteger();
    /**
     * AudioController can be used anywhere to play any audio if needed.
     * See how to play audio at {@link AudioController}.
     */
    public static AudioController audioController = new AudioController();
    private Level currentLevel;

    /**
     * Constructor with available stage.
     */
    public GameController(Stage stage) {
        this.stage = stage;
        currentLevel = new Level(gamePoint);
    }

    public Stage getStage() {
        return stage;
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
    Scene playingScene;
    Scene lobbyScene;

    /**
     * Run game engine.
     */
    public void run() {
        stage.getIcons().add(new Image("/stageIcon.png"));
        stage.setTitle("BOMBERMAN");
        stage.setResizable(false);
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
    private void update() {
        lobbyController.updateStatus();
        playingController.updateStatus();
        audioController.run();
    }

    private void render() {
        if (gameStatus == GAME_PLAYING) {
            currentLevel.render();
        }
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Render into playing canvas by gc.
     */

    public void reset() {
        gamePoint.set(0);
        currentLevelCode = 0;
        currentLevel.loadLevel(currentLevelCode);
        GameController.gameStatus = GAME_LOBBY;
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
        return gamePoint.get();
    }
}
