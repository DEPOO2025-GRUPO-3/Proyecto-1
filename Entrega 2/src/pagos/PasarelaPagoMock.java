package pagos;

import usuarios.Usuario;

public class PasarelaPagoMock implements PasarelaPago {

    @Override
    public void abonarSaldo(Usuario usuario, double monto, String numeroCuenta) {
        if (usuario == null || monto <= 0) return;
        usuario.creditarSaldo(monto);
        System.out.println("[Pasarela] Abono exitoso desde cuenta " + numeroCuenta + " por $" + monto);
    }

    @Override
    public void transferir_a_banco(Usuario usuario, double monto, String numeroCuenta) {
        if (usuario == null || monto <= 0) return;
        System.out.println("[Pasarela] Cashout a cuenta " + numeroCuenta + " por $" + monto + " completado.");
    }
}

