package uet.oop.bomberman.scenemaster;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.GameController;

import java.net.URL;

public class LobbyScene extends RootScene{
    public LobbyScene() {
        FXMLLoader fxmlLoader = new FXMLLoader(BombermanGame.class.getResource("/LobbyScene.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
