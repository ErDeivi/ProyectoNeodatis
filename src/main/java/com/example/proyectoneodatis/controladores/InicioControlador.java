package com.example.proyectoneodatis.controladores;

import java.io.IOException;

import com.example.proyectoneodatis.App;
import com.example.proyectoneodatis.Modelo.Constantes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;

public class InicioControlador {

@FXML
private TextField usuario;

@FXML
private PasswordField contrasena;

@FXML
private Text textoUsuario,textoContrasena,errorInicio;



@FXML
public void initialize(){

    usuario.setText("");
    contrasena.setText("");
    textoUsuario.setVisible(false);
    textoContrasena.setVisible(false);
    errorInicio.setVisible(false);
    
}


@FXML
public void iniciarSesion(ActionEvent event){

    textoUsuario.setVisible(false);
    textoContrasena.setVisible(false);
    errorInicio.setVisible(false);

    if(usuario.getText().isEmpty()){
        textoUsuario.setVisible(true);

    }else{
        if(contrasena.getText().isEmpty()){
        textoContrasena.setVisible(true);
    }else{
        if(!usuario.getText().equals(Constantes.USUARIO) || !contrasena.getText().equals(Constantes.CONTRASENA)){
        
       
        errorInicio.setVisible(true);

    }else{

            App.setRoot("menuPrincipal");
        }
}
}
}
}

