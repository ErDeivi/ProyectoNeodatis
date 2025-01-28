package com.example.proyectoneodatis.controladores;

import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Articulo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.proyectoneodatis.App.articulos;

public class ListadosControlador{
    @FXML
    private TableView<Articulo> tablaArticulos;

    @FXML
    private TableColumn<Articulo, Integer> colCodigo;

    @FXML
    private TableColumn<Articulo, String> colDenominacion;

    @FXML
    private TableColumn<Articulo, Double> colPrecio;

    @FXML
    private TableColumn<Articulo, String> colCategoria;


    @FXML
    private TableColumn<Articulo, Integer> colUnidadesVendidas;

    @FXML
    private TableColumn<Articulo, Integer> colStock;

    @FXML
    private ComboBox<String> comboBox;

    public ObservableList<Articulo> listaArticulos;
    public static Articulo articuloSeleccionado;

    @FXML
    public void initialize() {
        if (listaArticulos == null) {
            listaArticulos = FXCollections.observableArrayList();
        }
        // Configurar ComboBox
        comboBox.getItems().addAll(
                "Artículos con stock bajo (menos de 10 unidades)",
                "Artículos por categoría",
                "Artículos más vendidos",
                "Artículos con precio superior a 100",
                "Artículos con más de 20 unidades vendidas",
                "Tabla Completa"
        );

        // Configurar las columnas de la tabla
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDenominacion.setCellValueFactory(new PropertyValueFactory<>("denominacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioDeVentaAlPublico"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colUnidadesVendidas.setCellValueFactory(new PropertyValueFactory<>("unidadesVendidas"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Crear lista observable
        listaArticulos = FXCollections.observableArrayList();

        // Cargar los artículos desde la base de datos
        cargarArticulosDesdeDB();
    }

    public void atras(ActionEvent actionEvent) throws IOException {
        App.setRoot("menuPrincipal");
    }
    private void cargarArticulosDesdeDB() {
        listaArticulos.clear(); // Limpiar la lista antes de cargar nuevos datos
        ODB odb = ODBFactory.open("neodatis.test");

        // Si la base de datos está vacía, guarda los artículos de App
        if (odb.getObjects(Articulo.class).isEmpty()) {
            for (Articulo articulo : App.articulos) {
                odb.store(articulo);
            }
        }

        var objetos = odb.getObjects(Articulo.class);
        while (objetos.hasNext()) {
            listaArticulos.add((Articulo) objetos.next());
        }

        odb.close();

        // Actualizar la tabla con los datos obtenidos
        tablaArticulos.setItems(listaArticulos);
        tablaArticulos.refresh(); // Refrescar la tabla para ver los cambios
    }


    public ObservableList<Articulo> getListaArticulos() {
        return listaArticulos;
    }

    public void actualizarTabla() {
        if (listaArticulos == null) {
            listaArticulos = FXCollections.observableArrayList(); // Inicializar si es nulo
        }
        listaArticulos.setAll(App.articulos); // Actualizar con la lista más reciente
        if (tablaArticulos != null) {
            tablaArticulos.refresh(); // Refrescar la tabla solo si está inicializada
        }
    }


    public void buscarOnAction(ActionEvent actionEvent) {

    }

    @FXML
    private void ejecutarConsultaOnAction(ActionEvent event) {
        String consultaSeleccionada = comboBox.getValue();

        if (consultaSeleccionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Consulta no seleccionada");
            alert.setContentText("Por favor, selecciona una consulta del menú desplegable.");
            alert.showAndWait();
            return;
        }

        switch (consultaSeleccionada) {
            case "Artículos con stock bajo (menos de 10 unidades)":
                consultarStockBajo();
                break;
            case "Artículos por categoría":
                consultarPorCategoria();
                break;
            case "Artículos más vendidos":
                consultarMasVendidos();
                break;
            case "Artículos con precio superior a 100":
                consultarPrecioAlto();
                break;
            case "Artículos con más de 20 unidades vendidas":
                consultarUnidadesVendidas();
                break;
            case "Tabla Completa":
                mostrarResultados(getListaArticulos());
                break;
            default:
                System.out.println("Consulta no reconocida.");
        }
    }

    private void consultarStockBajo() {
        ObservableList<Articulo> resultados = FXCollections.observableArrayList();
        for (Articulo articulo : listaArticulos) {
            if (articulo.getStock() < 10) {
                resultados.add(articulo);
            }
        }
        mostrarResultados(resultados);
    }
    private void mostrarResultados(ObservableList<Articulo> resultados) {
        if (resultados.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultados");
            alert.setHeaderText("Consulta sin resultados");
            alert.setContentText("No se encontraron artículos que cumplan con los criterios.");
            alert.showAndWait();
        } else {
            tablaArticulos.setItems(resultados);
            tablaArticulos.refresh();
        }
    }
    private String mostrarInputDialog(String mensaje) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Consulta personalizada");
        dialog.setHeaderText(null);
        dialog.setContentText(mensaje);

        var resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }


    private void consultarPorCategoria() {
        ObservableList<Articulo> resultados = FXCollections.observableArrayList();
        String categoria = mostrarInputDialog("Introduce la categoría a buscar:");
        if (categoria != null) {
            for (Articulo articulo : listaArticulos) {
                if (articulo.getCategoria().equalsIgnoreCase(categoria)) {
                    resultados.add(articulo);
                }
            }
            mostrarResultados(resultados);
        }
    }

    private void consultarMasVendidos() {
        ObservableList<Articulo> resultados = FXCollections.observableArrayList();
        int maxUnidadesVendidas = listaArticulos.stream()
                .mapToInt(Articulo::getUnidadesVendidas)
                .max()
                .orElse(0);

        for (Articulo articulo : listaArticulos) {
            if (articulo.getUnidadesVendidas() == maxUnidadesVendidas) {
                resultados.add(articulo);
            }
        }
        mostrarResultados(resultados);
    }

    private void consultarPrecioAlto() {
        ObservableList<Articulo> resultados = FXCollections.observableArrayList();
        for (Articulo articulo : listaArticulos) {
            if (articulo.getPrecioDeVentaAlPublico() > 100) {
                resultados.add(articulo);
            }
        }
        mostrarResultados(resultados);
    }

    private void consultarUnidadesVendidas() {
        ObservableList<Articulo> resultados = FXCollections.observableArrayList();
        for (Articulo articulo : listaArticulos) {
            if (articulo.getUnidadesVendidas() > 20) {
                resultados.add(articulo);
            }
        }
        mostrarResultados(resultados);
    }

    public void nuevoArticuloOnAction(ActionEvent actionEvent) {
        try {
            // Cargar el FXML para la vista de crear artículo
            FXMLLoader loader = new FXMLLoader(App.class.getResource("crearArticulo.fxml")); // Asegúrate de incluir .fxml
            Parent root = loader.load();

            // Obtener el controlador de la nueva vista
            CrearArticuloControlador crearArticuloControlador = loader.getController();

            // Pasar la lista de artículos al controlador de crear artículo
            crearArticuloControlador.setListaArticulos(App.articulos); // Pasar la lista de artículos

            // Cambiar la vista
            App.setRoot("crearArticulo"); // Cambiar a la vista de crear artículo
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de errores
        }
    }

    public void borrarArticuloOnAction(ActionEvent actionEvent) {
        // Obtener el artículo seleccionado
        Articulo articuloSeleccionado = tablaArticulos.getSelectionModel().getSelectedItem();

        if (articuloSeleccionado != null) {
            // Confirmar eliminación (opcional, puedes usar un Alert para confirmación)
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("Eliminar artículo");
            alert.setContentText("¿Estás seguro de que deseas eliminar el artículo seleccionado?");

            // Esperar respuesta del usuario
            var respuesta = alert.showAndWait();
            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                // Eliminar el artículo de la lista
                listaArticulos.remove(articuloSeleccionado);

                ODB odb = ODBFactory.open("neodatis.test");
                var objetos = odb.getObjects(Articulo.class);

                boolean articuloEncontrado = false;

                while (objetos.hasNext()) {
                    Articulo articulo = (Articulo) objetos.next();

                    // Imprimir para depuración
                    System.out.println("Comparando: " + articulo.getCodigo() + " con " + articuloSeleccionado.getCodigo());

                    if (articulo.getCodigo().equals(articuloSeleccionado.getCodigo())) {
                        odb.delete(articulo); // Borrar el objeto exacto recuperado de la base de datos
                        articuloEncontrado = true;
                        System.out.println("Artículo eliminado de la base de datos: " + articulo);
                        break;
                    }
                }

                if (!articuloEncontrado) {
                    System.out.println("El artículo no fue encontrado en la base de datos.");
                }

                odb.close();

                // Refrescar la tabla
                tablaArticulos.refresh();
                System.out.println("Artículo eliminado con éxito: " + articuloSeleccionado);
            }
        } else {
            // Mostrar un mensaje de alerta si no hay selección
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ningún artículo seleccionado");
            alert.setContentText("Por favor, selecciona un artículo para eliminar.");
            alert.showAndWait();
        }
    }
    public void modificarOnAction(ActionEvent actionEvent) {
        // Obtener el artículo seleccionado
        articuloSeleccionado = tablaArticulos.getSelectionModel().getSelectedItem();

        if (articuloSeleccionado != null) {
                App.setRoot("camposArticulos");
        } else {
            // Mostrar alerta si no se seleccionó ningún artículo
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ningún artículo seleccionado");
            alert.setContentText("Por favor, selecciona un artículo para modificar.");
            alert.showAndWait();
        }
    }


    public void exportarOnAction(ActionEvent actionEvent) {

    }

    public void filtrosOnAction(ActionEvent actionEvent) {

    }
}