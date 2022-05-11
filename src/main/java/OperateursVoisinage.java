import java.util.ArrayList;

public class OperateursVoisinage {

    OperateursVoisinage(){}

    //cette fonction échange deux clients données dans notre tournée
    public void relocate2Clients(ArrayList<Node> list,int indexC1, int indexC2){
        if(indexC1 != 0 || indexC2 != 0) { // on ne change pas le point d'arrivée
            Node tempNode = list.get(indexC1);
            list.set(indexC1, list.get(indexC2));
            list.set(indexC2, tempNode);
        }
    }

    //cette fonction va échanger tous les clients de notre tournée
    public void relocateAllClients(ArrayList<Node> list){
        for(int i =1; i<= list.size()-1; i++){
            if(list.size() > 2) { // s'il n'y a que le point de départ et un autre point, on ne pourra pas changer
                if (i == list.size() - 1) relocate2Clients(list, i, 1);
                else relocate2Clients(list, i, i + 1);
            }
        }
    }


}
