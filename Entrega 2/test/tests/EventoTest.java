package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import eventos.*;



public class EventoTest {

    private Evento eventoSinVenue;

    @BeforeEach
    void setUp() {
        eventoSinVenue = new Evento(
                "E001",
                "Concierto Prueba",
                LocalDate.of(2025, 11, 1),
                LocalTime.of(20, 0),
                TipoEvento.musical,
                null
        );
    }

    @Test
    void testConstructorInicializaCorrectamente() {
        assertEquals("E001", eventoSinVenue.getId());
        assertEquals("Concierto Prueba", eventoSinVenue.getNombre());
        assertEquals(LocalDate.of(2025, 11, 1), eventoSinVenue.getFecha());
        assertEquals(LocalTime.of(20, 0), eventoSinVenue.getHora());
        assertEquals(TipoEvento.musical, eventoSinVenue.getTipo());

        assertEquals(EstadoEvento.programado, eventoSinVenue.getEstado());

        assertNull(eventoSinVenue.getVenue());

        assertNotNull(eventoSinVenue.getLocalidades(), "La lista de localidades no debería ser nula");
        assertTrue(eventoSinVenue.getLocalidades().isEmpty(), "Al inicio no debe haber localidades");
    }

    @Test
    void testCancelarCambiaEstadoACancelado() {
        eventoSinVenue.cancelar();
        assertEquals(EstadoEvento.cancelado, eventoSinVenue.getEstado(),
                "Después de cancelar(), el estado debe ser cancelado");
    }

    @Test
    void testAgregarLocalidadNullNoAgregaNada() {
        int antes = eventoSinVenue.getLocalidades().size();
        eventoSinVenue.agregarLocalidad(null);
        int despues = eventoSinVenue.getLocalidades().size();

        assertEquals(antes, despues,
                "agregarLocalidad(null) no debería cambiar el tamaño de la lista de localidades");
    }

    @Test
    void testIngresosNetosSinLocalidadesEsCero() {
        double ingresos = eventoSinVenue.obtenerIngresosNetos();
        assertEquals(0.0, ingresos,
                "Si no hay localidades, los ingresos netos deberían ser 0.0");
    }

    @Test
    void testGenerarTiquetesSinLocalidadesRetornaListaVacia() {
        List<tiquetes.Tiquete> tiquetesGenerados = eventoSinVenue.generarTiquetes();

        assertNotNull(tiquetesGenerados, "La lista de tiquetes no debería ser nula");
        assertTrue(tiquetesGenerados.isEmpty(),
                "Si no hay localidades, generarTiquetes() debería retornar lista vacía");
    }

    @Test
    void testVerificarReglaUnicidadSinVenueRetornaFalse() {
        boolean res = eventoSinVenue.verificarReglaUnicidad();
        assertFalse(res,
                "Si el evento no tiene venue asignado, verificarReglaUnicidad() debe ser false");
    }
}
