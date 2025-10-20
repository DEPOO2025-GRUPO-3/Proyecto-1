package eventos;

import java.util.ArrayList;
import java.util.List;
import tiquetes.Tiquete;

public class Localidad {

    private String id;
    private String nombre;
    private double precioBase;
    private final List<Tiquete> tiquetes = new ArrayList<>();

    public Localidad(String id, String nombre, double precioBase) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public void agregarTiquete(Tiquete t) { if (t != null) tiquetes.add(t); }

    public double ingresoNeto() {
        long vendidosOTransferidos = tiquetes.stream().filter(Tiquete::cuentaParaIngreso).count();
        return vendidosOTransferidos * precioBase;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public List<Tiquete> getTiquetes() { return tiquetes; }
}

