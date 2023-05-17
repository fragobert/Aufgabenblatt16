module aufgabe16.aufgabenblatt16 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens aufgabe16.aufgabenblatt16 to javafx.fxml;
    exports aufgabe16.aufgabenblatt16;
}