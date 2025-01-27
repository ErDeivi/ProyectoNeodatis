package com.example.proyectoneodatis.controladores;

import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Articulo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    public ObservableList<Articulo> listaArticulos;

    @FXML
    public void initialize() {
            // Configurar las columnas
            colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDenominacion.setCellValueFactory(new PropertyValueFactory<>("denominacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioDeVentaAlPublico"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colUnidadesVendidas.setCellValueFactory(new PropertyValueFactory<>("unidadesVendidas"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        // Agregar los artículos

        listaArticulos = FXCollections.observableArrayList();
        ODB odb = ODBFactory.open("neonatis.test");
        for (Articulo articulo : articulos) {
            odb.store(articulo);
            System.out.println(articulo);
        }
        var objetos = odb.getObjects(Articulo.class);
        while (objetos.hasNext()) {
            listaArticulos.add((Articulo) objetos.next());
        }
        odb.close();
        tablaArticulos.setItems(listaArticulos);
        System.out.println(listaArticulos.toString());
    }

    public void atras(ActionEvent actionEvent) throws IOException {
        App.setRoot("menuPrincipal");
    }

    public ObservableList<Articulo> getListaArticulos() {
        return listaArticulos;
    }

    public void actualizarTabla() {
        tablaArticulos.refresh();
    }

    public void buscarOnAction(ActionEvent actionEvent) {

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

    }

    public void modificarOnAction(ActionEvent actionEvent) {


    }

    public void exportarOnAction(ActionEvent actionEvent) {

    }

    public void filtrosOnAction(ActionEvent actionEvent) {

    }
}