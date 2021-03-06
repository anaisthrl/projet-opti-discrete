package project.controller;

import javafx.application.Platform;
import project.Algorithms.*;
import project.Main;
import project.Model.Graph;
import project.Model.Node;
import project.Model.Vehicule;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static int GRAPH_GROWTH = 5;
    private final static int POINT_RADIUS = 5;
    private final static int ARROW_HEAD_SIZE = 10;
    private final static List<Color> AVAILABLE_COLORS = Arrays.asList(Color.RED, Color.ORANGE,
            Color.GREEN, Color.BLUE, Color.VIOLET, Color.CORNFLOWERBLUE,Color.CRIMSON,Color.FUCHSIA);

    @FXML
    private AnchorPane graphPane;
    @FXML private Label statNbClients;
    @FXML private Label statNbVehicles;
    @FXML private Label statFitness;
    @FXML private Label graphZoneLabel;
    @FXML private Label fileLabel;

    @FXML private AnchorPane loadingPane;
    @FXML private Label loadingPercentage;
    @FXML private ProgressBar loadingProgressBar;

    @FXML private Label mouseCoordinates;

    // For AnchorPane dragging
    private double dragXOffset = 0;
    private double dragYOffset = 0;

    @FXML private CheckBox arrowCheckbox;
    @FXML private CheckBox colorCheckbox;

    @FXML private ComboBox algoTypeSelect;

    @FXML private CheckBox chkbox2Opt;
    @FXML private CheckBox chkboxExchange;
    @FXML private CheckBox chkboxRelocate;

    private Graph currentGraph;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.algoTypeSelect.getItems().addAll(Algorithm.values());
        this.algoTypeSelect.getSelectionModel().selectFirst();
    }

    @FXML
    public void filePickerClicked() {
        FileChooser fileChooser = new FileChooser();

        // Extensions
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        // Default directory
        String defaultDirectoryString = new File("").getAbsolutePath() + "\\ressources";
        File defaultDirectory = new File(defaultDirectoryString);
        if (!defaultDirectory.canRead()) {
            defaultDirectory = new File("c:/");
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
        this.currentGraph = Main.load(file);
        for (int i = 0; i < this.currentGraph.nodes.size(); i++) {
            System.out.println("index : " + i + "; "
                    + " x : " + this.currentGraph.nodes.get(i).getPos().getX() + "; "
                    + " y : " + this.currentGraph.nodes.get(i).getPos().getY() + "; "
                    + " q : " + this.currentGraph.nodes.get(i).getPoids());
        }
        Random.fillVehicle(this.currentGraph, Vehicule.MAX_CAPACITY);
        for (Vehicule vehicule : currentGraph.vehicules) {
            //tableau index AT
            ArrayList<Integer> arIndex = new ArrayList<>();

            System.out.println("NbColis du vehicule : " + vehicule.nbColis);
            for(Node node : vehicule.tournee) {
                System.out.print(currentGraph.nodes.indexOf(node) + " ");
                //AT
                arIndex.add(currentGraph.nodes.indexOf(node));
            }
            System.out.print("\n");
            System.out.println("Distance de la tournee : " + vehicule.longueur);
        }
        //Random.genAleatoire(this.currentGraph);
        this.graphZoneLabel.setVisible(false);
        this.graphPane.getChildren().clear();
        this.updateGraphStats();
        this.drawGraph(this.currentGraph);
    }

    public void updateGraphStats() {
        if(currentGraph == null) return;

        this.statNbVehicles.setText(currentGraph.getVehicules().size() + "");
        this.statFitness.setText(new DecimalFormat("#0.00").format(currentGraph.getFitness()));
    }

    @FXML
    public void resetGraph() {
        centerGraph();
        this.currentGraph = null;
        this.fileLabel.setText("Aucun fichier s??lectionn??");
        this.fileLabel.setTextFill(Color.DARKGRAY);
        this.statNbClients.setText("0");
        this.statNbVehicles.setText("0");
        this.statFitness.setText("n/a");
        graphPane.getChildren().clear();
    }

    @FXML
    public void startSimulation() {
        if(currentGraph == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez d'abord importer un graphe.");
            alert.showAndWait();
            return;
        }

        loadingPane.setVisible(true);

        Object selectedItem = algoTypeSelect.getSelectionModel().getSelectedItem();


        if (Algorithm.RECUIT.equals(selectedItem)) {
           // this.algorithme = new Recuit(currentGraph);
            setLoading(0.0);
            RecuitSimule recuitSimule = new RecuitSimule();
            recuitSimule.recuitSimule(currentGraph, 10000, 0.5, -300 / Math.log(0.8));
            drawGraph(this.currentGraph);
            setLoading(100.0);
        }
        else if(Algorithm.TABOU.equals(selectedItem)){
            setLoading(0.0);
            TabuSearch tabuSearch = new TabuSearch(20);
            this.currentGraph = tabuSearch.tabuSearch(this.currentGraph, 10000,20);
            drawGraph(this.currentGraph);
            setLoading(100.0);
        }
        for (Vehicule vehicule : currentGraph.vehicules) {

            System.out.println("NbColis du vehicule : " + vehicule.nbColis);
            for(Node node : vehicule.tournee) {
                System.out.print(currentGraph.nodes.indexOf(node) + " ");
            }
            System.out.print("\n");
            System.out.println("Distance de la tournee : " + vehicule.longueur);
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
    public void testClicked() {
        this.addLine(80, 80, 50, 50, Color.ORANGERED);
        this.addPoint(50, 50, Color.BLUEVIOLET);
        this.addPoint(80, 80, Color.BLUEVIOLET);

        // Test other stuff here...
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
        circle.fillProperty().bind(getColorBinding(color));


        Tooltip tooltip = new Tooltip();
        tooltip.setText("x : " + x/GRAPH_GROWTH + ", y : " + y/GRAPH_GROWTH);
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
        line.strokeProperty().bind(getColorBinding(color));

        StackPane arrow = new StackPane();
        String strColor = color.toString().substring(2, 8);
        arrow.styleProperty().bind(Bindings.createStringBinding(() ->
                "-fx-border-width:1px;-fx-shape: \"M0,-4L4,0L0,4Z\";" +
                        String.format("-fx-background-color:#%s;-fx-border-color:#%s",
                                colorCheckbox.isSelected() ? strColor : "000000",
                                colorCheckbox.isSelected() ? strColor : "000000"), colorCheckbox.selectedProperty()
        ));

        arrow.setLayoutX(x2 - ARROW_HEAD_SIZE);
        arrow.setLayoutY(y2 - ARROW_HEAD_SIZE / 2.0);
        arrow.setPrefSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.setMaxSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.setMinSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.visibleProperty().bind(arrowCheckbox.selectedProperty());

        double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
        double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
        double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
        double dt = lineLength - (POINT_RADIUS / 2.0) - (ARROW_HEAD_SIZE / 2.0);

        double t = dt / lineLength;
        double tX = ((1 - t) * line.getStartX()) + (t * line.getEndX());
        double tY = ((1 - t) * line.getStartY()) + (t * line.getEndY());

        arrow.setLayoutX(tX - ARROW_HEAD_SIZE / 2.0);
        arrow.setLayoutY(tY - ARROW_HEAD_SIZE / 2.0);

        double angle = Math.toDegrees(Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()));
        if (angle < 0) {
            angle += 360;
        }
        arrow.setRotate(angle);

        graphPane.getChildren().add(arrow);
        graphPane.getChildren().add(line);
        return line;
    }

    public ObjectBinding<Color> getColorBinding(Color color) {
        return Bindings.createObjectBinding(() ->
                colorCheckbox.isSelected() ? color : Color.BLACK, colorCheckbox.selectedProperty());
    }

    public void setLoading(double value) {
        loadingPercentage.setText(String.format("%.0f%%", value));
        loadingProgressBar.setProgress(value);
    }

    @FXML
    public void updateMouseCoordinates(MouseEvent mouseEvent) {
        mouseCoordinates.setText("x : " + (int)(mouseEvent.getX()/GRAPH_GROWTH) +
                " / y : " + (int)(mouseEvent.getY()/GRAPH_GROWTH));
    }
}
