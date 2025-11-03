package Consolas;

import eventos.Evento;
import eventos.TipoEvento;
import eventos.Venue;

import java.time.LocalDate;
import java.time.LocalTime;

public class OrganizadorConsola extends MainConsola {

    public static void main(String[] args) {
        new OrganizadorConsola().runLoop();
    }

    @Override
    protected void afterBoot() {
        if (db.getUsuarios().isEmpty()) {
            db.addUsuario(new usuarios.Usuario("organizador", "1234", 0));
        }
    }

    @Override
    protected void renderMenu() {
        System.out.println("\n=== ORGANIZADOR ===");
        System.out.println("1. Crear venue");
        System.out.println("2. Aprobar venue");
        System.out.println("3. Crear evento");
        System.out.println("0. Salir");
    }

    @Override
    protected int maxOpcion() { return 3; }

    @Override
    protected boolean handleOption(int op) {
        switch (op) {
            case 1 -> crearVenue();
            case 2 -> aprobarVenue();
            case 3 -> crearEvento();
            case 0 -> { return true; }
        }
        return false;
    }

    private void crearVenue() {
        String id = ConsolaUtil.readNonEmpty("ID venue: ");
        String nombre = ConsolaUtil.readNonEmpty("Nombre: ");
        String ubic = ConsolaUtil.readNonEmpty("Ubicación: ");
        int cap = ConsolaUtil.readInt("Capacidad máx: ", 1, 1_000_000);
        String restr = ConsolaUtil.readNonEmpty("Restricciones: ");
        db.addVenue(new Venue(id, nombre, ubic, cap, restr));
        System.out.println("Venue creado.");
        ConsolaUtil.pause();
    }

    private void aprobarVenue() {
        if (db.getVenues().isEmpty()) { System.out.println("No hay venues."); return; }
        String id = ConsolaUtil.readNonEmpty("ID venue: ");
        var v = db.getVenues().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        if (v == null) { System.out.println("No existe ese venue."); return; }
        v.aprobar();
        System.out.println("Venue aprobado.");
        ConsolaUtil.pause();
    }

    private void crearEvento() {
        if (db.getVenues().isEmpty()) { System.out.println("No hay venues (cree y apruebe uno)."); return; }

        String id = ConsolaUtil.readNonEmpty("ID evento: ");
        String nombre = ConsolaUtil.readNonEmpty("Nombre: ");
        String sFecha = ConsolaUtil.readNonEmpty("Fecha (YYYY-MM-DD): ");
        String sHora  = ConsolaUtil.readNonEmpty("Hora (HH:MM): ");
        System.out.println("Tipos: 1) concierto 2) teatro 3) conferencia");
        int t = ConsolaUtil.readInt("Tipo: ", 1, 3);
        TipoEvento tipo = switch (t) {
            case 1 -> TipoEvento.musical;
            case 2 -> TipoEvento.deportivo;
            default -> TipoEvento.musical;
        };

        System.out.println("Venues:");
        for (int i = 0; i < db.getVenues().size(); i++) {
            var v = db.getVenues().get(i);
            System.out.printf("%d) %s - %s %s\n", i+1, v.getId(), v.getNombre(), v.isAprobado() ? "[aprobado]" : "[no aprobado]");
        }
        int idx = ConsolaUtil.readInt("Seleccione venue: ", 1, db.getVenues().size()) - 1;
        var venue = db.getVenues().get(idx);

        try {
            var e = new Evento(id, nombre, LocalDate.parse(sFecha), LocalTime.parse(sHora), tipo, venue);
            db.addEvento(e);
            System.out.println("Evento creado.");
        } catch (Exception ex) {
            System.out.println("Error creando evento: " + ex.getMessage());
        }
        ConsolaUtil.pause();
    }
}
