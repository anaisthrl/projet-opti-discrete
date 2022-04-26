package pckgProjet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class CVRP extends JFrame{

    private ArrayList<Integer> AL_donnees = new ArrayList<>();

    public CVRP(ArrayList<Integer> AL){
        AL_donnees = AL;
        setTitle("Représentation CVRP");
        setSize(500,500);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    @Override
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawOval(58,41, 10, 10);
    }


}
