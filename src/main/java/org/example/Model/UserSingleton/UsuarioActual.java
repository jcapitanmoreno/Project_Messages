package org.example.Model.UserSingleton;

import org.example.Model.Entity.Usuario;

public class UsuarioActual {
    private static UsuarioActual instancia;

    private Usuario usuario;

    private UsuarioActual() {}

    public static UsuarioActual getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioActual();
        }
        return instancia;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
