package ui;

import javax.swing.SwingUtilities;

import marketplace.Marketplace;
import persistencia.Database;
import persistencia.JsonStore;
import persistencia.MarketplaceStore;
import ui.admin.AdminFrame;
import ui.cliente.ClienteFrame;
import ui.login.LoginDialog;
import ui.organizador.OrganizadorFrame;
import usuarios.Usuario;

public class MainApp {

    private final JsonStore jsonStore;
    private final MarketplaceStore mpStore;
    private final Database db;
    private final Marketplace mp;

    public MainApp() {
        jsonStore = new JsonStore();
        mpStore = new MarketplaceStore();
        db = new Database();
        Database loaded = jsonStore.load();
        db.getUsuarios().addAll(loaded.getUsuarios());
        db.getVenues().addAll(loaded.getVenues());
        db.getEventos().addAll(loaded.getEventos());
        mp = new Marketplace();
        mpStore.load(db, mp);
        if (db.getUsuarios().isEmpty()) {
            db.addUsuario(new Usuario("admin", "admin", 0));
            db.addUsuario(new Usuario("organizador", "1234", 0));
            db.addUsuario(new Usuario("cliente", "1234", 500000));
            jsonStore.save(db);
        }
    }

    public void start() {
        LoginDialog dlg = new LoginDialog(null, db.getUsuarios());
        dlg.setVisible(true);
        if (!dlg.isAceptado()) {
            guardarDatos();
            System.exit(0);
            return;
        }
        Usuario usuario = dlg.getUsuarioSeleccionado();
        String rol = dlg.getRolSeleccionado();
        if (usuario == null || rol == null) {
            guardarDatos();
            System.exit(0);
            return;
        }
        switch (rol) {
            case "Administrador":
                abrirAdmin(usuario);
                break;
            case "Organizador":
                abrirOrganizador(usuario);
                break;
            case "Cliente":
                abrirCliente(usuario);
                break;
            default:
                guardarDatos();
                System.exit(0);
        }
    }

    private void abrirAdmin(Usuario usuario) {
        AdminFrame frame = new AdminFrame(db, mp, jsonStore, mpStore, usuario);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void abrirOrganizador(Usuario usuario) {
        OrganizadorFrame frame = new OrganizadorFrame(db, mp, jsonStore, mpStore, usuario);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void abrirCliente(Usuario usuario) {
        ClienteFrame frame = new ClienteFrame(db, mp, jsonStore, mpStore, usuario);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void guardarDatos() {
        jsonStore.save(db);
        mpStore.save(db, mp);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.start();
        });
    }
}

