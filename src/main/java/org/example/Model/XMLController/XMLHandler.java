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

    // Leer usuarios del XML, si el archivo no existe, lo crea
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

    // Crear archivo XML vacío de usuarios
    private static void crearArchivoUsuarios() throws Exception {
        File file = new File(USERS_FILE);
        file.createNewFile();

        List<Usuario> usuarios = new ArrayList<>();
        UsuarioListWrapper wrapper = new UsuarioListWrapper();
        wrapper.setUsuarios(usuarios);

        JAXBContext context = JAXBContext.newInstance(UsuarioListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(wrapper, file);
    }

    // Guardar usuario en el XML
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

    // Buscar un usuario por nombre
    public static Usuario buscarUsuario(String nombre) throws Exception {
        List<Usuario> usuarios = leerUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombre)) {
                return usuario;
            }
        }
        return null;
    }

    // Leer mensajes del XML
    public static List<Mensaje> leerMensajes() throws Exception {
        File file = new File(MESSAGES_FILE);

        if (!file.exists()) {
            crearArchivoMensajes();
            return new ArrayList<>();
        }

        JAXBContext context = JAXBContext.newInstance(MensajeListWrapper.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        MensajeListWrapper wrapper = (MensajeListWrapper) unmarshaller.unmarshal(file);

        return wrapper.getMensajes();
    }

    // Crear archivo XML vacío de mensajes
    private static void crearArchivoMensajes() throws Exception {
        File file = new File(MESSAGES_FILE);
        file.createNewFile();

        List<Mensaje> mensajes = new ArrayList<>();
        MensajeListWrapper wrapper = new MensajeListWrapper();
        wrapper.setMensajes(mensajes);

        JAXBContext context = JAXBContext.newInstance(MensajeListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(wrapper, file);
    }

    // Guardar mensaje en el XML
    public static void enviarMensaje(Mensaje mensaje) throws Exception {
        List<Mensaje> mensajes = leerMensajes();
        mensajes.add(mensaje);

        JAXBContext context = JAXBContext.newInstance(MensajeListWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        MensajeListWrapper wrapper = new MensajeListWrapper();
        wrapper.setMensajes(mensajes);

        marshaller.marshal(wrapper, new File(MESSAGES_FILE));
    }

}
