package com.example.proyectoneodatis.controladores;

import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Articulo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.proyectoneodatis.controladores.ListadosControlador.articuloSeleccionado;

public class CamposArticulosControlador {
    public TextField codigo,denominacion,categoria,uv,pvp,stock;
    public Button editar,salir;
    private Articulo ac;
    private List<Articulo> listaArticulos;

    public void initialize(){
        listaArticulos=App.articulos;
        // Configurar los campos del formulario con los datos del artículo seleccionado
        if (articuloSeleccionado != null) {
            codigo.setText(String.valueOf(articuloSeleccionado.getCodigo()));
            denominacion.setText(String.valueOf(articuloSeleccionado.getDenominacion()));
            categoria.setText(String.valueOf(articuloSeleccionado.getCategoria()));
            uv.setText(String.valueOf(articuloSeleccionado.getUnidadesVendidas()));
            pvp.setText(String.valueOf(articuloSeleccionado.getPrecioDeVentaAlPublico()));
            stock.setText(String.valueOf(articuloSeleccionado.getStock()));
            ac=new Articulo(Integer.parseInt(codigo.getText()), denominacion.getText(),Double.parseDouble(pvp.getText()),categoria.getText(),Integer.parseInt(uv.getText()),Integer.parseInt(stock.getText()));
        }
    }


    @FXML
    public void editarOnAction(ActionEvent actionEvent) {
        try {
            ODB odb = ODBFactory.open("neodatis.test");

            // Validar y asignar un código
            int codigoValue;
            try {
                codigoValue = Integer.parseInt(codigo.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "El campo 'Código' debe ser un número entero.");
                return;
            }

            // Verificar si el código ya existe y asignar el siguiente libre si es necesario
            var objetos = odb.getObjects(Articulo.class);
            List<Integer> codigosExistentes = new ArrayList<>();

            while (objetos.hasNext()) {
                Articulo articulo = (Articulo) objetos.next();
                codigosExistentes.add(articulo.getCodigo());
            }

            if (codigosExistentes.contains(codigoValue) && codigoValue != ac.getCodigo()) {
                // Encontrar el siguiente código libre
                while (codigosExistentes.contains(codigoValue)) {
                    codigoValue++;
                }
                JOptionPane.showMessageDialog(null, "El código ya existe. Se asignó el siguiente código libre: " + codigoValue, "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            // Validar que denominación no esté vacío
            String denominacionValue = denominacion.getText();
            if (denominacionValue.isEmpty()) {
                odb.close();
                showAlert("Error", "El campo 'Denominación' no puede estar vacío.");
                return;
            }

            // Validar que el campo PVP es un número decimal
            double pvpValue;
            try {
                pvpValue = Double.parseDouble(pvp.getText());
            } catch (NumberFormatException e) {
                odb.close();
                showAlert("Error", "El campo 'PVP' debe ser un número decimal.");
                return;
            }

            // Validar que el campo UV es un número entero
            int uvValue;
            try {
                uvValue = Integer.parseInt(uv.getText());
            } catch (NumberFormatException e) {
                odb.close();
                showAlert("Error", "El campo 'Unidades Vendidas' debe ser un número entero.");
                return;
            }

            // Validar que el campo Stock es un número entero
            int stockValue;
            try {
                stockValue = Integer.parseInt(stock.getText());
            } catch (NumberFormatException e) {
                odb.close();
                showAlert("Error", "El campo 'Stock' debe ser un número entero.");
                return;
            }

            // Buscar el artículo en la base de datos
            var query = odb.criteriaQuery(Articulo.class, org.neodatis.odb.core.query.criteria.Where.equal("codigo", ac.getCodigo()));
            var objetosArticulo = odb.getObjects(query);

            if (!objetosArticulo.isEmpty()) {
                // Actualizar los datos del artículo
                Articulo articuloDb = (Articulo) objetosArticulo.getFirst();
                articuloDb.setCodigo(codigoValue); // Actualizar el código al nuevo valor
                articuloDb.setDenominacion(denominacionValue);
                articuloDb.setPrecioDeVentaAlPublico(pvpValue);
                articuloDb.setCategoria(categoria.getText());
                articuloDb.setUnidadesVendidas(uvValue);
                articuloDb.setStock(stockValue);

                odb.store(articuloDb);
            }

            odb.commit();
            odb.close();

            // Actualizar la lista en App
            for (Articulo a : App.articulos) {
                if (a.getCodigo() == ac.getCodigo()) {
                    a.setCodigo(codigoValue);
                    a.setDenominacion(denominacionValue);
                    a.setPrecioDeVentaAlPublico(pvpValue);
                    a.setCategoria(categoria.getText());
                    a.setUnidadesVendidas(uvValue);
                    a.setStock(stockValue);
                    break;
                }
            }

            // Actualizar la tabla
            ListadosControlador listadosControlador = new ListadosControlador();
            listadosControlador.actualizarTabla();

            // Mostrar mensaje de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Artículo modificado");
            alert.setContentText("Los datos del artículo han sido actualizados correctamente.");
            alert.showAndWait();

            // Volver a la vista anterior
            App.setRoot("listados");

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al modificar el artículo");
            alert.setContentText("Hubo un problema al guardar los cambios: " + e.getMessage());
            System.out.println(e.getMessage());
            alert.showAndWait();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void salirOnAction(ActionEvent actionEvent) {
        App.setRoot("listados");
    }



}
