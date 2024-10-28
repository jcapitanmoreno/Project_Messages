package org.example.XMLController;

import org.example.Model.Entity.Mensaje;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "mensajes")
public class MensajeListWrapper {
    private List<Mensaje> mensajes;

    /**
     * Constructor de la clase MensajeListWrapper.
     * Inicializa la lista de mensajes como una nueva ArrayList.
     */
    public MensajeListWrapper() {
        mensajes = new ArrayList<>();
    }


    /**
     * Método que obtiene la lista de mensajes.
     *
     * @return Lista de mensajes.
     */
    @XmlElement(name = "mensaje")
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    /**
     * Método que establece la lista de mensajes.
     *
     * @param mensajes Lista de mensajes a establecer.
     */
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

}
