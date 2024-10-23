package org.example.Model.XMLController;

import org.example.Model.Entity.Mensaje;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "chat")
public class MensajeListWrapper {
    private List<Mensaje> mensajes;

    public MensajeListWrapper() {
    }

    @XmlElement(name = "chat")
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

}
