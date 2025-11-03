package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usuarios.Usuario;
import tiquetes.*;
public class PaqueteDeluxeTest {

    private PaqueteDeluxe paquete;

    @BeforeEach
    void setUp() {
        paquete = new PaqueteDeluxe(
                "PK001",
                500000.0,   
                50000.0,    
                20000.0,    
                4,          
                TipoMultiple.Palco, 
                "Acceso VIP, bebidas y souvenirs",
                2           
        );
    }

    @Test
    void testConstructorInicializaCorrectamente() {
        assertNotNull(paquete);
        assertEquals("Acceso VIP, bebidas y souvenirs", paquete.getBeneficios());
        assertEquals(2, paquete.getCortesias());
    }

    @Test
    void testGettersHerencia() {
        assertEquals("PK001", paquete.getId());
        assertEquals(500000.0, paquete.getPrecioPublico());
        assertEquals(50000.0, paquete.getTarifaServicio());
        assertEquals(20000.0, paquete.getCostoEmision());
        assertEquals(4, paquete.getCantidadIncluida());
        assertEquals(TipoMultiple.Palco, paquete.getTipo());
    }

    @Test
    void testTransferirNoLanzaExcepcion() {
        Usuario user = new Usuario("U01", "Camila");
        assertDoesNotThrow(() -> paquete.transferir(user),
                "El método transferir() no debería lanzar excepciones");
    }

    @Test
    void testTransferirPaqueteNoLanzaExcepcion() {
        Usuario user = new Usuario("U02", "Juan");
        assertDoesNotThrow(() -> paquete.transferirPaquete(user),
                "El método transferirPaquete() no debería lanzar excepciones");
    }
}
