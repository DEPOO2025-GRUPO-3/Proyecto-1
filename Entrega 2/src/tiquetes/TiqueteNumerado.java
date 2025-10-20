package tiquetes;

public class TiqueteNumerado extends TiqueteSimple {
    private String asiento;

    public TiqueteNumerado(String id, double precioPublico, double tarifaServicio, double costoEmision, String asiento) {
        super(id, precioPublico, tarifaServicio, costoEmision);
        this.asiento = asiento;
    }

    public String getAsiento() { return asiento; }
    public void setAsiento(String asiento) { this.asiento = asiento; }
}

