package logica;

public class Usuario {
	public String login;
	public String contrasena;
	public double saldoVirtual;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public double getSaldoVirtual() {
		return saldoVirtual;
	}
	public void setSaldoVirtual(double saldoVirtual) {
		this.saldoVirtual = saldoVirtual;
	}
	public Usuario(String login, String contrasena, double saldoVirtual) {
		super();
		this.login = login;
		this.contrasena = contrasena;
		this.saldoVirtual = saldoVirtual;
	} 
	
}
