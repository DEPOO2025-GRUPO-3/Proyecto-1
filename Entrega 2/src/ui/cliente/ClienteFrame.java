package ui.cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import marketplace.Marketplace;
import persistencia.Database;
import persistencia.JsonStore;
import persistencia.MarketplaceStore;
import usuarios.Usuario;

public class ClienteFrame extends JFrame {

    private final Database db;
    private final Marketplace mp;
    private final JsonStore jsonStore;
    private final MarketplaceStore mpStore;
    private final Usuario actual;

    private JLabel lblSaldo;
    private MisTiquetesPanel panelMisTiquetes;
    private ComprarTiquetePanel panelComprar;

    public ClienteFrame(Database db, Marketplace mp, JsonStore jsonStore, MarketplaceStore mpStore, Usuario actual) {
        super("Cliente - " + actual.getLogin());
        this.db = db;
        this.mp = mp;
        this.jsonStore = jsonStore;
        this.mpStore = mpStore;
        this.actual = actual;
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearTabs(), BorderLayout.CENTER);
        actualizarSaldo();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblSaldo = new JLabel();
        JButton btnRefrescar = new JButton("Refrescar saldo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnSalir = new JButton("Salir");
        btnRefrescar.addActionListener(e -> actualizarSaldo());
        btnGuardar.addActionListener(e -> guardar());
        btnSalir.addActionListener(e -> salir());
        panel.add(new JLabel("Saldo virtual: "));
        panel.add(lblSaldo);
        panel.add(btnRefrescar);
        panel.add(btnGuardar);
        panel.add(btnSalir);
        return panel;
    }

    private JTabbedPane crearTabs() {
        JTabbedPane tabs = new JTabbedPane();
        panelComprar = new ComprarTiquetePanel(db, actual, this);
        panelMisTiquetes = new MisTiquetesPanel(db, actual, this);
        tabs.addTab("Comprar tiquetes", panelComprar);
        tabs.addTab("Mis tiquetes", panelMisTiquetes);
        return tabs;
    }

    public void actualizarSaldo() {
        lblSaldo.setText(String.format("$ %.2f", actual.consultarSaldo()));
    }

    public void refrescarTiquetesCliente() {
        panelMisTiquetes.recargar();
    }

    private void guardar() {
        jsonStore.save(db);
        mpStore.save(db, mp);
    }

    private void salir() {
        guardar();
        dispose();
        System.exit(0);
    }
}

