package uet.oop.bomberman.map_graph;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movingobject.enemies.Balloom;
import uet.oop.bomberman.entities.movingobject.enemies.Doll;
import uet.oop.bomberman.entities.movingobject.enemies.Enemy;
import uet.oop.bomberman.entities.movingobject.enemies.Oneal;
import uet.oop.bomberman.entities.stillobject.Portal;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.entities.stillobject.item.BombItem;
import uet.oop.bomberman.entities.stillobject.item.FlameItem;
import uet.oop.bomberman.entities.stillobject.item.SpeedItem;
import uet.oop.bomberman.entities.stillobject.Brick;
import uet.oop.bomberman.entities.stillobject.Grass;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static uet.oop.bomberman.BombermanGame.HEIGHT;
import static uet.oop.bomberman.BombermanGame.WIDTH;

public class Map {
    private int widthTile;
    private int heightTile;

    public int getWidthPixel() {
        return widthTile * Sprite.SCALED_SIZE;
    }

    public int getHeightPixel() {
        return heightTile * Sprite.SCALED_SIZE;
    }

    //private final List<List<Entity>> mapInfo = new ArrayList<>(); //Can be changed.
    private Entity[][] mapInfo;
    int LEVEL;
    private Graph graph;
    protected Entity[][] grassList;
    protected Entity[][] bombsArr = new Entity[HEIGHT][WIDTH];
    protected List<Entity> bombsList;
    protected int[][] listItem = new int[HEIGHT][WIDTH];
    protected int[][] listPortal = new int[HEIGHT][WIDTH];

    public Map(int LEVEL) {
        this.LEVEL = LEVEL;
        readMapFromFile();
        convertMapToGraph();
    }

