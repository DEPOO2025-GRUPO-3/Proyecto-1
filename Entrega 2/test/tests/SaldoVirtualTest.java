package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pagos.PasarelaPago;
import pagos.PasarelaPagoMock;
import usuarios.Cliente;


public class SaldoVirtualTest extends BaseEscenario {

    @Test
    void recargaYCashout() {
        setupBasico();

        double saldoInicial = cli.consultarSaldo();
        cli.abonarSaldo(200_000, "999-111", pasarela);
        assertEquals(saldoInicial + 200_000, cli.consultarSaldo(), 0.0001);

        double antes = cli.consultarSaldo();
        cli.retirarSaldo(100_000, "999-000", pasarela);
        assertEquals(antes - 100_000, cli.consultarSaldo(), 0.0001);
    }
}

