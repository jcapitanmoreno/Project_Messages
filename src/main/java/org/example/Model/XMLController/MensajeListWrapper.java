package org.example.Model.XMLController;

import org.example.Model.Entity.Mensaje;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "mensajes")
public class MensajeListWrapper {
    private List<Mensaje> mensajes;

    public MensajeListWrapper() {
        mensajes = new ArrayList<>();
    }


    @XmlElement(name = "mensaje")
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

}
