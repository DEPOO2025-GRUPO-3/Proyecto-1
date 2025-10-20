package tiquetes;

import java.util.ArrayList;
import java.util.List;
import usuarios.Usuario;

public class TiqueteMultiple extends Tiquete {
    protected int cantidadIncluida;
    protected TipoMultiple tipo;
    protected final List<TiqueteSimple> contenidos = new ArrayList<>();

    public TiqueteMultiple(String id, double precioPublico, double tarifaServicio, double costoEmision,
                           int cantidadIncluida, TipoMultiple tipo) {
        super(id, precioPublico, tarifaServicio, costoEmision);
        this.cantidadIncluida = Math.max(0, cantidadIncluida);
        this.tipo = tipo;
    }

    public void agregar(TiqueteSimple t) { if (t != null) contenidos.add(t); }

    public boolean validarTransferencia() { return verificarEstadosTiquetes(); }

    public void transferirPaquete(Usuario destino) {
        if (destino == null || !validarTransferencia()) return;
        for (TiqueteSimple t : contenidos) { t.transferir(destino); }
        marcarTransferido();
    }

    public boolean verificarEstadosTiquetes() {
        for (TiqueteSimple t : contenidos) if (!t.validarEstado()) return false;
        return true;
    }

    @Override
    public void transferir(Usuario destino) { transferirPaquete(destino); }
}

