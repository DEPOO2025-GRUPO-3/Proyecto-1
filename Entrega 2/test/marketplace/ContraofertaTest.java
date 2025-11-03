package marketplace;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import usuarios.Usuario;

public class ContraofertaTest {

    @Test
    void testCreacionContraoferta() {
        Usuario comprador = new Usuario("carlos", "1234");
        Contraoferta c = new Contraoferta(comprador, 150.0);

        assertEquals(comprador, c.getComprador());
        assertEquals(150.0, c.getPrecioPropuesto());
        assertFalse(c.isAceptada());
    }

    @Test
    void testAceptarContraoferta() {
        Usuario comprador = new Usuario("ana", "abcd");
        Contraoferta c = new Contraoferta(comprador, 200.0);

        c.aceptar();
        assertTrue(c.isAceptada());
    }

    @Test
    void testToStringIncluyeDatosPrincipales() {
        Usuario comprador = new Usuario("maria", "pass");
        Contraoferta c = new Contraoferta(comprador, 300.0);

        String texto = c.toString();
        assertTrue(texto.contains("maria"));
        assertTrue(texto.contains("300.0"));
    }
}

