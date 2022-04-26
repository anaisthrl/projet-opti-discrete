import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class CVRP extends JFrame{

    private Graph graph = new Graph();

    public CVRP(Graph g){
        graph = g;
        setTitle("Représentation CVRP");
        setSize(500,500);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < graph.nodes.size(); i++){
            g2d.drawOval(graph.nodes.get(i).posX, graph.nodes.get(i).posY,10,10);
        }
    }


}
