package org.example.Model.XMLController;

import org.example.Model.Entity.Mensaje;
import org.example.Model.Entity.Usuario;

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
     * Lee la lista de usuarios del archivo XML.
     * Si el archivo no existe, lo crea con una lista vacía de usuarios.
     * @return Lista de usuarios almacenados en el archivo XML.
     * @throws Exception En caso de error de lectura o escritura.
     */
    public static List<Usuario> leerUsuarios() throws Exception {
        File file = new File(USERS_FILE);

        if (!file.exists()) {
            crearArchivoUsuarios();
            return new ArrayList<>();
        }

        JAXBContext context = JAXBContext.newInstance(UsuarioListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        UsuarioListWrapper wrapper = (UsuarioListWrapper) unmarshaller.unmarshal(file);

        return wrapper.getUsuarios();
    }


    /**
     * Crea un nuevo archivo XML vacío para almacenar usuarios.
     * @throws Exception En caso de error al crear el archivo.
     */
    private static void crearArchivoUsuarios() throws Exception {
        File file = new File(USERS_FILE);


        List<Usuario> usuarios = new ArrayList<>();
        UsuarioListWrapper wrapper = new UsuarioListWrapper();
        wrapper.setUsuarios(usuarios);

        JAXBContext context = JAXBContext.newInstance(UsuarioListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(wrapper, file);
    }


    /**
     * Guarda un nuevo usuario en el archivo XML.
     * @param usuario El usuario que queremos registrar.
     * @throws Exception En caso de error al escribir en el archivo.
     */
    public static void registrarUsuario(Usuario usuario) throws Exception {
        List<Usuario> usuarios = leerUsuarios();
        usuarios.add(usuario);

        JAXBContext context = JAXBContext.newInstance(UsuarioListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        UsuarioListWrapper wrapper = new UsuarioListWrapper();
        wrapper.setUsuarios(usuarios);

        marshaller.marshal(wrapper, new File(USERS_FILE));
    }


    /**
     * Busca un usuario por su nombre en el archivo XML.
     * @param nombre El nombre del usuario que estamos buscando.
     * @return El objeto Usuario si lo encontramos, o null si no existe.
     * @throws Exception En caso de error de lectura.
     */
    public static Usuario buscarUsuario(String nombre) throws Exception {
        List<Usuario> usuarios = leerUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                return usuario;
            }
        }
        return null;
    }


    /**
     * Lee la lista de mensajes del archivo XML.
     * Si el archivo no existe, lo crea con una lista vacía de mensajes.
     * @return Lista de mensajes almacenados en el archivo XML.
     * @throws Exception En caso de error de lectura o escritura.
     */
    public static MensajeListWrapper leerMensajes() throws Exception {
        File file = new File(MESSAGES_FILE);

        if (!file.exists()) {
            System.out.println("El archivo no existe, creando uno nuevo.");
            crearArchivoMensajes();
            return new MensajeListWrapper();
        }

        JAXBContext context = JAXBContext.newInstance(MensajeListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        MensajeListWrapper wrapper = null;

        try {

            wrapper = (MensajeListWrapper) unmarshaller.unmarshal(file);
        } catch (Exception e) {
            e.printStackTrace(); // Esto te dará más información sobre lo que salió mal
        }

        return wrapper;
    }

    /**
     * Crea un nuevo archivo XML vacío para almacenar mensajes.
     * @throws Exception En caso de error al crear el archivo.
     */
    private static void crearArchivoMensajes() throws Exception {
        File file = new File(MESSAGES_FILE);


        List<Mensaje> mensajes = new ArrayList<>();
        MensajeListWrapper wrapper = new MensajeListWrapper();
        wrapper.setMensajes(mensajes);

        JAXBContext context = JAXBContext.newInstance(MensajeListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(wrapper, file);
    }


    /**
     * Guarda un nuevo mensaje en el archivo XML.
     * @param mensaje El mensaje que queremos guardar.
     * @throws Exception En caso de error al escribir en el archivo.
     */
    public static void enviarMensaje(Mensaje mensaje) throws Exception {
        MensajeListWrapper mensajes = leerMensajes();
        mensajes.getMensajes().add(mensaje);

        JAXBContext context = JAXBContext.newInstance(MensajeListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);



        marshaller.marshal(mensajes, new File(MESSAGES_FILE));
    }

}
