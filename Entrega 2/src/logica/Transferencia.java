package logica;
import java.util.Date;

public class Transferencia {
	public boolean autenticadaConPassword;
	public Date fecha;
	public boolean isAutenticadaConPassword() {
		return autenticadaConPassword;
	}
	public void setAutenticadaConPassword(boolean autenticadaConPassword) {
		this.autenticadaConPassword = autenticadaConPassword;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Transferencia(boolean autenticadaConPassword, Date fecha) {
		super();
		this.autenticadaConPassword = autenticadaConPassword;
		this.fecha = fecha;
	}
	
}
