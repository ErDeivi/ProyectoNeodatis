package com.example.proyectoneodatis.controladores;


import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Articulo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.util.List;

public class CrearArticuloControlador {
    @FXML
    private TextField codigo,denominacion,categoria,pvp,uv,stock;

    @FXML
    private Button crearArticulo,salir;

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
            // Validar que el campo de código es un número entero
            int codigoValue;
            try {
                codigoValue = Integer.parseInt(codigo.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "El campo 'Código' debe ser un número entero.");
                return;
            }

            // Validar que denominación no esté vacío
            String denominacionValue = denominacion.getText();
            if (denominacionValue.isEmpty()) {
                showAlert("Error", "El campo 'Denominación' no puede estar vacío.");
                return;
            }

            String categoriaValue = categoria.getText();
            if (categoriaValue.isEmpty()) {
                showAlert("Error", "El campo 'Categoría' no puede estar vacío.");
                return;
            }

            // Validar que el campo PVP es un número decimal
            double pvpValue;
            try {
                pvpValue = Double.parseDouble(pvp.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "El campo 'PVP' debe ser un número decimal.");
                return;
            }

            // Validar que el campo UV es un número entero
            int uvValue;
            try {
                uvValue = Integer.parseInt(uv.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "El campo 'Unidades Vendidas' debe ser un número entero.");
                return;
            }

            // Validar que el campo Stock es un número entero
            int stockValue;
            try {
                stockValue = Integer.parseInt(stock.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "El campo 'Stock' debe ser un número entero.");
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

            JOptionPane.showMessageDialog(null, "Artículo creado con éxito: " + nuevoArticulo, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            App.articulos = listaArticulos;

        } catch (Exception e) {
            showAlert("Error", "Error al crear el artículo: " + e.getMessage());
        }
    }

    // Método para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void salirOnAction(ActionEvent event){
        App.setRoot("listados");
    }
}