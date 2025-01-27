module com.example.proyectoneodatis {
    requires javafx.controls;
    requires javafx.fxml;
    requires neodatis.odb;
    requires javafx.base;
	requires com.google.gson;
	requires javafx.graphics;
    requires java.desktop;


    opens com.example.proyectoneodatis.controladores to javafx.fxml;
    opens com.example.proyectoneodatis.Modelo to neodatis.odb,javafx.base;
    exports com.example.proyectoneodatis;
}