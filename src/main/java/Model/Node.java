package Model;

public abstract class Node {
    private Point pos;
    private Integer poids;
    private Integer index;

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public Integer getPoids() {
        return poids;
    }

    public void setPoids(Integer poids) {
        this.poids = poids;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public double getDistanceToNode(Node b) {
        return Math.sqrt(Math.pow(this.getPos().getX() - b.getPos().getX(), 2)
                + Math.pow(this.getPos().getY() - b.getPos().getY(), 2));
    }
}
