package org.example.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.Model.Entity.Mensaje;
import org.example.Model.Entity.Usuario;
import org.example.Model.UserSingleton.UsuarioActual;
import org.example.XMLController.XMLHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatController {
    @FXML
    private ListView<String> usuariosList;
    @FXML
    public TextArea mensajesArea;
    @FXML
    private TextField mensajeField;
    @FXML
    private ImageView infoIcon;

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Carga la lista de usuarios, configura el escuchador de usuarios
     * y muestra el análisis de la conversación.
     */
    @FXML
    public void initialize() {
        cargarUsuarios();
        configurarEscuchadorUsuarios();
        mostrarAnalisis();
    }

    /**
     * Carga la lista de usuarios desde un archivo XML
     * y los agrega a la lista visual de usuarios.
     * Maneja excepciones si ocurre algún error durante la carga.
     */
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

    /**
     * Envía un mensaje al destinatario seleccionado en la lista de usuarios.
     * Se asegura de que el campo de texto no esté vacío antes de enviar.
     */
    @FXML
    public void enviarMensaje() {
        String text = mensajeField.getText().trim();

        if (!text.isEmpty()) {
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

    /**
     * Configura un escuchador para la lista de usuarios.
     * Al hacer doble clic en un usuario, se limpian los mensajes
     * y se cargan los mensajes de la conversación con ese usuario.
     */
    private void configurarEscuchadorUsuarios() {
        usuariosList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                clearMessageArea();
                String selectedUser = usuariosList.getSelectionModel().getSelectedItem();
                cargarMensajes(selectedUser);
            }
        });
    }

    /**
     * Limpia el área de mensajes, borrando todo el texto actualmente mostrado.
     */
    private void clearMessageArea() {
        mensajesArea.clear();
    }

    /**
     * Carga los mensajes de la conversación con el usuario especificado.
     * Filtra los mensajes según el remitente y el destinatario,
     * y los muestra en el área de mensajes.
     */
    private void cargarMensajes(String user) {
        try {
            List<Mensaje> mensajes = XMLHandler.leerMensajes().getMensajes();
            mensajesArea.clear();
            String remitente = UsuarioActual.getInstancia().getUsuario().getNombre();
            for (Mensaje mensaje : mensajes) {
                if ((mensaje.getDestinatario().equals(user) && mensaje.getRemitente().equals(remitente)) ||
                        (mensaje.getRemitente().equals(user) && mensaje.getDestinatario().equals(remitente))) {
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

    /**
     * Muestra la ventana del chat cargando el archivo FXML correspondiente.
     * Configura la escena en la ventana principal.
     *
     * @param stage La ventana donde se mostrará el chat.
     */
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

    /**
     * Regresa a la ventana de inicio de sesión cargando el archivo FXML correspondiente.
     * Configura la escena en la ventana principal.
     */
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

    /**
     * Método que exporta la conversación con un usuario seleccionado a un archivo de texto.
     */
    @FXML
    public void exportarConversacion() {
        String usuarioSeleccionado = obtenerUsuarioSeleccionado();
        if (usuarioSeleccionado == null) {
            return;
        }

        try {
            List<Mensaje> conversacion = obtenerConversacion(usuarioSeleccionado);
            File archivo = seleccionarArchivoParaGuardar();

            if (archivo != null) {
                guardarConversacionEnArchivo(conversacion, archivo);
                mensajesArea.appendText("Conversación exportada exitosamente a " + archivo.getAbsolutePath() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensajesArea.appendText("Error al exportar la conversación: " + e.getMessage() + "\n");
        }
    }

    /**
     * Método auxiliar que obtiene el usuario actualmente seleccionado en la lista.
     *
     * @return El nombre del usuario seleccionado o null si no hay selección.
     */
    private String obtenerUsuarioSeleccionado() {
        String usuarioSeleccionado = usuariosList.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mensajesArea.appendText("Por favor, selecciona un usuario para exportar la conversación.\n");
        }
        return usuarioSeleccionado;
    }

    /**
     * Método que obtiene la conversación entre el usuario actual y el usuario seleccionado.
     *
     * @param usuarioSeleccionado El nombre del usuario con el que se está conversando.
     * @return Una lista de objetos Mensaje que representan la conversación.
     * @throws Exception En caso de error al leer los mensajes.
     */
    private List<Mensaje> obtenerConversacion(String usuarioSeleccionado) throws Exception {
        List<Mensaje> mensajes = XMLHandler.leerMensajes().getMensajes();
        List<Mensaje> conversacion = new ArrayList<>();
        String remitente = UsuarioActual.getInstancia().getUsuario().getNombre();

        for (Mensaje mensaje : mensajes) {
            if ((mensaje.getDestinatario().equals(usuarioSeleccionado) && mensaje.getRemitente().equals(remitente)) ||
                    (mensaje.getRemitente().equals(usuarioSeleccionado) && mensaje.getDestinatario().equals(remitente))) {
                conversacion.add(mensaje);
            }
        }
        return conversacion;
    }

    /**
     * Método que abre un cuadro de diálogo para seleccionar el archivo donde se guardará la conversación.
     *
     * @return El archivo seleccionado por el usuario o null si se cancela.
     */
    private File seleccionarArchivoParaGuardar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Conversación");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Texto (*.txt)", "*.txt"));
        return fileChooser.showSaveDialog((Stage) mensajesArea.getScene().getWindow());
    }

    /**
     * Método que guarda la conversación en un archivo de texto.
     *
     * @param conversacion La lista de mensajes que forman la conversación a guardar.
     * @param archivo      El archivo en el que se guardará la conversación.
     * @throws IOException En caso de error al escribir en el archivo.
     */
    private void guardarConversacionEnArchivo(List<Mensaje> conversacion, File archivo) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(archivo);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             PrintWriter writer = new PrintWriter(osw)) {

            for (Mensaje mensaje : conversacion) {
                writer.println(mensaje.getRemitente() + " a " + mensaje.getDestinatario() + ": " + mensaje.getTexto() + " (" + mensaje.getFecha() + ")");
            }
        }
    }


    /**
     * Exporta la conversación entre el usuario actual y el usuario seleccionado
     * a un archivo de texto. Si no hay usuario seleccionado, muestra un mensaje de error.
     */
    @FXML
    private void mostrarAnalisis() {
        String usuarioSeleccionado = usuariosList.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mensajesArea.appendText("Por favor, selecciona un usuario para analizar la conversación.\n");
            return;
        }

        try {
            List<Mensaje> conversacion = filtrarMensajes(usuarioSeleccionado);
            StringBuilder analisis = realizarAnalisisConversacion(conversacion);
            mostrarResultadoAnalisis(analisis.toString());

        } catch (Exception e) {
            e.printStackTrace();
            mensajesArea.appendText("Error al analizar la conversación: " + e.getMessage() + "\n");
        }
    }

    /**
     * Filtra los mensajes entre el usuario actual y el usuario seleccionado en la conversación.
     * Solo devuelve los mensajes que involucran al usuario actual como remitente o destinatario.
     *
     * @param usuarioSeleccionado Nombre del usuario con el que se está teniendo la conversación.
     * @return Lista de mensajes que corresponden a la conversación con el usuario seleccionado.
     * @throws Exception Si ocurre un error al leer los mensajes.
     */
    private List<Mensaje> filtrarMensajes(String usuarioSeleccionado) throws Exception {
        List<Mensaje> mensajes = XMLHandler.leerMensajes().getMensajes();
        String remitente = UsuarioActual.getInstancia().getUsuario().getNombre();
        return mensajes.stream()
                .filter(mensaje -> (mensaje.getDestinatario().equals(usuarioSeleccionado) && mensaje.getRemitente().equals(remitente)) ||
                        (mensaje.getRemitente().equals(usuarioSeleccionado) && mensaje.getDestinatario().equals(remitente)))
                .collect(Collectors.toList());
    }

    /**
     * Realiza un análisis sobre la conversación proporcionada, calculando el total de mensajes,
     * el conteo de mensajes por usuario y las palabras más repetidas.
     *
     * @param conversacion Lista de mensajes a analizar.
     * @return Un StringBuilder que contiene el análisis de la conversación.
     */
    private StringBuilder realizarAnalisisConversacion(List<Mensaje> conversacion) {
        long totalMensajes = contarTotalMensajes(conversacion);
        Map<String, Long> mensajesPorUsuario = contarMensajesPorUsuario(conversacion);
        Map<String, Long> palabrasRepetidas = contarPalabrasRepetidas(conversacion);

        return construirResultadoAnalisis(totalMensajes, mensajesPorUsuario, palabrasRepetidas);
    }

    /**
     * Cuenta el total de mensajes en una conversación dada.
     *
     * @param conversacion Lista de mensajes de la conversación.
     * @return El número total de mensajes.
     */
    private long contarTotalMensajes(List<Mensaje> conversacion) {
        return conversacion.size();
    }

    /**
     * Cuenta los mensajes enviados por cada usuario en una conversación dada.
     *
     * @param conversacion Lista de mensajes de la conversación.
     * @return Un mapa donde la clave es el nombre del remitente y el valor es el número de mensajes enviados.
     */
    private Map<String, Long> contarMensajesPorUsuario(List<Mensaje> conversacion) {
        return conversacion.stream()
                .collect(Collectors.groupingBy(Mensaje::getRemitente, Collectors.counting()));
    }

    /**
     * Cuenta la frecuencia de cada palabra en la conversación, para determinar palabras más repetidas.
     *
     * @param conversacion Lista de mensajes de la conversación.
     * @return Un mapa donde la clave es una palabra y el valor es el número de veces que se repite.
     */
    private Map<String, Long> contarPalabrasRepetidas(List<Mensaje> conversacion) {
        return conversacion.stream()
                .flatMap(mensaje -> Arrays.stream(mensaje.getTexto().split(" ")))
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }

    /**
     * Construye el análisis de la conversación, que incluye el total de mensajes,
     * el conteo de mensajes por usuario, y las palabras más repetidas.
     *
     * @param totalMensajes      El número total de mensajes en la conversación.
     * @param mensajesPorUsuario Mapa de conteo de mensajes por usuario.
     * @param palabrasRepetidas  Mapa de las palabras más repetidas en la conversación.
     * @return Un StringBuilder que contiene el análisis detallado de la conversación.
     */
    private StringBuilder construirResultadoAnalisis(long totalMensajes, Map<String, Long> mensajesPorUsuario, Map<String, Long> palabrasRepetidas) {
        StringBuilder analisis = new StringBuilder();
        analisis.append("Total de mensajes: ").append(totalMensajes).append("\n");
        analisis.append("Mensajes por usuario:\n");
        mensajesPorUsuario.forEach((user, count) -> analisis.append(user).append(": ").append(count).append("\n"));

        analisis.append("Palabras más repetidas:\n");
        palabrasRepetidas.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> analisis.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));

        return analisis;
    }

    /**
     * Muestra el resultado del análisis de la conversación en un cuadro de diálogo.
     *
     * @param resultadoAnalisis Un string que contiene el análisis de la conversación.
     */
    private void mostrarResultadoAnalisis(String resultadoAnalisis) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Análisis de Conversación");
        alert.setHeaderText(null);
        alert.setContentText(resultadoAnalisis);
        alert.showAndWait();
    }
}
