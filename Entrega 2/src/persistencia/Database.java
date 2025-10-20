package persistencia;

import java.util.ArrayList;
import java.util.List;

import eventos.Evento;
import eventos.Localidad;
import eventos.Venue;
import tiquetes.Tiquete;
import usuarios.Usuario;

public class Database {
    public final List<Usuario> usuarios = new ArrayList<>();
    public final List<Venue> venues = new ArrayList<>();
    public final List<Evento> eventos = new ArrayList<>();
    public final List<Localidad> localidades = new ArrayList<>();
    public final List<Tiquete> tiquetes = new ArrayList<>();
}

