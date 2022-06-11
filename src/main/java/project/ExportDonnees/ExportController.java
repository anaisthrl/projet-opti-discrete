package project.ExportDonnees;

import com.opencsv.CSVWriter;
import project.Algorithms.Algorithm;
import project.Algorithms.Random;
import project.Algorithms.RecuitSimule;
import project.Algorithms.TabuSearch;
import project.Main;
import project.Model.Graph;
import project.Model.Vehicule;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class ExportController {
    public static void prepareExport(Algorithm algorithm, int iterationCount, float variation, float temperature, int tabuListSize) {
        ExportService exportService;
        if(Algorithm.RECUIT.equals(algorithm)){
            exportService = new ExportService(algorithm, iterationCount, variation, temperature);
        }else{
            exportService = new ExportService(algorithm, iterationCount, tabuListSize);
        }
        CSVWriter writer = exportService.createCsv();

        long start = System.nanoTime();
        System.out.println("[Simulation] Lancement de tous les fichiers les parametres " + exportService);

        File directory = new File(new File("").getAbsolutePath() + "\\ressources\\");
        FilenameFilter filter = (f, name) -> name.endsWith(".txt");
        String[] pathnames = directory.list(filter);

        assert pathnames != null;
        long startLocal, stopLocal;
        int index = 1;
        int totalSize = pathnames.length;
        for (String file : pathnames) {
            startLocal = System.nanoTime();
            System.out.printf("[Simulation] (%s) Lecture fichier %s", index++ + "/" + totalSize, file);

            try {
                ExportController.startSimu(file, exportService, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }

            stopLocal = System.nanoTime();
            System.out.println(" - simulation terminee (" + Math.abs(startLocal- stopLocal) / 1000000 + "ms).");
        }

        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long stop = System.nanoTime();
        System.out.printf("[SIMULATION] Simulation terminee (temps total = %.2fs).%n", Math.abs(start - stop) / 1000000000.0f);
    }

    public static void startSimu(String nomFichier, ExportService exportService, CSVWriter writer) throws IOException {
        File file = new File(new File("").getAbsolutePath() + "\\ressources\\" + nomFichier);
        Graph _graph = Main.load(file);
        Graph graph = Random.fillVehicle(_graph, Vehicule.MAX_CAPACITY);

        ExportData csvData = ExportDataBuilder.builder()
                .nomFichier(nomFichier)
                .nbClient(graph.getNodes().size())
                .baseFitness(graph.getFitness())
                .nbVehicule(graph.getVehicules().size())
                .nomAlgo(exportService.getAlgorithm().algorithmName)
                .iterationCount(exportService.getIterationCount())
                .mu(exportService.getMu())
                .temperature(exportService.getTemperature())
                .tailleListTabou(exportService.getTailleTabouList())
                .build();


        // Ici : on lance la simulation
        TabuSearch tabuSearch = new TabuSearch(20);
        //this.currentGraph = tabuSearch.tabuSearch(this.currentGraph, 10000,20);
        RecuitSimule recuitSimule = new RecuitSimule();
        //recuitSimule.recuitSimule(currentGraph, 10000, 0.5f, -300 / Math.log(0.8));
        Graph optimizedGraph;

        if(exportService.getAlgorithm() == Algorithm.RECUIT) {
            optimizedGraph =  recuitSimule.recuitSimule(graph, exportService.getIterationCount(), exportService.getMu(), exportService.getTemperature());
        } else {
            optimizedGraph = tabuSearch.tabuSearch(graph, exportService.getIterationCount(), exportService.getTailleTabouList());
        }

        // Fin de la simulation
        csvData.setEndFitness(optimizedGraph.getFitness());
        csvData.setNbVehicule(optimizedGraph.getVehicules().size());

        exportService.writeLine(csvData, writer);
    }
}
