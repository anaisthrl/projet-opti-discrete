package project.Model;

public class Client extends Node {
    public Client(int x, int y, int q, int i) {
        this.setPos(new Point(x,y));
        this.setPoids(q);
        this.setIndex(i);
    }
}
