package project.Algorithms;

public enum Algorithm {
    RANDOM("Random"),
    RECUIT("Recuit"),
    TABOU("Tabou");

    public final String algorithmName;

    Algorithm(String nomAlgoTourne)
    {
        this.algorithmName = nomAlgoTourne;
    }
}
