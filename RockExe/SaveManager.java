import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Tiny file-based save helper for Rock.exe.
 * Saves only simple numbers so the project stays beginner-friendly.
 */
public final class SaveManager {
    private static final String SAVE_FOLDER = ".jps-games";
    private static final String SAVE_FILE = "rock-exe.properties";

    private SaveManager() {
        // Utility class.
    }

    public static RockStats loadStats() {
        Properties properties = new Properties();
        Path savePath = getSavePath();

        if (!Files.exists(savePath)) {
            return RockStats.defaults();
        }

        try (InputStream input = Files.newInputStream(savePath)) {
            properties.load(input);
            return new RockStats(
                    parseInt(properties.getProperty("happiness"), 50),
                    parseInt(properties.getProperty("cleanliness"), 50),
                    parseInt(properties.getProperty("energy"), 50),
                    parseInt(properties.getProperty("boredom"), 50),
                    parseInt(properties.getProperty("respect"), 0)
            ).clamped();
        } catch (IOException ex) {
            System.err.println("Could not load Rock.exe stats: " + ex.getMessage());
            return RockStats.defaults();
        }
    }

    public static void saveStats(RockStats stats) {
        RockStats safeStats = stats.clamped();
        Properties properties = new Properties();
        properties.setProperty("happiness", String.valueOf(safeStats.happiness));
        properties.setProperty("cleanliness", String.valueOf(safeStats.cleanliness));
        properties.setProperty("energy", String.valueOf(safeStats.energy));
        properties.setProperty("boredom", String.valueOf(safeStats.boredom));
        properties.setProperty("respect", String.valueOf(safeStats.respect));

        Path savePath = getSavePath();
        try {
            Files.createDirectories(savePath.getParent());
            try (OutputStream output = Files.newOutputStream(savePath)) {
                properties.store(output, "Rock.exe save data");
            }
        } catch (IOException ex) {
            System.err.println("Could not save Rock.exe stats: " + ex.getMessage());
        }
    }

    private static Path getSavePath() {
        String home = System.getProperty("user.home", ".");
        return Paths.get(home, SAVE_FOLDER, SAVE_FILE);
    }

    private static int parseInt(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static final class RockStats {
        public final int happiness;
        public final int cleanliness;
        public final int energy;
        public final int boredom;
        public final int respect;

        public RockStats(int happiness, int cleanliness, int energy, int boredom, int respect) {
            this.happiness = happiness;
            this.cleanliness = cleanliness;
            this.energy = energy;
            this.boredom = boredom;
            this.respect = respect;
        }

        public static RockStats defaults() {
            return new RockStats(50, 50, 50, 50, 0);
        }

        public RockStats clamped() {
            return new RockStats(
                    clamp(happiness, 0, 100),
                    clamp(cleanliness, 0, 100),
                    clamp(energy, 0, 100),
                    clamp(boredom, 0, 100),
                    clamp(respect, -100, 999)
            );
        }

        private static int clamp(int value, int min, int max) {
            return Math.max(min, Math.min(max, value));
        }
    }
}