    public List<Entity> getBombsList() {
        return bombsList;
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Read map, add still objects to mapInfo such as wall, brick, grass.
     * Bomber and other enemies are added to entities list in GameController class.
     */
    public void readMapFromFile() {
        File file = new File("res/levels/Level" + (LEVEL + 1) + ".txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("No file exist");
        }
        String rowString; //Save info of a row into string.
        assert scanner != null;
        rowString = scanner.nextLine(); //Read first line in Level.txt.

        String[] specs = rowString.split(" "); //Line 1 splits: LEVEL, WIDTH, HEIGHT.
        widthTile = Integer.parseInt(specs[2]);
        heightTile = Integer.parseInt(specs[1]);

        mapInfo = new Entity[heightTile][widthTile];
        grassList = new Entity[heightTile][widthTile];
        bombsArr = new Entity[heightTile][widthTile];
        bombsList = new LinkedList<>();

        for (int i = 0; i < heightTile; i++) {
            rowString = scanner.nextLine();
            // mapInfo.add(new ArrayList<>());
            for (int j = 0; j < widthTile; j++) {
                grassList[i][j] = new Grass(j, i, Sprite.grass.getFxImage());
                switch (rowString.charAt(j)) {
                    case 'p':
                        Entity bomberman = new Bomber(j, i, new CollisionManager(this, Bomber.WIDTH, Bomber.HEIGHT));
                        GameController.entities.get(LEVEL).add(bomberman);
                        break;
                    case '#':
                        mapInfo[i][j] = new Wall(j, i, Sprite.wall.getFxImage());
                        break;
                    case '*':
                        mapInfo[i][j] = new Brick(j, i, Sprite.brick.getFxImage());
                        break;
                    case 'x':
                        mapInfo[i][j] = new Brick(j, i, Sprite.brick.getFxImage());
                        listPortal[i][j] = Portal.code;
                        break;
                    case '1':
                        Enemy balloom = new Balloom(j, i, Sprite.balloom_left1.getFxImage(), new CollisionManager(this, Balloom.WIDTH, Balloom.HEIGHT));
                        GameController.entities.get(LEVEL).add(balloom);
                        break;
                    case '2':
                        Enemy oneal = new Oneal(j, i, Sprite.oneal_right1.getFxImage(), new CollisionManager(this, Oneal.WIDTH, Oneal.HEIGHT), GameController.entities.get(LEVEL).get(0));
                        GameController.entities.get(LEVEL).add(oneal);
                        break;
                    case '3':
                        Enemy doll = new Doll(j, i, Sprite.doll_right1.getFxImage(), new CollisionManager(this, Doll.WIDTH, Doll.HEIGHT));
                        GameController.entities.get(LEVEL).add(doll);
                        break;
                    case 'b':
                        mapInfo[i][j] = new Brick(j, i, Sprite.brick.getFxImage());
                        listItem[i][j] = BombItem.code;
                        break;
                    case 'f':
                        mapInfo[i][j] = new Brick(j, i, Sprite.brick.getFxImage());
                        listItem[i][j] = FlameItem.code;
                        break;
                    case 's':
                        mapInfo[i][j] = new Brick(j, i, Sprite.brick.getFxImage());
                        listItem[i][j] = SpeedItem.code;
                        break;
                    default:
                        break;
                }
            }
        }
        scanner.close();
    }

    /**
     * Render still object.
     */
    public void mapRender(GraphicsContext gc) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                grassList[i][j].render(gc);
            }
        }

        for (int i = 0; i < heightTile; i++) {
            for (int j = 0; j < widthTile; j++) {
                if (mapInfo[i][j] != null) {
                    mapInfo[i][j].render(gc);
                }
            }
        }
        for (Entity i : bombsList) i.render(gc);
    }

    /**
     * Get entity in map.
     *
     * @param x int
     * @param y int
     * @return Entity
     */
    public Entity getEntityAt(int x, int y) {
        return mapInfo[y / Sprite.SCALED_SIZE][x / Sprite.SCALED_SIZE];
    }

    /**
     * Get item in map.
     *
     * @param xPos int
     * @param yPos int
     * @return listItem
     */
    public int getItem(int xPos, int yPos) {
        return listItem[yPos][xPos];
    }

    /**
     * Get portal in map.
     *
     * @param xPos int
     * @param yPos int
     * @return listPortal
     */
    public int getPortal(int xPos, int yPos) {
        return listPortal[yPos][xPos];
    }

    /**
     * Read map again and refresh entities list in GameController class.
     */
    public void reset() {
        GameController.entities.get(LEVEL).clear();
        bombsArr = null;
        bombsList = null;
        mapInfo = null;
        readMapFromFile();
        convertMapToGraph();
    }


    /**
     * Replace any entity by other entity in mapInfo.
     */
    public void replace(int rowPos, int columnPos, Entity insertedObject) {
        mapInfo[rowPos][columnPos] = insertedObject;
    }

    /**
     * Read from mapInfo to graph.
     * Add all cells on map and add adj vertices into Graph.
     */
    public void convertMapToGraph() {
        List<Vertice> verticesList = new ArrayList<>();
        for (int i = 0; i < heightTile; i++) {
            for (int j = 0; j < widthTile; j++) {
                verticesList.add(new Vertice(j, i));
            }
        }
        graph = new Graph(verticesList);
        for (int i = 0; i < verticesList.size() - 1; i++) {
            for (int j = i + 1; j < verticesList.size(); j++) {
                boolean isAdj = false;
                int x1 = verticesList.get(i).getxTilePos();
                int x2 = verticesList.get(j).getxTilePos();
                int y1 = verticesList.get(i).getyTilePos();
                int y2 = verticesList.get(j).getyTilePos();
                if (mapInfo[y1][x1] == null
                        && mapInfo[y2][x2] == null
                        && bombsArr[y1][x1] == null
                        && bombsArr[y2][x2] == null) {
                    if (x1 == x2) {
                        if (y1 == y2 + 1) isAdj = true;
                        else if (y1 == y2 - 1) isAdj = true;
                    } else if (y1 == y2) {
                        if (x1 == x2 + 1) isAdj = true;
                        else if (x1 == x2 - 1) isAdj = true;
                    }
                }
                if (isAdj) graph.addAdjVertice(i, j);
            }
        }
    }

    public static int MAX_BOMB = 3;

    /**
     * Set bomb for bomber.
     *
     * @param xPixel int
     * @param yPixel int
     */
    public void setBomb(int xPixel, int yPixel) {
        if (bombsList.size() < MAX_BOMB) {
            int xTile = xPixel / Sprite.SCALED_SIZE;
            int yTile = yPixel / Sprite.SCALED_SIZE;
            if (bombsArr[yTile][xTile] == null) {
                bombsArr[yTile][xTile] = new Bomb(xTile, yTile, Sprite.bomb.getFxImage(), this);
                bombsList.add(bombsArr[yTile][xTile]);
                convertMapToGraph();
            }
        }
    }

    /**
     * Update bomb list.
     */
    public void updateBombsList() {
        bombsList.forEach(Entity::update);
        if (!bombsList.isEmpty())
            if (((Bomb) bombsList.get(0)).getBombStatus() == Bomb.BombStatus.DISAPPEAR) {
                bombsArr[bombsList.get(0).getY() / Sprite.SCALED_SIZE][bombsList.get(0).getX() / Sprite.SCALED_SIZE] = null;
                bombsList.remove(0);
                convertMapToGraph();
            }
    }
}
