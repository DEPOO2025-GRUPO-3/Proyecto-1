package infra.qr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import tiquetes.Tiquete;

public class TiqueteQRGenerator {

    private static final int WIDTH = 350;
    private static final int HEIGHT = 350;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        sb.append("EVENTO: ").append(t.getEvento().getNombre()).append("\n");
        sb.append("FECHA EVENTO: ").append(t.getEvento().getFecha().format(FMT)).append("\n");
        sb.append("LOCALIDAD: ").append(t.getLocalidad().getNombre()).append("\n");
        sb.append("COMPRADOR: ").append(t.getComprador().getLogin()).append("\n");
        sb.append("FECHA IMPRESIÓN: ").append(t.getFechaImpresion() == null
                ? "SIN IMPRIMIR"
                : t.getFechaImpresion().format(FMT));
        return sb.toString();
    }
}

