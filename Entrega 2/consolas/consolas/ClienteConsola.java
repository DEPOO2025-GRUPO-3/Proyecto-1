package consolas;

import eventos.Evento;
import eventos.Localidad;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class ClienteConsola extends MainConsola {

    public static void main(String[] args) {
        new ClienteConsola().runLoop();
    }

    @Override
    protected void afterBoot() {
        if (db.getUsuarios().isEmpty()) {
            db.addUsuario(new Usuario("cliente", "1234", 500_000));
        }
    }

    @Override
    protected void renderMenu() {
        System.out.println("\n=== CLIENTE ===");
        System.out.println("1. Listar eventos");
        System.out.println("2. Comprar tiquete simple");
        System.out.println("3. Ver saldo");
        System.out.println("0. Salir");
    }

    @Override
    protected int maxOpcion() {
        return 3;
    }

    @Override
    protected boolean handleOption(int op) {
        switch (op) {
            case 1 -> listarEventos();
            case 2 -> comprarTiqueteSimple();
            case 3 -> verSaldo();
            case 0 -> { return true; }
        }
        return false;
    }

    private void listarEventos() {
        if (db.getEventos().isEmpty()) {
            System.out.println("No hay eventos cargados.");
            return;
        }
        db.getEventos().forEach(e -> {
            System.out.printf("- %s | %s %s | %s | %s | venue=%s%n",
                    e.getId(), e.getFecha(), e.getHora(), e.getTipo(), e.getNombre(),
                    e.getVenue() == null ? "N/A" : e.getVenue().getNombre());
        });
        pause();
    }

    private void comprarTiqueteSimple() {
        if (db.getEventos().isEmpty()) {
            System.out.println("No hay eventos.");
            return;
        }
        String idEvt = readNonEmpty("ID evento: ");
        Evento evt = db.getEventos().stream()
                .filter(e -> e.getId().equals(idEvt))
                .findFirst()
                .orElse(null);

        if (evt == null) {
            System.out.println("Evento no encontrado.");
            return;
        }
        if (evt.getLocalidades().isEmpty()) {
            System.out.println("Evento sin localidades.");
            return;
        }

        for (int i = 0; i < evt.getLocalidades().size(); i++) {
            Localidad l = evt.getLocalidades().get(i);
            System.out.printf("%d) %s - $%.2f (tiquetes=%d)%n",
                    i + 1, l.getNombre(), l.getPrecioBase(), l.getTiquetes().size());
        }
        int idx = readInt("Seleccione localidad: ", 1, evt.getLocalidades().size()) - 1;
        Localidad loc = evt.getLocalidades().get(idx);

        TiqueteSimple cand = loc.getTiquetes().stream()
                .filter(t -> t instanceof TiqueteSimple)
                .map(t -> (TiqueteSimple) t)
                .findFirst()
                .orElse(null);

        if (cand == null) {
            System.out.println("No hay TiqueteSimple disponible en esa localidad.");
            return;
        }

        double precio = cand.getPrecioPublico();
        if (actual.consultarSaldo() < precio) {
            System.out.printf("Saldo insuficiente. Necesita $%.2f y tiene $%.2f%n",
                    precio, actual.consultarSaldo());
            return;
        }

        try {
            actual.debitarSaldo(precio);
            cand.transferir(actual);
            System.out.println("Compra exitosa. Tiquete: " + cand.getId());
        } catch (Exception ex) {
            System.out.println("No se pudo completar la compra: " + ex.getMessage());
        }
        pause();
    }

    private void verSaldo() {
        System.out.printf("Saldo actual: $%.2f%n", actual.consultarSaldo());
        pause();
    }
}

