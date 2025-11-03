package marketplace;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class MarketplaceTest {

    private Marketplace mp;
    private Usuario vendedor;
    private Usuario comprador;
    private TiqueteSimple tiquete;

    @BeforeEach
    void setup() {
        mp = new Marketplace();
        vendedor = new Usuario("vendedor", "abc");
        comprador = new Usuario("cliente", "xyz");
        tiquete = new TiqueteSimple("TQ003", 100.0, 5.0, 2.0);
    }

    @Test
    void testPublicarOferta() {
        Oferta oferta = mp.publicarOferta(vendedor, tiquete, 120.0);
        assertNotNull(oferta);
        assertEquals(1, mp.getOfertas().size());
        assertTrue(mp.getLog().get(0).getAccion().contains("Oferta"));
    }

    @Test
    void testOfertarYRegistrarContraoferta() {
        Oferta o = mp.publicarOferta(vendedor, tiquete, 120.0);
        Contraoferta c = new Contraoferta(comprador, 110.0);

        boolean ok = mp.ofertar(o.getId(), c);
        assertTrue(ok);
        assertEquals(1, o.getContraofertas().size());
        assertTrue(mp.getLog().stream().anyMatch(l -> l.getAccion().contains("Contraoferta")));
    }

    @Test
    void testAceptarContraofertaCierraOferta() {
        Oferta o = mp.publicarOferta(vendedor, tiquete, 120.0);
        Contraoferta c = new Contraoferta(comprador, 110.0);
        mp.ofertar(o.getId(), c);

        boolean ok = mp.aceptarContraoferta(o.getId(), c, vendedor);
        assertTrue(ok);
        assertFalse(o.isActiva());
    }

    @Test
    void testEliminarOfertaGeneraLog() {
        Oferta o = mp.publicarOferta(vendedor, tiquete, 120.0);
        mp.eliminarOferta(o.getId(), vendedor);

        assertTrue(mp.getLog().stream().anyMatch(l -> l.getAccion().contains("eliminada")));
    }
}

