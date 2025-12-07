package tiquetes;

import java.time.LocalDateTime;

import eventos.Evento;
import eventos.Localidad;
import usuarios.Usuario;

public abstract class Tiquete {

    protected String id;
    protected double precioPublico;
    protected double tarifaServicio;
    protected double costoEmision;
    protected EstadoTiquete estado = EstadoTiquete.Disponible;

    // NUEVOS CAMPOS PARA PROYECTO 3
    protected Evento evento;

    protected Localidad localidad;

    protected Usuario comprador;

    protected LocalDateTime fechaImpresion;

    // ---------------------------------------------------------

    public Tiquete(String id, double precioPublico, double tarifaServicio, double costoEmision) {
        this.id = id;
        this.precioPublico = Math.max(0, precioPublico);
        this.tarifaServicio = Math.max(0, tarifaServicio);
        this.costoEmision = Math.max(0, costoEmision);
    }

    public abstract void transferir(Usuario destino);

    public boolean validarEstado() {
        return estado != EstadoTiquete.Reembolsado
                && estado != EstadoTiquete.Vencido
                && estado != EstadoTiquete.Usado
                && fechaImpresion == null;
    }

    public void marcarComoVencido() {
        this.estado = EstadoTiquete.Vencido;
    }

    public void marcarReembolsado() {
        this.estado = EstadoTiquete.Reembolsado;
    }

    public boolean cuentaParaIngreso() {
        return estado == EstadoTiquete.Vendido || estado == EstadoTiquete.Transferido;
    }

    public boolean registrarImpresion() {
        if (fechaImpresion != null) {
            return false;
        }
        fechaImpresion = LocalDateTime.now();
        return true;
    }

    public LocalDateTime getFechaImpresion() {
        return fechaImpresion;
    }

    public void asociarEventoYLocalidad(Evento evento, Localidad localidad) {
        this.evento = evento;
        this.localidad = localidad;
    }

    public void asignarComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public Evento getEvento() {
        return evento;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public String getId() {
        return id;
    }

    public double getPrecioPublico() {
        return precioPublico;
    }

    public double getTarifaServicio() {
        return tarifaServicio;
    }

    public double getCostoEmision() {
        return costoEmision;
    }

    public EstadoTiquete getEstado() {
        return estado;
    }

    protected void marcarVendido() {
        this.estado = EstadoTiquete.Vendido;
    }

    protected void marcarTransferido() {
        this.estado = EstadoTiquete.Transferido;
    }

    public void setEstadoDesdePersistencia(EstadoTiquete estado) {
        this.estado = estado;
    }
}

