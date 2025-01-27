package com.example.proyectoneodatis.controladores;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;

import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Articulo;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import static com.example.proyectoneodatis.App.articulos;

public class MenuPrincipalControlador {

    @FXML
    public void initialize(){


    }

    @FXML
    public void gestionarArticulosOnAction(ActionEvent actionEvent) throws IOException {
        App.setRoot("listados");
    }
    @FXML
    public void exportarArticulosOnAction(ActionEvent actionEvent) {
        if (articulos.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("No hay artículos para exportar");
            alert.setContentText("La lista de artículos está vacía. No se puede realizar la exportación.");
            alert.showAndWait();
            return;
        }

        // Generar el nombre del archivo dinámicamente con la fecha y hora actuales
        String fechaHoraActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "articulos_exportados_" + fechaHoraActual + ".csv";

        // Crear el FileChooser para que el usuario elija dónde guardar el archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName(nombreArchivo);
        File archivoSeleccionado = fileChooser.showSaveDialog(null);

        if (archivoSeleccionado != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSeleccionado))) {
                // Escribir la cabecera del archivo CSV
                writer.write("Codigo,Denominacion,PVP,Categoria,UV,Stock");
                writer.newLine();

                // Escribir los datos de los artículos
                for (Articulo articulo : articulos) {

                    // Generar la línea en formato CSV
                    String lineaCsv = String.format(
                            "%d,\"%s\",%s,\"%s\",%s,%s",
                            articulo.getCodigo(),
                            articulo.getDenominacion(),
                            articulo.getPrecioDeVentaAlPublico(),
                            articulo.getCategoria(),
                            articulo.getUnidadesVendidas(),
                            articulo.getStock()
                    );
                    writer.write(lineaCsv);
                    writer.newLine();
                }

                // Mostrar alerta de éxito
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Exportación Exitosa");
                alert.setContentText("Los artículos se han exportado correctamente a: " + archivoSeleccionado.getAbsolutePath());
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error de Exportación");
                alert.setContentText("Hubo un error al exportar los artículos: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }


    @FXML
    public void cerrarSesionOnAction(ActionEvent actionEvent) {
        App.setRoot("inicio");
    }
    @FXML
    public void cambiarContrasenaOnAction(ActionEvent actionEvent) throws IOException {
        App.setRoot("cambiarContrasena");
    }

    @FXML
    public void importarArticulosOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Seleccionar Archivo CSV");

        File archivoSeleccionado = fileChooser.showOpenDialog(null);

        if (archivoSeleccionado != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivoSeleccionado))) {
                List<Articulo> articulosImportados = new ArrayList<>();

                // Leer y descartar la cabecera
                String linea = reader.readLine();

                // Leer cada línea restante del archivo
                while ((linea = reader.readLine()) != null) {
                    String[] campos = linea.split(","); // Dividir por comas

                    // Validar que haya exactamente 6 campos
                    if (campos.length == 6) {
                        int codigo = Integer.parseInt(campos[0].trim());
                        String denominacion = campos[1].trim();
                        double pvp = Double.parseDouble(campos[2].trim());
                        String categoria = campos[3].trim();
                        int unidadesVendidas = Integer.parseInt(campos[4].trim());
                        int stock = Integer.parseInt(campos[5].trim());

                        // Crear y añadir el artículo
                        Articulo articulo = new Articulo(codigo, denominacion, pvp, categoria, unidadesVendidas, stock);
                        articulosImportados.add(articulo);
                    }
                }

                // Guardar los artículos en la base de datos Neodatis y en la lista en memoria
                ODB odb = ODBFactory.open("neodatis.test");
                for (Articulo articulo : articulosImportados) {
                    odb.store(articulo);
                    articulos.add(articulo);
                }
                odb.close();

                // Mostrar mensaje de éxito
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Importación Exitosa");
                alert.setContentText("Se han importado " + articulosImportados.size() + " artículos correctamente.");
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error de Importación");
                alert.setContentText("Hubo un error al importar los artículos: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }


}
