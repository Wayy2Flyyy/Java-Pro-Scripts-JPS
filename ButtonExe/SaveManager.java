import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Tiny file-based save helper for Button.exe.
 * Keeps beginner projects dependency-free while still remembering the high score.
 */
public final class SaveManager {
    private static final String SAVE_FOLDER = ".jps-games";
    private static final String SAVE_FILE = "button-exe.properties";
    private static final String HIGH_SCORE_KEY = "highScore";

    private SaveManager() {
        // Utility class.
    }

    public static int loadHighScore() {
        Properties properties = new Properties();
        Path savePath = getSavePath();

        if (!Files.exists(savePath)) {
            return 0;
        }

        try (InputStream input = Files.newInputStream(savePath)) {
            properties.load(input);
            return parseNonNegativeInt(properties.getProperty(HIGH_SCORE_KEY), 0);
        } catch (IOException ex) {
            System.err.println("Could not load Button.exe high score: " + ex.getMessage());
            return 0;
        }
    }

    public static void saveHighScore(int highScore) {
        Properties properties = new Properties();
        properties.setProperty(HIGH_SCORE_KEY, String.valueOf(Math.max(0, highScore)));

        Path savePath = getSavePath();
        try {
            Files.createDirectories(savePath.getParent());
            try (OutputStream output = Files.newOutputStream(savePath)) {
                properties.store(output, "Button.exe save data");
            }
        } catch (IOException ex) {
            System.err.println("Could not save Button.exe high score: " + ex.getMessage());
        }
    }

    private static Path getSavePath() {
        String home = System.getProperty("user.home", ".");
        return Paths.get(home, SAVE_FOLDER, SAVE_FILE);
    }

    private static int parseNonNegativeInt(String value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        try {
            return Math.max(0, Integer.parseInt(value.trim()));
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
