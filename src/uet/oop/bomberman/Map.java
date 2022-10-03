package uet.oop.bomberman;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
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
    private List<Entity> stillObjects = new ArrayList<>();
    int LEVEL;

    public Map(int LEVEL) {
        this.LEVEL = LEVEL;
    }

    public void createMap() throws IOException {

        File file = new File("res/levels/Level" + LEVEL + ".txt");
        Scanner scanner = new Scanner(file);

        scanner.nextLine();

        List<List<Character>> mapInfo = new ArrayList<>();
        List<Character> rowInfo;
        String rowString = "";
        for (int i = 0; i < HEIGHT; i++) {
            if (scanner.hasNext()) {
                rowString = scanner.nextLine();
            }

            for (int j = 0; j < WIDTH; j++) {
                Entity object;

                rowInfo = new ArrayList<>();
                rowInfo.add(rowString.charAt(j));

                switch (rowString.charAt(j)) {
                    case '#':
                        object = new Wall(j, i, Sprite.wall.getFxImage());
                        break;
                    case '*':
                        object = new Wall(j, i, Sprite.brick.getFxImage());
                        break;
                    case 'x':
                        object = new Wall(j, i, Sprite.portal.getFxImage());
                        break;
                    default:
                        object = new Grass(j, i, Sprite.grass.getFxImage());
                        break;
                }

                mapInfo.add(rowInfo);

                stillObjects.add(object);
            }
        }
    }

    public void mapRender(GraphicsContext gc) {
        stillObjects.forEach(g -> g.render(gc));
    }
}
