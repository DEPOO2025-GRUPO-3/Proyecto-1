package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import eventos.EstadoEvento;
import tiquetes.EstadoTiquete;
import tiquetes.TiqueteSimple;
import transacciones.TransaccionCompra;

public class CompraTest extends BaseEscenario {

    @Test
    void compraDescuentaSaldoYMarcaTiquetes() {
        setupBasico();
        assertEquals(EstadoEvento.programado, evento.getEstado());

        double saldoAntes = cli.consultarSaldo();

        TransaccionCompra tx = new TransaccionCompra(cli, evento, plat, 2);
        assertTrue(tx.ejecutar());

        double esperado = saldoAntes - plat.getPrecioBase() * 2;
        assertEquals(esperado, cli.consultarSaldo(), 0.0001);

        long transferidos = plat.getTiquetes().stream()
                .filter(t -> t instanceof TiqueteSimple)
                .filter(t -> t.getEstado() == EstadoTiquete.Transferido)
                .count();
        assertTrue(transferidos >= 2);
    }

    @Test
    void noPermiteCompraSiEventoNoProgramado() {
        setupBasico();
        evento.cancelar();

        TransaccionCompra tx = new TransaccionCompra(cli, evento, plat, 1);
        IllegalStateException ex = assertThrows(IllegalStateException.class, tx::ejecutar);
        assertTrue(ex.getMessage().toLowerCase().contains("evento"));
    }
}

