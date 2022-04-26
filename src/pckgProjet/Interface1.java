package pckgProjet;

import javax.swing.JFrame;
import java.util.ArrayList;

public class Interface1 extends JFrame{

    private static ArrayList<Integer> AL_donnees = new ArrayList<>();

    public static void main(String [] args)
    {
        //point 1
        AL_donnees.add(0);
        AL_donnees.add(82);
        AL_donnees.add(76);
        AL_donnees.add(0);

        //point 2
        AL_donnees.add(1);
        AL_donnees.add(96);
        AL_donnees.add(44);
        AL_donnees.add(19);

        new CVRP(AL_donnees);
    }
}
