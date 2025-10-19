package logica;
import java.util.Date;

public class Transaccion {
	public String id;
	public Date fecha;
	public double total; 
	public int maximoTiquetesPorTransaccion;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getMaximoTiquetesPorTransaccion() {
		return maximoTiquetesPorTransaccion;
	}
	public void setMaximoTiquetesPorTransaccion(int maximoTiquetesPorTransaccion) {
		this.maximoTiquetesPorTransaccion = maximoTiquetesPorTransaccion;
	}
	public Transaccion(String id, Date fecha, double total, int maximoTiquetesPorTransaccion) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.total = total;
		this.maximoTiquetesPorTransaccion = maximoTiquetesPorTransaccion;
	}
	
}
