package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.Model.Entity.Mensaje;
import org.example.Model.Entity.Usuario;
import org.example.Model.UserSingleton.UsuarioActual;
import org.example.Model.XMLController.XMLHandler;

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
            if (event.getClickCount() == 2) { // Verifica si fue un doble clic
                clearMessageArea(); // Limpiamos el área de mensajes
                String selectedUser = usuariosList.getSelectionModel().getSelectedItem();
                // Aquí puedes cargar los mensajes del usuario seleccionado
                cargarMensajes(selectedUser);
            }
        });
    }

    // Método para limpiar el área de mensajes
    private void clearMessageArea() {
        mensajesArea.clear(); // Limpia el contenido del TextArea
    }
    private void cargarMensajes(String user) {
        try {
            // Leer todos los mensajes
            List<Mensaje> mensajes = XMLHandler.leerMensajes().getMensajes();

            // Limpiar el área de mensajes antes de cargar los nuevos
            mensajesArea.clear();

            // Filtrar y mostrar solo los mensajes relacionados con el usuario seleccionado
            String remitente = UsuarioActual.getInstancia().getUsuario().getNombre();
            for (Mensaje mensaje : mensajes) {
                if (mensaje.getDestinatario().equals(user) || mensaje.getRemitente().equals(user)) {
                    mensajesArea.appendText(mensaje.getRemitente() + " a " + mensaje.getDestinatario() + ": " + mensaje.getTexto() + "\n");
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
            //System.out.println(ChatController.class.getResource("src/main/resources/org/example/chat.fxml"));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
