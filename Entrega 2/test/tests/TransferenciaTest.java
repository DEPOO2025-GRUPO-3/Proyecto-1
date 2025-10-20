package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tiquetes.EstadoTiquete;
import tiquetes.PaqueteDeluxe;
import tiquetes.TiqueteSimple;
import tiquetes.TipoMultiple;
import transacciones.TransaccionCompra;
import transacciones.TransaccionTransferencia;
import usuarios.Usuario;

public class TransferenciaTest extends BaseEscenario {

    @Test
    void transferenciaDeTiqueteSimple() {
        setupBasico();

        TransaccionCompra compra = new TransaccionCompra(cli, evento, plat, 1);
        assertTrue(compra.ejecutar());

        Usuario destino = new Usuario("destino@mail.com", "123");
        TiqueteSimple source = plat.getTiquetes().stream()
                .filter(t -> t instanceof TiqueteSimple)
                .map(t -> (TiqueteSimple) t)
                .filter(t -> t.getEstado() == EstadoTiquete.Transferido)
                .findFirst().orElseThrow();

        TransaccionTransferencia tx = new TransaccionTransferencia(cli, destino, source);
        assertTrue(tx.ejecutar());

        assertEquals(EstadoTiquete.Transferido, source.getEstado());
    }

    @Test
    void deluxeNoEsTransferible() {
        setupBasico();

        PaqueteDeluxe deluxe = new PaqueteDeluxe("DX1", 300000, 10000, 2000, 2, TipoMultiple.Palco, "VIP", 1);
        Usuario otro = new Usuario("x@mail.com", "x");

        TransaccionTransferencia tx = new TransaccionTransferencia(cli, otro, deluxe);
        assertTrue(tx.ejecutar());

        assertNotEquals(EstadoTiquete.Transferido, deluxe.getEstado());
    }
}

