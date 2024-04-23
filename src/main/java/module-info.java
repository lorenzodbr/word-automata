module it.univr.wordautomata {
    requires javafx.controls;
    requires javafx.fxml;

    opens it.univr.wordautomata to javafx.fxml;
    exports it.univr.wordautomata;
}
