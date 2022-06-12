package project.Algorithms;

public enum Algorithm {
    RECUIT("Recuit"),
    TABOU("Tabou");

    public final String algorithmName;

    Algorithm(String nomAlgoTourne)
    {
        this.algorithmName = nomAlgoTourne;
    }
}
