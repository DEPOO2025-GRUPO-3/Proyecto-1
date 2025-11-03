package Consolas;

import persistencia.Database;
import persistencia.JsonStore;
import persistencia.MarketplaceStore;
import usuarios.Usuario;
import marketplace.Marketplace;

public abstract class MainConsola {

    protected final JsonStore jsonStore = new JsonStore();
    protected final MarketplaceStore mpStore = new MarketplaceStore();
    protected final Database db = new Database();
    protected Marketplace mp;

    protected Usuario actual;

    protected void boot() {
        Database loaded = jsonStore.load();
        db.getUsuarios().addAll(loaded.getUsuarios());
        db.getVenues().addAll(loaded.getVenues());
        db.getEventos().addAll(loaded.getEventos());
        mp = new Marketplace();
        mpStore.load(db, mp);

        afterBoot();
    }

    protected abstract void afterBoot();

    protected void shutdown() {
        jsonStore.save(db);
        mpStore.save(db, mp);
        System.out.println("Datos guardados. Hasta pronto.");
    }

    protected void login() {
        System.out.println("== Autenticación ==");
        for (int i = 0; i < 3; i++) {
            String login = ConsolaUtil.readNonEmpty("Usuario: ");
            String pass  = ConsolaUtil.readNonEmpty("Contraseña: ");
            Usuario u = db.getUsuarios().stream()
                    .filter(x -> x.getLogin().equals(login) && x.getContrasena().equals(pass))
                    .findFirst().orElse(null);
            if (u == null) {
                System.out.println("Credenciales inválidas.");
            } else {
                actual = u;
                System.out.println("Bienvenido, " + u.getLogin());
                return;
            }
        }
        System.out.println("Demasiados intentos fallidos.");
        System.exit(1);
    }

    public final void runLoop() {
        boot();
        try {
            login();
            boolean salir = false;
            while (!salir) {
                renderMenu();
                int op = ConsolaUtil.readInt("Seleccione opción: ", 0, maxOpcion());
                salir = handleOption(op);
            }
        } finally {
            shutdown();
        }
    }

    protected abstract void renderMenu();
    protected abstract int maxOpcion();
    protected abstract boolean handleOption(int op);
}
