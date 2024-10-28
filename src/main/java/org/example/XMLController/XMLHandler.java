package org.example.XMLController;

import org.example.Model.Entity.Mensaje;
import org.example.Model.Entity.Usuario;
import org.example.Utils.FileHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class XMLHandler {
    private static final String USERS_FILE = "usuarios.xml";
    private static final String MESSAGES_FILE = "mensajes.xml";


    /**
     * Lee la lista de usuarios desde el archivo XML. Si el archivo no existe, lo crea
     * y retorna una lista vacía.
     *
     * @return Lista de objetos Usuario almacenados en el archivo XML.
     * @throws Exception En caso de error de lectura o escritura.
     */
    public static List<Usuario> leerUsuarios() throws Exception {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            crearArchivoUsuarios();
            return new ArrayList<>();
        }


        UsuarioListWrapper wrapper = FileHandler.leerDesdeArchivo(file, UsuarioListWrapper.class);
        return wrapper.getUsuarios();
    }
    /**
     * Crea un archivo XML vacío para almacenar usuarios si no existe ya uno.
     *
     * @throws Exception En caso de error al crear o escribir en el archivo.
     */
    private static void crearArchivoUsuarios() throws Exception {
        UsuarioListWrapper wrapper = new UsuarioListWrapper();
        wrapper.setUsuarios(new ArrayList<>());
        FileHandler.escribirEnArchivo(USERS_FILE, wrapper, UsuarioListWrapper.class);  // Uso de FileHandler
    }

    /**
     * Agrega un nuevo usuario al archivo XML de usuarios, conservando los usuarios ya
     * existentes.
     *
     * @param usuario El objeto Usuario a registrar.
     * @throws Exception En caso de error al leer o escribir en el archivo.
     */
    public static void registrarUsuario(Usuario usuario) throws Exception {
        List<Usuario> usuarios = leerUsuarios();
        usuarios.add(usuario);

        UsuarioListWrapper wrapper = new UsuarioListWrapper();
        wrapper.setUsuarios(usuarios);
        FileHandler.escribirEnArchivo(USERS_FILE, wrapper, UsuarioListWrapper.class);
    }

    /**
     * Busca un usuario en el archivo XML por su nombre.
     *
     * @param nombre El nombre del usuario a buscar.
     * @return El objeto Usuario encontrado, o null si no existe.
     * @throws Exception En caso de error de lectura del archivo.
     */
    public static Usuario buscarUsuario(String nombre) throws Exception {
        return leerUsuarios().stream()
                .filter(usuario -> usuario.getNombre().equals(nombre))
                .findFirst()
                .orElse(null);
    }




    //Mensajes


    /**
     * Lee la lista de mensajes desde el archivo XML. Si el archivo no existe, lo crea
     * y retorna un objeto MensajeListWrapper vacío.
     *
     * @return Objeto MensajeListWrapper que contiene la lista de mensajes.
     * @throws Exception En caso de error de lectura o escritura.
     */
    public static MensajeListWrapper leerMensajes() throws Exception {
        File file = new File(MESSAGES_FILE);
        if (!file.exists()) {
            crearArchivoMensajes();
            return new MensajeListWrapper();
        }

        return (MensajeListWrapper) FileHandler.leerDesdeArchivo(file, MensajeListWrapper.class);
    }

    /**
     * Crea un archivo XML vacío para almacenar mensajes si no existe ya uno.
     *
     * @throws Exception En caso de error al crear o escribir en el archivo.
     */
    private static void crearArchivoMensajes() throws Exception {
        MensajeListWrapper wrapper = new MensajeListWrapper();
        wrapper.setMensajes(new ArrayList<>());
        FileHandler.escribirEnArchivo(MESSAGES_FILE, wrapper, MensajeListWrapper.class);
    }


    /**
     * Agrega un nuevo mensaje al archivo XML de mensajes, conservando los mensajes ya
     * existentes.
     *
     * @param mensaje El objeto Mensaje a almacenar.
     * @throws Exception En caso de error al leer o escribir en el archivo.
     */
    public static void enviarMensaje(Mensaje mensaje) throws Exception {
        MensajeListWrapper mensajesWrapper = leerMensajes();
        mensajesWrapper.getMensajes().add(mensaje);
        FileHandler.escribirEnArchivo(MESSAGES_FILE, mensajesWrapper, MensajeListWrapper.class);
    }




}
