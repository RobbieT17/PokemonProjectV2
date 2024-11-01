module PokemonProject{
    requires javafx.controls;
    requires javafx.fxml;

    opens project.applications to javafx.fxml;
    exports project.applications;
}
