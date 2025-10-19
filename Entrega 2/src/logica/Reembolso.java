package logica;

public class Reembolso {
	public String id;
	public double valor;
	public String motivo;
	public boolean aprobado; 
	public String origen;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public boolean isAprobado() {
		return aprobado;
	}
	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public Reembolso(String id, double valor, String motivo, boolean aprobado, String origen) {
		super();
		this.id = id;
		this.valor = valor;
		this.motivo = motivo;
		this.aprobado = aprobado;
		this.origen = origen;
	}
	
}
