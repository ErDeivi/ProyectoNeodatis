package com.example.proyectoneodatis;

import com.example.proyectoneodatis.Modelo.Articulo;
import com.example.proyectoneodatis.Modelo.Constantes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public static List<Articulo> articulos = new ArrayList<>();
    private static Stage primaryStage; // Almacenar el escenario principal
    public static File contrasenaInicioFile = new File("contrasenaFile");

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Inicializar el escenario principal

        // Agregar artículos de ejemplo
        articulos.add(new Articulo(1, "Portatil Acer", 500.0, "Informática", 10, 20));
        articulos.add(new Articulo(2, "Pala Pádel", 100.0, "Deportes", 5, 30));
        articulos.add(new Articulo(3, "Caja Lápices", 6.0, "Escritorio", 10, 6));
        articulos.add(new Articulo(4, "Marcadores", 10.0, "Escritorio", 20, 19));
        articulos.add(new Articulo(5, "Memoria 32GB", 120.0, "Informática", 8, 10));
        articulos.add(new Articulo(6, "Micro Intel", 150.0, "Informática", 4, 10));
        articulos.add(new Articulo(7, "Bolas Pádel", 5.0, "Deportes", 15, 30));
        articulos.add(new Articulo(8, "Falda Pádel", 15.0, "Deportes", 10, 10));

        // Leer la contraseña desde el archivo
        if (contrasenaInicioFile.exists() && contrasenaInicioFile.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(contrasenaInicioFile))) {
                String contenido = reader.readLine();
                if (contenido != null && !contenido.trim().isEmpty()) {
                    Constantes.CONTRASENA = contenido.trim();
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de contraseña: " + e.getMessage());
            }
        }

        // Cargar la primera escena
        setRoot("listados"); // Cambiar a la vista de listados
        stage.setTitle("App");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml); // Cargar el nuevo FXML
            Scene scene = new Scene(root);
            primaryStage.setScene(scene); // Cambiar la escena del escenario principal
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}