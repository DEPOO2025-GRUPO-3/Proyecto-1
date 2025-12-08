package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tiquetes.EstadoTiquete;
import tiquetes.Tiquete;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class TiqueteProyecto3Test {

    private static class DummyTiquete extends Tiquete {

        public DummyTiquete(String id, double precioPublico, double tarifaServicio, double costoEmision) {
            super(id, precioPublico, tarifaServicio, costoEmision);
        }

        @Override
        public void transferir(Usuario destino) {
        }

        public void marcarVendidoPublico() {
            marcarVendido();
        }

        public void marcarTransferidoPublico() {
            marcarTransferido();
        }
    }

    @Test
    public void cuentaParaIngresoSoloSiVendidoOTransferido() {
        DummyTiquete t = new DummyTiquete("T-7", 80_000, 8_000, 4_000);

        assertFalse(t.cuentaParaIngreso());

        t.setEstadoDesdePersistencia(EstadoTiquete.Disponible);
        assertFalse(t.cuentaParaIngreso());

        t.marcarVendidoPublico();
        assertTrue(t.cuentaParaIngreso());

        t.marcarTransferidoPublico();
        assertTrue(t.cuentaParaIngreso());

        t.marcarReembolsado();
        assertFalse(t.cuentaParaIngreso());
    }

    @Test
    public void registrarImpresionSoloUnaVez() {
        TiqueteSimple t = new TiqueteSimple("T-1", 50_000, 5_000, 2_000);

        assertTrue(t.registrarImpresion());
        assertFalse(t.registrarImpresion());

        assertFalse(t.validarEstado());
    }

    @Test
    public void tiqueteReembolsadoNoCuentaParaIngreso() {
        TiqueteSimple t = new TiqueteSimple("T-2", 30_000, 3_000, 1_000);

        t.marcarReembolsado();
        assertFalse(t.cuentaParaIngreso());
        assertFalse(t.validarEstado());
    }
}

