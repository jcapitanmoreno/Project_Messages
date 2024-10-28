package org.example.XMLController;

import org.example.Model.Entity.Usuario;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "usuarios")
public class UsuarioListWrapper {
    private List<Usuario> usuarios;

    public UsuarioListWrapper() {
    }

    @XmlElement(name = "usuario")
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
