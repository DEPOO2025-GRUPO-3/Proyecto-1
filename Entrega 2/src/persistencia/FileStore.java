package persistencia;

import java.io.*;

public class FileStore {

    private final String path;

    public FileStore(String path) { this.path = path; }

    public void save(Database db) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(db);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando DB: " + e.getMessage(), e);
        }
    }

    public Database load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (Database) ois.readObject();
        } catch (FileNotFoundException e) {
            return new Database(); // primera vez
        } catch (Exception e) {
            throw new RuntimeException("Error cargando DB: " + e.getMessage(), e);
        }
    }
}

