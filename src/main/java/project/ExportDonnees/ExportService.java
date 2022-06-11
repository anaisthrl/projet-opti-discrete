package project.ExportDonnees;

import project.Algorithms.Algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.opencsv.CSVWriter;

public class ExportService {
    private String pathFile;
    private Algorithm algorithm;
    private int iterationCount;
    private double mu;
    private double temperature;
    private int tailleTabouList;
    private double executionTime;
    private String transfoElem;

    private final static String[] COLUMNS_HEADERS = new String[]{
            "Fichier", "Clients", "Algorithme applique", "Transformations Elementaires", "Fitness initiale", "Fitness finale",
            "Vehicules" , "Iterations" , "Temps d'execution"
    };
    private final static String[] COLUMNS_SIMULATED_ANNEALING = new String[]{"Variation (mu)", "Temperature"};
    private final static String[] COLUMNS_TABU = new String[]{"Taille liste tabou"};


    public ExportService(Algorithm algorithm, int iterationCount) {
        this.algorithm = algorithm;
        this.iterationCount = iterationCount;
        this.pathFile = new File("").getAbsolutePath() + "\\exports\\";
    }

    public ExportService(Algorithm algorithm, int iterationCount, double mu, double temperature, String transfoElem) {
        this(algorithm, iterationCount);
        this.mu = (double) Math.round(mu * 100) / 100;;
        this.temperature = temperature;
        this.transfoElem = transfoElem;
    }

    public ExportService(Algorithm algorithm, int iterationCount, int tailleTabouList, String transfoElem) {
        this(algorithm, iterationCount);
        this.tailleTabouList = tailleTabouList;
        this.transfoElem = transfoElem;
    }


    public CSVWriter createCsv() {

        CSVWriter writer = null;
        try {
            String strVariation = mu == 0f ? "" : String.valueOf(mu);
            String strTemperature = temperature == 0f ? "" : String.valueOf(temperature);
            String strTabuListSize = tailleTabouList == 0 ? "" : String.valueOf(tailleTabouList);
            String fileName = this.pathFile + String.format("%s_%d_%s_%s_%s.csv", algorithm, iterationCount, strVariation, strTemperature, strTabuListSize);

            if(new File(fileName).exists()) {
                throw new RuntimeException("Le fichier " + fileName + " existe deja. Creation annulee.");
            }

            writer = new CSVWriter(new FileWriter(fileName),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.RFC4180_LINE_END);

            // Colonnes de base + recuit OU colonnes de base + tabou
            String[] allColumns = (algorithm == Algorithm.RECUIT)
                    ? Stream.of(COLUMNS_HEADERS,COLUMNS_SIMULATED_ANNEALING).flatMap(Stream::of).toArray(String[]::new)
                    : Stream.of(COLUMNS_HEADERS,COLUMNS_TABU).flatMap(Stream::of).toArray(String[]::new);
            writer.writeNext(allColumns);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fichier en cours d'utilisation.");
        }
        return writer;
    }
    public void writeLine(ExportData exportData, CSVWriter writer) {
        try {
            if(writer == null) return;
            writer.writeNext(exportData.getRowForAlgorithm(this.algorithm));
            writer.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String writeCsv(List<String[]> stringArray) {
        String fileName = "";
        try {
            fileName = getDateTime() + ".csv";
            CSVWriter writer = new CSVWriter(new FileWriter(this.pathFile + fileName),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.RFC4180_LINE_END);
            writer.writeAll(Collections.singleton(COLUMNS_HEADERS));
            writer.writeAll(stringArray);
            writer.flush();
            writer.close();
        } catch(IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }

    public String getPathFile() {
        return pathFile;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public double getMu() {
        return mu;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getTailleTabouList() {
        return tailleTabouList;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public String getTransfoElem() {
        return transfoElem;
    }

    public void setTransfoElem(String transfoElem) {
        this.transfoElem = transfoElem;
    }
}
