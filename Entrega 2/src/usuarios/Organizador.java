package usuarios;

import java.time.LocalDate;
import java.time.LocalTime;

import eventos.Evento;
import eventos.Localidad;
import eventos.TipoEvento;
import eventos.Venue;

public class Organizador extends Usuario {

    public Organizador(String login, String contrasena) {
        super(login, contrasena);
    }

    public Evento crearEvento(String nombre, LocalDate fecha, LocalTime hora, Venue venue, TipoEvento tipo) {
        return new Evento("EV-" + System.nanoTime(), nombre, fecha, hora, tipo, venue);
    }

    public double generarReporte(Evento evento) {
        return evento.obtenerIngresosNetos();
    }

    public double consultarIngresosNetos(Evento evento) {
        return evento.obtenerIngresosNetos();
    }

    public void agregarLocalidad(Evento evento, Localidad loc) {
        if (evento != null && loc != null) {
            evento.agregarLocalidad(loc);
        }
    }
}

