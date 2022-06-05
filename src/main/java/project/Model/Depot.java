package project.Model;

public class Depot extends Node {
    public Depot(int x, int y, int q, int i) {
        this.setPos(new Point(x,y));
        this.setPoids(q);
        this.setIndex(i);
    }
}
