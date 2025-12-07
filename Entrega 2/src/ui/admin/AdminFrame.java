package ui.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import administracion.Administracion;
import eventos.Evento;
import eventos.EstadoEvento;
import eventos.Venue;
import marketplace.Marketplace;
import persistencia.Database;
import ui.login.LoginDialog;
import usuarios.Administrador;

/**
 * Ventana principal para el rol Administrador.
 *
 * Desde aquí el administrador puede:
 *  - Ver y aprobar venues.
 *  - Ver eventos programados y cancelarlos.
 *  - Consultar el reporte global de ingresos.
 *
 * Esta clase solo usa la lógica que ya existe en el proyecto:
 *  - {@link Administrador}
 *  - {@link Database}
 *  - {@link Administracion} (para orquestar si es necesario)
 */
public class AdminFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Referencia opcional a una clase coordinadora (si la usas). */
    private final Administracion administracion;

    /** Administrador logueado. */
    private final Administrador administrador;

    /** Base de datos en memoria. */
    private final Database database;

    /** Marketplace (por si luego se quiere mostrar algo asociado). */
    private final Marketplace marketplace;

    // --- Componentes de UI ---

    // Venues
    private final DefaultListModel<Venue> modeloVenuesPendientes = new DefaultListModel<>();
    private final DefaultListModel<Venue> modeloVenuesAprobados = new DefaultListModel<>();
    private JList<Venue> listaVenuesPendientes;
    private JList<Venue> listaVenuesAprobados;

    // Eventos
    private final DefaultListModel<Evento> modeloEventosProgramados = new DefaultListModel<>();
    private JList<Evento> listaEventosProgramados;
    private JLabel lblTotalIngresos;

    /**
     * Constructor principal.
     *
     * @param administracion objeto de coordinación general (puede ser null si no lo usas).
     * @param administrador  administrador autenticado.
     * @param database       base de datos con usuarios, venues y eventos.
     * @param marketplace    marketplace de reventa (por ahora solo se mantiene la referencia).
     */
    public AdminFrame(Administracion administracion,
                      Administrador administrador,
                      Database database,
                      Marketplace marketplace) {

        this.administracion = administracion;
        this.administrador = administrador;
        this.database = database;
        this.marketplace = marketplace;

        setTitle("BoletaMaster - Administrador (" + administrador.getLogin() + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        construirInterfaz();
        cargarDatos();

        pack();
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
    }

    /**
     * Constructor de conveniencia si no estás usando aún la clase Administracion
     * como coordinadora.
     */
    public AdminFrame(Administrador administrador,
                      Database database,
                      Marketplace marketplace) {
        this(null, administrador, database, marketplace);
    }

    // ---------------------------------------------------------------------
    // Construcción de interfaz
    // ---------------------------------------------------------------------

    private void construirInterfaz() {
        getContentPane().setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Venues", crearPanelVenues());
        pestañas.addTab("Eventos", crearPanelEventos());

        getContentPane().add(pestañas, BorderLayout.CENTER);
        getContentPane().add(crearBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelVenues() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Listas
        listaVenuesPendientes = new JList<>(modeloVenuesPendientes);
        listaVenuesPendientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaVenuesPendientes.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel(value.getNombre() + " (" + value.getUbicacion() + ")");
            if (isSelected) {
                l.setOpaque(true);
                l.setBackground(list.getSelectionBackground());
                l.setForeground(list.getSelectionForeground());
            }
            return l;
        });

        listaVenuesAprobados = new JList<>(modeloVenuesAprobados);
        listaVenuesAprobados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaVenuesAprobados.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel l = new JLabel(value.getNombre() + " (" + value.getUbicacion() + ")");
            if (isSelected) {
                l.setOpaque(true);
                l.setBackground(list.getSelectionBackground());
                l.setForeground(list.getSelectionForeground());
            }
            return l;
        });

        JPanel panelListas = new JPanel(new BorderLayout(10, 10));

        JPanel panelPendientes = new JPanel(new BorderLayout());
        panelPendientes.setBorder(BorderFactory.createTitledBorder("Venues pendientes de aprobación"));
        panelPendientes.add(new JScrollPane(listaVenuesPendientes), BorderLayout.CENTER);

        JButton btnAprobar = new JButton("Aprobar venue seleccionado");
        btnAprobar.addActionListener(this::aprobarVenueSeleccionado);
        panelPendientes.add(btnAprobar, BorderLayout.SOUTH);

        JPanel panelAprobados = new JPanel(new BorderLayout());
        panelAprobados.setBorder(BorderFactory.createTitledBorder("Venues aprobados"));
        panelAprobados.add(new JScrollPane(listaVenuesAprobados), BorderLayout.CENTER);

        panelListas.add(panelPendientes, BorderLayout.WEST);
        panelListas.add(panelAprobados, BorderLayout.CENTER);

        panel.add(panelListas, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listaEventosProgramados = new JList<>(modeloEventosProgramados);
        listaEventosProgramados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEventosProgramados.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            String texto = value.getNombre() + " - " + value.getFecha() + " " + value.getHora();
            JLabel l = new JLabel(texto);
            if (isSelected) {
                l.setOpaque(true);
                l.setBackground(list.getSelectionBackground());
                l.setForeground(list.getSelectionForeground());
            }
            return l;
        });

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder("Eventos programados"));
        panelCentro.add(new JScrollPane(listaEventosProgramados), BorderLayout.CENTER);

        JButton btnCancelar = new JButton("Cancelar evento seleccionado");
        btnCancelar.addActionListener(this::cancelarEventoSeleccionado);

        JButton btnReporte = new JButton("Calcular ingresos globales");
        btnReporte.addActionListener(this::calcularIngresosGlobales);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnReporte);

        lblTotalIngresos = new JLabel("Ingresos globales: $0.0");

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(lblTotalIngresos, BorderLayout.WEST);
        panelSur.add(panelBotones, BorderLayout.EAST);

        panel.add(panelCentro, BorderLayout.CENTER);
        panel.add(panelSur, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearBarraInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> salirAplicacion());

        panel.add(btnCerrarSesion);
        panel.add(btnSalir);

        return panel;
    }

    // ---------------------------------------------------------------------
    // Carga de datos
    // ---------------------------------------------------------------------

    private void cargarDatos() {
        modeloVenuesPendientes.clear();
        modeloVenuesAprobados.clear();
        modeloEventosProgramados.clear();

        if (database != null) {
            List<Venue> venues = database.getVenues();
            for (Venue v : venues) {
                if (v.isAprobado()) {
                    modeloVenuesAprobados.addElement(v);
                } else {
                    modeloVenuesPendientes.addElement(v);
                }
            }

            List<Evento> eventos = database.getEventos();
            for (Evento e : eventos) {
                if (e.getEstado() == EstadoEvento.programado) {
                    modeloEventosProgramados.addElement(e);
                }
            }
        }
    }

    // ---------------------------------------------------------------------
    // Acciones
    // ---------------------------------------------------------------------

    private void aprobarVenueSeleccionado(ActionEvent e) {
        Venue seleccionado = listaVenuesPendientes.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un venue pendiente para aprobar.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        administrador.aprobarVenue(seleccionado);
        cargarDatos();
    }

    private void cancelarEventoSeleccionado(ActionEvent e) {
        Evento seleccionado = listaEventosProgramados.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un evento para cancelar.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resp = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cancelar el evento \"" + seleccionado.getNombre() + "\"?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            administrador.cancelarEvento(seleccionado);
            cargarDatos();
        }
    }

    private void calcularIngresosGlobales(ActionEvent e) {
        if (database == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay base de datos cargada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = administrador.generarReporteGlobal(database.getEventos());
        lblTotalIngresos.setText(String.format("Ingresos globales: $%,.2f", total));
    }

    private void cerrarSesion() {
        // Cierra esta ventana y abre de nuevo el Login si quieres.
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginDialog dialogo = new LoginDialog(null, null, database, marketplace);
            dialogo.setVisible(true);
        });
    }

    private void salirAplicacion() {
        dispose();
        System.exit(0);
    }
}

