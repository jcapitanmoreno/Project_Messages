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
import org.example.Model.UserSingleton.UsuarioActual;
import org.example.Model.XMLController.XMLHandler;

public class ChatController {
    @FXML
    private ListView<String> usuariosList;
    @FXML
    private TextArea mensajesArea;
    @FXML
    private TextField mensajeField;



    @FXML
    public void enviarMensaje() {
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

    public static void mostrarChat(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(ChatController.class.getResource("org/example/chat.fxml"));
            //System.out.println(ChatController.class.getResource("src/main/resources/org/example/chat.fxml"));
            Parent root = loader.load();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
