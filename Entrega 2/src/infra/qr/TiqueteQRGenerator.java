package infra.qr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import tiquetes.Tiquete;

public final class TiqueteQRGenerator {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private TiqueteQRGenerator() {
    }

    public static void generarQR(Tiquete tiquete, Path destino) throws QRException {
        try {
            if (destino.getParent() != null && !Files.exists(destino.getParent())) {
                Files.createDirectories(destino.getParent());
            }

            String contenido = construirContenidoQR(tiquete);

            BitMatrix matrix = new MultiFormatWriter().encode(
                    contenido,
                    BarcodeFormat.QR_CODE,
                    WIDTH,
                    HEIGHT
            );

            MatrixToImageWriter.writeToPath(matrix, "PNG", destino);

        } catch (WriterException | IOException e) {
            throw new QRException("Error generando el QR del tiquete: " + e.getMessage(), e);
        }
    }

    private static String construirContenidoQR(Tiquete t) {
        StringBuilder sb = new StringBuilder();

        sb.append("TIQUETE ID: ").append(t.getId()).append("\n");

        if (t.getEvento() != null) {
            sb.append("EVENTO: ").append(t.getEvento().getNombre()).append("\n");
            if (t.getEvento().getFecha() != null) {
                sb.append("FECHA EVENTO: ").append(t.getEvento().getFecha());
                if (t.getEvento().getHora() != null) {
                    sb.append(" ").append(t.getEvento().getHora());
                }
                sb.append("\n");
            }
        } else {
            sb.append("EVENTO: SIN ASOCIAR\n");
        }

        if (t.getLocalidad() != null) {
            sb.append("LOCALIDAD: ").append(t.getLocalidad().getNombre()).append("\n");
        } else {
            sb.append("LOCALIDAD: SIN ASOCIAR\n");
        }

        if (t.getComprador() != null) {
            sb.append("COMPRADOR: ").append(t.getComprador().getLogin()).append("\n");
        } else {
            sb.append("COMPRADOR: SIN COMPRADOR\n");
        }

        sb.append("FECHA IMPRESION: ");
        if (t.getFechaImpresion() != null) {
            sb.append(t.getFechaImpresion().format(FMT));
        } else {
            sb.append("SIN IMPRIMIR");
        }
        sb.append("\n");

        return sb.toString();
    }
}

