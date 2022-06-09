package project.Algorithms;

import project.Model.Graph;

public interface Algorithme {

    void update();

    Graph getGraph();

    void setGraph(Graph graph);

    Graph stop();
}
