package cz.cvut.fel.zahorto2.animalworld.model;

import java.io.*;

/**
 * Loads and saves worlds from/to files in binary or text format.
 */
public class WorldLoader {
    private WorldLoader() {}
    public static final String BINARY_FILE_EXTENSION = "world";

    /**
     * Loads a world from a binary file.
     * @param file File to load from.
     * @return Loaded world.
     */
    public static World load(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (World) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves a world to a binary file.
     * @param world World to save.
     * @param file File to save to.
     */
    public static void save(World world, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
