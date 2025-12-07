package ui.organizador;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import eventos.Evento;
import eventos.TipoEvento;
import eventos.Venue;
import persistencia.Database;
import usuarios.Organizador;

public class OrganizadorFrame extends JFrame {

    private final Database db;
    private final Organizador organizador;

    private JTextField txtIdVenue;
    private JTextField txtNombreVenue;
    private JTextField txtUbicacionVenue;
    private JTextField txtCapacidadVenue;
    private JTextField txtRestriccionesVenue;

    private JTextField txtIdEvento;
    private JTextField txtNombreEvento;
    private JTextField txtFechaEvento;
    private JTextField txtHoraEvento;
    private JComboBox<TipoEvento> cbTipoEvento;
    private JComboBox<Venue> cbVenues;

    private DefaultListModel<Venue> venuesModel;
    private JList<Venue> lstVenues;

    private DefaultListModel<Evento> eventosModel;
    private JList<Evento> lstEventos;

    public OrganizadorFrame(Database db, Organizador organizador) {
        super("BoletaMaster - Organizador");
        this.db = db;
        this.organizador = organizador;
        setLayout(new BorderLayout());
        construirUI();
        cargarDatos();
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void construirUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        txtIdVenue = new JTextField(10);
        txtNombreVenue = new JTextField(15);
        txtUbicacionVenue = new JTextField(15);
        txtCapacidadVenue = new JTextField(10);
        txtRestriccionesVenue = new JTextField(15);

        txtIdEvento = new JTextField(10);
        txtNombreEvento = new JTextField(15);
        txtFechaEvento = new JTextField(10);
        txtHoraEvento = new JTextField(10);
        cbTipoEvento = new JComboBox<>(TipoEvento.values());
        cbVenues = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("ID Venue"), gbc);
        gbc.gridx = 1; panel.add(txtIdVenue, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Nombre Venue"), gbc);
        gbc.gridx = 1; panel.add(txtNombreVenue, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Ubicación"), gbc);
        gbc.gridx = 1; panel.add(txtUbicacionVenue, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Capacidad"), gbc);
        gbc.gridx = 1; panel.add(txtCapacidadVenue, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Restricciones"), gbc);
        gbc.gridx = 1; panel.add(txtRestriccionesVenue, gbc); y++;

        JButton btnCrearVenue = new JButton("Crear Venue");
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        panel.add(btnCrearVenue, gbc);
        y++;

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("ID Evento"), gbc);
        gbc.gridx = 1; panel.add(txtIdEvento, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Nombre Evento"), gbc);
        gbc.gridx = 1; panel.add(txtNombreEvento, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Fecha (YYYY-MM-DD)"), gbc);
        gbc.gridx = 1; panel.add(txtFechaEvento, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Hora (HH:MM)"), gbc);
        gbc.gridx = 1; panel.add(txtHoraEvento, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Tipo Evento"), gbc);
        gbc.gridx = 1; panel.add(cbTipoEvento, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Venue"), gbc);
        gbc.gridx = 1; panel.add(cbVenues, gbc); y++;

        JButton btnCrearEvento = new JButton("Crear Evento");
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        panel.add(btnCrearEvento, gbc);
        y++;

        gbc.gridwidth = 1;

        venuesModel = new DefaultListModel<>();
        lstVenues = new JList<>(venuesModel);
        lstVenues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        eventosModel = new DefaultListModel<>();
        lstEventos = new JList<>(eventosModel);
        lstEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel listas = new JPanel(new GridBagLayout());
        GridBagConstraints l = new GridBagConstraints();
        l.insets = new Insets(4, 4, 4, 4);
        l.fill = GridBagConstraints.BOTH;
        l.weightx = 1;
        l.weighty = 1;

        l.gridx = 0; l.gridy = 0;
        listas.add(new JScrollPane(lstVenues), l);
        l.gridx = 1;
        listas.add(new JScrollPane(lstEventos), l);

        JButton btnAprobarVenue = new JButton("Aprobar Venue");
        JButton btnIngresos = new JButton("Ver Ingresos Evento");

        JPanel acciones = new JPanel();
        acciones.add(btnAprobarVenue);
        acciones.add(btnIngresos);

        add(panel, BorderLayout.NORTH);
        add(listas, BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);

        btnCrearVenue.addActionListener(e -> crearVenue());
        btnCrearEvento.addActionListener(e -> crearEvento());
        btnAprobarVenue.addActionListener(e -> aprobarVenue());
        btnIngresos.addActionListener(e -> ingresosEvento());
    }

    private void cargarDatos() {
        venuesModel.clear();
        for (Venue v : db.getVenues()) {
            venuesModel.addElement(v);
            cbVenues.addItem(v);
        }
        eventosModel.clear();
        for (Evento e : db.getEventos()) {
            eventosModel.addElement(e);
        }
    }

    private void crearVenue() {
        try {
            String id = txtIdVenue.getText().trim();
            String nombre = txtNombreVenue.getText().trim();
            String ubic = txtUbicacionVenue.getText().trim();
            int capacidad = Integer.parseInt(txtCapacidadVenue.getText().trim());
            String restr = txtRestriccionesVenue.getText().trim();
            Venue v = new Venue(id, nombre, ubic, capacidad, restr);
            db.addVenue(v);
            venuesModel.addElement(v);
            cbVenues.addItem(v);
            JOptionPane.showMessageDialog(this, "Venue creado");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 0);
        }
    }

    private void aprobarVenue() {
        Venue v = lstVenues.getSelectedValue();
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un venue");
            return;
        }
        v.aprobar();
        JOptionPane.showMessageDialog(this, "Venue aprobado");
    }

    private void crearEvento() {
        try {
            String id = txtIdEvento.getText().trim();
            String nombre = txtNombreEvento.getText().trim();
            LocalDate fecha = LocalDate.parse(txtFechaEvento.getText().trim());
            LocalTime hora = LocalTime.parse(txtHoraEvento.getText().trim());
            TipoEvento tipo = (TipoEvento) cbTipoEvento.getSelectedItem();
            Venue venue = (Venue) cbVenues.getSelectedItem();
            Evento e = new Evento(id, nombre, fecha, hora, tipo, venue);
            db.addEvento(e);
            eventosModel.addElement(e);
            JOptionPane.showMessageDialog(this, "Evento creado");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", 0);
        }
    }

    private void ingresosEvento() {
        Evento e = lstEventos.getSelectedValue();
        if (e == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento");
            return;
        }
        double total = organizador.consultarIngresosNetos(e);
        JOptionPane.showMessageDialog(this, "Ingresos netos: $" + total);
    }
}

