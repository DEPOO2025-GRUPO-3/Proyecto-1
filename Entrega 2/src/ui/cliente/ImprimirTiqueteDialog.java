package ui.cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import infra.qr.QRException;
import infra.qr.TiqueteQRGenerator;
import tiquetes.Tiquete;

public class ImprimirTiqueteDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Tiquete tiquete;

    public ImprimirTiqueteDialog(java.awt.Frame owner, Tiquete tiquete) {
        super(owner, "Imprimir Tiquete", true);
        this.tiquete = tiquete;

        setSize(480, 320);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        add(crearPanelDatos(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelDatos() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));

        panel.add(new JLabel("TIQUETE ID: " + tiquete.getId()));

        if (tiquete.getEvento() != null) {
            panel.add(new JLabel("EVENTO: " + tiquete.getEvento().getNombre()));
            panel.add(new JLabel("FECHA EVENTO: " 
                    + tiquete.getEvento().getFecha() + " " + tiquete.getEvento().getHora()));
        }

        if (tiquete.getLocalidad() != null) {
            panel.add(new JLabel("LOCALIDAD: " + tiquete.getLocalidad().getNombre()));
        }

        if (tiquete.getComprador() != null) {
            panel.add(new JLabel("COMPRADOR: " + tiquete.getComprador().getLogin()));
        }

        String fechaImp = (tiquete.getFechaImpresion() == null)
                ? "AÚN NO IMPRESO"
                : tiquete.getFechaImpresion().toString();

        panel.add(new JLabel("FECHA IMPRESIÓN: " + fechaImp));

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnGenerar = new JButton("Generar QR");
        JButton btnCerrar = new JButton("Cerrar");

        btnGenerar.addActionListener(e -> generarQR());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnGenerar);
        panel.add(btnCerrar);

        return panel;
    }

    private void generarQR() {
        if (tiquete.getFechaImpresion() != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Este tiquete YA fue impreso.\nNo es posible reimprimirlo.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Path carpetaQR = Path.of("data", "qr");
            if (!Files.exists(carpetaQR)) {
                Files.createDirectories(carpetaQR);
            }

            Path destino = carpetaQR.resolve("tiquete_" + tiquete.getId() + ".png");

            TiqueteQRGenerator.generarQR(tiquete, destino);

            tiquete.registrarImpresion();

            JOptionPane.showMessageDialog(
                    this,
                    "QR generado exitosamente en:\n" + destino.toAbsolutePath(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (QRException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error generando QR:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error inesperado:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

