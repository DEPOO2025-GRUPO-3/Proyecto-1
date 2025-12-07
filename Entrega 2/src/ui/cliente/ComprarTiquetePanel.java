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
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class ComprarTiquetePanel extends JPanel {

    private final Database db;
    private final Usuario actual;
    private final ClienteFrame parent;

    private JTable tablaEventos;
    private JTable tablaLocalidades;

    private EventosTableModel modeloEventos = new EventosTableModel();
    private LocalidadesTableModel modeloLocalidades = new LocalidadesTableModel();

    public ComprarTiquetePanel(Database db, Usuario actual, ClienteFrame parent) {
        this.db = db;
        this.actual = actual;
        this.parent = parent;

        setLayout(new BorderLayout());

        tablaEventos = new JTable(modeloEventos);
        tablaLocalidades = new JTable(modeloLocalidades);

        add(new JScrollPane(tablaEventos), BorderLayout.WEST);
        add(new JScrollPane(tablaLocalidades), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        recargarEventos();
    }

    private JPanel crearPanelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCargarLocalidades = new JButton("Ver localidades");
        JButton btnComprar = new JButton("Comprar");
        JButton btnRefrescar = new JButton("Refrescar");

        btnCargarLocalidades.addActionListener(e -> cargarLocalidades());
        btnComprar.addActionListener(e -> comprar());
        btnRefrescar.addActionListener(e -> recargarEventos());

        p.add(new JLabel("Seleccione evento → localidad → comprar"));
        p.add(btnRefrescar);
        p.add(btnCargarLocalidades);
        p.add(btnComprar);

        return p;
    }

    private void recargarEventos() {
        modeloEventos.setEventos(db.getEventos());
        modeloLocalidades.setLocalidades(new ArrayList<>());
    }

    private void cargarLocalidades() {
        int row = tablaEventos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Evento e = modeloEventos.getEvento(row);
        modeloLocalidades.setLocalidades(e.getLocalidades());
    }

    private void comprar() {
        int rowEvt = tablaEventos.getSelectedRow();
        int rowLoc = tablaLocalidades.getSelectedRow();

        if (rowEvt < 0 || rowLoc < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento y una localidad.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Evento e = modeloEventos.getEvento(rowEvt);
        Localidad l = modeloLocalidades.getLocalidad(rowLoc);

        List<TiqueteSimple> disponibles = new ArrayList<>();
        for (var t : l.getTiquetes()) {
            if (t instanceof TiqueteSimple ts && ts.getComprador() == null) {
                disponibles.add(ts);
            }
        }

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tiquetes disponibles en esta localidad.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        TiqueteSimple elegido = disponibles.get(0);
        double precio = elegido.getPrecioPublico();

        if (actual.consultarSaldo() < precio) {
            JOptionPane.showMessageDialog(this,
                    "Saldo insuficiente.\nPrecio: " + precio + "\nSaldo: " + actual.consultarSaldo(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            actual.debitarSaldo(precio);
            elegido.transferir(actual);
            JOptionPane.showMessageDialog(this, "Compra exitosa. ID: " + elegido.getId());
            parent.actualizarSaldo();
            cargarLocalidades();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODELOS
    private static class EventosTableModel extends AbstractTableModel {

        private List<Evento> eventos = new ArrayList<>();
        private final String[] columns = { "ID", "Nombre", "Fecha", "Hora", "Tipo" };

        public void setEventos(List<Evento> eventos) {
            this.eventos = new ArrayList<>(eventos);
            fireTableDataChanged();
        }

        public Evento getEvento(int row) {
            return eventos.get(row);
        }

        @Override
        public int getRowCount() {
            return eventos.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Evento e = eventos.get(row);

            return switch (col) {
                case 0 -> e.getId();
                case 1 -> e.getNombre();
                case 2 -> e.getFecha();
                case 3 -> e.getHora();
                case 4 -> e.getTipo().toString();
                default -> "";
            };
        }
    }

    private static class LocalidadesTableModel extends AbstractTableModel {

        private List<Localidad> localidades = new ArrayList<>();
        private final String[] columns = { "Localidad", "Precio", "Disponibles" };

        public void setLocalidades(List<Localidad> locs) {
            this.localidades = new ArrayList<>(locs);
            fireTableDataChanged();
        }

        public Localidad getLocalidad(int row) {
            return localidades.get(row);
        }

        @Override
        public int getRowCount() {
            return localidades.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Localidad l = localidades.get(row);

            return switch (col) {
                case 0 -> l.getNombre();
                case 1 -> l.getPrecioBase();
                case 2 -> l.getTiquetes().stream().filter(t -> t.getComprador() == null).count();
                default -> "";
            };
        }
    }
}

