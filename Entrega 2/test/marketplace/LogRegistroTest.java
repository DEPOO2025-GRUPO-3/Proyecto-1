package marketplace;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class LogRegistroTest {

    @Test
    void testCreacionDeLog() {
        LogRegistro log = new LogRegistro("juan", "Oferta creada", "Oferta 001 publicada");

        assertNotNull(log.getFechaHora());
        assertEquals("juan", log.getUsuario());
        assertEquals("Oferta creada", log.getAccion());
        assertEquals("Oferta 001 publicada", log.getDetalle());
    }

    @Test
    void testToStringIncluyeUsuarioYAccion() {
        LogRegistro log = new LogRegistro("admin", "Eliminación", "Oferta 005 removida");
        String texto = log.toString();

        assertTrue(texto.contains("admin"));
        assertTrue(texto.contains("Eliminación"));
    }
}

