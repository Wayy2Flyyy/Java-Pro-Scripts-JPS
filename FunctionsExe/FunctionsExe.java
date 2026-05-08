import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FunctionsExe extends JFrame {

    private JTextArea outputArea;

    public FunctionsExe() {
        setupWindow();
        setupUI();
    }

    private void setupWindow() {
        setTitle("Functions.exe");
        setSize(920, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupUI() {
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBackground(new Color(14, 14, 18));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        JLabel title = new JLabel("Functions.exe — Java Utility Toolkit");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        root.add(title, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setBackground(new Color(24, 24, 32));
        outputArea.setForeground(new Color(225, 225, 235));
        outputArea.setCaretColor(Color.WHITE);
        outputArea.setBorder(new EmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 90)));
        root.add(scrollPane, BorderLayout.CENTER);

        JPanel side = new JPanel(new GridLayout(0, 1, 8, 8));
        side.setBackground(new Color(14, 14, 18));
        side.setPreferredSize(new Dimension(230, 0));
        root.add(side, BorderLayout.EAST);

        addDemoButton(side, "Math Demo", this::demoMath);
        addDemoButton(side, "Random Demo", this::demoRandom);
        addDemoButton(side, "Text Demo", this::demoText);
        addDemoButton(side, "Time Demo", this::demoTime);
        addDemoButton(side, "File Demo", this::demoFiles);
        addDemoButton(side, "Colour Demo", this::demoColours);
        addDemoButton(side, "Collision Demo", this::demoCollision);
        addDemoButton(side, "Stats Demo", this::demoStats);
        addDemoButton(side, "Copy Output", e -> Fn.UI.copyToClipboard(outputArea.getText()));
        addDemoButton(side, "Clear", e -> outputArea.setText(""));

        log("Functions.exe loaded.");
        log("Use this file as a toolbox for Java games, apps, and learning projects.");
        log("The reusable functions are inside: FunctionsExe.Fn");
    }

    private void addDemoButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        Fn.UI.styleButton(button);
        button.addActionListener(action);
        panel.add(button);
    }

    private void demoMath(ActionEvent e) {
        logHeader("MATH FUNCTIONS");
        log("Clamp 150 to 0-100: " + Fn.Maths.clamp(150, 0, 100));
        log("Map 50 from 0-100 into 0-1: " + Fn.Maths.map(50, 0, 100, 0, 1));
        log("Distance between (0,0) and (3,4): " + Fn.Maths.distance(0, 0, 3, 4));
        log("Lerp between 10 and 20 at 0.25: " + Fn.Maths.lerp(10, 20, 0.25));
        log("Angle from (0,0) to (10,10): " + Fn.Maths.roundTo(Fn.Maths.angleDegreesTo(0, 0, 10, 10), 2) + " degrees");
    }

    private void demoRandom(ActionEvent e) {
        logHeader("RANDOM FUNCTIONS");
        log("Random int 1-100: " + Fn.Randoms.intRange(1, 100));
        log("Random double 0-1: " + Fn.Maths.roundTo(Fn.Randoms.doubleRange(0, 1), 4));
        log("Random ID: " + Fn.Randoms.randomId("ITEM", 8));
        log("Pick from list: " + Fn.Randoms.pick("Common", "Rare", "Epic", "Mythic"));
        log("25% chance result: " + Fn.Randoms.chancePercent(25));
    }

    private void demoText(ActionEvent e) {
        logHeader("TEXT FUNCTIONS");
        log("Title case: " + Fn.Text.titleCase("hello from java functions exe"));
        log("Slug: " + Fn.Text.slug("Rock Fella: Mythic Update!!!"));
        log("Limit: " + Fn.Text.limit("This sentence is deliberately too long for a small UI label.", 32));
        log("Progress bar: " + Fn.Text.progressBar(73, 100, 20));
        log("Plural: " + Fn.Text.plural(2, "achievement", "achievements"));
    }

    private void demoTime(ActionEvent e) {
        logHeader("TIME FUNCTIONS");
        log("Now: " + Fn.Time.nowReadable());
        log("File timestamp: " + Fn.Time.timestampForFile());
        log("Format 90500ms: " + Fn.Time.formatDurationMs(90_500));
    }

    private void demoFiles(ActionEvent e) {
        logHeader("FILE FUNCTIONS");
        Path path = Path.of("functions_demo_save.txt");
        boolean saved = Fn.Files.writeText(path, "Functions.exe demo save created at " + Fn.Time.nowReadable());
        log("Saved file: " + saved + " -> " + path.toAbsolutePath());
        log("Read file: " + Fn.Files.readText(path));
    }

    private void demoColours(ActionEvent e) {
        logHeader("COLOUR FUNCTIONS");
        Color base = new Color(80, 170, 255);
        log("Base hex: " + Fn.Colours.toHex(base));
        log("Darker hex: " + Fn.Colours.toHex(Fn.Colours.darker(base, 0.35)));
        log("Brighter hex: " + Fn.Colours.toHex(Fn.Colours.brighter(base, 0.25)));
        log("Contrast text colour: " + Fn.Colours.toHex(Fn.Colours.contrastText(base)));
    }

    private void demoCollision(ActionEvent e) {
        logHeader("COLLISION FUNCTIONS");
        Rectangle a = new Rectangle(10, 10, 50, 50);
        Rectangle b = new Rectangle(45, 45, 50, 50);
        log("Rect A intersects Rect B: " + Fn.Game2D.intersects(a, b));
        log("Point inside Rect A: " + Fn.Game2D.pointInRect(20, 20, a));
        log("Circle collision: " + Fn.Game2D.circleIntersectsCircle(100, 100, 20, 120, 100, 20));
    }

    private void demoStats(ActionEvent e) {
        logHeader("STATS FUNCTIONS");
        int health = 100;
        health = Fn.Stats.damage(health, 35);
        log("Health after 35 damage: " + health);
        health = Fn.Stats.heal(health, 20, 100);
        log("Health after 20 healing: " + health);
        log("Level from 450 XP: " + Fn.Stats.levelFromXp(450, 100));
        log("XP progress bar: " + Fn.Text.progressBar(Fn.Stats.xpProgressCurrentLevel(450, 100), 100, 20));
    }

    private void logHeader(String title) {
        log("\n=== " + title + " ===");
    }

    private void log(String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FunctionsExe app = new FunctionsExe();
            app.setVisible(true);
        });
    }

    /*
     * ============================================================
     *                      REUSABLE FUNCTION TOOLKIT
     * ============================================================
     *
     * Use these from any other Java file like this:
     *
     * int hp = FunctionsExe.Fn.Maths.clamp(150, 0, 100);
     * String name = FunctionsExe.Fn.Text.titleCase("rock fella");
     */
    public static final class Fn {

        private Fn() {
        }

        public static final class Maths {
            private Maths() {
            }

            public static int clamp(int value, int min, int max) {
                return Math.max(min, Math.min(max, value));
            }

            public static double clamp(double value, double min, double max) {
                return Math.max(min, Math.min(max, value));
            }

            public static int wrap(int value, int min, int max) {
                if (max < min) throw new IllegalArgumentException("max must be >= min");
                int range = max - min + 1;
                return ((value - min) % range + range) % range + min;
            }

            public static double lerp(double start, double end, double amount) {
                return start + (end - start) * amount;
            }

            public static double inverseLerp(double start, double end, double value) {
                if (start == end) return 0;
                return (value - start) / (end - start);
            }

            public static double map(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
                double t = inverseLerp(inputMin, inputMax, value);
                return lerp(outputMin, outputMax, t);
            }

            public static double distance(double x1, double y1, double x2, double y2) {
                double dx = x2 - x1;
                double dy = y2 - y1;
                return Math.sqrt(dx * dx + dy * dy);
            }

            public static double length(double x, double y) {
                return Math.sqrt(x * x + y * y);
            }

            public static Point2D.Double normalize(double x, double y) {
                double length = length(x, y);
                if (length == 0) return new Point2D.Double(0, 0);
                return new Point2D.Double(x / length, y / length);
            }

            public static Point2D.Double moveTowards(double x, double y, double targetX, double targetY, double speed) {
                double dx = targetX - x;
                double dy = targetY - y;
                double distance = distance(x, y, targetX, targetY);

                if (distance <= speed || distance == 0) {
                    return new Point2D.Double(targetX, targetY);
                }

                return new Point2D.Double(x + (dx / distance) * speed, y + (dy / distance) * speed);
            }

            public static double angleDegreesTo(double x1, double y1, double x2, double y2) {
                return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
            }

            public static double roundTo(double value, int decimals) {
                double scale = Math.pow(10, decimals);
                return Math.round(value * scale) / scale;
            }

            public static double percent(double current, double max) {
                if (max == 0) return 0;
                return (current / max) * 100.0;
            }

            public static boolean inRange(double value, double min, double max) {
                return value >= min && value <= max;
            }

            public static int midpoint(int a, int b) {
                return a + (b - a) / 2;
            }
        }

        public static final class Randoms {
            private static final Random RNG = new Random();

            private Randoms() {
            }

            public static int intRange(int min, int max) {
                if (max < min) throw new IllegalArgumentException("max must be >= min");
                return RNG.nextInt(max - min + 1) + min;
            }

            public static double doubleRange(double min, double max) {
                if (max < min) throw new IllegalArgumentException("max must be >= min");
                return min + (RNG.nextDouble() * (max - min));
            }

            public static boolean chance(double probability) {
                return RNG.nextDouble() < Maths.clamp(probability, 0.0, 1.0);
            }

            public static boolean chancePercent(double percent) {
                return chance(percent / 100.0);
            }

            @SafeVarargs
            public static <T> T pick(T... values) {
                if (values == null || values.length == 0) return null;
                return values[RNG.nextInt(values.length)];
            }

            public static <T> T pick(List<T> values) {
                if (values == null || values.isEmpty()) return null;
                return values.get(RNG.nextInt(values.size()));
            }

            public static <T> List<T> shuffledCopy(List<T> values) {
                List<T> copy = new ArrayList<>(values);
                Collections.shuffle(copy, RNG);
                return copy;
            }

            public static Color randomColor() {
                return new Color(intRange(0, 255), intRange(0, 255), intRange(0, 255));
            }

            public static String randomId(String prefix, int length) {
                String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                StringBuilder id = new StringBuilder(prefix == null ? "" : prefix);
                if (!id.isEmpty()) id.append("-");

                for (int i = 0; i < length; i++) {
                    id.append(chars.charAt(RNG.nextInt(chars.length())));
                }

                return id.toString();
            }

            public static <T> T weightedPick(List<WeightedItem<T>> items) {
                if (items == null || items.isEmpty()) return null;

                int totalWeight = 0;
                for (WeightedItem<T> item : items) {
                    totalWeight += Math.max(0, item.weight);
                }

                if (totalWeight <= 0) return items.get(0).value;

                int roll = intRange(1, totalWeight);
                int running = 0;

                for (WeightedItem<T> item : items) {
                    running += Math.max(0, item.weight);
                    if (roll <= running) return item.value;
                }

                return items.get(items.size() - 1).value;
            }
        }

        public static final class WeightedItem<T> {
            public final T value;
            public final int weight;

            public WeightedItem(T value, int weight) {
                this.value = value;
                this.weight = weight;
            }
        }

        public static final class Text {
            private Text() {
            }

            public static boolean isBlank(String value) {
                return value == null || value.trim().isEmpty();
            }

            public static String cleanSpaces(String value) {
                if (value == null) return "";
                return value.trim().replaceAll("\\s+", " ");
            }

            public static String capitalise(String value) {
                value = cleanSpaces(value);
                if (value.isEmpty()) return value;
                return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
            }

            public static String titleCase(String value) {
                value = cleanSpaces(value);
                if (value.isEmpty()) return value;

                StringBuilder result = new StringBuilder();
                for (String word : value.split(" ")) {
                    if (!result.isEmpty()) result.append(" ");
                    result.append(capitalise(word));
                }
                return result.toString();
            }

            public static String limit(String value, int maxLength) {
                if (value == null) return "";
                if (maxLength <= 0) return "";
                if (value.length() <= maxLength) return value;
                if (maxLength <= 3) return value.substring(0, maxLength);
                return value.substring(0, maxLength - 3) + "...";
            }

            public static String repeat(String value, int times) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < times; i++) {
                    result.append(value);
                }
                return result.toString();
            }

            public static String padLeft(String value, int length, char character) {
                if (value == null) value = "";
                if (value.length() >= length) return value;
                return repeat(String.valueOf(character), length - value.length()) + value;
            }

            public static String padRight(String value, int length, char character) {
                if (value == null) value = "";
                if (value.length() >= length) return value;
                return value + repeat(String.valueOf(character), length - value.length());
            }

            public static String slug(String value) {
                if (value == null) return "";
                return value.toLowerCase()
                        .replaceAll("[^a-z0-9]+", "-")
                        .replaceAll("^-|-$", "");
            }

            public static boolean containsIgnoreCase(String source, String search) {
                if (source == null || search == null) return false;
                return source.toLowerCase().contains(search.toLowerCase());
            }

            public static String join(List<String> values, String separator) {
                return String.join(separator, values);
            }

            public static String plural(int amount, String singular, String plural) {
                return amount == 1 ? amount + " " + singular : amount + " " + plural;
            }

            public static String progressBar(int current, int max, int width) {
                if (max <= 0) max = 1;
                current = Maths.clamp(current, 0, max);
                int filled = (int) Math.round((current / (double) max) * width);
                return "[" + repeat("#", filled) + repeat("-", width - filled) + "] " + current + "/" + max;
            }
        }

        public static final class Time {
            private Time() {
            }

            public static String nowReadable() {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            }

            public static String timestampForFile() {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            }

            public static String formatDurationMs(long milliseconds) {
                Duration duration = Duration.ofMillis(milliseconds);
                long hours = duration.toHours();
                long minutes = duration.toMinutesPart();
                long seconds = duration.toSecondsPart();
                long millis = duration.toMillisPart();

                if (hours > 0) return hours + "h " + minutes + "m " + seconds + "s";
                if (minutes > 0) return minutes + "m " + seconds + "s";
                if (seconds > 0) return seconds + "s " + millis + "ms";
                return millis + "ms";
            }

            public static final class Stopwatch {
                private long startNano;
                private long endNano;
                private boolean running;

                public void start() {
                    startNano = System.nanoTime();
                    endNano = 0;
                    running = true;
                }

                public void stop() {
                    if (running) {
                        endNano = System.nanoTime();
                        running = false;
                    }
                }

                public long elapsedMs() {
                    long end = running ? System.nanoTime() : endNano;
                    return (end - startNano) / 1_000_000;
                }
            }
        }

        public static final class Files {
            private Files() {
            }

            public static boolean exists(Path path) {
                return path != null && java.nio.file.Files.exists(path);
            }

            public static boolean ensureDirectory(Path directory) {
                try {
                    java.nio.file.Files.createDirectories(directory);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }

            public static boolean writeText(Path path, String text) {
                try {
                    Path parent = path.getParent();
                    if (parent != null) ensureDirectory(parent);
                    java.nio.file.Files.writeString(path, text == null ? "" : text, StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }

            public static boolean appendLine(Path path, String text) {
                try {
                    Path parent = path.getParent();
                    if (parent != null) ensureDirectory(parent);
                    java.nio.file.Files.writeString(path, (text == null ? "" : text) + System.lineSeparator(), StandardCharsets.UTF_8,
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }

            public static String readText(Path path) {
                try {
                    if (!exists(path)) return "";
                    return java.nio.file.Files.readString(path, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    return "";
                }
            }

            public static List<String> readLines(Path path) {
                try {
                    if (!exists(path)) return new ArrayList<>();
                    return java.nio.file.Files.readAllLines(path, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    return new ArrayList<>();
                }
            }

            public static boolean deleteIfExists(Path path) {
                try {
                    return java.nio.file.Files.deleteIfExists(path);
                } catch (IOException e) {
                    return false;
                }
            }

            public static boolean saveKeyValues(Path path, Map<String, String> values) {
                StringBuilder builder = new StringBuilder();

                for (Map.Entry<String, String> entry : values.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue()).append(System.lineSeparator());
                }

                return writeText(path, builder.toString());
            }

            public static Map<String, String> loadKeyValues(Path path) {
                Map<String, String> values = new LinkedHashMap<>();

                for (String line : readLines(path)) {
                    if (!line.contains("=")) continue;
                    String[] parts = line.split("=", 2);
                    values.put(parts[0].trim(), parts[1].trim());
                }

                return values;
            }
        }

        public static final class Game2D {
            private Game2D() {
            }

            public static boolean intersects(Rectangle a, Rectangle b) {
                return a != null && b != null && a.intersects(b);
            }

            public static boolean pointInRect(int x, int y, Rectangle rect) {
                return rect != null && rect.contains(x, y);
            }

            public static boolean circleIntersectsCircle(double x1, double y1, double r1, double x2, double y2, double r2) {
                return Maths.distance(x1, y1, x2, y2) <= r1 + r2;
            }

            public static Rectangle rectFromCenter(int centerX, int centerY, int width, int height) {
                return new Rectangle(centerX - width / 2, centerY - height / 2, width, height);
            }

            public static Point center(Rectangle rect) {
                return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
            }

            public static boolean insideScreen(double x, double y, int width, int height) {
                return x >= 0 && y >= 0 && x <= width && y <= height;
            }

            public static Point clampPointToScreen(int x, int y, int width, int height) {
                return new Point(Maths.clamp(x, 0, width), Maths.clamp(y, 0, height));
            }

            public static Point shakeOffset(int strength) {
                if (strength <= 0) return new Point(0, 0);
                return new Point(Randoms.intRange(-strength, strength), Randoms.intRange(-strength, strength));
            }

            public static boolean reachedTarget(double x, double y, double targetX, double targetY, double threshold) {
                return Maths.distance(x, y, targetX, targetY) <= threshold;
            }
        }

        public static final class Stats {
            private Stats() {
            }

            public static int damage(int health, int amount) {
                return Math.max(0, health - Math.max(0, amount));
            }

            public static int heal(int health, int amount, int maxHealth) {
                return Maths.clamp(health + Math.max(0, amount), 0, maxHealth);
            }

            public static int addBounded(int value, int amount, int min, int max) {
                return Maths.clamp(value + amount, min, max);
            }

            public static int decay(int value, int amount, int min) {
                return Math.max(min, value - Math.max(0, amount));
            }

            public static int levelFromXp(int xp, int xpPerLevel) {
                if (xpPerLevel <= 0) xpPerLevel = 1;
                return Math.max(1, (xp / xpPerLevel) + 1);
            }

            public static int xpForLevel(int level, int xpPerLevel) {
                return Math.max(0, level - 1) * Math.max(1, xpPerLevel);
            }

            public static int xpProgressCurrentLevel(int xp, int xpPerLevel) {
                if (xpPerLevel <= 0) xpPerLevel = 1;
                return xp % xpPerLevel;
            }
        }

        public static final class Colours {
            private Colours() {
            }

            public static Color withAlpha(Color color, int alpha) {
                alpha = Maths.clamp(alpha, 0, 255);
                return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            }

            public static Color darker(Color color, double amount) {
                amount = Maths.clamp(amount, 0, 1);
                return new Color(
                        (int) (color.getRed() * (1 - amount)),
                        (int) (color.getGreen() * (1 - amount)),
                        (int) (color.getBlue() * (1 - amount)),
                        color.getAlpha()
                );
            }

            public static Color brighter(Color color, double amount) {
                amount = Maths.clamp(amount, 0, 1);
                return new Color(
                        (int) Maths.lerp(color.getRed(), 255, amount),
                        (int) Maths.lerp(color.getGreen(), 255, amount),
                        (int) Maths.lerp(color.getBlue(), 255, amount),
                        color.getAlpha()
                );
            }

            public static Color lerpColor(Color a, Color b, double amount) {
                amount = Maths.clamp(amount, 0, 1);
                return new Color(
                        (int) Maths.lerp(a.getRed(), b.getRed(), amount),
                        (int) Maths.lerp(a.getGreen(), b.getGreen(), amount),
                        (int) Maths.lerp(a.getBlue(), b.getBlue(), amount),
                        (int) Maths.lerp(a.getAlpha(), b.getAlpha(), amount)
                );
            }

            public static String toHex(Color color) {
                return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
            }

            public static Color fromHex(String hex) {
                if (hex == null) return Color.WHITE;
                hex = hex.replace("#", "").trim();
                if (hex.length() != 6) return Color.WHITE;

                try {
                    int r = Integer.parseInt(hex.substring(0, 2), 16);
                    int g = Integer.parseInt(hex.substring(2, 4), 16);
                    int b = Integer.parseInt(hex.substring(4, 6), 16);
                    return new Color(r, g, b);
                } catch (NumberFormatException e) {
                    return Color.WHITE;
                }
            }

            public static Color contrastText(Color background) {
                double brightness = (background.getRed() * 0.299 + background.getGreen() * 0.587 + background.getBlue() * 0.114);
                return brightness > 155 ? Color.BLACK : Color.WHITE;
            }
        }

        public static final class UI {
            private UI() {
            }

            public static void styleButton(JButton button) {
                button.setFont(new Font("Segoe UI", Font.BOLD, 14));
                button.setForeground(Color.WHITE);
                button.setBackground(new Color(48, 48, 65));
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createLineBorder(new Color(95, 95, 125)));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public static JLabel label(String text, int size, int style, Color color) {
                JLabel label = new JLabel(text);
                label.setFont(new Font("Segoe UI", style, size));
                label.setForeground(color);
                return label;
            }

            public static void showInfo(Component parent, String title, String message) {
                JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
            }

            public static void copyToClipboard(String text) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text == null ? "" : text), null);
            }
        }

        public static final class Debug {
            private Debug() {
            }

            public static void print(String label, Object value) {
                System.out.println("[DEBUG] " + label + ": " + value);
            }

            public static Map<String, Object> map(Object... keyValues) {
                Map<String, Object> result = new HashMap<>();
                for (int i = 0; i < keyValues.length - 1; i += 2) {
                    result.put(String.valueOf(keyValues[i]), keyValues[i + 1]);
                }
                return result;
            }

            public static String arrayToString(int[] values) {
                return Arrays.toString(values);
            }
        }
    }
}
