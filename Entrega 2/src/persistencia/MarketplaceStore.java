package persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import eventos.Evento;
import eventos.Localidad;
import marketplace.Contraoferta;
import marketplace.LogRegistro;
import marketplace.Marketplace;
import marketplace.Oferta;
import tiquetes.Tiquete;
import tiquetes.TiqueteMultiple;
import tiquetes.TiqueteSimple;
import usuarios.Usuario;

public class MarketplaceStore {

    private final Path dataDir = Path.of("data");
    private final Path file = dataDir.resolve("marketplace.json");


    public void save(Database db, Marketplace mp) {
        try {
            if (!Files.exists(dataDir)) Files.createDirectories(dataDir);
            String json = toJson(mp);
            Files.writeString(file, json, StandardCharsets.UTF_8);
            System.out.println("[MarketplaceStore] Guardado en " + file.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Error guardando marketplace.json: " + e.getMessage(), e);
        }
    }

    public void load(Database db, Marketplace mp) {
        if (!Files.exists(file)) {
            System.out.println("[MarketplaceStore] No existe marketplace.json. Marketplace vacío.");
            return;
        }
        try {
            String json = Files.readString(file, StandardCharsets.UTF_8);
            fromJson(json, db, mp);
            System.out.println("[MarketplaceStore] Cargado desde " + file.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo marketplace.json: " + e.getMessage(), e);
        }
    }


    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String toJson(Marketplace mp) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // Ofertas
        sb.append("  \"ofertas\": [\n");
        List<Oferta> ofertas = mp.getOfertas();
        for (int i = 0; i < ofertas.size(); i++) {
            Oferta o = ofertas.get(i);
            sb.append("    {\n");
            sb.append("      \"id\":\"").append(esc(o.getId())).append("\",\n");
            sb.append("      \"vendedor\":\"").append(esc(o.getVendedor().getLogin())).append("\",\n");
            sb.append("      \"tiqueteId\":\"").append(esc(o.getTiquete().getId())).append("\",\n");
            sb.append("      \"precio\":").append(o.getPrecioBase()).append(",\n");
            sb.append("      \"activa\":").append(o.isActiva()).append(",\n");
            sb.append("      \"contraofertas\":[\n");
            List<Contraoferta> cs = o.getContraofertas();
            for (int j = 0; j < cs.size(); j++) {
                Contraoferta c = cs.get(j);
                sb.append("        {")
                  .append("\"comprador\":\"").append(esc(c.getComprador().getLogin())).append("\",")
                  .append("\"precio\":").append(c.getPrecioPropuesto()).append(",")
                  .append("\"aceptada\":").append(c.isAceptada())
                  .append("}");
                if (j < cs.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("      ]\n");
            sb.append("    }");
            if (i < ofertas.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");

        // Log
        sb.append("  \"log\": [\n");
        List<LogRegistro> logs = mp.getLog();
        for (int i = 0; i < logs.size(); i++) {
            LogRegistro lr = logs.get(i);
            sb.append("    {")
              .append("\"fechaHora\":\"").append(esc(lr.getFechaHora().toString())).append("\",")
              .append("\"usuario\":\"").append(esc(lr.getUsuario())).append("\",")
              .append("\"accion\":\"").append(esc(lr.getAccion())).append("\",")
              .append("\"detalle\":\"").append(esc(lr.getDetalle())).append("\"")
              .append("}");
            if (i < logs.size() - 1) sb.append(",");
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
        if (start < src.length() && src.charAt(start) == '"') {
            int end = start + 1, i = end; boolean escp = false;
            while (i < src.length()) {
                char c = src.charAt(i);
                if (c == '\\' && !escp) { escp = true; i++; continue; }
                if (c == '"' && !escp) break;
                escp = false; i++;
            }
            return src.substring(start + 1, i);
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
        return src.indexOf("[", colon);
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

    private void fromJson(String json, Database db, Marketplace mp) {
        // Ofertas
        int oArr = indexOfArrayStart(json, "ofertas", 0);
        if (oArr >= 0) {
            int oEnd = findMatching(json, oArr, '[', ']');
            String arr = json.substring(oArr + 1, oEnd);
            int pos = 0;
            while (true) {
                int objS = arr.indexOf("{", pos);
                if (objS < 0) break;
                int objE = findMatching(arr, objS, '{', '}');
                String obj = arr.substring(objS, objE + 1);

                String vendedorLogin = between(obj, "vendedor", 0);
                String tiqueteId = between(obj, "tiqueteId", 0);
                String sPrecio = between(obj, "precio", 0);
                String sActiva = between(obj, "activa", 0);

                if (vendedorLogin != null && tiqueteId != null && sPrecio != null) {
                    Usuario vendedor = findUsuarioByLogin(db, vendedorLogin);
                    Tiquete tiquete = findTiqueteById(db, tiqueteId);
                    if (vendedor != null && tiquete != null) {
                        double precio = Double.parseDouble(sPrecio);
                        boolean activa = sActiva == null ? true : Boolean.parseBoolean(sActiva);

                        Oferta o = mp.publicarOferta(vendedor, tiquete, precio);
                        if (!activa) {
                            mp.eliminarOferta(o.getId(), vendedor);
                        }

                        // Contraofertas
                        int cArr = indexOfArrayStart(obj, "contraofertas", 0);
                        if (cArr >= 0) {
                            int cEnd = findMatching(obj, cArr, '[', ']');
                            String carr = obj.substring(cArr + 1, cEnd);
                            int p2 = 0;
                            while (true) {
                                int cS = carr.indexOf("{", p2);
                                if (cS < 0) break;
                                int cE = findMatching(carr, cS, '{', '}');
                                String cobj = carr.substring(cS, cE + 1);

                                String compradorLogin = between(cobj, "comprador", 0);
                                String pStr = between(cobj, "precio", 0);
                                String aStr = between(cobj, "aceptada", 0);

                                if (compradorLogin != null && pStr != null) {
                                    Usuario comprador = findUsuarioByLogin(db, compradorLogin);
                                    if (comprador != null) {
                                        double p = Double.parseDouble(pStr);
                                        Contraoferta c = new Contraoferta(comprador, p);
                                        mp.ofertar(o.getId(), c);
                                        if ("true".equalsIgnoreCase(aStr)) {
                                            mp.aceptarContraoferta(o.getId(), c, vendedor);
                                        }
                                    }
                                }
                                p2 = cE + 1;
                            }
                        }
                    }
                }

                pos = objE + 1;
            }
        }

        int lArr = indexOfArrayStart(json, "log", 0);
        if (lArr >= 0) {
            int lEnd = findMatching(json, lArr, '[', ']');
            String arr = json.substring(lArr + 1, lEnd);
            int pos2 = 0;
            while (true) {
                int objS = arr.indexOf("{", pos2);
                if (objS < 0) break;
                int objE = findMatching(arr, objS, '{', '}');
                String obj = arr.substring(objS, objE + 1);

                String usuario = between(obj, "usuario", 0);
                String accion = between(obj, "accion", 0);
                String detalle = between(obj, "detalle", 0);

                if (usuario != null && accion != null && detalle != null) {
                    mp.registrarAccion(usuario, accion, detalle);
                }
                pos2 = objE + 1;
            }
        }
    }


    private static Usuario findUsuarioByLogin(Database db, String login) {
        for (Usuario u : db.getUsuarios()) {
            if (u.getLogin().equals(login)) return u;
        }
        return null;
    }

    private static Tiquete findTiqueteById(Database db, String id) {
        for (Evento e : db.getEventos()) {
            for (Localidad l : e.getLocalidades()) {
                for (Tiquete t : l.getTiquetes()) {
                    if (t.getId().equals(id)) return t;
                    if (t instanceof TiqueteMultiple tm) {
                        for (TiqueteSimple ts : tm.getContenidos()) {
                            if (ts.getId().equals(id)) return ts;
                        }
                    }
                }
            }
        }
        return null;
    }
}

