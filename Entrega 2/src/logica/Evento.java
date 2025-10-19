package logica;
import java.sql.Time;
import java.util.Date;

public class Evento {
	public String id;
	public String nombre;
	public Date fecha;
	public Time hora; 
	public EstadoEvento estado;
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
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Time getHora() {
		return hora;
	}
	public void setHora(Time hora) {
		this.hora = hora;
	}
	public EstadoEvento getEstado() {
		return estado;
	}
	public void setEstado(EstadoEvento estado) {
		this.estado = estado;
	}
	public Evento(String id, String nombre, Date fecha, Time hora, EstadoEvento estado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fecha = fecha;
		this.hora = hora;
		this.estado = estado;
	}
	
}
