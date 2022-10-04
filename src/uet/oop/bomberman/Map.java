package uet.oop.bomberman;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobjectmaster.Brick;
import uet.oop.bomberman.entities.stillobjectmaster.Grass;
import uet.oop.bomberman.entities.stillobjectmaster.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static uet.oop.bomberman.BombermanGame.*;

public class Map {
    private List<List<Entity>> mapInfo = new ArrayList<>();
    private List<Entity> renderObject = new ArrayList<>();
    int LEVEL;

    public Map(int LEVEL) {
        this.LEVEL = LEVEL;
    }

    public void createMap() throws IOException {

        File file = new File("res/levels/Level" + LEVEL + ".txt");
        Scanner scanner = new Scanner(file);
        scanner.nextLine(); //Read first line in Level.txt.
        String rowString = ""; //Save info of a row into string.

        for (int i = 0; i < HEIGHT; i++) {
            rowString = scanner.nextLine();
            List<Entity> stillObject = new ArrayList<>();
            for (int j = 0; j < WIDTH; j++) {
                switch (rowString.charAt(j)) {
                    case '#':
                        stillObject.add(new Wall(j, i, Sprite.wall.getFxImage()));
                        renderObject.add(new Wall(j, i, Sprite.wall.getFxImage()));
                        break;
                    case '*':
                        stillObject.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        renderObject.add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    default:
                        stillObject.add(new Grass(j, i, Sprite.grass.getFxImage()));
                        renderObject.add(new Grass(j, i, Sprite.grass.getFxImage()));
                        break;
                }
            }
            mapInfo.add(stillObject);
        }
        scanner.close();
    }

    public void mapRender(GraphicsContext gc) {
        for (Entity x : renderObject) {
            x.render(gc);
        }
    }

    public Entity getEntityAt(int x, int y) {
        return mapInfo.get(y / Sprite.SCALED_SIZE).get(x / Sprite.SCALED_SIZE);
    }
}
