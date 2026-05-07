import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;

public class RockExe extends JFrame {
    private static final String VERSION = "1.1.0";
    private static final int WINDOW_WIDTH = 850;
    private static final int WINDOW_HEIGHT = 620;

    private static final Color BACKGROUND = new Color(14, 14, 18);
    private static final Color CARD_BACKGROUND = new Color(24, 24, 32);
    private static final Color BUTTON_BACKGROUND = new Color(45, 45, 60);
    private static final Color BUTTON_HOVER_BACKGROUND = new Color(65, 65, 88);

    private final Random random = new Random();

    private JPanel rockStage;
    private JLabel titleLabel;
    private JLabel rockLabel;
    private JLabel messageLabel;
    private JLabel happinessLabel;
    private JLabel cleanlinessLabel;
    private JLabel energyLabel;
    private JLabel boredomLabel;
    private JLabel respectLabel;

    private int happiness;
    private int cleanliness;
    private int energy;
    private int boredom;
    private int respect;

    private Timer shakeTimer;
    private Point rockHome;

    public RockExe() {
        loadSavedStats();
        setupWindow();
        setupInterface();
        clampStats();
        updateStats();
        updateRockMood();
    }

    private void loadSavedStats() {
        SaveManager.RockStats stats = SaveManager.loadStats();
        happiness = stats.happiness;
        cleanliness = stats.cleanliness;
        energy = stats.energy;
        boredom = stats.boredom;
        respect = stats.respect;
    }

