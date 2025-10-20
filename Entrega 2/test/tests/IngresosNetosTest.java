package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import transacciones.TransaccionCompra;

public class IngresosNetosTest extends BaseEscenario {

    @Test
    void ingresosNetosPorPrecioBase() {
        setupBasico();

        TransaccionCompra c1 = new TransaccionCompra(cli, evento, plat, 3);
        assertTrue(c1.ejecutar());

        double esperado = 3 * plat.getPrecioBase();
        assertEquals(esperado, evento.obtenerIngresosNetos(), 0.0001);
    }
}

