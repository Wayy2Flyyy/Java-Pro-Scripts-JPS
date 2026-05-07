import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Random;

public class ButtonExe extends JFrame {
    private static final String VERSION = "1.1.0";
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 600;
    private static final int BUTTON_WIDTH = 230;
    private static final int BUTTON_HEIGHT = 70;
    private static final int SAFE_PADDING = 18;

    private static final Color DEFAULT_BACKGROUND = new Color(12, 12, 16);
    private static final Color PANEL_BACKGROUND = new Color(18, 18, 26);
    private static final Color BUTTON_BACKGROUND = new Color(120, 40, 255);
    private static final Color BUTTON_HOVER_BACKGROUND = new Color(150, 75, 255);

    private static final String[] MESSAGES = {
            "Why would you press that?",
            "You had one job.",
            "Button.exe is watching.",
            "That click felt personal.",
            "You are making it stronger.",
            "Stop. Or don't.",
            "Nothing happened. Probably.",
            "The button is learning.",
            "You clicked with confidence. Wrongly.",
            "This is how it begins.",
            "The button filed a complaint.",
            "A tiny chaos goblin applauds."
    };

    private static final String[] BUTTON_TEXTS = {
            "DO NOT PRESS",
            "WHY AGAIN?",
            "BAD IDEA",
            "CLICK ME",
            "DON'T.",
            "TOO LATE",
            "RUN",
            "ONE MORE?",
            "PRESS? NO.",
            "I DARE YOU"
    };

    private final Random random = new Random();

    private JPanel gamePanel;
    private JButton chaosButton;
    private JLabel scoreLabel;
    private JLabel messageLabel;

    private int score;
    private int clicks;
    private int highScore;

    public ButtonExe() {
        highScore = SaveManager.loadHighScore();
        setupWindow();
        setupUI();
        setupButtonAction();
    }

    private void setupWindow() {
        setTitle("Button.exe v" + VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(640, 460));
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private void setupUI() {
        JPanel rootPanel = new JPanel(new BorderLayout(0, 12));
        rootPanel.setBackground(DEFAULT_BACKGROUND);
        rootPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        setContentPane(rootPanel);

        JLabel titleLabel = new JLabel("Button.exe", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 42));
        rootPanel.add(titleLabel, BorderLayout.NORTH);

        gamePanel = new JPanel(null);
        gamePanel.setBackground(PANEL_BACKGROUND);
        gamePanel.setBorder(BorderFactory.createLineBorder(new Color(55, 55, 75), 2));
        gamePanel.setPreferredSize(new Dimension(780, 360));
        rootPanel.add(gamePanel, BorderLayout.CENTER);

        chaosButton = createChaosButton();
        gamePanel.add(chaosButton);
        gamePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                keepButtonInsidePlayArea();
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 8));
        bottomPanel.setOpaque(false);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setForeground(new Color(215, 215, 225));
        scoreLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        bottomPanel.add(scoreLabel, BorderLayout.NORTH);

        messageLabel = new JLabel("Press the button. Nothing bad will happen.", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(185, 185, 200));
        messageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        bottomPanel.add(messageLabel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        updateScoreLabel();
        SwingUtilities.invokeLater(this::centerButton);
    }

    private JButton createChaosButton() {
        JButton button = new JButton("DO NOT PRESS");
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_BACKGROUND);
        button.setBorder(BorderFactory.createLineBorder(new Color(205, 180, 255), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
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

    private void setupButtonAction() {
        chaosButton.addActionListener((ActionEvent event) -> {
            clicks++;
            int earned = randomValue(1, 15);
            score += earned;

            if (score > highScore) {
                highScore = score;
                SaveManager.saveHighScore(highScore);
            }

            updateScoreLabel();
            moveButtonSafely();
            changeBackground();
            showRandomMessage(earned);
            randomizeButtonText();
        });
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score + " | Clicks: " + clicks + " | High Score: " + highScore);
    }

    private void centerButton() {
        int x = Math.max(SAFE_PADDING, (gamePanel.getWidth() - BUTTON_WIDTH) / 2);
        int y = Math.max(SAFE_PADDING, (gamePanel.getHeight() - BUTTON_HEIGHT) / 2);
        chaosButton.setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        keepButtonInsidePlayArea();
    }

    private void moveButtonSafely() {
        int maxX = Math.max(SAFE_PADDING, gamePanel.getWidth() - chaosButton.getWidth() - SAFE_PADDING);
        int maxY = Math.max(SAFE_PADDING, gamePanel.getHeight() - chaosButton.getHeight() - SAFE_PADDING);

        int x = randomValue(SAFE_PADDING, maxX);
        int y = randomValue(SAFE_PADDING, maxY);
        chaosButton.setLocation(x, y);
    }

    private void keepButtonInsidePlayArea() {
        if (chaosButton == null || gamePanel.getWidth() <= 0 || gamePanel.getHeight() <= 0) {
            return;
        }

        Point location = chaosButton.getLocation();
        int maxX = Math.max(SAFE_PADDING, gamePanel.getWidth() - chaosButton.getWidth() - SAFE_PADDING);
        int maxY = Math.max(SAFE_PADDING, gamePanel.getHeight() - chaosButton.getHeight() - SAFE_PADDING);
        int safeX = clamp(location.x, SAFE_PADDING, maxX);
        int safeY = clamp(location.y, SAFE_PADDING, maxY);
        chaosButton.setLocation(safeX, safeY);
    }

    private void changeBackground() {
        // Keep colors dark enough for white text and the purple button to stay readable.
        Color randomDarkColor = new Color(randomValue(5, 45), randomValue(5, 45), randomValue(20, 80));
        gamePanel.setBackground(randomDarkColor);
    }

    private void showRandomMessage(int earned) {
        String message = randomChoice(MESSAGES);
        messageLabel.setText(message + "  +" + earned + " points");
    }

    private void randomizeButtonText() {
        chaosButton.setText(randomChoice(BUTTON_TEXTS));
    }

    private int randomValue(int min, int max) {
        if (max < min) {
            return min;
        }
        return random.nextInt(max - min + 1) + min;
    }

    private String randomChoice(String[] choices) {
        return choices[random.nextInt(choices.length)];
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Button.exe needs a desktop display to show its Swing window.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ButtonExe game = new ButtonExe();
            game.setVisible(true);
        });
    }
}
