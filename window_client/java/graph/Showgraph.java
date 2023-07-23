package graph;

import java.util.ArrayList;

public class Showgraph {
    Database db;

    public Showgraph() {
        db = new Database();
        TempLinechart tl = new TempLinechart(this);
    }

    public ArrayList<Double> getTemperatureData() {
        return db.getTemperatureData();
    }

    public static void main(String[] args) {
        new Showgraph();

    }
}
