package logica;

public class Venue {
	public String id;
	public String nombre;
	public int capacidadMaxima;
	public String restriccionesDeUso; 
	public boolean aprobado;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}
	public void setCapacidadMaxima(int capacidadMaxima) {
		this.capacidadMaxima = capacidadMaxima;
	}
	public String getRestriccionesDeUso() {
		return restriccionesDeUso;
	}
	public void setRestriccionesDeUso(String restriccionesDeUso) {
		this.restriccionesDeUso = restriccionesDeUso;
	}
	public boolean isAprobado() {
		return aprobado;
	}
	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}
	public Venue(String id, String nombre, int capacidadMaxima, String restriccionesDeUso, boolean aprobado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.capacidadMaxima = capacidadMaxima;
		this.restriccionesDeUso = restriccionesDeUso;
		this.aprobado = aprobado;
	}
	
}
