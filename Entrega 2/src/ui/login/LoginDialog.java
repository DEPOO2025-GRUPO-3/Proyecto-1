package ui.login;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import usuarios.Usuario;

public class LoginDialog extends JDialog {

    private final List<Usuario> usuarios;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRol;

    private boolean aceptado;
    private Usuario usuarioSeleccionado;
    private String rolSeleccionado;

    public LoginDialog(java.awt.Frame owner, List<Usuario> usuarios) {
        super(owner, "Inicio de sesión", true);
        this.usuarios = usuarios;
        setSize(360, 220);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        add(crearPanelCampos(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelCampos() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        panel.add(new JLabel("Rol:"));
        comboRol = new JComboBox<>();
        comboRol.addItem("Administrador");
        comboRol.addItem("Organizador");
        comboRol.addItem("Cliente");
        panel.add(comboRol);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAceptar = new JButton("Ingresar");
        JButton btnCancelar = new JButton("Cancelar");
        btnAceptar.addActionListener(e -> intentarLogin());
        btnCancelar.addActionListener(e -> cancelar());
        panel.add(btnAceptar);
        panel.add(btnCancelar);
        return panel;
    }

    private void intentarLogin() {
        String login = txtUsuario.getText().trim();
        String pass = new String(txtPassword.getPassword());
        if (login.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Usuario encontrado = null;
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.getContrasena().equals(pass)) {
                encontrado = u;
                break;
            }
        }
        if (encontrado == null) {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        aceptado = true;
        usuarioSeleccionado = encontrado;
        rolSeleccionado = (String) comboRol.getSelectedItem();
        dispose();
    }

    private void cancelar() {
        aceptado = false;
        usuarioSeleccionado = null;
        rolSeleccionado = null;
        dispose();
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public String getRolSeleccionado() {
        return rolSeleccionado;
    }
}

