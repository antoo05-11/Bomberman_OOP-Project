package uet.oop.bomberman.map_graph;

import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.*;


public class Graph {
    private final int numOfVertices;
    private final List<Vertice> verticesList;
    private final List<Integer>[] adj;

    public Graph(List<Vertice> verticesList) {
        this.verticesList = verticesList;
        numOfVertices = verticesList.size();
        adj = new List[numOfVertices];
        for (int i = 0; i < numOfVertices; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addAdjVertice(int v1, int v2) {
        adj[v1].add(v2);
        adj[v2].add(v1);
    }

    @Override
    public String toString() {
        String graphInfo = "New graph: \n";
        for (int i = 0; i < numOfVertices; i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < adj[i].size(); j++) {
                System.out.print(adj[i].get(j) + " ");
            }
            System.out.println();
        }
        return graphInfo;
    }

    public List<Vertice> breathFirstSearch(int start, int end) {
        boolean[] marked = new boolean[numOfVertices];
        int[] trace = new int[numOfVertices];

        for (boolean i : marked) {
            i = false;
        }
        marked[start] = true;

        Queue<Integer> q = new LinkedList();
        q.add(start);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u].get(i);
                if (!marked[v]) {
                    q.add(v);
                    marked[v] = true;
                    trace[v] = u;
                }
            }
        }

        if (!marked[end]) return null;

        List<Integer> path = new ArrayList<>();
        while (true) {
            path.add(end);
            if (end == start) {
                if (path.size() == 1) path.add(start);
                break;
            }
            end = trace[end];
        }
        Collections.reverse(path);

        List<Vertice> verticesPathList = new ArrayList<>();
        for (int i : path) verticesPathList.add(verticesList.get(i));
        return verticesPathList;

    }

    public static int getVerticeIndex(int xPixel, int yPixel) {
        int x = xPixel / Sprite.SCALED_SIZE;
        int y = yPixel / Sprite.SCALED_SIZE;
        return y * BombermanGame.WIDTH + x;
    }
}
