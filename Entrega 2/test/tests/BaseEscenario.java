package tests;

import eventos.*;
import pagos.PasarelaPago;
import pagos.PasarelaPagoMock;
import tiquetes.TiqueteSimple;
import usuarios.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class BaseEscenario {

    protected Administrador admin;
    protected Organizador org;
    protected Cliente cli;
    protected PasarelaPago pasarela;

    protected Venue venue;
    protected Evento evento;
    protected Localidad plat;

    protected void setupBasico() {
        admin = new Administrador("admin@acme.com", "admin123");
        org   = new Organizador("org@acme.com", "org123");
        cli   = new Cliente("cli@mail.com", "clave");
        pasarela = new PasarelaPagoMock();

        venue = new Venue("V1", "Arena", "Bogotá", 10000, "Sin pirotecnia");
        admin.aprobarVenue(venue);

        evento = new Evento("E1", "Show", LocalDate.now().plusDays(5), LocalTime.of(20, 0),
                TipoEvento.musical, venue);

        plat = new Localidad("L1", "Platea", 120000.0);
        for (int i = 1; i <= 10; i++) {
            plat.agregarTiquete(new TiqueteSimple("T" + i, plat.getPrecioBase(), 10000, 2000));
        }
        evento.agregarLocalidad(plat);

        cli.abonarSaldo(1_000_000, "123-456", pasarela);
    }
}

