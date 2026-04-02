module PokemonProject{
    requires javafx.controls;
    requires javafx.fxml;

    opens project.ui.applications to javafx.fxml;
    opens project.ui.controllers to javafx.fxml;

    exports project.ui.applications;
    exports project.ui.controllers;
}
