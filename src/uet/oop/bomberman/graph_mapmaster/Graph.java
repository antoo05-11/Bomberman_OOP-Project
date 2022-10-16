package uet.oop.bomberman.graph_mapmaster;

import javafx.scene.image.Image;
import javafx.util.Pair;
import uet.oop.bomberman.Map;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobjectmaster.Grass;

import java.util.*;

public class Graph {
    private int numOfVertices;
    private final List<Vertice> verticesList;
    private final List<List<Edge>> adjList;

    public Graph(int numOfVertices, List<Vertice> verticesList) {
        this.numOfVertices = numOfVertices;
        this.verticesList = verticesList;
        adjList = new ArrayList<>();
        while (numOfVertices-- > 0) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addVertice(Vertice v) {
        verticesList.add(v);
        adjList.add(new ArrayList<>());
    }

    public void addEdge(Edge edge) {
        adjList.get(edge.getDes()).add(edge);
        //edge.swapVertice();
        adjList.get(edge.getSrc()).add(edge);
    }

    public List<Integer> getAdj(int v) { //Return all indexes of all adj vertices in verticesList.
        List<Integer> adjVertex = new ArrayList<>();
        for (int i = 0; i < adjList.get(v).size(); i++) {
            adjVertex.add(adjList.get(v).get(i).getOther(v));
        }
        return adjVertex;
    }

    public void complete(List<List<Entity>> mapInfo) {
        for (int i = 0; i < verticesList.size() - 1; i++) {
            int firstX = verticesList.get(i).getxTilePos();
            int firstY = verticesList.get(i).getyTilePos();

            for (int j = i + 1; j < verticesList.size(); j++) {
                {
                    int secondX = verticesList.get(j).getxTilePos();
                    int secondY = verticesList.get(j).getyTilePos();

                    // Case vertice i and j on same column.
                    if (secondX == firstX) {
                        boolean check = true;
                        int start = Math.min(firstY, secondY) + 1;
                        int end = Math.max(firstY, secondY) - 1;
                        int distance = end - start + 2;
                        while (start <= end) {
                            Vertice vertice = new Vertice(secondX, start);
                            if (!(mapInfo.get(start).get(secondX) instanceof Grass)
                                    || verticesList.contains(vertice)) {
                                check = false;
                                break;
                            }
                            start++;
                        }
                        if (check) {
                            Edge edge = new Edge(i, j, distance);
                            addEdge(edge);
                        }
                    }

                    // Case vertice i and j on same row.
                    if (secondY == firstY) {
                        boolean check = true;
                        int start = Math.min(firstX, secondX) + 1;
                        int end = Math.max(firstX, secondX) - 1;
                        int distance = end - start + 2;
                        while (start <= end) {
                            Vertice vertice = new Vertice(start, secondY);
                            if (!(mapInfo.get(secondY).get(start) instanceof Grass)
                                    || verticesList.contains(vertice)) {
                                check = false;
                                break;
                            }
                            start++;
                        }
                        if (check) {
                            Edge edge = new Edge(i, j, distance);
                            addEdge(edge);
                        }
                    }
                }
            }
        }
    }

    public boolean isConnected() {
        boolean[] visited = new boolean[verticesList.size()];
        for (boolean i : visited) {
            i = false;
        }

        DepthFirstSearch(0, visited); //Pos 0 - Bomberman

        return visited[1]; // Pos 1 - Oneal
    }

    private void DepthFirstSearch(int i, boolean[] visited) {
        visited[i] = true;
        for (int j = 0; j < getAdj(i).size(); j++) {
            int n = getAdj(i).get(j);
            if (!visited[n]) DepthFirstSearch(n, visited);
        }
    }

    @Override
    public String toString() {
        String graphInfo = "New Graph: \n";
        for (int i = 0; i < verticesList.size(); i++) {
            graphInfo = graphInfo + "Vertex " + i + ": " + verticesList.get(i) + ": ";
            for (int j = 0; j < adjList.get(i).size(); j++) {
                graphInfo = graphInfo + verticesList.get(adjList.get(i).get(j).getOther(i)) + ", ";
            }
            graphInfo += '\n';
        }
        return graphInfo;
    }

    public List<Vertice> findWay(int s, int t) {
        int[] prev = new int[numOfVertices];
        List<Integer> distanceToSrc = new ArrayList<>();

        for (int i = 0; i < numOfVertices; i++) {
            distanceToSrc.add(10000);
        }

        distanceToSrc.set(s, 0); //Set distance from oneal to itself 0.

        Queue<Edge> Q = new PriorityQueue<>();

        Q.add(new Edge(s, s, 0));

        while (!Q.isEmpty()) {
            Edge min = Q.poll();
            int u = min.getDes(); //Get vertice of which distance to src is min.
            int distance = min.getWeight();

            if (distance > distanceToSrc.get(u)) continue;

            for (int i = 0; i < adjList.get(u).size(); i++) {
                int v = adjList.get(u).get(i).getOther(u);
                int weight = adjList.get(u).get(i).getWeight();
                if (distanceToSrc.get(v) > distanceToSrc.get(u) + weight) {
                    distanceToSrc.set(v, distanceToSrc.get(u) + weight);
                    Q.add(new Edge(s, v, distanceToSrc.get(v)));
                    prev[v] = u;
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        while (true) {
            path.add(t);
            if (t == s) break;
            t = prev[t];
        }
        Collections.reverse(path);
        List<Vertice> verticesPathList = new ArrayList<>();
        for (int x : path) {
            verticesPathList.add(verticesList.get(x));
        }
        return verticesPathList;
    }
}
