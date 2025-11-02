package marketplace;

import java.time.LocalDateTime;

public class LogRegistro {

    private LocalDateTime fechaHora;
    private String usuario;
    private String accion;
    private String detalle;

    public LogRegistro(String usuario, String accion, String detalle) {
        this.fechaHora = LocalDateTime.now();
        this.usuario = usuario;
        this.accion = accion;
        this.detalle = detalle;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getAccion() {
        return accion;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        return "[" + fechaHora + "] (" + usuario + ") " + accion + " -> " + detalle;
    }
}

