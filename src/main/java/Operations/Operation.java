package Operations;

import Model.Graph;

public abstract class Operation {
    public abstract Graph apply(Graph graph);

    public abstract Graph revert(Graph graph);

    public abstract boolean isValid(Graph graph);
}
