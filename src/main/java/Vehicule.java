import java.util.ArrayList;

public class Vehicule {
    public int nbColis;
    public ArrayList<Node> tournee;

    public double getDistanceBetween2Node(Node a, Node b) {
        return Math.sqrt(Math.pow(a.posX - b.posX, 2) + Math.pow(a.posY - b.posY, 2));
    }

    public double getDistanceTournee() {
        double res = 0;
        for (int i = 1; i < tournee.size(); i++) {
            res += getDistanceBetween2Node(tournee.get(i-1), tournee.get(i));
        }
        return  res;
    }
}
