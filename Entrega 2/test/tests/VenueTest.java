package tests;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eventos.*;
public class VenueTest {

    private Venue venue;
    private Evento evento1;

    @BeforeEach
    void setUp() {
        venue = new Venue("V001", "Auditorio Nacional", "Bogotá", 5000, "No eventos deportivos");
        evento1 = new Evento("E001", "Concierto A", LocalDate.of(2025, 11, 10), LocalTime.of(20, 0), TipoEvento.musical, venue);
    }

    @Test
    void testConstructorInicializaCorrectamente() {
        assertEquals("V001", venue.getId());
        assertEquals("Auditorio Nacional", venue.getNombre());
        assertEquals("Bogotá", venue.getUbicacion());
        assertEquals(5000, venue.getCapacidadMaxima());
        assertEquals("No eventos deportivos", venue.getRestriccionesDeUso());
        assertFalse(venue.isAprobado(), "El venue debe iniciar como no aprobado");
    }

    @Test
    void testAprobarCambiaEstado() {
        venue.aprobar();
        assertTrue(venue.isAprobado(), "Después de aprobar(), el venue debe estar aprobado");
    }

    @Test
    void testAgregarEventoSinAprobarLanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> venue.agregarEvento(evento1),
                "No se debe poder agregar un evento si el venue no ha sido aprobado");
    }

    @Test
    void testAgregarEventoAprobadoAgregaCorrectamente() {
        venue.aprobar();
        assertDoesNotThrow(() -> venue.agregarEvento(evento1),
                "Un venue aprobado debería poder agregar un evento en una fecha libre");
        assertFalse(venue.verificarDisponibilidad(evento1.getFecha()),
                "Después de agregar el evento, esa fecha ya no debe estar disponible");
    }

    @Test
    void testAgregarEventoConFechaRepetidaLanzaExcepcion() {
        venue.aprobar();
        venue.agregarEvento(evento1);
        Evento eventoMismoDia = new Evento("E003", "Concierto Duplicado",
                LocalDate.of(2025, 11, 10), LocalTime.of(19, 0), TipoEvento.musical, venue);

        assertThrows(IllegalStateException.class, () -> venue.agregarEvento(eventoMismoDia),
                "No se puede agregar un segundo evento en la misma fecha");
    }

    @Test
    void testVerificarDisponibilidad() {
        LocalDate fecha = LocalDate.of(2025, 12, 25);
        assertTrue(venue.verificarDisponibilidad(fecha),
                "Una fecha sin reservas debe estar disponible");

        venue.reservar(fecha);
        assertFalse(venue.verificarDisponibilidad(fecha),
                "Una fecha reservada no debe estar disponible");
    }

    @Test
    void testReservarAgregaFechaCorrectamente() {
        LocalDate fecha = LocalDate.of(2026, 1, 1);
        venue.reservar(fecha);
        assertFalse(venue.verificarDisponibilidad(fecha),
                "Después de reservar(), la fecha no debe estar disponible");
    }
}
