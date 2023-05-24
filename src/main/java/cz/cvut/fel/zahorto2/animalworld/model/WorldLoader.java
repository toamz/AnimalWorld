package cz.cvut.fel.zahorto2.animalworld.model;

import cz.cvut.fel.zahorto2.animalworld.model.entities.EntityType;
import cz.cvut.fel.zahorto2.animalworld.model.tiles.TileType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Loads and saves worlds from/to files in binary or text format.
 */
public class WorldLoader {
    private static final Logger logger = LogManager.getFormatterLogger(WorldLoader.class.getName());
    private WorldLoader() {}
    public static final String BINARY_FILE_EXTENSION = "world";
    public static final String TEXT_FILE_EXTENSION = "tworld";
    public static final char TEXT_FILE_SEPARATOR = ';';

    /**
     * Loads a world from a binary file.
     * If the file is not a binary world file, tries to load it as a text file.
     * @param file File to load from.
     * @return Loaded world.
     */
    public static World load(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            World world = (World) objectInputStream.readObject();
            logger.info("Loaded world from binary file");
            return world;
        } catch (StreamCorruptedException e) {
            logger.warn("File is not a binary world file, trying to load as text file");
            return loadText(file);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Failed to load world from file", e);
        }
        return null;
    }

    /**
     * Saves a world to a file.
     * If the file name does not end with {@link #BINARY_FILE_EXTENSION} or {@link #TEXT_FILE_EXTENSION},
     * saves to a file with the extension {@link #BINARY_FILE_EXTENSION}.
     * @param world World to save.
     * @param file File to save to.
     */
    public static void save(World world, File file) {
        logger.info("Saving world to file %s", file.getAbsolutePath());

        if (file.getName().endsWith(TEXT_FILE_EXTENSION)) {
            saveText(world, file);
            logger.info("Saved world to text file");
            return;
        }
        if (!file.getName().endsWith(BINARY_FILE_EXTENSION)) {
            file = new File(file.getAbsolutePath() + "." + BINARY_FILE_EXTENSION);
            logger.warn("File name did not end with %s or %s, saving to %s", BINARY_FILE_EXTENSION, TEXT_FILE_EXTENSION, file.getAbsolutePath());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(world);
            logger.info("Saved world to binary file");
        } catch (IOException e) {
            logger.error("Failed to save world", e);
        }
    }

    /**
     * Loads a world from a text file.
     * @param file File to load from.
     */
    public static World loadText(File file) {
        logger.info("Loading world from text file %s", file.getAbsolutePath());

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
                if (line == null) { // leave the world as default tiles if file ends too soon
                    break;
                }
                String[] tiles = line.split(TEXT_FILE_SEPARATOR + "", -1);
                for (int x = 0; x < Math.min(width, tiles.length / 2); x++) {
                    world.getTileGrid().setTile(TileType.valueOf(tiles[x * 2]).createTile(), x, y);
                    String entity = tiles[x * 2 + 1];
                    if (!entity.equals("")) {
                        world.getEntityMap().setEntity(EntityType.valueOf(entity).createEntity(world), x, y);
                    }
                }
            }
            return world;
        } catch (IOException e) {
            logger.error("Failed to load world", e);
        }
        return null;
    }

    /**
     * Saves a world to a text file.
     */
    public static void saveText(World world, File file) {
        logger.info("Saving world to file %s", file.getAbsolutePath());

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
            logger.error("Failed to save world to file", e);
        }
    }
}
