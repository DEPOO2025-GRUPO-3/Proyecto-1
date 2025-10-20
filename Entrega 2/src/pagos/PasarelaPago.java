package pagos;

import usuarios.Usuario;

public interface PasarelaPago {
    void abonarSaldo(Usuario usuario, double monto, String numeroCuenta);
    void transferir_a_banco(Usuario usuario, double monto, String numeroCuenta);
}

