package com.example.proyectoneodatis.controladores;

import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Articulo;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import static com.example.proyectoneodatis.controladores.ListadosControlador.articuloSeleccionado;

public class CamposArticulosControlador {
    public TextField codigo,denominacion,categoria,uv,pvp,stock;
    public Button editar,salir;

    public void initialize(){
        // Configurar los campos del formulario con los datos del artículo seleccionado
        if (articuloSeleccionado != null) {
            codigo.setText(String.valueOf(articuloSeleccionado.getCodigo()));
            denominacion.setText(String.valueOf(articuloSeleccionado.getDenominacion()));
            categoria.setText(String.valueOf(articuloSeleccionado.getCategoria()));
            uv.setText(String.valueOf(articuloSeleccionado.getUnidadesVendidas()));
            pvp.setText(String.valueOf(articuloSeleccionado.getPrecioDeVentaAlPublico()));
            stock.setText(String.valueOf(articuloSeleccionado.getStock()));
        }
    }


    public void editarOnAction(ActionEvent actionEvent) {
        try {

            // Guardar los cambios en la base de datos
            ODB odb = ODBFactory.open("neonatis.test");
            odb.store(articuloSeleccionado);
            odb.commit();
            odb.close();

            // Mostrar un mensaje de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Artículo modificado");
            alert.setContentText("Los datos del artículo han sido actualizados correctamente.");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al modificar el artículo");
            alert.setContentText("Hubo un problema al guardar los cambios: " + e.getMessage());
            alert.showAndWait();
        }
    }


    public void salirOnAction(ActionEvent actionEvent) {
        App.setRoot("listados");
    }



}
