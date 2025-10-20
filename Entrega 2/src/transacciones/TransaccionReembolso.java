package transacciones;

import tiquetes.Tiquete;
import usuarios.Usuario;

public class TransaccionReembolso extends Transaccion {
    private final Tiquete tiquete;
    private final double valor;

    public TransaccionReembolso(Usuario usuario, Tiquete tiquete) {
        super(usuario);
        this.tiquete = tiquete;
        this.valor = (tiquete != null) ? tiquete.getPrecioPublico() : 0.0;
    }

    @Override
    public boolean ejecutar() {
        if (!usuario.autenticar()) throw new IllegalStateException("Autenticación requerida.");
        if (tiquete == null) return false;
        usuario.creditarSaldo(valor);
        tiquete.marcarReembolsado();
        return true;
    }

    public double getValor() { return valor; }
}

