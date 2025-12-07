package ui.cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import eventos.Evento;
import eventos.Localidad;
import persistencia.Database;
import tiquetes.Tiquete;
import usuarios.Usuario;

public class MisTiquetesPanel extends JPanel {

    private final Database db;
    private final Usuario actual;
    private final ClienteFrame parent;

    private JTable tabla;
    private TiquetesTableModel modelo;

    public MisTiquetesPanel(Database db, Usuario actual, ClienteFrame parent) {
        this.db = db;
        this.actual = actual;
        this.parent = parent;

        setLayout(new BorderLayout());

        modelo = new TiquetesTableModel();
        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);

        recargar();
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnImprimir = new JButton("Imprimir tiquete");

        panel.add(new JLabel("Seleccione un tiquete para imprimir"));
        panel.add(btnRefrescar);
        panel.add(btnImprimir);

        btnRefrescar.addActionListener(e -> recargar());
        btnImprimir.addActionListener(e -> imprimirSeleccionado());

        return panel;
    }

    public void recargar() {
        List<Tiquete> tiquetes = new ArrayList<>();

        for (Evento e : db.getEventos()) {
            for (Localidad l : e.getLocalidades()) {
                for (Tiquete t : l.getTiquetes()) {
                    if (t.getComprador() != null && t.getComprador().equals(actual)) {
                        tiquetes.add(t);
                    }
                }
            }
        }

        modelo.setTiquetes(tiquetes);
    }

    private void imprimirSeleccionado() {
        int row = tabla.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un tiquete.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Tiquete t = modelo.getTiquete(row);

        ImprimirTiqueteDialog dlg = new ImprimirTiqueteDialog(null, t);
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);

        recargar();
        parent.actualizarSaldo();
    }

    private static class TiquetesTableModel extends AbstractTableModel {

        private final String[] columnas = {
                "ID", "Evento", "Localidad", "Estado", "Fecha impresión"
        };

        private List<Tiquete> datos = new ArrayList<>();

        public void setTiquetes(List<Tiquete> tiquetes) {
            this.datos = new ArrayList<>(tiquetes);
            fireTableDataChanged();
        }

        public Tiquete getTiquete(int row) {
            return datos.get(row);
        }

        @Override
        public int getRowCount() {
            return datos.size();
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Tiquete t = datos.get(rowIndex);

            return switch (columnIndex) {
                case 0 -> t.getId();
                case 1 -> t.getEvento() == null ? "" : t.getEvento().getNombre();
                case 2 -> t.getLocalidad() == null ? "" : t.getLocalidad().getNombre();
                case 3 -> t.getEstado();
                case 4 -> t.getFechaImpresion() == null ? "" : t.getFechaImpresion().toString();
                default -> "";
            };
        }
    }
}

