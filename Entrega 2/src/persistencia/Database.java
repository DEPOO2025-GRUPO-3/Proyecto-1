package persistencia;

import java.util.ArrayList;
import java.util.List;
import eventos.Evento;
import eventos.Venue;
import usuarios.Usuario;

public class Database {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Venue> venues = new ArrayList<>();
    private final List<Evento> eventos = new ArrayList<>();

    // --- Usuarios
    public List<Usuario> getUsuarios() { return usuarios; }
    public void addUsuario(Usuario u) { if (u != null) usuarios.add(u); }

    // --- Venues
    public List<Venue> getVenues() { return venues; }
    public void addVenue(Venue v) { if (v != null) venues.add(v); }

    // --- Eventos
    public List<Evento> getEventos() { return eventos; }
    public void addEvento(Evento e) { if (e != null) eventos.add(e); }

    public void clear() {
        usuarios.clear();
        venues.clear();
        eventos.clear();
    }
}

