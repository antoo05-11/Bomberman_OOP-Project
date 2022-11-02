package uet.oop.bomberman.map_graph;

public class Edge implements Comparable<Edge> {
    int src;
    int des;
    int weight;

    public Edge(int src, int des, int weight) {
        this.src = src;
        this.des = des;
        this.weight = weight;
    }

    public int getSrc() {
        return src;
    }

    public int getDes() {
        return des;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge o) {
        return Integer.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("Edge: (%d,%d) - Weight: %d", src, des, weight);
    }

    public int getOther(int i) {
        if (i == des) return src;
        return des;
    }
}