module PokemonProject{
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;

    opens project.ui.applications to javafx.fxml;
    opens project.ui.controllers to javafx.fxml;
    opens project.data to org.yaml.snakeyaml;

    exports project.ui.applications;
    exports project.ui.controllers;
}
