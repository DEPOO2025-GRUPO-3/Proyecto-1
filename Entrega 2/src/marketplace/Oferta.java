package marketplace;

import java.util.ArrayList;
import java.util.List;
import tiquetes.Tiquete;
import usuarios.Usuario;

public class Oferta {

    private String id;
    private Usuario vendedor;
    private Tiquete tiquete;
    private double precioBase;
    private boolean activa;
    private final List<Contraoferta> contraofertas = new ArrayList<>();

    public Oferta(String id, Usuario vendedor, Tiquete tiquete, double precioBase) {
        this.id = id;
        this.vendedor = vendedor;
        this.tiquete = tiquete;
        this.precioBase = Math.max(0, precioBase);
        this.activa = true;
    }

    public void agregarContraoferta(Contraoferta c) {
        if (activa && c != null) {
            contraofertas.add(c);
        }
    }

    public void aceptarContraoferta(Contraoferta c) {
        if (c != null && contraofertas.contains(c)) {
            c.aceptar();
            this.activa = false;
        }
    }

    public void cerrarOferta() {
        this.activa = false;
    }

    public boolean isActiva() {
        return activa;
    }

    public String getId() {
        return id;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public Tiquete getTiquete() {
        return tiquete;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public List<Contraoferta> getContraofertas() {
        return contraofertas;
    }

    @Override
    public String toString() {
        return "Oferta{" +
                "id='" + id + '\'' +
                ", vendedor=" + vendedor.getLogin() +
                ", precioBase=" + precioBase +
                ", activa=" + activa +
                ", numContraofertas=" + contraofertas.size() +
                '}';
    }
}

