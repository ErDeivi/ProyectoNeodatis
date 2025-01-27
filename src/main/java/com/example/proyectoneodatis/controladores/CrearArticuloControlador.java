package com.example.proyectoneodatis.controladores;


import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Articulo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import java.util.List;

public class CrearArticuloControlador {
    @FXML
    private TextField codigo;

    @FXML
    private TextField denominacion;

    @FXML
    private TextField categoria;

    @FXML
    private TextField pvp;

    @FXML
    private TextField uv;

    @FXML
    private TextField stock;

    @FXML
    private Button crearArticulo;

    @FXML
    private Button salir;
    private List<Articulo> listaArticulos; // Lista de artículos a la que se añadirá el nuevo artículo

    public void setListaArticulos(List<Articulo> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public void initialize(){
        listaArticulos=App.articulos;
    }

    @FXML
    void crearArticuloOnAction(ActionEvent event) {
        try {
            // Obtener valores de los campos
            int codigoValue = Integer.parseInt(codigo.getText());
            String denominacionValue = denominacion.getText();
            String categoriaValue = categoria.getText();
            double pvpValue = Double.parseDouble(pvp.getText());
            int uvValue = Integer.parseInt(uv.getText());
            int stockValue = Integer.parseInt(stock.getText());

            // Validar que los campos no estén vacíos
            if (denominacionValue.isEmpty() || categoriaValue.isEmpty()) {
                System.out.println("Por favor completa todos los campos.");
                return;
            }

            // Crear un nuevo artículo
            Articulo nuevoArticulo = new Articulo(codigoValue, denominacionValue, pvpValue, categoriaValue, uvValue, stockValue);

            // Agregarlo a la lista de artículos
            listaArticulos.add(nuevoArticulo);

            // Limpiar los campos del formulario
            codigo.clear();
            denominacion.clear();
            categoria.clear();
            pvp.clear();
            uv.clear();
            stock.clear();

            System.out.println("Artículo creado con éxito: " + nuevoArticulo);

        } catch (Exception e) {
            System.out.println("Error al crear el artículo: " + e.getMessage());
        }
    }
    @FXML
    void salirOnAction(ActionEvent event){

    }
}