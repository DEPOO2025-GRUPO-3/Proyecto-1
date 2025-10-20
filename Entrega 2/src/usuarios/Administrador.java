package usuarios;

import eventos.Evento;
import eventos.Venue;

public class Administrador extends Usuario {
    public Administrador(String login, String contrasena) { super(login, contrasena); }

    public void aprobarVenue(Venue venue) { if (venue != null) venue.aprobar(); }

    public void cancelarEvento(Evento evento) { if (evento != null) evento.cancelar(); }

    public void configurarTarifasPorTipo(String tipo, double tarifa) {
    }

    public double generarReporteGlobal(java.util.List<Evento> eventos) {
        return eventos.stream().mapToDouble(Evento::obtenerIngresosNetos).sum();
    }
}

