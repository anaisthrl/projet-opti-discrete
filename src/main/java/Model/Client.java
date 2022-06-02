package Model;

public class Client extends Node {
    public Client(int x, int y, int q, int i) {
        this.getPos().setX(x);
        this.getPos().setX(y);
        this.setPoids(q);
        this.setIndex(i);
    }
}
