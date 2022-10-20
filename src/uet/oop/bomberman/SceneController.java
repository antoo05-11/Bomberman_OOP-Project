package uet.oop.bomberman;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SceneController implements Initializable {
    public static final int SCREEN_WIDTH = 700;
    public static final int SCREEN_HEIGHT = 400;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button musicButton;
    @FXML
    private Button playButton;

    @FXML
    private Button BacktolobbyButton;

    @FXML
    private Button quitButton;
    @FXML
    private Button rankButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL lobbyUrl, playingURL;
        lobbyUrl = BombermanGame.class.getResource("/LobbyScene.fxml");
        playingURL = BombermanGame.class.getResource("/PlayingScene.fxml");

        /**
         * Disable action of button when enter SPACE KEY.
         */
        if (location.equals(lobbyUrl)) {
            //playButton.addEventFilter(KeyEvent.KEY_PRESSED, Event::consume);
            playButton.setFocusTraversable(false);
            musicButton.setFocusTraversable(false);
            quitButton.setFocusTraversable(false);
            rankButton.setFocusTraversable(false);
        }
        if (location.equals(playingURL)) {
            BacktolobbyButton.setFocusTraversable(false);
        }

    }

    public void setEffect(MouseEvent event) {
        Effect shadow = new Glow();
        ((Button) event.getSource()).setEffect(shadow);
    }

    public void removeEffect(MouseEvent event) {
        ((Button) event.getSource()).setEffect(null);
    }

    public void switchToLobbyScene(MouseEvent event) {
        URL url = BombermanGame.class.getResource("/LobbyScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        scene = new Scene(root);
        stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPlayingScene(ActionEvent event) {
        GameController.gameStatus = GameController.GameStatus.GAME_PLAYING;
        URL url = BombermanGame.class.getResource("/PlayingScene.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        try {
            root = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((VBox) root).getChildren().add(GameController.playingCanvas);

        scene = new Scene(root);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                GameController.entities.get(GameController.LEVEL).get(0).saveKeyEvent(event.getCode(), true);
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                GameController.entities.get(GameController.LEVEL).get(0).saveKeyEvent(event.getCode(), false);
            }
        });

        stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void setMusic(MouseEvent event) {
        GameController.audioController.setMuted(!GameController.audioController.isMuted());
    }
}
