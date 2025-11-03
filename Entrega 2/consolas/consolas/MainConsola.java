package consolas;

import java.util.Scanner;

import marketplace.Marketplace;
import persistencia.Database;
import persistencia.JsonStore;
import persistencia.MarketplaceStore;
import usuarios.Usuario;

public abstract class MainConsola {

    protected final JsonStore jsonStore = new JsonStore();
    protected final MarketplaceStore mpStore = new MarketplaceStore();
    protected final Database db = new Database();
    protected Marketplace mp;

    protected Usuario actual;

    private final Scanner in = new Scanner(System.in);


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
            String login = readNonEmpty("Usuario: ");
            String pass  = readNonEmpty("Contraseña: ");
            Usuario u = db.getUsuarios().stream()
                    .filter(x -> x.getLogin().equals(login) && x.getContrasena().equals(pass))
                    .findFirst()
                    .orElse(null);
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
                int op = readInt("Seleccione opción: ", 0, maxOpcion());
                salir = handleOption(op);
            }
        } finally {
            shutdown();
        }
    }

    protected abstract void renderMenu();
    protected abstract int maxOpcion();
    protected abstract boolean handleOption(int op);


    protected String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            if (s != null && !s.isBlank()) {
                return s.trim();
            }
            System.out.println("Entrada vacía. Intente de nuevo.");
        }
    }

    protected int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine();
            try {
                int v = Integer.parseInt(line.trim());
                if (v < min || v > max) {
                    System.out.println("Valor fuera de rango [" + min + "," + max + "].");
                } else {
                    return v;
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    protected double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine();
            try {
                double v = Double.parseDouble(line.trim());
                if (v < min || v > max) {
                    System.out.println("Valor fuera de rango [" + min + "," + max + "].");
                } else {
                    return v;
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número (use punto decimal).");
            }
        }
    }

    protected void pause() {
        System.out.print("Presione ENTER para continuar...");
        in.nextLine();
    }
}

