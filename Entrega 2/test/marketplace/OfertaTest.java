package marketplace;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class OfertaTest {

    @Test
    void testCrearOfertaYAgregarContraoferta() {
        Usuario vendedor = new Usuario("vendedor", "abc");
        TiqueteSimple t = new TiqueteSimple("TQ001", 100.0, 5.0, 2.0);
        Oferta oferta = new Oferta("OF001", vendedor, t, 120.0);

        Usuario comprador = new Usuario("cliente", "123");
        Contraoferta c = new Contraoferta(comprador, 110.0);
        oferta.agregarContraoferta(c);

        List<Contraoferta> contraofertas = oferta.getContraofertas();
        assertEquals(1, contraofertas.size());
        assertEquals(c, contraofertas.get(0));
    }

    @Test
    void testAceptarContraofertaDesactivaOferta() {
        Usuario vendedor = new Usuario("vendedor2", "pass");
        TiqueteSimple t = new TiqueteSimple("TQ002", 100.0, 5.0, 2.0);
        Oferta oferta = new Oferta("OF002", vendedor, t, 120.0);

        Usuario comprador = new Usuario("comprador", "xyz");
        Contraoferta c = new Contraoferta(comprador, 115.0);

        oferta.agregarContraoferta(c);
        oferta.aceptarContraoferta(c);

        assertTrue(c.isAceptada());
        assertFalse(oferta.isActiva());
    }
}

