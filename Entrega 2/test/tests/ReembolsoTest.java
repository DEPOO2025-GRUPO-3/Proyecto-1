package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tiquetes.EstadoTiquete;
import tiquetes.Tiquete;
import tiquetes.TiqueteSimple;
import transacciones.TransaccionCompra;
import transacciones.TransaccionReembolso;

public class ReembolsoTest extends BaseEscenario {

    @Test
    void reembolsoAcreditaSaldoYMarcaTiquete() {
        setupBasico();

        double saldoAntes = cli.consultarSaldo();
        TransaccionCompra compra = new TransaccionCompra(cli, evento, plat, 1);
        assertTrue(compra.ejecutar());

        Tiquete t = plat.getTiquetes().stream()
                .filter(x -> x instanceof TiqueteSimple)
                .filter(x -> x.getEstado() == EstadoTiquete.Transferido)
                .findFirst().orElseThrow();

        TransaccionReembolso re = new TransaccionReembolso(cli, t);
        assertTrue(re.ejecutar());

        assertEquals(saldoAntes, cli.consultarSaldo(), 0.0001);

        assertEquals(EstadoTiquete.Reembolsado, t.getEstado());
    }
}

