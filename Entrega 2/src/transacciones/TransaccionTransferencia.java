package transacciones;

import tiquetes.Tiquete;
import usuarios.Usuario;

public class TransaccionTransferencia extends Transaccion {
    private final Usuario destino;
    private final Tiquete tiquete;

    public TransaccionTransferencia(Usuario origen, Usuario destino, Tiquete tiquete) {
        super(origen);
        this.destino = destino; this.tiquete = tiquete;
    }

    @Override
    public boolean ejecutar() {
        if (destino == null || tiquete == null) return false;
        tiquete.transferir(destino);
        return true;
    }
}

