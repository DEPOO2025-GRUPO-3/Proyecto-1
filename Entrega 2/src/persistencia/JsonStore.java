package persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import eventos.EstadoEvento;
import eventos.Evento;
import eventos.Localidad;
import eventos.TipoEvento;
import eventos.Venue;
import tiquetes.EstadoTiquete;
import tiquetes.Tiquete;
import tiquetes.TiqueteNumerado;
import tiquetes.TiqueteSimple;
import tiquetes.TiqueteMultiple;
import tiquetes.TipoMultiple;
import usuarios.Usuario;


public class JsonStore {

    private final Path dataDir = Path.of("data");
    private final Path dbFile = dataDir.resolve("db.json");

    public void save(Database db) {
        try {
            if (!Files.exists(dataDir)) Files.createDirectories(dataDir);
            String json = toJson(db);
            Files.writeString(dbFile, json, StandardCharsets.UTF_8);
            System.out.println("[JsonStore] Guardado en " + dbFile.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Error guardando JSON: " + e.getMessage(), e);
        }
    }

    public Database load() {
        Database db = new Database();
        if (!Files.exists(dbFile)) {
            System.out.println("[JsonStore] No existe " + dbFile + ". DB vacía.");
            return db;
        }
        try {
            String json = Files.readString(dbFile, StandardCharsets.UTF_8);
            fromJson(json, db);
            System.out.println("[JsonStore] Cargado desde " + dbFile.toAbsolutePath());
            return db;
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo JSON: " + e.getMessage(), e);
        }
    }


    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String toJson(Database db) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // Usuarios
        sb.append("  \"usuarios\": [\n");
        for (int i = 0; i < db.getUsuarios().size(); i++) {
            Usuario u = db.getUsuarios().get(i);
            sb.append("    {")
              .append("\"login\":\"").append(esc(u.getLogin())).append("\",")
              .append("\"contrasena\":\"").append(esc(u.getContrasena())).append("\",")
              .append("\"saldo\":").append(u.consultarSaldo())
              .append("}");
            if (i < db.getUsuarios().size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Venues
        sb.append("  \"venues\": [\n");
        for (int i = 0; i < db.getVenues().size(); i++) {
            Venue v = db.getVenues().get(i);
            sb.append("    {")
              .append("\"id\":\"").append(esc(v.getId())).append("\",")
              .append("\"nombre\":\"").append(esc(v.getNombre())).append("\",")
              .append("\"ubicacion\":\"").append(esc(v.getUbicacion())).append("\",")
              .append("\"capacidad\":").append(v.getCapacidadMaxima()).append(",")
              .append("\"restricciones\":\"").append(esc(v.getRestriccionesDeUso())).append("\",")
              .append("\"aprobado\":").append(v.isAprobado())
              .append("}");
            if (i < db.getVenues().size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Eventos con localidades y tiquetes
        sb.append("  \"eventos\": [\n");
        for (int i = 0; i < db.getEventos().size(); i++) {
            Evento e = db.getEventos().get(i);
            sb.append("    {\n");
            sb.append("      \"id\":\"").append(esc(e.getId())).append("\",\n");
            sb.append("      \"nombre\":\"").append(esc(e.getNombre())).append("\",\n");
            sb.append("      \"fecha\":\"").append(e.getFecha()).append("\",\n");
            sb.append("      \"hora\":\"").append(e.getHora()).append("\",\n");
            sb.append("      \"tipo\":\"").append(e.getTipo().name()).append("\",\n");
            sb.append("      \"estado\":\"").append(e.getEstado().name()).append("\",\n");
            sb.append("      \"venueId\":\"").append(esc(e.getVenue().getId())).append("\",\n");

            sb.append("      \"localidades\": [\n");
            List<Localidad> locs = e.getLocalidades();
            for (int j = 0; j < locs.size(); j++) {
                Localidad l = locs.get(j);
                sb.append("        {\n");
                sb.append("          \"id\":\"").append(esc(l.getId())).append("\",\n");
                sb.append("          \"nombre\":\"").append(esc(l.getNombre())).append("\",\n");
                sb.append("          \"precioBase\":").append(l.getPrecioBase()).append(",\n");

                sb.append("          \"tiquetes\": [\n");
                List<Tiquete> ts = l.getTiquetes();
                for (int k = 0; k < ts.size(); k++) {
                    Tiquete t = ts.get(k);
                    sb.append("            {");
                    sb.append("\"id\":\"").append(esc(t.getId())).append("\",");
                    sb.append("\"estado\":\"").append(t.getEstado().name()).append("\"");

                    if (t instanceof TiqueteNumerado tn) {
                        sb.append(",\"tipo\":\"Numerado\",");
                        sb.append("\"asiento\":\"").append(esc(tn.getAsiento())).append("\"");
                    } else if (t instanceof TiqueteMultiple tm) {
                        sb.append(",\"tipo\":\"Multiple\",");
                        sb.append("\"incluye\":").append(tm.getCantidadIncluida()).append(",");
                        sb.append("\"subtipo\":\"").append(tm.getTipo().name()).append("\"");
                    } else {
                        sb.append(",\"tipo\":\"Simple\"");
                    }
                    sb.append("}");
                    if (k < ts.size() - 1) sb.append(",");
                    sb.append("\n");
                }
                sb.append("          ]\n");
                sb.append("        }");
                if (j < locs.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("      ]\n");
            sb.append("    }");
            if (i < db.getEventos().size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");

        sb.append("}\n");
        return sb.toString();
    }


    private static String between(String src, String key, int fromIdx) {
        int k = src.indexOf("\"" + key + "\"", fromIdx);
        if (k < 0) return null;
        int colon = src.indexOf(":", k);
        if (colon < 0) return null;
        int start = colon + 1;
        while (start < src.length() && Character.isWhitespace(src.charAt(start))) start++;
        if (src.charAt(start) == '\"') {
            int end = start + 1;
            boolean escape = false;
            while (end < src.length()) {
                char c = src.charAt(end);
                if (c == '\\' && !escape) { escape = true; end++; continue; }
                if (c == '\"' && !escape) break;
                escape = false; end++;
            }
            return src.substring(start + 1, end);
        } else {
            int end = start;
            while (end < src.length() && ",}] \n\r\t".indexOf(src.charAt(end)) == -1) end++;
            return src.substring(start, end);
        }
    }
    private static int indexOfArrayStart(String src, String key, int fromIdx) {
        int k = src.indexOf("\"" + key + "\"", fromIdx);
        if (k < 0) return -1;
        int colon = src.indexOf(":", k);
        int arr = src.indexOf("[", colon);
        return arr;
    }
    private static int findMatching(String s, int openIndex, char open, char close) {
        int depth = 0;
        for (int i = openIndex; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == open) depth++;
            if (c == close) { depth--; if (depth == 0) return i; }
        }
        return -1;
    }

    private void fromJson(String json, Database db) {
        db.clear();

        // Usuarios
        int uArr = indexOfArrayStart(json, "usuarios", 0);
        if (uArr >= 0) {
            int uEnd = findMatching(json, uArr, '[', ']');
            String usersArray = json.substring(uArr + 1, uEnd);
            int pos = 0;
            while (true) {
                int objStart = usersArray.indexOf("{", pos);
                if (objStart < 0) break;
                int objEnd = findMatching(usersArray, objStart, '{', '}');
                String obj = usersArray.substring(objStart, objEnd + 1);

                String login = between(obj, "login", 0);
                String pass  = between(obj, "contrasena", 0);
                String saldoS = between(obj, "saldo", 0);
                double saldo = (saldoS == null || saldoS.isBlank()) ? 0.0 : Double.parseDouble(saldoS);

                Usuario u = new Usuario(login, pass, saldo);
                db.addUsuario(u);

                pos = objEnd + 1;
            }
        }

        // Venues
        int vArr = indexOfArrayStart(json, "venues", 0);
        if (vArr >= 0) {
            int vEnd = findMatching(json, vArr, '[', ']');
            String arr = json.substring(vArr + 1, vEnd);
            int pos = 0;
            while (true) {
                int objStart = arr.indexOf("{", pos);
                if (objStart < 0) break;
                int objEnd = findMatching(arr, objStart, '{', '}');
                String obj = arr.substring(objStart, objEnd + 1);

                String id = between(obj, "id", 0);
                String nombre = between(obj, "nombre", 0);
                String ubicacion = between(obj, "ubicacion", 0);
                int capacidad = Integer.parseInt(between(obj, "capacidad", 0));
                String restr = between(obj, "restricciones", 0);
                boolean aprobado = Boolean.parseBoolean(between(obj, "aprobado", 0));

                Venue v = new Venue(id, nombre, ubicacion, capacidad, restr);
                if (aprobado) v.aprobar();
                db.addVenue(v);

                pos = objEnd + 1;
            }
        }

        // Eventos
        int eArr = indexOfArrayStart(json, "eventos", 0);
        if (eArr >= 0) {
            int eEnd = findMatching(json, eArr, '[', ']');
            String arr = json.substring(eArr + 1, eEnd);
            int pos = 0;
            while (true) {
                int objStart = arr.indexOf("{", pos);
                if (objStart < 0) break;
                int objEnd = findMatching(arr, objStart, '{', '}');
                String obj = arr.substring(objStart, objEnd + 1);

                String id = between(obj, "id", 0);
                String nombre = between(obj, "nombre", 0);
                LocalDate fecha = LocalDate.parse(between(obj, "fecha", 0));
                LocalTime hora = LocalTime.parse(between(obj, "hora", 0));
                TipoEvento tipo = TipoEvento.valueOf(between(obj, "tipo", 0));
                EstadoEvento estado = EstadoEvento.valueOf(between(obj, "estado", 0));
                String venueId = between(obj, "venueId", 0);

                Venue venue = db.getVenues().stream()
                        .filter(v -> v.getId().equals(venueId))
                        .findFirst().orElse(null);

                Evento e = new Evento(id, nombre, fecha, hora, tipo, venue);
                if (estado == EstadoEvento.cancelado) e.cancelar();

                // Localidades
                int lArr = indexOfArrayStart(obj, "localidades", 0);
                if (lArr >= 0) {
                    int lEnd = findMatching(obj, lArr, '[', ']');
                    String locs = obj.substring(lArr + 1, lEnd);
                    int p2 = 0;
                    while (true) {
                        int lObjS = locs.indexOf("{", p2);
                        if (lObjS < 0) break;
                        int lObjE = findMatching(locs, lObjS, '{', '}');
                        String lObj = locs.substring(lObjS, lObjE + 1);

                        String lid = between(lObj, "id", 0);
                        String lnom = between(lObj, "nombre", 0);
                        double precioBase = Double.parseDouble(between(lObj, "precioBase", 0));

                        Localidad loc = new Localidad(lid, lnom, precioBase);

                        // Tiquetes
                        int tArr = indexOfArrayStart(lObj, "tiquetes", 0);
                        if (tArr >= 0) {
                            int tEnd = findMatching(lObj, tArr, '[', ']');
                            String ts = lObj.substring(tArr + 1, tEnd);
                            int p3 = 0;
                            while (true) {
                                int tS = ts.indexOf("{", p3);
                                if (tS < 0) break;
                                int tE = findMatching(ts, tS, '{', '}');
                                String tObj = ts.substring(tS, tE + 1);

                                String tid = between(tObj, "id", 0);
                                String tipoT = between(tObj, "tipo", 0);
                                EstadoTiquete est = EstadoTiquete.valueOf(between(tObj, "estado", 0));

                                Tiquete t;
                                switch (tipoT) {
                                    case "Numerado" -> {
                                        String asiento = between(tObj, "asiento", 0);
                                        t = new TiqueteNumerado(tid, precioBase, 0, 0, asiento);
                                    }
                                    case "Multiple" -> {
                                        int incluye = Integer.parseInt(between(tObj, "incluye", 0));
                                        TipoMultiple subt = TipoMultiple.valueOf(between(tObj, "subtipo", 0));
                                        t = new TiqueteMultiple(tid, precioBase, 0, 0, incluye, subt);
                                    }
                                    default -> {
                                        t = new TiqueteSimple(tid, precioBase, 0, 0);
                                    }
                                }
                                t.setEstadoDesdePersistencia(est);
                                loc.agregarTiquete(t);

                                p3 = tE + 1;
                            }
                        }
                        e.agregarLocalidad(loc);
                        p2 = lObjE + 1;
                    }
                }
                db.addEvento(e);

                pos = objEnd + 1;
            }
        }
    }
}

