package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import administracion.Administracion;

public class AdministracionTest {

    private Administracion admin;

    @BeforeEach
    void setUp() {
        admin = new Administracion();
    }

    @Test
    void testInstanciaNoNula() {
        assertNotNull(admin, "El objeto Administracion no debería ser nulo");
    }

    @Test
    void testRunNoLanzaExcepciones() {
        assertDoesNotThrow(() -> admin.run(),
                "El método run() no debería lanzar excepciones aunque esté vacío");
    }
}
