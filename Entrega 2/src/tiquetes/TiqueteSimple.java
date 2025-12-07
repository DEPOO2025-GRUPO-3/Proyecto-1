package tiquetes;

import usuarios.Usuario;

public class TiqueteSimple extends Tiquete {

    public TiqueteSimple(String id, double precioPublico, double tarifaServicio, double costoEmision) {
        super(id, precioPublico, tarifaServicio, costoEmision);
    }

    @Override
    public void transferir(Usuario destino) {
        if (destino == null || !validarEstado()) {
            return;
        }

        Usuario anterior = this.comprador;
        asignarComprador(destino);

        if (anterior == null) {
            marcarVendido();
        } else {
            marcarTransferido();
        }
    }

    public void aplicarDescuento(double porc) {
        if (porc <= 0) {
            return;
        }
        double factor = Math.max(0.0, 1.0 - (porc / 100.0));
        this.precioPublico = this.precioPublico * factor;
    }
}

