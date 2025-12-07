package infra.qr;

public class QRException extends Exception {

    private static final long serialVersionUID = 1L;

    public QRException(String mensaje) {
        super(mensaje);
    }

    public QRException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

