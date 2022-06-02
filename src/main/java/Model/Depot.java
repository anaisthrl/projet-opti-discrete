package Model;

public class Depot extends Node {
    public Depot(int x, int y, int q, int i) {
        this.pos.setX(x);
        this.pos.setY(y);
        this.poids = q;
        this.index = i;
    }
}
