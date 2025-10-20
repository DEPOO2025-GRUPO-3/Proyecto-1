package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import eventos.Evento;
import eventos.Localidad;
import eventos.TipoEvento;
import eventos.Venue;
import persistencia.Database;
import persistencia.JsonStore;
import tiquetes.TiqueteSimple;
import usuarios.Administrador;
import usuarios.Cliente;
import usuarios.Organizador;

public class PersistenciaJsonTest {

    @Test
    void guardaYLuegoCarga() {
        // Construir DB
        Database db = new Database();

        Administrador admin = new Administrador("admin@acme.com", "admin123");
        Organizador org = new Organizador("org@acme.com", "org123");
        Cliente cli = new Cliente("cli@mail.com", "clave", 500_000);

        db.addUsuario(admin); db.addUsuario(org); db.addUsuario(cli);

        Venue v = new Venue("V1", "Arena", "Bogotá", 10000, "Sin pirotecnia"); v.aprobar();
        db.addVenue(v);

        Evento e = new Evento("E1", "Show", LocalDate.now().plusDays(5), LocalTime.of(20, 0), TipoEvento.musical, v);
        Localidad platea = new Localidad("L1", "Platea", 120000);
        for (int i=1; i<=3; i++) platea.agregarTiquete(new TiqueteSimple("T"+i, platea.getPrecioBase(), 10000, 2000));
        e.agregarLocalidad(platea);
        db.addEvento(e);

        // Guardar
        JsonStore store = new JsonStore();
        store.save(db);

        // Cargar
        Database db2 = store.load();

        // Validaciones mínimas
        assertTrue(db2.getUsuarios().size() >= 3);
        assertEquals(1, db2.getVenues().size());
        assertEquals(1, db2.getEventos().size());

        Evento e2 = db2.getEventos().get(0);
        assertEquals("E1", e2.getId());
        assertEquals("Show", e2.getNombre());
        assertEquals(v.getId(), e2.getVenue().getId());
        assertEquals(1, e2.getLocalidades().size());
        assertEquals(3, e2.getLocalidades().get(0).getTiquetes().size());
    }
}

