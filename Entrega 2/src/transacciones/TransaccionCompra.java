package transacciones;

import eventos.Evento;
import eventos.Localidad;
import eventos.EstadoEvento;
import tiquetes.EstadoTiquete;
import tiquetes.Tiquete;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class TransaccionCompra extends Transaccion {

    private final Evento evento;
    private final Localidad localidad;
    private final int cantidad;
    private double total;

    public TransaccionCompra(Usuario usuario, Evento evento, Localidad localidad, int cantidad) {
        super(usuario);
        this.evento = evento;
        this.localidad = localidad;
        this.cantidad = cantidad;
    }

    @Override
    public boolean ejecutar() {
        if (!usuario.autenticar()) {
            throw new IllegalStateException("Autenticación requerida.");
        }
        if (cantidad <= 0) {
            return false;
        }

        if (evento == null || evento.getEstado() != EstadoEvento.programado) {
            throw new IllegalStateException("El evento no está habilitado para venta.");
        }

        total = cantidad * localidad.getPrecioBase();

        if (!usuario.debitarSaldo(total)) {
            throw new IllegalStateException("Saldo insuficiente.");
        }

        int asignados = 0;
        for (Tiquete t : localidad.getTiquetes()) {
            if (asignados == cantidad) {
                break;
            }
            if (t instanceof TiqueteSimple && t.getEstado() == EstadoTiquete.Disponible) {

                t.asociarEventoYLocalidad(evento, localidad);
                t.asignarComprador(usuario);

                t.transferir(usuario);
                asignados++;
            }
        }

        return asignados == cantidad;
    }

    public double getTotal() {
        return total;
    }

    public Evento getEvento() {
        return evento;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public int getCantidad() {
        return cantidad;
    }
}

