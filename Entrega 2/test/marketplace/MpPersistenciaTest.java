package marketplace;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import org.junit.jupiter.api.*;
import persistencia.Database;
import persistencia.MarketplaceStore;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class MpPersistenciaTest {

    private Database db;
    private Marketplace mp;
    private MarketplaceStore store;

    @BeforeEach
    void setup() {
        db = new Database();
        mp = new Marketplace();
        store = new MarketplaceStore();

        Usuario vendedor = new Usuario("vendedor", "1234");
        db.addUsuario(vendedor);

        TiqueteSimple t = new TiqueteSimple("TQ004", 100.0, 5.0, 2.0);
        mp.publicarOferta(vendedor, t, 120.0);
    }

    @Test
    void testGuardarYLeerArchivoJSON() {
        store.save(db, mp);

        Path path = Path.of("data/marketplace.json");
        assertTrue(Files.exists(path));

        Marketplace nuevo = new Marketplace();
        store.load(db, nuevo);
        assertNotNull(nuevo.getOfertas());
    }
}

