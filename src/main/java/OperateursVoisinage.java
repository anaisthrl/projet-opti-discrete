import java.util.ArrayList;

public class OperateursVoisinage {

    OperateursVoisinage(){}

    //cette fonction va placer un client derrière un autre pour une tournée donnée
    public void relocate2Clients(ArrayList<Node> list,int indexC1, int indexCAChanger){
        if(indexCAChanger != 0 && indexCAChanger < list.size() && indexC1 < list.size()) { // on ne change pas le point d'arrivée + vérification indexOutOfBounds
            Node tempNode = list.get(indexC1+1);
            list.set(indexC1+1, list.get(indexCAChanger));
            for(int i = indexC1+2; i<list.size(); i++){
                Node tempNode2 = list.get(i);
                if(tempNode2 == list.get(indexC1+1)) {
                    list.set(i,tempNode);
                    return;
                }
                else{
                    list.set(i,tempNode);
                    tempNode = tempNode2;
                }
            }
        }
    }


}