    private void setupWindow() {
        setTitle("Rock.exe v" + VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 560));
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private void setupInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout(18, 18));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(22, 28, 28, 28));
        setContentPane(mainPanel);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        headerPanel.setOpaque(false);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        titleLabel = new JLabel("Rock.exe", SwingConstants.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("A deeply serious pet rock simulator.", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(160, 160, 175));
        headerPanel.add(subtitleLabel);

        JPanel rockPanel = new JPanel(new BorderLayout(0, 14));
        rockPanel.setOpaque(false);
        mainPanel.add(rockPanel, BorderLayout.CENTER);

        rockStage = new JPanel(null);
        rockStage.setOpaque(false);
        rockStage.setPreferredSize(new Dimension(260, 170));
        rockStage.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                if (shakeTimer == null || !shakeTimer.isRunning()) {
                    centerRockLabel();
                }
            }
        });
        rockPanel.add(rockStage, BorderLayout.CENTER);

        rockLabel = new JLabel(getRockSymbol(), SwingConstants.CENTER);
        rockLabel.setFont(getRockFont(112));
        rockLabel.setSize(260, 170);
        rockStage.add(rockLabel);

        messageLabel = new JLabel("The rock is doing rock things.", SwingConstants.CENTER);
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        messageLabel.setForeground(new Color(210, 210, 220));
        rockPanel.add(messageLabel, BorderLayout.SOUTH);

        JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        lowerPanel.setOpaque(false);
        lowerPanel.setPreferredSize(new Dimension(780, 190));
        mainPanel.add(lowerPanel, BorderLayout.SOUTH);

        lowerPanel.add(createStatsPanel());
        lowerPanel.add(createActionButtons());

        pack();
        setLocationRelativeTo(null);
        SwingUtilities.invokeLater(this::centerRockLabel);
    }

    private void centerRockLabel() {
        if (rockStage == null || rockLabel == null) {
            return;
        }

        int x = Math.max(0, (rockStage.getWidth() - rockLabel.getWidth()) / 2);
        int y = Math.max(0, (rockStage.getHeight() - rockLabel.getHeight()) / 2);
        rockLabel.setLocation(x, y);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(6, 1, 0, 6));
        statsPanel.setBackground(CARD_BACKGROUND);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 65, 82)),
                new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel statsTitle = new JLabel("Rock Stats");
        statsTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        statsTitle.setForeground(Color.WHITE);
        statsPanel.add(statsTitle);

        happinessLabel = createStatLabel();
        cleanlinessLabel = createStatLabel();
        energyLabel = createStatLabel();
        boredomLabel = createStatLabel();
        respectLabel = createStatLabel();

        statsPanel.add(happinessLabel);
        statsPanel.add(cleanlinessLabel);
        statsPanel.add(energyLabel);
        statsPanel.add(boredomLabel);
        statsPanel.add(respectLabel);
        return statsPanel;
    }

    private JLabel createStatLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        label.setForeground(new Color(218, 218, 228));
        return label;
    }

    private JPanel createActionButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setOpaque(false);

        buttonPanel.add(createButton("Feed Rock", this::feedRock));
        buttonPanel.add(createButton("Wash Rock", this::washRock));
        buttonPanel.add(createButton("Talk To Rock", this::talkToRock));
        buttonPanel.add(createButton("Insult Rock", this::insultRock));
        buttonPanel.add(createButton("Let Rock Sleep", this::sleepRock));
        buttonPanel.add(createButton("Praise Rock", this::praiseRock));
        buttonPanel.add(createButton("Throw Rock", this::throwRock));
        buttonPanel.add(createButton("Stare At Rock", this::stareAtRock));
        return buttonPanel;
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_BACKGROUND);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 130)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(BUTTON_HOVER_BACKGROUND);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(BUTTON_BACKGROUND);
            }
        });
        return button;
    }

    private void feedRock(ActionEvent event) {
        changeStats(randomValue(3, 8), -randomValue(1, 5), randomValue(1, 4), randomValue(1, 3), 0);
        showMessage(randomMessage(
                "You fed the rock. It did not eat.",
                "The rock accepts your imaginary food.",
                "The rock looks exactly the same.",
                "The rock gained emotional calories."
        ));
        afterAction(false);
    }

    private void washRock(ActionEvent event) {
        changeStats(randomValue(1, 4), randomValue(8, 15), -randomValue(1, 3), 0, 0);
        showMessage(randomMessage(
                "The rock is now slightly less dusty.",
                "You cleaned the rock. It remains unimpressed.",
                "The rock shines with unnecessary confidence.",
                "The rock is cleaner than your decisions."
        ));
        afterAction(false);
    }

    private void talkToRock(ActionEvent event) {
        changeStats(randomValue(2, 7), 0, 0, -randomValue(4, 9), randomValue(0, 2));
        showMessage(randomMessage(
                "You spoke to the rock. The rock listened aggressively.",
                "The rock says nothing, but somehow says everything.",
                "The rock now knows too much.",
                "You and the rock shared a powerful silence."
        ));
        afterAction(false);
    }

    private void insultRock(ActionEvent event) {
        changeStats(-randomValue(6, 12), 0, 0, -randomValue(1, 4), -randomValue(1, 4));
        showMessage(randomMessage(
                "The rock remembers that.",
                "You insulted the rock. Bold move.",
                "The rock is emotionally unavailable.",
                "The rock judges you harder now."
        ));
        afterAction(false);
    }

    private void sleepRock(ActionEvent event) {
        changeStats(randomValue(1, 4), 0, randomValue(10, 20), randomValue(3, 7), 0);
        showMessage(randomMessage(
                "The rock sleeps. Probably.",
                "The rock entered deep geological rest.",
                "The rock has been asleep for 40 million years already.",
                "The rock is now fully recharged and still useless."
        ));
        afterAction(false);
    }

    private void praiseRock(ActionEvent event) {
        changeStats(randomValue(5, 12), 0, 0, -randomValue(2, 5), randomValue(2, 6));
        showMessage(randomMessage(
                "The rock accepts your praise with terrifying confidence.",
                "The rock feels powerful.",
                "The rock's ego has increased.",
                "The rock now believes it is the main character."
        ));
        afterAction(false);
    }

    private void throwRock(ActionEvent event) {
        changeStats(-randomValue(10, 20), -randomValue(5, 12), -randomValue(5, 12), -randomValue(8, 15), randomValue(1, 5));
        showMessage(randomMessage(
                "You threw the rock. The rock enjoyed the violence.",
                "The rock travelled three feet and learned nothing.",
                "The rock has seen the floor. It was not impressed.",
                "The rock has entered combat mode."
        ));
        afterAction(true);
    }

    private void stareAtRock(ActionEvent event) {
        changeStats(randomValue(0, 4), 0, 0, -randomValue(1, 5), randomValue(0, 3));
        showMessage(randomMessage(
                "You stared at the rock. The rock stared back.",
                "This became uncomfortable very quickly.",
                "The rock blinked. Maybe.",
                "You have formed a strange bond with the rock."
        ));
        afterAction(false);
    }

    private void changeStats(int happinessDelta, int cleanlinessDelta, int energyDelta, int boredomDelta, int respectDelta) {
        happiness += happinessDelta;
        cleanliness += cleanlinessDelta;
        energy += energyDelta;
        boredom += boredomDelta;
        respect += respectDelta;
    }

    private void afterAction(boolean shouldShake) {
        naturalDecay();
        clampStats();
        updateStats();
        updateRockMood();
        SaveManager.saveStats(currentStats());

        if (shouldShake) {
            shakeRock();
        }
    }

    private void naturalDecay() {
        // Every action costs a tiny bit of time; even rocks have schedules.
        boredom += randomValue(1, 4);
        cleanliness -= randomValue(0, 2);
        energy -= randomValue(0, 2);
    }

    private void clampStats() {
        happiness = clamp(happiness, 0, 100);
        cleanliness = clamp(cleanliness, 0, 100);
        energy = clamp(energy, 0, 100);
        boredom = clamp(boredom, 0, 100);
        respect = clamp(respect, -100, 999);
    }

    private void updateStats() {
        happinessLabel.setText("Happiness: " + happiness + "/100 " + bar(happiness));
        cleanlinessLabel.setText("Cleanliness: " + cleanliness + "/100 " + bar(cleanliness));
        energyLabel.setText("Energy: " + energy + "/100 " + bar(energy));
        boredomLabel.setText("Boredom: " + boredom + "/100 " + bar(100 - boredom));
        respectLabel.setText("Respect: " + respect);
    }

    private void updateRockMood() {
        if (happiness >= 80 && respect >= 20) {
            rockLabel.setText(canDisplay("💎") ? "💎" : "DIAMOND ROCK");
            titleLabel.setText("Rock.exe — Ascended Rock");
        } else if (happiness <= 20) {
            rockLabel.setText(getRockSymbol());
            titleLabel.setText("Rock.exe — Sad Rock");
        } else if (boredom >= 80) {
            rockLabel.setText(canDisplay("🧱") ? "🧱" : "BRICK ROCK");
            titleLabel.setText("Rock.exe — Bored Rock");
        } else if (cleanliness <= 15) {
            rockLabel.setText(getRockSymbol());
            titleLabel.setText("Rock.exe — Dusty Rock");
        } else {
            rockLabel.setText(getRockSymbol());
            titleLabel.setText("Rock.exe");
        }
    }

    private void shakeRock() {
        if (shakeTimer != null && shakeTimer.isRunning()) {
            shakeTimer.stop();
            if (rockHome != null) {
                rockLabel.setLocation(rockHome);
            }
        }

        rockHome = rockLabel.getLocation();
        final int[] ticks = {0};
        shakeTimer = new Timer(35, event -> {
            int offsetX = randomValue(-8, 8);
            int offsetY = randomValue(-6, 6);
            rockLabel.setLocation(rockHome.x + offsetX, rockHome.y + offsetY);
            ticks[0]++;

            if (ticks[0] >= 12) {
                rockLabel.setLocation(rockHome);
                shakeTimer.stop();
            }
        });
        shakeTimer.start();
    }

    private String bar(int value) {
        int filled = clamp(value, 0, 100) / 20;
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < 5; i++) {
            builder.append(i < filled ? '#' : '-');
        }
        return builder.append(']').toString();
    }

    private SaveManager.RockStats currentStats() {
        return new SaveManager.RockStats(happiness, cleanliness, energy, boredom, respect);
    }

    private String getRockSymbol() {
        return canDisplay("🪨") ? "🪨" : "ROCK";
    }

    private Font getRockFont(int size) {
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, size);
        return canDisplay("🪨") ? emojiFont : new Font(Font.SANS_SERIF, Font.BOLD, 56);
    }

    private boolean canDisplay(String text) {
        return new Font("Segoe UI Emoji", Font.PLAIN, 12).canDisplayUpTo(text) == -1;
    }

    private int randomValue(int min, int max) {
        if (max < min) {
            return min;
        }
        return random.nextInt(max - min + 1) + min;
    }

    private String randomMessage(String... messages) {
        return messages[random.nextInt(messages.length)];
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Rock.exe needs a desktop display to show its Swing window.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            RockExe app = new RockExe();
            app.setVisible(true);
        });
    }
}
