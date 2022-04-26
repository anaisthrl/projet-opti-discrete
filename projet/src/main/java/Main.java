import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String args[]) throws IOException {
        Graph graph = load("C:/Users/Epulapp/Documents/4A/OptiDiscrete/Projet/A6409.txt");

        for (int i = 0; i < graph.nodes.size(); i++) {
            System.out.println("index : " + i + "; "
                    + " x : " + graph.nodes.get(i).posX + "; "
                    + " y : " + graph.nodes.get(i).posY + "; "
                    + " q : " + graph.nodes.get(i).poids);
        }
    }


    public static Graph load(String pathFile) throws IOException {
        File myFile = new File(pathFile);

        FileInputStream inputStream = new FileInputStream(myFile);

        // Problem problem = new Problem();

        List<String> inputLines = IOUtils.readLines(inputStream);

        /*
         * READ input
         */
        int i = 0, x, y, q;
        String[] splittedLine;
        Graph graph = new Graph();
        graph.nodes = new ArrayList<Node>();

        for (String line : inputLines) {

            splittedLine = line.trim().split(";");

            //     log.debug("line {}: {}", i, Arrays.toString(splittedLine));

            if (i == 0) {

            }
            else if (i == 1){                            // second line: depot coordinates
                x = Integer.parseInt(splittedLine[1]);    // x-coordinates
                y = Integer.parseInt(splittedLine[2]);    // y-coordinates
                q = Integer.parseInt(splittedLine[3]);    // y-coordinates
                graph.nodes.add(new Depot(x, y, q));

            } else {                                        // other lines
                x = Integer.parseInt(splittedLine[1]);    // x-coordinates
                y = Integer.parseInt(splittedLine[2]);    // y-coordinates
                q = Integer.parseInt(splittedLine[3]);    // demand

                graph.nodes.add(new Client(x, y, q));
            }
            i++;
        }
        return graph;
    }
}
