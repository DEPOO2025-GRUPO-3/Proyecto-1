package ui.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import marketplace.Marketplace;
import persistencia.Database;
import persistencia.JsonStore;
import persistencia.MarketplaceStore;
import usuarios.Usuario;

public class AdminFrame extends JFrame {

    private final Database db;
    private final Marketplace mp;
    private final JsonStore jsonStore;
    private final MarketplaceStore mpStore;
    private final Usuario actual;

    private JTextArea areaUsuarios;

    public AdminFrame(Database db, Marketplace mp, JsonStore jsonStore, MarketplaceStore mpStore, Usuario actual) {
        super("Administrador - " + actual.getLogin());
        this.db = db;
        this.mp = mp;
        this.jsonStore = jsonStore;
        this.mpStore = mpStore;
        this.actual = actual;
        setSize(640, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(crearPanelBotones(), BorderLayout.NORTH);
        add(crearPanelListado(), BorderLayout.CENTER);
        refrescarUsuarios();
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrear = new JButton("Crear usuario");
        JButton btnListar = new JButton("Listar usuarios");
        JButton btnGuardar = new JButton("Guardar ahora");
        JButton btnSalir = new JButton("Salir");
        btnCrear.addActionListener(e -> crearUsuario());
        btnListar.addActionListener(e -> refrescarUsuarios());
        btnGuardar.addActionListener(e -> guardar());
        btnSalir.addActionListener(e -> salir());
        panel.add(btnCrear);
        panel.add(btnListar);
        panel.add(btnGuardar);
        panel.add(btnSalir);
        return panel;
    }

    private JScrollPane crearPanelListado() {
        areaUsuarios = new JTextArea();
        areaUsuarios.setEditable(false);
        return new JScrollPane(areaUsuarios);
    }

    private void crearUsuario() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 4, 4));
        JTextField txtLogin = new JTextField();
        JTextField txtPass = new JTextField();
        JTextField txtSaldo = new JTextField();
        panel.add(new JLabel("Login:"));
        panel.add(txtLogin);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPass);
        panel.add(new JLabel("Saldo inicial:"));
        panel.add(txtSaldo);
        int op = JOptionPane.showConfirmDialog(this, panel, "Crear usuario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op != JOptionPane.OK_OPTION) {
            return;
        }
        String login = txtLogin.getText().trim();
        String pass = txtPass.getText().trim();
        String sSaldo = txtSaldo.getText().trim();
        if (login.isEmpty() || pass.isEmpty() || sSaldo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            double saldo = Double.parseDouble(sSaldo);
            if (saldo < 0) {
                JOptionPane.showMessageDialog(this, "El saldo no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            db.addUsuario(new Usuario(login, pass, saldo));
            JOptionPane.showMessageDialog(this, "Usuario creado.");
            refrescarUsuarios();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Saldo inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarUsuarios() {
        StringBuilder sb = new StringBuilder();
        if (db.getUsuarios().isEmpty()) {
            sb.append("No hay usuarios.\n");
        } else {
            for (Usuario u : db.getUsuarios()) {
                sb.append("- ").append(u.getLogin()).append(" | saldo: ").append(String.format("%.2f", u.consultarSaldo())).append("\n");
            }
        }
        areaUsuarios.setText(sb.toString());
    }

    private void guardar() {
        jsonStore.save(db);
        mpStore.save(db, mp);
        JOptionPane.showMessageDialog(this, "Guardado completo.");
    }

    private void salir() {
        guardar();
        dispose();
        System.exit(0);
    }
}

