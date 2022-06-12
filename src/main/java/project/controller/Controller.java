package project.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import project.Algorithms.Algorithm;
import project.Algorithms.Random;
import project.Algorithms.RecuitSimule;
import project.Algorithms.TabuSearch;
import project.Main;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.util.*;

public class Controller implements Initializable {

    private static int GRAPH_GROWTH = 5;
    private final static int POINT_RADIUS = 5;
    private final static List<Color> AVAILABLE_COLORS = Arrays.asList(Color.RED, Color.ORANGE,
            Color.GREEN, Color.BLUE, Color.VIOLET, Color.CORNFLOWERBLUE, Color.CRIMSON, Color.FUCHSIA);

    @FXML
    private AnchorPane graphPane;
    @FXML
    private Label statNbClients;
    @FXML
    private Label statNbVehicles;
    @FXML
    private Label statFitness;
    @FXML
    private Label graphZoneLabel;
    @FXML
    private Label fileLabel;

    @FXML
    private AnchorPane loadingPane;
    @FXML
    private Label loadingPercentage;
    @FXML
    private ProgressBar loadingProgressBar;

    @FXML
    private Label mouseCoordinates;

    private double dragXOffset = 0;
    private double dragYOffset = 0;

    @FXML
    private CheckBox colorCheckbox = new CheckBox();

    @FXML
    private ComboBox algoTypeSelect;

    @FXML
    private CheckBox chkbox2Opt;
    @FXML
    private CheckBox chkboxExchange;
    @FXML
    private CheckBox chkboxRelocate;

    @FXML
    private TextField temp_textfield;
    @FXML
    private TextField mu_textfield;
    @FXML
    private TextField tablist_textfield;

