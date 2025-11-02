package marketplace;

import usuarios.Usuario;

public class Contraoferta {

    private Usuario comprador;
    private double precioPropuesto;
    private boolean aceptada;

    public Contraoferta(Usuario comprador, double precioPropuesto) {
        this.comprador = comprador;
        this.precioPropuesto = Math.max(0, precioPropuesto);
        this.aceptada = false;
    }

    public void aceptar() {
        this.aceptada = true;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public double getPrecioPropuesto() {
        return precioPropuesto;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    @Override
    public String toString() {
        return "Contraoferta{" +
                "comprador=" + comprador.getLogin() +
                ", precioPropuesto=" + precioPropuesto +
                ", aceptada=" + aceptada +
                '}';
    }
}

