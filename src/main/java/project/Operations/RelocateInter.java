package project.Operations;

import project.Model.Depot;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RelocateInter extends Operation {
    private final static Random random = new Random();
    private Graph graph;

    public RelocateInter(Graph graph) {
        this.graph = graph;
    }

    public void relocateInter() {
        Vehicule randomVehicleToModify = this.graph.getVehicules().get(random.nextInt(this.graph.getVehicules().size()));
        int indexRandomClientToModify = random.nextInt(randomVehicleToModify.tournee.size());
        Node randomClientToModify = randomVehicleToModify.tournee.get(indexRandomClientToModify);

        if(!(randomClientToModify instanceof Depot)) {
            randomClientToModify = randomVehicleToModify.tournee.remove(indexRandomClientToModify);

            if (randomVehicleToModify.tournee.size() == 0) {
                this.graph.getVehicules().remove(randomVehicleToModify);
            }

            //On randomize l'ordre des véhicules
            ArrayList<Vehicule> randomizeVehicles = (ArrayList<Vehicule>) this.graph.getVehicules().clone();
            Collections.shuffle(randomizeVehicles);

            boolean clientAdded = false;
            //On parcourt tout les véhicules
            for (Vehicule v : randomizeVehicles) {
                //Sauf le véhicule que l'on modifie et
                if (!v.equals(randomVehicleToModify)) {
                    // S'il a la place pour accepter ce nouveau client
                    if (v.getNbColis() + randomClientToModify.getPoids() <= Vehicule.MAX_CAPACITY) {
                        //Alors on insere le point aléatoirement dans la route
                        v.tournee.add(random.nextInt(v.tournee.size()), randomClientToModify);
                        clientAdded = true;
                        break;
                    }

                }
            }
            if (!clientAdded) {
                Vehicule newVehicle = new Vehicule(graph.getDepot());
                if (randomClientToModify instanceof Depot) {
                    System.out.println("DEPOOOOOOOOOOT");
                }
                newVehicle.tournee.add(randomClientToModify);
                this.graph.getVehicules().add(newVehicle);
            }
        }

    }

    @Override
    public void apply(Graph graph) {
        relocateInter();
    }

    @Override
    public Operation revert() {
        return null;
    }

    @Override
    public boolean isValid(Graph graph) {
        return false;
    }
}
