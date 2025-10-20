package usuarios;

import java.util.ArrayList;
import java.util.List;

import pagos.PasarelaPago;
import transacciones.Transaccion;
import transacciones.TransaccionReembolso;
import tiquetes.Tiquete;

public class Usuario {

    private String login;
    private String contrasena;
    private double saldoVirtual;
    private final List<Transaccion> transacciones = new ArrayList<>();

    public Usuario(String login, String contrasena) {
        this.login = login;
        this.contrasena = contrasena;
    }

    public Usuario(String login, String contrasena, double saldoInicial) {
        this.login = login;
        this.contrasena = contrasena;
        this.saldoVirtual = Math.max(0, saldoInicial);
    }

    public boolean login(String correo, String contrasena) {
        return this.login.equals(correo) && this.contrasena.equals(contrasena);
    }

    public boolean autenticar() {
        return contrasena != null && !contrasena.isBlank();
    }

    public void abonarSaldo(double monto, String numeroCuenta, PasarelaPago pasarela) {
        if (pasarela == null || monto <= 0 || numeroCuenta == null || numeroCuenta.isBlank()) return;
        pasarela.abonarSaldo(this, monto, numeroCuenta);
    }

    public void retirarSaldo(double monto, String numeroCuenta, PasarelaPago pasarela) {
        if (pasarela == null || monto <= 0 || numeroCuenta == null || numeroCuenta.isBlank()) return;
        if (!debitarSaldo(monto)) throw new IllegalStateException("Saldo insuficiente.");
        pasarela.transferir_a_banco(this, monto, numeroCuenta);
    }

    public void creditarSaldo(double monto) {
        if (monto > 0) saldoVirtual += monto;
    }

    public boolean debitarSaldo(double monto) {
        if (monto <= 0 || saldoVirtual < monto) return false;
        saldoVirtual -= monto;
        return true;
    }

    public double consultarSaldo() { return saldoVirtual; }

    public void registrarTransaccion(Transaccion t) { if (t != null) transacciones.add(t); }

    public void recibirReembolso(TransaccionReembolso t) { if (t != null) creditarSaldo(t.getValor()); }

    public void transferirTiquete(Tiquete tiquete, Usuario destino) {
        if (!autenticar()) throw new IllegalStateException("Debe autenticarse.");
        if (tiquete == null || destino == null) return;
        tiquete.transferir(destino);
    }

    public String getLogin() { return login; }

    public String getContrasena() { return contrasena; }

    public double getSaldoVirtual() { return saldoVirtual; }

    public List<Transaccion> getTransacciones() { return transacciones; }
}

