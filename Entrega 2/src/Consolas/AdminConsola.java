package Consolas;

public class AdminConsola extends MainConsola {

    public static void main(String[] args) {
        new AdminConsola().runLoop();
    }

    @Override
    protected void afterBoot() {
        if (db.getUsuarios().isEmpty()) {
            db.addUsuario(new usuarios.Usuario("admin", "admin", 0));
        }
    }

    @Override
    protected void renderMenu() {
        System.out.println("\n=== ADMIN ===");
        System.out.println("1. Crear usuario");
        System.out.println("2. Listar usuarios");
        System.out.println("3. Guardar ahora");
        System.out.println("0. Salir");
    }

    @Override
    protected int maxOpcion() { return 3; }

    @Override
    protected boolean handleOption(int op) {
        switch (op) {
            case 1 -> crearUsuario();
            case 2 -> listarUsuarios();
            case 3 -> guardarAhora();
            case 0 -> { return true; }
        }
        return false;
    }

    private void crearUsuario() {
        String login = ConsolaUtil.readNonEmpty("Login: ");
        String pass  = ConsolaUtil.readNonEmpty("Contraseña: ");
        double saldo = ConsolaUtil.readDouble("Saldo inicial: ", 0, 1_000_000_000);
        db.addUsuario(new usuarios.Usuario(login, pass, saldo));
        System.out.println("Usuario creado.");
        ConsolaUtil.pause();
    }

    private void listarUsuarios() {
        if (db.getUsuarios().isEmpty()) { System.out.println("No hay usuarios."); return; }
        db.getUsuarios().forEach(u ->
                System.out.printf("- %s | saldo: %.2f\n", u.getLogin(), u.consultarSaldo()));
        ConsolaUtil.pause();
    }

    private void guardarAhora() {
        jsonStore.save(db);
        mpStore.save(db, mp);
        System.out.println("Guardado manual completo.");
        ConsolaUtil.pause();
    }
}
