package ui.cliente;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import tiquetes.Tiquete;

public class TiquetesTableModel extends AbstractTableModel {

    private final List<Tiquete> data;

    public TiquetesTableModel(List<Tiquete> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int col) {
        return switch (col) {
            case 0 -> "ID";
            case 1 -> "Evento";
            case 2 -> "Fecha";
            case 3 -> "Localidad";
            case 4 -> "Imp.";
            default -> "";
        };
    }

    @Override
    public Object getValueAt(int row, int col) {
        Tiquete t = data.get(row);
        return switch (col) {
            case 0 -> t.getId();
            case 1 -> t.getEvento() != null ? t.getEvento().getNombre() : "";
            case 2 -> t.getEvento() != null ? t.getEvento().getFecha() : "";
            case 3 -> t.getLocalidad() != null ? t.getLocalidad().getNombre() : "";
            case 4 -> t.getFechaImpresion() != null ? "Sí" : "No";
            default -> "";
        };
    }
}

