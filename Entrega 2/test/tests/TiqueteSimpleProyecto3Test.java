package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class TiqueteSimpleProyecto3Test {

    @Test
    public void aplicarDescuentoReducePrecioPublico() {
        TiqueteSimple t = new TiqueteSimple("TS-1", 100_000, 10_000, 5_000);

        assertEquals(100_000, t.getPrecioPublico(), 0.0001);

        t.aplicarDescuento(10);
        assertEquals(90_000, t.getPrecioPublico(), 0.0001);

        double antes = t.getPrecioPublico();
        t.aplicarDescuento(0);
        assertEquals(antes, t.getPrecioPublico(), 0.0001);

        t.aplicarDescuento(-5);
        assertEquals(antes, t.getPrecioPublico(), 0.0001);
    }

    @Test
    public void transferirSoloSiEstadoValido() {
        TiqueteSimple t = new TiqueteSimple("TS-2", 50_000, 5_000, 2_000);
        Usuario u = new Usuario("cliente", "1234", 100_000);

        assertTrue(t.validarEstado());

        t.transferir(u);
        assertNotNull(t.getEstado());
        assertTrue(t.cuentaParaIngreso());

        t.marcarReembolsado();
        assertFalse(t.validarEstado());

        t.transferir(u);
        assertEquals(t.getEstado(), tiquetes.EstadoTiquete.Reembolsado);
    }
}

