package usuarios;

import eventos.Evento;
import eventos.Localidad;
import tiquetes.Tiquete;
import transacciones.TransaccionCompra;
import transacciones.TransaccionReembolso;

public class Cliente extends Usuario {

    public Cliente(String login, String contrasena) {
        super(login, contrasena);
    }

    public Cliente(String login, String contrasena, double saldoInicial) {
        super(login, contrasena, saldoInicial);
    }

    public TransaccionCompra comprarTiquete(Evento evento, Localidad localidad, int cantidad) {
        TransaccionCompra tx = new TransaccionCompra(this, evento, localidad, cantidad);
        if (tx.ejecutar()) {
            registrarTransaccion(tx);
        }
        return tx;
    }

    public TransaccionReembolso solicitarReembolso(Tiquete tiquete) {
        TransaccionReembolso tx = new TransaccionReembolso(this, tiquete);
        if (tx.ejecutar()) {
            registrarTransaccion(tx);
        }
        return tx;
    }
}

