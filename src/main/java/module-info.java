module cvrp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires java.desktop;

    opens project.controller to javafx.fxml;
    exports project;
}