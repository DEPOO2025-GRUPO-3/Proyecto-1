package transacciones;

import java.time.LocalDateTime;
import java.util.UUID;
import usuarios.Usuario;

public abstract class Transaccion {
    protected final String id = UUID.randomUUID().toString();
    protected final LocalDateTime fecha = LocalDateTime.now();
    protected final Usuario usuario;

    protected Transaccion(Usuario usuario) { this.usuario = usuario; }

    public abstract boolean ejecutar();
    public String getId() { return id; }
    public LocalDateTime getFecha() { return fecha; }
    public Usuario getUsuario() { return usuario; }
}

