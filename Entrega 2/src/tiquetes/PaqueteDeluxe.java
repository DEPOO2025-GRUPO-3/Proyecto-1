package tiquetes;

import usuarios.Usuario;

public class PaqueteDeluxe extends TiqueteMultiple {
    private String beneficios;
    private int cortesias;

    public PaqueteDeluxe(String id, double precioPublico, double tarifaServicio, double costoEmision,
                         int cantidadIncluida, TipoMultiple tipo,
                         String beneficios, int cortesias) {
        super(id, precioPublico, tarifaServicio, costoEmision, cantidadIncluida, tipo);
        this.beneficios = beneficios;
        this.cortesias = cortesias;
    }

    public String getBeneficios() { return beneficios; }
    public int getCortesias() { return cortesias; }

    @Override public void transferir(Usuario destino) { /* no-op */ }
    @Override public void transferirPaquete(Usuario destino) { /* no-op */ }
}

