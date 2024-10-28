package org.example.XMLController;

import org.example.Model.Entity.Usuario;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "usuarios")
public class UsuarioListWrapper {
    private List<Usuario> usuarios;

    /**
     * Constructor de la clase UsuarioListWrapper.
     * No realiza ninguna acción en particular en este caso,
     * pero se incluye para permitir la creación de instancias de esta clase.
     */
    public UsuarioListWrapper() {
    }

    /**
     * Método que obtiene la lista de usuarios.
     *
     * @return Lista de usuarios.
     */
    @XmlElement(name = "usuario")
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Método que establece la lista de usuarios.
     *
     * @param usuarios Lista de usuarios a establecer.
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

}
