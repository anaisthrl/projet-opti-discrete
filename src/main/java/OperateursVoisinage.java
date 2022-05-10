import java.util.ArrayList;

public class OperateursVoisinage {

    OperateursVoisinage(){}

    public void relocate(ArrayList<Integer> arIndex){
        //tri des index
        triInsertion(arIndex);
        for(int i =0; i<=arIndex.size()-1; i++)
            System.out.print(arIndex.get(i) + " ");
    }

    private void triInsertion(ArrayList<Integer> arIndex){
        int tmp;
        int k;

        for(int i = 2; i<= arIndex.size()-2; i++){
            tmp = arIndex.get(i);
            k = i;
            while(k>1 && arIndex.get(i-1)> tmp){
                arIndex.set(k,arIndex.get(k-1));
                k--;
            }
            arIndex.set(k,tmp);
        }
    }
}
