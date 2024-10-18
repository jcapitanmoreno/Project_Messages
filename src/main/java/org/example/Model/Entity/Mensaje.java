package org.example.Model.Entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Mensaje {
    private String remitente;
    private String destinatario;
    private String texto;
    private String fecha;

    public Mensaje() {}

    public Mensaje(String remitente, String destinatario, String texto, String fecha) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.texto = texto;
        this.fecha = fecha;
    }

    @XmlElement
    public String getRemitente() {
        return remitente;
    }

    @XmlElement
    public String getDestinatario() {
        return destinatario;
    }

    @XmlElement
    public String getTexto() {
        return texto;
    }

    @XmlElement
    public String getFecha() {
        return fecha;
    }
}
