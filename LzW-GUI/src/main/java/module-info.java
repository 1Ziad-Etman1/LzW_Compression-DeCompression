module com.example.lzwgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lzwgui to javafx.fxml;
    exports com.example.lzwgui;
}