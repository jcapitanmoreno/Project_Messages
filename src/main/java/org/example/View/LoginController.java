package org.example.View;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Model.Entity.Usuario;
import org.example.Model.UserSingleton.UsuarioActual;
import org.example.Utils.PasswordUtils;
import org.example.XMLController.XMLHandler;

public class LoginController {
    @FXML
    private TextField loginUsuarioField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Label loginErrorLabel;

    @FXML
    private TextField registroUsuarioField;
    @FXML
    private PasswordField registroPasswordField;
    @FXML
    private Label registroErrorLabel;


    @FXML
    public void iniciarSesion() {
        String nombreUsuario = loginUsuarioField.getText();
        String contrasena = loginPasswordField.getText();

        try {
            Usuario usuario = XMLHandler.buscarUsuario(nombreUsuario);


            String contrasenaHashed = PasswordUtils.hashPassword(contrasena);
            if (usuario != null && usuario.getContraseña().equals(contrasenaHashed)) {
                UsuarioActual.getInstancia().setUsuario(usuario);
                ChatController.mostrarChat((Stage) loginUsuarioField.getScene().getWindow());
            } else {
                loginErrorLabel.setText("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void registrarUsuario() {
        String nombreUsuario = registroUsuarioField.getText();
        String contrasena = registroPasswordField.getText();

        try {
            Usuario usuarioExistente = XMLHandler.buscarUsuario(nombreUsuario);
            if (usuarioExistente != null) {
                mostrarAdvertencia("El usuario ya está registrado", "El nombre de usuario introducido ya está registrado.");
                registroErrorLabel.setText("Usuario ya registrado");
            } else {

                String contrasenaHashed = PasswordUtils.hashPassword(contrasena);
                Usuario nuevoUsuario = new Usuario(nombreUsuario, contrasenaHashed);
                XMLHandler.registrarUsuario(nuevoUsuario);
                mostrarExito("Usuario registrado", "El usuario ha sido registrado exitosamente.");
                registroErrorLabel.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
