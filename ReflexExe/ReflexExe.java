import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ReflexExe extends JFrame {

    private enum TestMode {
        KEYBOARD,
        MOUSE
    }

    private enum GameState {
        READY,
        WAITING,
        GO,
        RESULT
    }

    private final Random random = new Random();

    private JPanel mainPanel;
    private JPanel testPanel;

    private JLabel titleLabel;
    private JLabel promptLabel;
    private JLabel subPromptLabel;
    private JLabel lastResultLabel;
    private JLabel keyboardStatsLabel;
    private JLabel mouseStatsLabel;
    private JLabel falseStartLabel;

    private JButton keyboardButton;
    private JButton mouseButton;
    private JButton resetButton;

    private GameState state = GameState.READY;
    private TestMode currentMode = null;

    private Timer waitTimer;
    private long startNanoTime;

    private int keyboardAttempts = 0;
    private int mouseAttempts = 0;

    private long keyboardBestMs = Long.MAX_VALUE;
    private long mouseBestMs = Long.MAX_VALUE;

    private long keyboardTotalMs = 0;
    private long mouseTotalMs = 0;

    private int falseStarts = 0;

    public ReflexExe() {
        setupWindow();
        setupUI();
        setupKeyboardInput();
        setupMouseInput();
        updateStats();
    }

    private void setupWindow() {
        setTitle("Reflex.exe");
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupUI() {
        mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(12, 12, 16));
        mainPanel.setFocusable(true);
        setContentPane(mainPanel);

        titleLabel = new JLabel("Reflex.exe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 25, 850, 55);
        mainPanel.add(titleLabel);

        JLabel descriptionLabel = new JLabel(
                "Test how fast Java receives your keyboard press or mouse click.",
                SwingConstants.CENTER
        );
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionLabel.setForeground(new Color(170, 170, 185));
        descriptionLabel.setBounds(0, 78, 850, 30);
        mainPanel.add(descriptionLabel);

        testPanel = new JPanel(null);
        testPanel.setBackground(new Color(28, 28, 36));
        testPanel.setBorder(BorderFactory.createLineBorder(new Color(75, 75, 95), 2));
        testPanel.setBounds(50, 125, 750, 210);
        testPanel.setFocusable(true);
        mainPanel.add(testPanel);

        promptLabel = new JLabel("Choose a test to begin", SwingConstants.CENTER);
        promptLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setBounds(0, 55, 750, 55);
        testPanel.add(promptLabel);

        subPromptLabel = new JLabel("Keyboard = SPACE | Mouse = Left Click", SwingConstants.CENTER);
        subPromptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        subPromptLabel.setForeground(new Color(180, 180, 195));
        subPromptLabel.setBounds(0, 115, 750, 35);
        testPanel.add(subPromptLabel);

        keyboardButton = createButton("Keyboard Test", 80, 360);
        keyboardButton.addActionListener(e -> startTest(TestMode.KEYBOARD));
        mainPanel.add(keyboardButton);

        mouseButton = createButton("Mouse Test", 325, 360);
        mouseButton.addActionListener(e -> startTest(TestMode.MOUSE));
        mainPanel.add(mouseButton);

        resetButton = createButton("Reset Stats", 570, 360);
        resetButton.addActionListener(e -> resetStats());
        mainPanel.add(resetButton);

        lastResultLabel = createInfoLabel("Last Result: None", 50, 430, 750, 25);
        keyboardStatsLabel = createInfoLabel("Keyboard: No attempts", 50, 460, 750, 25);
        mouseStatsLabel = createInfoLabel("Mouse: No attempts", 50, 490, 750, 25);
        falseStartLabel = createInfoLabel("False Starts: 0", 50, 520, 750, 25);

        mainPanel.add(lastResultLabel);
        mainPanel.add(keyboardStatsLabel);
        mainPanel.add(mouseStatsLabel);
        mainPanel.add(falseStartLabel);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 45);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(55, 55, 75));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 150), 1));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(75, 75, 105));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(55, 55, 75));
            }
        });

        return button;
    }

    private JLabel createInfoLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(new Color(210, 210, 220));
        return label;
    }

    private void setupKeyboardInput() {
        InputMap inputMap = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mainPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "spacePressed");

        actionMap.put("spacePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleInput(TestMode.KEYBOARD);
            }
        });
    }

    private void setupMouseInput() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainPanel.requestFocusInWindow();

                if (SwingUtilities.isLeftMouseButton(e)) {
                    handleInput(TestMode.MOUSE);
                }
            }
        };

        mainPanel.addMouseListener(mouseAdapter);
        testPanel.addMouseListener(mouseAdapter);
    }

    private void startTest(TestMode mode) {
        if (state == GameState.WAITING || state == GameState.GO) {
            return;
        }

        currentMode = mode;
        state = GameState.WAITING;

        keyboardButton.setEnabled(false);
        mouseButton.setEnabled(false);
        resetButton.setEnabled(false);

        testPanel.setBackground(new Color(45, 35, 20));
        promptLabel.setText("WAIT...");
        promptLabel.setForeground(new Color(255, 210, 120));

        if (mode == TestMode.KEYBOARD) {
            subPromptLabel.setText("Get ready. Press SPACE when it says GO!");
        } else {
            subPromptLabel.setText("Get ready. Left click when it says GO!");
        }

        int delay = 1000 + random.nextInt(2500);

        waitTimer = new Timer(delay, e -> showGoPrompt());
        waitTimer.setRepeats(false);
        waitTimer.start();

        mainPanel.requestFocusInWindow();
    }

    private void showGoPrompt() {
        state = GameState.GO;

        testPanel.setBackground(new Color(25, 90, 45));
        promptLabel.setForeground(new Color(120, 255, 160));
        promptLabel.setText("GO!");

        if (currentMode == TestMode.KEYBOARD) {
            subPromptLabel.setText("PRESS SPACE NOW");
        } else {
            subPromptLabel.setText("LEFT CLICK NOW");
        }

        /*
         * Forces Swing to paint the GO prompt immediately.
         * The timer starts after this repaint call, giving a cleaner measurement.
         */
        testPanel.paintImmediately(0, 0, testPanel.getWidth(), testPanel.getHeight());

        startNanoTime = System.nanoTime();
    }

    private void handleInput(TestMode inputMode) {
        if (currentMode == null) {
            return;
        }

        if (inputMode != currentMode) {
            return;
        }

        if (state == GameState.WAITING) {
            falseStart();
            return;
        }

        if (state != GameState.GO) {
            return;
        }

        long endNanoTime = System.nanoTime();
        long reactionMs = (endNanoTime - startNanoTime) / 1_000_000;

        recordResult(inputMode, reactionMs);
    }

    private void falseStart() {
        falseStarts++;

        if (waitTimer != null) {
            waitTimer.stop();
        }

        state = GameState.RESULT;

        testPanel.setBackground(new Color(90, 25, 25));
        promptLabel.setForeground(new Color(255, 120, 120));
        promptLabel.setText("TOO EARLY");
        subPromptLabel.setText("You pressed before GO appeared.");

        lastResultLabel.setText("Last Result: False start");
        updateStats();

        unlockButtons();
    }

    private void recordResult(TestMode mode, long reactionMs) {
        state = GameState.RESULT;

        if (mode == TestMode.KEYBOARD) {
            keyboardAttempts++;
            keyboardTotalMs += reactionMs;
            keyboardBestMs = Math.min(keyboardBestMs, reactionMs);
            lastResultLabel.setText("Last Result: Keyboard registered in " + reactionMs + " ms");
        } else {
            mouseAttempts++;
            mouseTotalMs += reactionMs;
            mouseBestMs = Math.min(mouseBestMs, reactionMs);
            lastResultLabel.setText("Last Result: Mouse registered in " + reactionMs + " ms");
        }

        testPanel.setBackground(new Color(28, 28, 36));
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setText(reactionMs + " ms");
        subPromptLabel.setText(getReactionRating(reactionMs));

        updateStats();
        unlockButtons();
    }

    private String getReactionRating(long ms) {
        if (ms < 120) {
            return "Insane reaction. Either cracked or lucky.";
        }

        if (ms < 180) {
            return "Very fast reaction.";
        }

        if (ms < 250) {
            return "Solid reaction.";
        }

        if (ms < 350) {
            return "Average reaction.";
        }

        return "Slow reaction. Wake up.";
    }

    private void updateStats() {
        if (keyboardAttempts == 0) {
            keyboardStatsLabel.setText("Keyboard: No attempts");
        } else {
            long average = keyboardTotalMs / keyboardAttempts;
            keyboardStatsLabel.setText(
                    "Keyboard: Attempts " + keyboardAttempts +
                            " | Best " + keyboardBestMs + " ms" +
                            " | Average " + average + " ms"
            );
        }

        if (mouseAttempts == 0) {
            mouseStatsLabel.setText("Mouse: No attempts");
        } else {
            long average = mouseTotalMs / mouseAttempts;
            mouseStatsLabel.setText(
                    "Mouse: Attempts " + mouseAttempts +
                            " | Best " + mouseBestMs + " ms" +
                            " | Average " + average + " ms"
            );
        }

        falseStartLabel.setText("False Starts: " + falseStarts);
    }

    private void unlockButtons() {
        keyboardButton.setEnabled(true);
        mouseButton.setEnabled(true);
        resetButton.setEnabled(true);

        currentMode = null;
        mainPanel.requestFocusInWindow();
    }

    private void resetStats() {
        keyboardAttempts = 0;
        mouseAttempts = 0;

        keyboardBestMs = Long.MAX_VALUE;
        mouseBestMs = Long.MAX_VALUE;

        keyboardTotalMs = 0;
        mouseTotalMs = 0;

        falseStarts = 0;
        state = GameState.READY;
        currentMode = null;

        if (waitTimer != null) {
            waitTimer.stop();
        }

        testPanel.setBackground(new Color(28, 28, 36));
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setText("Choose a test to begin");
        subPromptLabel.setText("Keyboard = SPACE | Mouse = Left Click");
        lastResultLabel.setText("Last Result: None");

        updateStats();
        unlockButtons();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReflexExe app = new ReflexExe();
            app.setVisible(true);
            app.mainPanel.requestFocusInWindow();
        });
    }
}
