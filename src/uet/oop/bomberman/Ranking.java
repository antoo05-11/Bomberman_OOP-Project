package uet.oop.bomberman;

import javafx.beans.property.SimpleStringProperty;

public class Ranking implements Comparable {
    private SimpleStringProperty username;
    private SimpleStringProperty point;
    private SimpleStringProperty timeDate;
    private SimpleStringProperty rank;
    private int gamePoint;

    public Ranking(String username, String point, String timeDate) {
        this.point = new SimpleStringProperty(point);
        gamePoint = Integer.parseInt(point);
        this.timeDate = new SimpleStringProperty(timeDate);
        this.username = new SimpleStringProperty(username);
        rank=new SimpleStringProperty();
    }

    public String getRank() {
        return rank.get();
    }

    public String getPoint() {
        return point.get();
    }

    public SimpleStringProperty pointProperty() {
        return point;
    }

    public void setPoint(String point) {
        this.point.set(point);
    }

    public void setRankIndex(String rankIndex) {
        this.rank.set(rankIndex);
    }

    public String getTimeDate() {
        return timeDate.get();
    }

    public SimpleStringProperty timeDateProperty() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate.set(timeDate);
    }

    public String getUsername() {
        return username.get();
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Ranking) {
            return Integer.compare(gamePoint, ((Ranking) o).gamePoint);
        }
        return -1;
    }
}
