package transacciones;

import pagos.PasarelaPago;
import usuarios.Usuario;

public class TransaccionCashout extends Transaccion {

    private final double monto;
    private final String numeroCuenta;
    private final PasarelaPago pasarela;

    public TransaccionCashout(Usuario usuario, double monto, String numeroCuenta, PasarelaPago pasarela) {
        super(usuario);
        this.monto = monto;
        this.numeroCuenta = numeroCuenta;
        this.pasarela = pasarela;
    }

    @Override
    public boolean ejecutar() {
        if (!usuario.autenticar()) throw new IllegalStateException("Autenticación requerida.");
        if (pasarela == null || monto <= 0 || numeroCuenta == null || numeroCuenta.isBlank()) return false;
        if (!usuario.debitarSaldo(monto)) throw new IllegalStateException("Saldo insuficiente.");
        pasarela.transferir_a_banco(usuario, monto, numeroCuenta);
        return true;
    }

    public double getMonto() { return monto; }
}

