import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class CVRP extends JFrame{

    private Graph graph = new Graph();
    private int DECALAGE = 100;
    private int ZOOM = 6;
    private JButton buttonRedraw;




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
        /*for (int i = 0; i < graph.nodes.size(); i++){
            if(i==0){
                g2d.setColor(Color.green);
                Shape circleShape = new Ellipse2D.Double((graph.nodes.get(i).posX * ZOOM + DECALAGE) - ZOOM / 2, (graph.nodes.get(i).posY * ZOOM + DECALAGE) - ZOOM / 2, 15, 15);
                g2d.fill(circleShape);
            }
            else {
                g2d.setColor(Color.black);
                Shape circleShape = new Ellipse2D.Double((graph.nodes.get(i).posX * ZOOM + DECALAGE) - ZOOM / 2, (graph.nodes.get(i).posY * ZOOM + DECALAGE) - ZOOM / 2, 10, 10);
                g2d.fill(circleShape);
            }
        }*/

        Random rand = new Random();
        for(Vehicule vehicule : this.graph.vehicules) {
            Color visitColor = new Color(rand.nextInt(0,255), rand.nextInt(0,255), rand.nextInt(0,255));
            Iterator<Node> it = vehicule.tournee.iterator();
            Node previous = vehicule.tournee.get(0);
            while (it.hasNext()) {
                Node current = it.next();
                g.setColor(Color.BLACK);
                if (graph.nodes.indexOf(previous) == 0) g.setColor(Color.RED);
                //Pour chaque noeud on prend celui d'apres et on les relies
                g.fillOval((previous.posX * ZOOM + DECALAGE) - ZOOM, (previous.posY * ZOOM + DECALAGE)- ZOOM, 10, 10);

                g.setColor(visitColor);
                g.drawLine((previous.posX * ZOOM + DECALAGE)- ZOOM / 2, (previous.posY * ZOOM + DECALAGE)- ZOOM / 2, (current.posX * ZOOM + DECALAGE)- ZOOM / 2, (current.posY * ZOOM + DECALAGE)- ZOOM / 2);
                previous = current; //On garde en mémoire le noeud précedent
            }
        }
    }


}
