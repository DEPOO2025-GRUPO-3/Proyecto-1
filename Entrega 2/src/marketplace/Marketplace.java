package marketplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tiquetes.Tiquete;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class Marketplace {

    private final List<Oferta> ofertas = new ArrayList<>();
    private final List<LogRegistro> log = new ArrayList<>();

    public Oferta publicarOferta(Usuario vendedor, Tiquete tiquete, double precio) {
        if (vendedor == null || tiquete == null || precio <= 0) return null;
        if (!(tiquete instanceof TiqueteSimple)) {
            registrarAccion(vendedor.getLogin(), "Oferta rechazada",
                    "El tiquete no es elegible para reventa.");
            return null;
        }
        String id = UUID.randomUUID().toString();
        Oferta o = new Oferta(id, vendedor, tiquete, precio);
        ofertas.add(o);
        registrarAccion(vendedor.getLogin(), "Oferta creada",
                "Oferta " + id + " por $" + precio + " publicada.");
        return o;
    }

    public boolean ofertar(String idOferta, Contraoferta contraoferta) {
        Optional<Oferta> opt = buscarPorId(idOferta);
        if (opt.isEmpty() || contraoferta == null) return false;
        Oferta o = opt.get();
        if (!o.isActiva()) return false;
        o.agregarContraoferta(contraoferta);
        registrarAccion(contraoferta.getComprador().getLogin(), "Contraoferta creada",
                "Oferta " + idOferta + " por $" + contraoferta.getPrecioPropuesto());
        return true;
    }

    public boolean aceptarContraoferta(String idOferta, Contraoferta c, Usuario ejecutor) {
        Optional<Oferta> opt = buscarPorId(idOferta);
        if (opt.isEmpty() || c == null) return false;
        Oferta o = opt.get();
        if (!o.isActiva()) return false;
        o.aceptarContraoferta(c);
        registrarAccion(ejecutor != null ? ejecutor.getLogin() : "sistema",
                "Contraoferta aceptada",
                "Oferta " + idOferta + " cerrada.");
        return true;
    }

    public boolean eliminarOferta(String idOferta, Usuario ejecutor) {
        Optional<Oferta> opt = buscarPorId(idOferta);
        if (opt.isEmpty()) return false;
        Oferta o = opt.get();
        o.cerrarOferta();
        ofertas.remove(o);
        registrarAccion(ejecutor != null ? ejecutor.getLogin() : "sistema",
                "Oferta eliminada",
                "Oferta " + idOferta + " removida del marketplace.");
        return true;
    }

    public List<Oferta> listarOfertasActivas() {
        List<Oferta> activas = new ArrayList<>();
        for (Oferta o : ofertas) {
            if (o.isActiva()) activas.add(o);
        }
        return new ArrayList<>(activas);
    }

    public Optional<Oferta> buscarPorId(String id) {
        for (Oferta o : ofertas) {
            if (o.getId().equals(id)) return Optional.of(o);
        }
        return Optional.empty();
    }

    public void registrarAccion(String usuario, String accion, String detalle) {
        log.add(new LogRegistro(usuario, accion, detalle));
    }

    public List<LogRegistro> getLog() {
        return new ArrayList<>(log);
    }

    public List<Oferta> getOfertas() {
        return new ArrayList<>(ofertas);
    }
}

