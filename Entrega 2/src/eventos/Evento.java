package eventos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import tiquetes.Tiquete;

public class Evento {

    private String id;
    private String nombre;
    private LocalDate fecha;
    private LocalTime hora;
    private TipoEvento tipo;
    private EstadoEvento estado;
    private Venue venue;

    private final List<Localidad> localidades = new ArrayList<>();

    public Evento(String id, String nombre, LocalDate fecha, LocalTime hora, TipoEvento tipo, Venue venue) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.tipo = tipo;
        this.estado = EstadoEvento.programado;
        this.venue = venue;
    }

    public void cancelar() { this.estado = EstadoEvento.cancelado; }

    public void agregarLocalidad(Localidad loc) {
        if (loc != null) localidades.add(loc);
    }

    public double obtenerIngresosNetos() {
        return localidades.stream().mapToDouble(Localidad::ingresoNeto).sum();
    }

    public boolean verificarReglaUnicidad() {
        return venue != null && !venue.verificarDisponibilidad(fecha);
    }

    public List<Tiquete> generarTiquetes() {
        List<Tiquete> out = new ArrayList<>();
        for (Localidad l : localidades) {
            out.addAll(l.getTiquetes());
        }
        return out;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public TipoEvento getTipo() { return tipo; }
    public EstadoEvento getEstado() { return estado; }
    public Venue getVenue() { return venue; }
    public List<Localidad> getLocalidades() { return localidades; }
}