    private Graph currentGraph;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.colorCheckbox.setSelected(true);
        this.algoTypeSelect.getItems().addAll(Algorithm.values());
        this.algoTypeSelect.getSelectionModel().selectFirst();
    }

    @FXML
    public void filePickerClicked() {
        String path = System.getProperty("user.dir");
        String sep = FileSystems.getDefault().getSeparator();

       // URL fxmlFile = new File("src" + sep + "main" + sep + "resources" + sep + "form" + sep + "cvrp.fxml").toURI().toURL();

        FileChooser fileChooser = new FileChooser();

        // Extensions
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);


        // Default directory
        String defaultDirectoryString = new File("").getAbsolutePath() + sep +"ressources";
        File defaultDirectory = new File(defaultDirectoryString);
        if (!defaultDirectory.canRead()) {
            defaultDirectory = new File(path);
        }
        fileChooser.setInitialDirectory(defaultDirectory);

        // File chooser
        try {
            fileChooser.setTitle("Choisissez un fichier");
            File file = fileChooser.showOpenDialog(Main.getStage());
            if (file != null) {
                fileLabel.setText(file.getName());
                fileLabel.setTextFill(Color.BLACK);
                this.loadGraph(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGraph(File file) throws IOException {


        this.currentGraph = Graph.load(file);
        showresult();
        //Random.fillVehicle(this.currentGraph, Vehicule.MAX_CAPACITY);
        /*for (Vehicule vehicule : currentGraph.vehicules) {

            System.out.println("NbColis du vehicule : " + vehicule.nbColis);
            for(Node node : vehicule.tournee) {
                System.out.print(currentGraph.nodes.indexOf(node) + " ");
            }
            System.out.print("\n");
            System.out.println("Distance de la tournee : " + vehicule.longueur);
        }*/
        Random.genAleatoire(this.currentGraph);
        this.graphZoneLabel.setVisible(false);
        this.graphPane.getChildren().clear();
        this.updateGraphStats();
        this.drawGraph(this.currentGraph);

    }

    public void showresult() throws IOException {
        double nbColis = 0;

        for (int i = 0; i < this.currentGraph.nodes.size(); i++) {
            nbColis += this.currentGraph.nodes.get(i).getPoids();
            /*System.out.println("index : " + i + "; "
                    + " x : " + this.currentGraph.nodes.get(i).getPos().getX() + "; "
                    + " y : " + this.currentGraph.nodes.get(i).getPos().getY() + "; "
                    + " q : " + this.currentGraph.nodes.get(i).getPoids());*/
        }

        double res = nbColis / Vehicule.MAX_CAPACITY;
        System.out.println("Nb colis : " + nbColis);
        System.out.println("Nb minimum de vehicules : " + Math.ceil(res));
/*
        graph = genAleatoire(graph);
        System.out.println("Nb vehicule : " + graph.vehicules.size());

        for (Vehicule vehicule : graph.vehicules) {
            System.out.println("NbColis du vehicule : " + vehicule.nbColis);
            for (Node node : vehicule.tournee) {
                System.out.print(graph.nodes.indexOf(node) + " ");
            }
            System.out.print("\n");
            System.out.println("Distance de la tournee : " + vehicule.longueur);
        }*/
    }

    public void updateGraphStats() {
        if (currentGraph == null) return;

        this.statNbVehicles.setText(currentGraph.getVehicules().size() + "");
        this.statFitness.setText(new DecimalFormat("#0.00").format(currentGraph.getFitness()));
    }

    @FXML
    public void startSimulation() {
        if (currentGraph == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez d'abord importer un graphe.");
            alert.showAndWait();
            return;
        }

        loadingPane.setVisible(true);
        Object selectedItem = algoTypeSelect.getSelectionModel().getSelectedItem();

        double mu, temp;
        int tabuL;
        if (!Objects.equals(this.mu_textfield.getText(), "")
                && Double.parseDouble(this.mu_textfield.getText()) > 0.0
                && Double.parseDouble(this.mu_textfield.getText()) < 1.0) {
            mu = Double.parseDouble(this.mu_textfield.getText());
        } else {
            mu = 0.9;
        }
        if (!Objects.equals(this.temp_textfield.getText(), "")
                && Double.parseDouble(this.temp_textfield.getText()) > 0.0) {
            temp = Double.parseDouble(this.temp_textfield.getText());
        } else {
            temp = -300 / Math.log(0.8);
        }
        if (!Objects.equals(this.tablist_textfield.getText(), "")
                && Double.parseDouble(this.tablist_textfield.getText()) > 0.0) {
            tabuL = Integer.parseInt(this.tablist_textfield.getText());
        } else {
            tabuL = 20;
        }
        System.out.println(mu);
        System.out.println(temp);
        System.out.println(tabuL);

        if (Algorithm.RECUIT.equals(selectedItem)) {
            // this.algorithme = new Recuit(currentGraph);
            setLoading(0.0);
            RecuitSimule recuitSimule = new RecuitSimule();
            recuitSimule.recuitSimule(currentGraph, 10000, mu, temp);
            drawGraph(this.currentGraph);
            setLoading(100.0);
        } else if (Algorithm.TABOU.equals(selectedItem)) {
            setLoading(0.0);
            TabuSearch tabuSearch = new TabuSearch(20);
            this.currentGraph = tabuSearch.tabuSearch(this.currentGraph, 10000, tabuL);
            drawGraph(this.currentGraph);
            setLoading(100.0);
        }
        for (Vehicule vehicule : currentGraph.vehicules) {

            System.out.println("NbColis du vehicule : " + vehicule.nbColis);
            for (Node node : vehicule.tournee) {
                System.out.print(currentGraph.nodes.indexOf(node) + " ");
            }
            System.out.print("\n");
            System.out.println("Distance de la tournee : " + vehicule.longueurTournee);
        }
    }

    public void drawGraph(Graph graph) {
        this.graphPane.getChildren().clear();
        this.updateGraphStats();

        int colorIndex = 0;

        this.addPoint(graph.getDepot().getPos().getX() * GRAPH_GROWTH,
                graph.getDepot().getPos().getY() * GRAPH_GROWTH, Color.BLACK).setRadius(2);

        for (Vehicule vehicule : graph.getVehicules()) {
            this.setLoading(0.01f);
            ArrayList<Node> listClient = (ArrayList<Node>) vehicule.tournee;
            Color color = AVAILABLE_COLORS.get(colorIndex % AVAILABLE_COLORS.size());
            Node previous = graph.getDepot();

            colorIndex++;
            for (Node current : listClient) {
                this.addPoint(current.getPos().getX() * GRAPH_GROWTH, current.getPos().getY() * GRAPH_GROWTH, color);

                this.addLine(previous.getPos().getX() * GRAPH_GROWTH,
                        previous.getPos().getY() * GRAPH_GROWTH,
                        current.getPos().getX() * GRAPH_GROWTH,
                        current.getPos().getY() * GRAPH_GROWTH,
                        color);
                previous = current;
            }
            Circle newPoint = this.addPoint(previous.getPos().getX() * GRAPH_GROWTH, previous.getPos().getY() * GRAPH_GROWTH, color);
            this.addLine(previous.getPos().getX() * GRAPH_GROWTH,
                    previous.getPos().getY() * GRAPH_GROWTH,
                    graph.getDepot().getPos().getX() * GRAPH_GROWTH,
                    graph.getDepot().getPos().getY() * GRAPH_GROWTH,
                    color);
        }

    }

    @FXML
    public void handleGraphPanePressed(MouseEvent mouseEvent) {
        dragXOffset = mouseEvent.getX();
        dragYOffset = mouseEvent.getY();
    }

    @FXML
    public void handleGraphPaneDragged(MouseEvent mouseEvent) {
        graphPane.setManaged(false);
        graphPane.setTranslateX(mouseEvent.getX() + graphPane.getTranslateX() - dragXOffset);
        graphPane.setTranslateY(mouseEvent.getY() + graphPane.getTranslateY() - dragYOffset);
        mouseEvent.consume();
    }

    @FXML
    public void centerGraph() {
        graphPane.setTranslateX(0);
        graphPane.setTranslateY(0);
        setZoomLevel(1);
    }

    public void setZoomLevel(double value) {
        if (value <= 3 && value >= 0.1) {
            graphPane.setScaleX(value);
            graphPane.setScaleY(value);
        }
    }


    public Circle addPoint(int x, int y, Color color) {
        Circle circle = new Circle();
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setRadius(POINT_RADIUS);
        circle.fillProperty().bind(colorBind(color));

        Tooltip tooltip = new Tooltip();
        tooltip.setText("x : " + x / GRAPH_GROWTH + ", y : " + y / GRAPH_GROWTH);
        Tooltip.install(circle, tooltip);

        graphPane.getChildren().add(circle);
        return circle;
    }

    public Line addLine(int x1, int y1, int x2, int y2, Color color) {
        Line line = new Line();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        line.strokeProperty().bind(colorBind(color));
        graphPane.getChildren().add(line);
        return line;
    }

    public ObjectBinding<Color> colorBind(Color color) {
        return Bindings.createObjectBinding(() ->
                colorCheckbox.isSelected() ? color : Color.BLACK, colorCheckbox.selectedProperty());
    }

    public void setLoading(double value) {
        loadingPercentage.setText(String.format("%.0f%%", value));
        loadingProgressBar.setProgress(value);
    }

    @FXML
    public void updateMouseCoordinates(MouseEvent mouseEvent) {
        mouseCoordinates.setText("x : " + (int) (mouseEvent.getX() / GRAPH_GROWTH) +
                " / y : " + (int) (mouseEvent.getY() / GRAPH_GROWTH));
    }
}
