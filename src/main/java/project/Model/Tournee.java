package project.Model;

import java.util.ArrayList;

public class Tournee extends ArrayList<Node> {
    public boolean changed = false;

    public ArrayList<Node> getTournee() {
        return this;
    }

    public double getDistanceBetween2Node(Node a, Node b) {
        return Math.sqrt(Math.pow(a.getPos().getX() - b.getPos().getX(), 2)
                + Math.pow(a.getPos().getY() - b.getPos().getY(), 2));
    }

    public double getDistanceTournee() {
        double res = 0;

        for (int i = 1; i < this.size(); i++) {
            res += getDistanceBetween2Node(this.get(i-1), this.get(i));
        }
        return  res;
    }

    public void addElementAtIndex(int index, Node node) {
        changed = true;
        this.add(index, node);
    }
}
