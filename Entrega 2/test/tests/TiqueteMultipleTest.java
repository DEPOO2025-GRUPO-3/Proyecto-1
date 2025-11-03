package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import usuarios.Usuario;
import tiquetes.*;

public class TiqueteMultipleTest {

    private TiqueteMultiple paquete;
    private Usuario destinoValido;

    private static class TiqueteSimpleStub extends TiqueteSimple {
        private boolean estadoValido;
        private boolean fueTransferido = false;
        private Usuario ultimoDestino;

        public TiqueteSimpleStub(String id, boolean estadoValido) {
            super(id, 100_000.0, 5_000.0, 2_000.0);
            this.estadoValido = estadoValido;
        }

        @Override
        public boolean validarEstado() {
            return estadoValido;
        }

        @Override
        public void transferir(Usuario destino) {
            fueTransferido = true;
            ultimoDestino = destino;
        }

        public boolean fueTransferido() { return fueTransferido; }
        public Usuario getUltimoDestino() { return ultimoDestino; }
    }

    private static class UsuarioStub extends Usuario {
        public UsuarioStub(String id, String nombre) {
            super(id, nombre);
        }
    }

    @BeforeEach
    void setUp() {
        paquete = new TiqueteMultiple(
                "PK001",
                500000.0,  
                50000.0,  
                20000.0,   
                4,         
                TipoMultiple.Palco
        );

        destinoValido = new UsuarioStub("U001", "Camila");
    }

    @Test
    void testConstructorInicializaCorrecto() {
        assertEquals("PK001", paquete.getId());
        assertEquals(500000.0, paquete.getPrecioPublico());
        assertEquals(50000.0, paquete.getTarifaServicio());
        assertEquals(20000.0, paquete.getCostoEmision());

        assertEquals(4, paquete.getCantidadIncluida(), "La cantidadIncluida debe guardarse igual si es positiva");
        assertEquals(TipoMultiple.Palco, paquete.getTipo());
        assertNotNull(paquete.getContenidos());
        assertTrue(paquete.getContenidos().isEmpty(), "El paquete debe iniciar sin tiquetes internos");
    }

    @Test
    void testConstructorNoPermiteCantidadNegativa() {
        TiqueteMultiple paqueteNegativo = new TiqueteMultiple(
                "PK002",
                100000.0,
                10000.0,
                3000.0,
                -5,
                TipoMultiple.Palco
        );

        assertEquals(0, paqueteNegativo.getCantidadIncluida(),
                "Si la cantidadIncluida es negativa, debe quedar en 0 por Math.max(0, cantidadIncluida)");
    }

    @Test
    void testAgregarNoAgregaNull() {
        paquete.agregar(null);
        assertTrue(paquete.getContenidos().isEmpty(),
                "agregar(null) no debe insertar elementos");
    }

    @Test
    void testAgregarTiqueteSimpleSeGuarda() {
        TiqueteSimpleStub t1 = new TiqueteSimpleStub("T1", true);
        paquete.agregar(t1);

        List<TiqueteSimple> contenidos = paquete.getContenidos();
        assertEquals(1, contenidos.size());
        assertSame(t1, contenidos.get(0),
                "El tiquete agregado debe quedar en la lista contenidos");
    }

    @Test
    void testVerificarEstadosTiquetesTrueCuandoTodosValidos() {
        TiqueteSimpleStub t1 = new TiqueteSimpleStub("T1", true);
        TiqueteSimpleStub t2 = new TiqueteSimpleStub("T2", true);

        paquete.agregar(t1);
        paquete.agregar(t2);

        assertTrue(paquete.verificarEstadosTiquetes(),
                "Todos los tiquetes están en estado válido, debe retornar true");
        assertTrue(paquete.validarTransferencia(),
                "validarTransferencia() delega a verificarEstadosTiquetes()");
    }

    @Test
    void testVerificarEstadosTiquetesFalseSiAlgunoInvalido() {
        TiqueteSimpleStub bueno = new TiqueteSimpleStub("T1", true);
        TiqueteSimpleStub malo  = new TiqueteSimpleStub("T2", false);

        paquete.agregar(bueno);
        paquete.agregar(malo);

        assertFalse(paquete.verificarEstadosTiquetes(),
                "Si algún tiquete NO está válido, debe retornar false");
        assertFalse(paquete.validarTransferencia(),
                "validarTransferencia debe ser false en este caso");
    }

    @Test
    void testTransferirPaqueteNoHaceNadaSiDestinoEsNull() {
        TiqueteSimpleStub t1 = new TiqueteSimpleStub("T1", true);
        paquete.agregar(t1);

        assertDoesNotThrow(() -> paquete.transferirPaquete(null),
                "transferirPaquete(null) no debería lanzar excepción");

        assertFalse(t1.fueTransferido(),
                "Si el destino es null, no debe transferir los tiquetes internos");
    }

    @Test
    void testTransferirPaqueteNoTransfiereSiEstadosInvalidos() {
        TiqueteSimpleStub bueno = new TiqueteSimpleStub("T1", true);
        TiqueteSimpleStub malo  = new TiqueteSimpleStub("T2", false);
        paquete.agregar(bueno);
        paquete.agregar(malo);

        assertDoesNotThrow(() -> paquete.transferirPaquete(destinoValido),
                "No debería lanzar excepción aunque no transfiera");

        assertFalse(bueno.fueTransferido(), "No debe transferir si hay estados inválidos");
        assertFalse(malo.fueTransferido(),  "No debe transferir si hay estados inválidos");
    }

    @Test
    void testTransferirPaqueteTransfiereATodosSiTodoValido() {
        TiqueteSimpleStub a = new TiqueteSimpleStub("TA", true);
        TiqueteSimpleStub b = new TiqueteSimpleStub("TB", true);
        paquete.agregar(a);
        paquete.agregar(b);

        assertDoesNotThrow(() -> paquete.transferirPaquete(destinoValido),
                "Cuando todo está válido y hay destino, no debe lanzar excepción");

        assertTrue(a.fueTransferido(), "Todos los tiquetes válidos deben ser transferidos");
        assertTrue(b.fueTransferido(), "Todos los tiquetes válidos deben ser transferidos");
        assertSame(destinoValido, a.getUltimoDestino(), "El destino debe ser el mismo para todos");
        assertSame(destinoValido, b.getUltimoDestino(), "El destino debe ser el mismo para todos");
    }

    @Test
    void testTransferirDelegadoLlamaTransferirPaquete() {
        TiqueteSimpleStub a = new TiqueteSimpleStub("TA", true);
        paquete.agregar(a);

        assertDoesNotThrow(() -> paquete.transferir(destinoValido),
                "transferir() debe delegar en transferirPaquete() y no romper");

        assertTrue(a.fueTransferido(),
                "transferir() debería haber transferido el contenido como paquete");
    }
}
