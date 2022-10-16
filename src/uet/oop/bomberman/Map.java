package uet.oop.bomberman;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemiesmaster.Balloom;
import uet.oop.bomberman.entities.enemiesmaster.Enemy;
import uet.oop.bomberman.entities.enemiesmaster.Oneal;
import uet.oop.bomberman.entities.itemmaster.BombItem;
import uet.oop.bomberman.entities.itemmaster.FlameItem;
import uet.oop.bomberman.entities.itemmaster.SpeedItem;
import uet.oop.bomberman.entities.stillobjectmaster.Brick;
import uet.oop.bomberman.entities.stillobjectmaster.Grass;
import uet.oop.bomberman.entities.stillobjectmaster.Wall;
import uet.oop.bomberman.graph_mapmaster.Graph;
import uet.oop.bomberman.graph_mapmaster.Vertice;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static uet.oop.bomberman.BombermanGame.HEIGHT;
import static uet.oop.bomberman.BombermanGame.WIDTH;

public class Map {

    private final List<List<Entity>> mapInfo = new ArrayList<>(); //Can be changed.
    int LEVEL;
    Graph graph ;

    public Map(int LEVEL) {
        this.LEVEL = LEVEL;
        readMapFromFile();

    }

    /**
     * Read map.
     */
    public void readMapFromFile() {
        File file = new File("res/levels/Level" + (LEVEL + 1) + ".txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("No file exist");
        }

        scanner.nextLine(); //Read first line in Level.txt.
        String rowString = ""; //Save info of a row into string.

        for (int i = 0; i < HEIGHT; i++) {
            rowString = scanner.nextLine();
            mapInfo.add(new ArrayList<>());
            for (int j = 0; j < WIDTH; j++) {
                switch (rowString.charAt(j)) {
                    case 'p':
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Entity bomberman = new Bomber(j, i, new CollisionManager(this, Bomber.WIDTH, Bomber.HEIGHT));
                        GameController.entities.get(LEVEL).add(bomberman);
                        break;
                    case '#':
                        mapInfo.get(i).add(new Wall(j, i, Sprite.wall.getFxImage()));
                        break;
                    case '*':
                        mapInfo.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case '1':
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy ballom = new Balloom(j, i, Sprite.balloom_left1.getFxImage(), new CollisionManager(this, Balloom.WIDTH, Balloom.HEIGHT));
                        GameController.entities.get(LEVEL).add(ballom);
                        break;
                    case '2':
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy oneal = new Oneal(j, i, Sprite.oneal_right1.getFxImage(), new CollisionManager(this, Oneal.WIDTH, Oneal.HEIGHT));
                        GameController.entities.get(LEVEL).add(oneal);
                        break;
                    default:
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        break;
                }
            }
        }
        scanner.close();
    }

    public void mapRender(GraphicsContext gc) {
        for (int i = 0; i < mapInfo.size(); i++) {
            for (int j = 0; j < mapInfo.get(i).size(); j++) {
                mapInfo.get(i).get(j).render(gc);
            }
        }
    }

    public Entity getEntityAt(int x, int y) {
        return mapInfo.get(y / Sprite.SCALED_SIZE).get(x / Sprite.SCALED_SIZE);
    }

    public void reset() {
        mapInfo.clear();
        GameController.entities.get(LEVEL).clear();
        readMapFromFile();
    }

    public Entity randomItem(int rowPos, int columnPos) {
        Entity newItem = null;
        Random random = new Random();
        int rand = random.nextInt(1);
        switch (rand) {
            case 0:
                newItem = new SpeedItem(columnPos, rowPos, Sprite.powerup_speed.getFxImage());
                break;
            case 1:
                newItem = new FlameItem(columnPos, rowPos, Sprite.powerup_flames.getFxImage());
                break;
            case 2:
                newItem = new BombItem(columnPos, rowPos, Sprite.powerup_bombs.getFxImage());
                break;
        }
        replace(rowPos, columnPos, new Grass(columnPos, rowPos, Sprite.grass.getFxImage()));
        return newItem;
    }

    public void replace(int rowPos, int columnPos, Entity newItem) {
        mapInfo.get(rowPos).set(columnPos, newItem);
    }

    public Graph convertToGraph(Vertice onealVertice) {
        //TODO: read from mapinfo to graph

        List<Vertice> verticesList = new ArrayList<>();

        //Add bomber pos and oneal pos as first and second vertice.
        Vertice bomberVertice = new Vertice((GameController.entities.get(LEVEL).get(0).getX()+Bomber.WIDTH/2)/Sprite.SCALED_SIZE,
                (GameController.entities.get(LEVEL).get(0).getY()+Bomber.HEIGHT/2)/Sprite.SCALED_SIZE);
        verticesList.add(bomberVertice);
        verticesList.add(onealVertice);

        //Then add all vertice from mapInfo list.
        for (int i = 1; i < mapInfo.size() - 1; i++) {
            for (int j = 1; j < mapInfo.get(i).size() - 1; j++) {
                if (mapInfo.get(i).get(j) instanceof Grass) {
                    Vertice vertice = new Vertice(j, i);
                    if (vertice.isAVerticeInGraph(mapInfo) && !vertice.equals(bomberVertice) && !vertice.equals(onealVertice)) {
                        verticesList.add(vertice);
                    }
                }
            }
        }

        Graph graph = new Graph(verticesList.size(), verticesList);


        //Finally let graph complete itself.
        graph.complete(mapInfo);

        //System.out.println(graph);
        return graph;
    }


}
