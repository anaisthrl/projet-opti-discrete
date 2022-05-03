import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;


public class CVRP extends JFrame{

    private Graph graph = new Graph();
    private int DECALAGE = 100;
    private int ZOOM = 6;

    public CVRP(Graph g){
        graph = g;
        setTitle("Représentation CVRP");
        setSize(1000,1000);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < graph.nodes.size(); i++){
            if(i==0){
                g2d.setColor(Color.green);
                Shape circleShape = new Ellipse2D.Double((graph.nodes.get(i).posX * ZOOM + DECALAGE) - ZOOM / 2, (graph.nodes.get(i).posY * ZOOM + DECALAGE) - ZOOM / 2, 20, 20);
                g2d.fill(circleShape);
            }
            else {
                g2d.setColor(Color.black);
                Shape circleShape = new Ellipse2D.Double((graph.nodes.get(i).posX * ZOOM + DECALAGE) - ZOOM / 2, (graph.nodes.get(i).posY * ZOOM + DECALAGE) - ZOOM / 2, 15, 15);
                g2d.fill(circleShape);
            }
        }
    }


}
