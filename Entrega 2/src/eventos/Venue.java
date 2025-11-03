package eventos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Venue {
    private String id;
    private String nombre;
    private String ubicacion;
    private int capacidadMaxima;
    private String restriccionesDeUso;
    private boolean aprobado;

    private final Set<LocalDate> fechasReservadas = new HashSet<>();

    public Venue(String id, String nombre, String ubicacion, int capacidadMaxima, String restriccionesDeUso) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.restriccionesDeUso = restriccionesDeUso;
        this.aprobado = false;
    }

    public void agregarEvento(Evento e) {
        if (e == null) return;
        if (!aprobado) throw new IllegalStateException("Venue no aprobado por Administrador.");
        if (!verificarDisponibilidad(e.getFecha()))
            throw new IllegalStateException("El venue ya tiene evento en esa fecha.");
        fechasReservadas.add(e.getFecha());
    }

    public boolean verificarDisponibilidad(java.time.LocalDate fecha) {
        return !fechasReservadas.contains(fecha);
    }

    public void aprobar() { this.aprobado = true; }

    public void reservar(LocalDate fecha) { fechasReservadas.add(fecha); }

    public boolean isAprobado() { return aprobado; }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public String getRestriccionesDeUso() { return restriccionesDeUso; }
}

