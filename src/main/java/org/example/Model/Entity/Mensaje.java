package org.example.Model.Entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name ="Mensaje")
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

    @XmlElement(name = "remitente")
    public String getRemitente() {
        return remitente;
    }

    @XmlElement(name = "destinatario")
    public String getDestinatario() {
        return destinatario;
    }

    @XmlElement(name = "texto")
    public String getTexto() {
        return texto;
    }

    @XmlElement(name = "fecha")
    public String getFecha() {
        return fecha;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensaje mensaje = (Mensaje) o;
        return Objects.equals(fecha, mensaje.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha);
    }
}
