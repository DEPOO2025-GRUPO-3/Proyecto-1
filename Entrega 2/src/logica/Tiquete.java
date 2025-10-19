package logica;

public abstract class Tiquete {
	public String id;
	public double precioPublico;
	public double tarifaServicio;
	public double costoEmision; 
	public EstadoTiquete estado;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getPrecioPublico() {
		return precioPublico;
	}
	public void setPrecioPublico(double precioPublico) {
		this.precioPublico = precioPublico;
	}
	public double getTarifaServicio() {
		return tarifaServicio;
	}
	public void setTarifaServicio(double tarifaServicio) {
		this.tarifaServicio = tarifaServicio;
	}
	public double getCostoEmision() {
		return costoEmision;
	}
	public void setCostoEmision(double costoEmision) {
		this.costoEmision = costoEmision;
	}
	public EstadoTiquete getEstado() {
		return estado;
	}
	public void setEstado(EstadoTiquete estado) {
		this.estado = estado;
	}
	public Tiquete(String id, double precioPublico, double tarifaServicio, double costoEmision, EstadoTiquete estado) {
		super();
		this.id = id;
		this.precioPublico = precioPublico;
		this.tarifaServicio = tarifaServicio;
		this.costoEmision = costoEmision;
		this.estado = estado;
	}
	
}
