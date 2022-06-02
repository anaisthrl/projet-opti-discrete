package Operations;

import Model.Graph;

public abstract class Operation {
    public abstract void apply(Graph graph);

    public abstract Operation revert();

    public abstract boolean isValid(Graph graph);
}
