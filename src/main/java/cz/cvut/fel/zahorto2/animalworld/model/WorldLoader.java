package cz.cvut.fel.zahorto2.animalworld.model;

import cz.cvut.fel.zahorto2.animalworld.model.entities.EntityType;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;

import java.io.*;

/**
 * Loads and saves worlds from/to files in binary or text format.
 */
public class WorldLoader {
    private WorldLoader() {}
    public static final String BINARY_FILE_EXTENSION = "world";
    public static final String TEXT_FILE_EXTENSION = "tworld";
    public static final char TEXT_FILE_SEPARATOR = ';';

    /**
     * Loads a world from a binary file.
     * @param file File to load from.
     * @return Loaded world.
     */
    public static World load(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return (World) objectInputStream.readObject();
        } catch (StreamCorruptedException e) {
            System.err.println("File is not a world file, trying to load as text file");
            return loadText(file);
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
        if (file.getName().endsWith(TEXT_FILE_EXTENSION)) {
            saveText(world, file);
            return;
        }
        if (!file.getName().endsWith(BINARY_FILE_EXTENSION)) {
            file = new File(file.getAbsolutePath() + "." + BINARY_FILE_EXTENSION);
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(world);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a world from a text file.
     * @param file File to load from.
     */
    public static World loadText(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();
            String[] dimensions = line.split(TEXT_FILE_SEPARATOR + "");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            if (width <= 0 || height <= 0) {
                throw new IOException("Invalid world dimensions");
            }

            World world = new World(width, height);
            for (int y = 0; y < height; y++) {
                line = bufferedReader.readLine();
                String[] tiles = line.split(TEXT_FILE_SEPARATOR + "", -1);
                for (int x = 0; x < width; x++) {
                    world.getTileGrid().setTile(TileType.valueOf(tiles[x * 2]).createTile(), x, y);
                    String entity = tiles[x * 2 + 1];
                    if (!entity.equals("")) {
                        world.getEntityMap().setEntity(EntityType.valueOf(entity).createEntity(world), x, y);
                    }
                }
            }
            return world;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveText(World world, File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(String.format("%d%c%d%n", world.getWidth(), TEXT_FILE_SEPARATOR, world.getHeight()));
            for (int y = 0; y < world.getHeight(); y++) {
                for (int x = 0; x < world.getWidth(); x++) {
                    bufferedWriter.write(world.getTileGrid().getTile(x, y).getType().name());
                    bufferedWriter.write(TEXT_FILE_SEPARATOR);
                    bufferedWriter.write(world.getEntityMap().getEntity(x, y) == null ? "" : world.getEntityMap().getEntity(x, y).getType().name());
                    if (x < world.getWidth() - 1) {
                        bufferedWriter.write(TEXT_FILE_SEPARATOR);
                    }
                }
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
