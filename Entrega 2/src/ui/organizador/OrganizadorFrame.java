package ui.organizador;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import eventos.Evento;
import eventos.TipoEvento;
import eventos.Venue;
import marketplace.Marketplace;
import persistencia.Database;
import persistencia.JsonStore;
import persistencia.MarketplaceStore;
import usuarios.Usuario;

public class OrganizadorFrame extends JFrame {

    private final Database db;
    private final Marketplace mp;
    private final JsonStore jsonStore;
    private final MarketplaceStore mpStore;
    private final Usuario actual;

    private JTextArea areaInfo;

    public OrganizadorFrame(Database db, Marketplace mp, JsonStore jsonStore, MarketplaceStore mpStore, Usuario actual) {
        super("Organizador - " + actual.getLogin());
        this.db = db;
        this.mp = mp;
        this.jsonStore = jsonStore;
        this.mpStore = mpStore;
        this.actual = actual;
        setSize(720, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(crearPanelBotones(), BorderLayout.NORTH);
        add(crearPanelCentro(), BorderLayout.CENTER);
        refrescarInfo();
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrearVenue = new JButton("Crear venue");
        JButton btnAprobarVenue = new JButton("Aprobar venue");
        JButton btnCrearEvento = new JButton("Crear evento");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnSalir = new JButton("Salir");
        btnCrearVenue.addActionListener(e -> crearVenue());
        btnAprobarVenue.addActionListener(e -> aprobarVenue());
        btnCrearEvento.addActionListener(e -> crearEvento());
        btnRefrescar.addActionListener(e -> refrescarInfo());
        btnGuardar.addActionListener(e -> guardar());
        btnSalir.addActionListener(e -> salir());
        panel.add(btnCrearVenue);
        panel.add(btnAprobarVenue);
        panel.add(btnCrearEvento);
        panel.add(btnRefrescar);
        panel.add(btnGuardar);
        panel.add(btnSalir);
        return panel;
    }

    private JScrollPane crearPanelCentro() {
        areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        return new JScrollPane(areaInfo);
    }

    private void refrescarInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("VENUES:\n");
        if (db.getVenues().isEmpty()) {
            sb.append("  No hay venues.\n");
        } else {
            for (Venue v : db.getVenues()) {
                sb.append("  ").append(v.getId()).append(" - ").append(v.getNombre()).append(" [")
                  .append(v.isAprobado() ? "aprobado" : "no aprobado").append("]\n");
            }
        }
        sb.append("\nEVENTOS:\n");
        if (db.getEventos().isEmpty()) {
            sb.append("  No hay eventos.\n");
        } else {
            for (Evento e : db.getEventos()) {
                sb.append("  ").append(e.getId()).append(" - ").append(e.getNombre()).append(" ")
                  .append(e.getFecha()).append(" ").append(e.getHora()).append(" | ")
                  .append(e.getTipo()).append(" | venue=");
                if (e.getVenue() == null) {
                    sb.append("N/A");
                } else {
                    sb.append(e.getVenue().getNombre());
                }
                sb.append("\n");
            }
        }
        areaInfo.setText(sb.toString());
    }

    private void crearVenue() {
        String id = JOptionPane.showInputDialog(this, "ID venue:");
        if (id == null || id.isBlank()) {
            return;
        }
        String nombre = JOptionPane.showInputDialog(this, "Nombre:");
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        String ubic = JOptionPane.showInputDialog(this, "Ubicación:");
        if (ubic == null || ubic.isBlank()) {
            return;
        }
        String sCap = JOptionPane.showInputDialog(this, "Capacidad máxima:");
        if (sCap == null || sCap.isBlank()) {
            return;
        }
        String restr = JOptionPane.showInputDialog(this, "Restricciones de uso:");
        if (restr == null || restr.isBlank()) {
            return;
        }
        try {
            int cap = Integer.parseInt(sCap.trim());
            if (cap <= 0) {
                JOptionPane.showMessageDialog(this, "Capacidad debe ser positiva.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            db.addVenue(new Venue(id.trim(), nombre.trim(), ubic.trim(), cap, restr.trim()));
            JOptionPane.showMessageDialog(this, "Venue creado.");
            refrescarInfo();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aprobarVenue() {
        if (db.getVenues().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay venues.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = JOptionPane.showInputDialog(this, "ID venue a aprobar:");
        if (id == null || id.isBlank()) {
            return;
        }
        Venue encontrado = null;
        for (Venue v : db.getVenues()) {
            if (v.getId().equals(id.trim())) {
                encontrado = v;
                break;
            }
        }
        if (encontrado == null) {
            JOptionPane.showMessageDialog(this, "No existe ese venue.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        encontrado.aprobar();
        JOptionPane.showMessageDialog(this, "Venue aprobado.");
        refrescarInfo();
    }

    private void crearEvento() {
        if (db.getVenues().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay venues. Cree y apruebe uno primero.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String id = JOptionPane.showInputDialog(this, "ID evento:");
        if (id == null || id.isBlank()) {
            return;
        }
        String nombre = JOptionPane.showInputDialog(this, "Nombre:");
        if (nombre == null || nombre.isBlank()) {
            return;
        }
        String sFecha = JOptionPane.showInputDialog(this, "Fecha (YYYY-MM-DD):");
        if (sFecha == null || sFecha.isBlank()) {
            return;
        }
        String sHora = JOptionPane.showInputDialog(this, "Hora (HH:MM):");
        if (sHora == null || sHora.isBlank()) {
            return;
        }
        Object tipoSel = JOptionPane.showInputDialog(this, "Tipo de evento:", "Tipo",
                JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "musical", "cultural", "deportivo", "religioso" }, "musical");
        if (tipoSel == null) {
            return;
        }
        TipoEvento tipo;
        String sTipo = tipoSel.toString();
        if (sTipo.equals("musical")) {
            tipo = TipoEvento.musical;
        } else if (sTipo.equals("cultural")) {
            tipo = TipoEvento.cultural;
        } else if (sTipo.equals("deportivo")) {
            tipo = TipoEvento.deportivo;
        } else {
            tipo = TipoEvento.religioso;
        }
        StringBuilder sb = new StringBuilder();
        for (Venue v : db.getVenues()) {
            sb.append(v.getId()).append(" - ").append(v.getNombre()).append(v.isAprobado() ? " [aprobado]" : " [no aprobado]").append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Venues disponibles", JOptionPane.INFORMATION_MESSAGE);
        String idVenue = JOptionPane.showInputDialog(this, "ID del venue a usar:");
        if (idVenue == null || idVenue.isBlank()) {
            return;
        }
        Venue venue = null;
        for (Venue v : db.getVenues()) {
            if (v.getId().equals(idVenue.trim())) {
                venue = v;
                break;
            }
        }
        if (venue == null) {
            JOptionPane.showMessageDialog(this, "Venue no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!venue.isAprobado()) {
            JOptionPane.showMessageDialog(this, "El venue no está aprobado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            LocalDate fecha = LocalDate.parse(sFecha.trim());
            LocalTime hora = LocalTime.parse(sHora.trim());
            Evento e = new Evento(id.trim(), nombre.trim(), fecha, hora, tipo, venue);
            db.addEvento(e);
            JOptionPane.showMessageDialog(this, "Evento creado.");
            refrescarInfo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creando evento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

