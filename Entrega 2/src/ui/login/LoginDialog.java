package ui.login;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import marketplace.Marketplace;
import persistencia.Database;
import persistencia.JsonStore;
import persistencia.MarketplaceStore;
import usuarios.Administrador;
import usuarios.Cliente;
import usuarios.Organizador;
import usuarios.Usuario;

public class LoginDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Database database;
    private final Marketplace marketplace;
    private final JsonStore jsonStore;
    private final MarketplaceStore marketplaceStore;

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> comboRol;

    public LoginDialog(JFrame owner,
                       Database database,
                       Marketplace marketplace,
                       JsonStore jsonStore,
                       MarketplaceStore marketplaceStore) {
        super(owner, "Boletamaster - Inicio de sesión", true);
        this.database = database;
        this.marketplace = marketplace;
        this.jsonStore = jsonStore;
        this.marketplaceStore = marketplaceStore;
        inicializarComponentes();
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }

    // ---------------------------------------------------------------------
    // Inicialización UI
    // ---------------------------------------------------------------------

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Usuario
        panelCampos.add(new JLabel("Usuario (login):"));
        txtUsuario = new JTextField(15);
        panelCampos.add(txtUsuario);

        // Contraseña
        panelCampos.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField(15);
        panelCampos.add(txtContrasena);

        // Rol
        panelCampos.add(new JLabel("Rol:"));
        comboRol = new JComboBox<>(new String[] { "Cliente", "Organizador", "Administrador" });
        panelCampos.add(comboRol);

        add(panelCampos, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnSalir = new JButton("Salir");

        btnIngresar.addActionListener(e -> intentarLogin());
        btnSalir.addActionListener(e -> cerrarAplicacion());

        panelBotones.add(btnIngresar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.SOUTH);
    }

    // ---------------------------------------------------------------------
    // Lógica de login
    // ---------------------------------------------------------------------

    private void intentarLogin() {
        String login = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
        char[] passChars = txtContrasena.getPassword();
        String pass = new String(passChars);

        if (login.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes ingresar usuario y contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario encontrado = buscarUsuarioPorLogin(login);

        if (encontrado == null || !pass.equals(encontrado.getContrasena())) {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña inválidos.",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String rolSeleccionado = (String) comboRol.getSelectedItem();

        if (!rolCoincideConUsuario(rolSeleccionado, encontrado)) {
            JOptionPane.showMessageDialog(this,
                    "El usuario no corresponde al rol seleccionado.",
                    "Rol incorrecto",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        abrirVentanaPrincipalSegunRol(rolSeleccionado, encontrado);
        dispose();
    }

    private Usuario buscarUsuarioPorLogin(String login) {
        for (Usuario u : database.getUsuarios()) {
            if (u.getLogin() != null && u.getLogin().equals(login)) {
                return u;
            }
        }
        return null;
    }

    private boolean rolCoincideConUsuario(String rolSeleccionado, Usuario u) {
        boolean coincide = false;

        if ("Administrador".equals(rolSeleccionado) && u instanceof Administrador) {
            coincide = true;
        } else if ("Organizador".equals(rolSeleccionado) && u instanceof Organizador) {
            coincide = true;
        } else if ("Cliente".equals(rolSeleccionado) && u instanceof Cliente) {
            coincide = true;
        }

        return coincide;
    }

    private void abrirVentanaPrincipalSegunRol(String rolSeleccionado, Usuario u) {
        Runnable tarea = null;

        if ("Administrador".equals(rolSeleccionado) && u instanceof Administrador) {
            Administrador admin = (Administrador) u;
            tarea = () -> {
                ui.admin.AdminFrame frame =
                        new ui.admin.AdminFrame(database, marketplace, jsonStore, marketplaceStore, admin);
                frame.setVisible(true);
            };
        } else if ("Organizador".equals(rolSeleccionado) && u instanceof Organizador) {
            Organizador org = (Organizador) u;
            tarea = () -> {
                ui.organizador.OrganizadorFrame frame =
                        new ui.organizador.OrganizadorFrame(database, marketplace, jsonStore, marketplaceStore, org);
                frame.setVisible(true);
            };
        } else if ("Cliente".equals(rolSeleccionado) && u instanceof Cliente) {
            Cliente cliente = (Cliente) u;
            tarea = () -> {
                ui.cliente.ClienteFrame frame =
                        new ui.cliente.ClienteFrame(database, marketplace, jsonStore, marketplaceStore, cliente);
                frame.setVisible(true);
            };
        }

        if (tarea != null) {
            SwingUtilities.invokeLater(tarea);
        }
    }

    // ---------------------------------------------------------------------
    // Cierre de la app
    // ---------------------------------------------------------------------

    private void cerrarAplicacion() {
        try {
            jsonStore.save(database);
            marketplaceStore.save(database, marketplace);
        } catch (Exception e) {
            // En una app real se podría loguear, aquí solo mostramos mensaje.
            JOptionPane.showMessageDialog(this,
                    "Error guardando datos antes de salir:\n" + e.getMessage(),
                    "Persistencia",
                    JOptionPane.ERROR_MESSAGE);
        }
        dispose();
        System.exit(0);
    }
}

