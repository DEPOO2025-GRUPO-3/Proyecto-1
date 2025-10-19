package logica;

public class Localidad {
	public String nombre;
	public int capacidad;
	public double precioBase; 
	public boolean numerada;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public double getPrecioBase() {
		return precioBase;
	}
	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}
	public boolean isNumerada() {
		return numerada;
	}
	public void setNumerada(boolean numerada) {
		this.numerada = numerada;
	}
	public Localidad(String nombre, int capacidad, double precioBase, boolean numerada) {
		super();
		this.nombre = nombre;
		this.capacidad = capacidad;
		this.precioBase = precioBase;
		this.numerada = numerada;
	}

}
