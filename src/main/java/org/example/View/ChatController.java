package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Model.Entity.Mensaje;
import org.example.Model.Entity.Usuario;
import org.example.Model.UserSingleton.UsuarioActual;
import org.example.Model.XMLController.XMLHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    @FXML
    private ListView<String> usuariosList;
    @FXML
    public TextArea mensajesArea;
    @FXML
    private TextField mensajeField;

    @FXML
    public void initialize() {
        cargarUsuarios();
        configurarEscuchadorUsuarios();
    }



    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = XMLHandler.leerUsuarios();
            for (Usuario usuario : usuarios) {
                usuariosList.getItems().add(usuario.getNombre());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void enviarMensaje() {
        String text = mensajeField.getText().trim();

        if (!text.isEmpty()){
            String destinatario = usuariosList.getSelectionModel().getSelectedItem();
            String texto = mensajeField.getText();
            String fecha = java.time.LocalDateTime.now().toString();
            String remitente = UsuarioActual.getInstancia().getUsuario().getNombre();
            Mensaje mensaje = new Mensaje(remitente, destinatario, texto, fecha);
            try {
                XMLHandler.enviarMensaje(mensaje);
                mensajesArea.appendText(remitente + ": " + texto + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    private void configurarEscuchadorUsuarios() {
        usuariosList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                clearMessageArea();
                String selectedUser = usuariosList.getSelectionModel().getSelectedItem();
                cargarMensajes(selectedUser);
            }
        });
    }


    private void clearMessageArea() {
        mensajesArea.clear();
    }
    private void cargarMensajes(String user) {
        try {
            List<Mensaje> mensajes = XMLHandler.leerMensajes().getMensajes();
            mensajesArea.clear();
            String remitente = UsuarioActual.getInstancia().getUsuario().getNombre();
            for (Mensaje mensaje : mensajes) {
                if ((mensaje.getDestinatario().equals(user) && mensaje.getRemitente().equals(remitente)) ||
                        (mensaje.getRemitente().equals(user) && mensaje.getDestinatario().equals(remitente)))  {
                    if (mensaje.getRemitente().equals(remitente)) {
                        mensajesArea.appendText("Tú: " + mensaje.getTexto() + "\n");
                    } else {
                        mensajesArea.appendText(user + ": " + mensaje.getTexto() + "\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensajesArea.appendText("Error al cargar los mensajes: " + e.getMessage() + "\n");
        }
    }

    public static void mostrarChat(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("/org/example/chat.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void volverAlLogin() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usuariosList.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void exportarConversacion() {
        String usuarioSeleccionado = usuariosList.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mensajesArea.appendText("Por favor, selecciona un usuario para exportar la conversación.\n");
            return;
        }

        try {
            List<Mensaje> mensajes = XMLHandler.leerMensajes().getMensajes();
            List<Mensaje> conversacion = new ArrayList<>();
            String remitente = UsuarioActual.getInstancia().getUsuario().getNombre();

            for (Mensaje mensaje : mensajes) {
                if ((mensaje.getDestinatario().equals(usuarioSeleccionado) && mensaje.getRemitente().equals(remitente)) ||
                        (mensaje.getRemitente().equals(usuarioSeleccionado) && mensaje.getDestinatario().equals(remitente))) {
                    conversacion.add(mensaje);
                }
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Conversación");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto (*.txt)", "*.txt"));
            File archivo = fileChooser.showSaveDialog((Stage) mensajesArea.getScene().getWindow());

            if (archivo != null) {
                try (FileOutputStream fos = new FileOutputStream(archivo);
                     OutputStreamWriter osw = new OutputStreamWriter(fos);
                     PrintWriter writer = new PrintWriter(osw)) {

                    for (Mensaje mensaje : conversacion) {
                        writer.println(mensaje.getRemitente() + " a " + mensaje.getDestinatario() + ": " + mensaje.getTexto() + " (" + mensaje.getFecha() + ")");
                    }
                }
                mensajesArea.appendText("Conversación exportada exitosamente a " + archivo.getAbsolutePath() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensajesArea.appendText("Error al exportar la conversación: " + e.getMessage() + "\n");
        }
    }
}
