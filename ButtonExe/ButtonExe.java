import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class ButtonExe extends JFrame {

    private final Random random = new Random();

    private JPanel gamePanel;
    private JButton chaosButton;
    private JLabel titleLabel;
    private JLabel scoreLabel;
    private JLabel messageLabel;

    private int score = 0;
    private int clicks = 0;

    private final String[] messages = {
            "Why would you press that?",
            "You had one job.",
            "Button.exe is watching.",
            "That click felt personal.",
            "You are making it stronger.",
            "Stop. Or don't.",
            "Nothing happened. Probably.",
            "The button is learning.",
            "You clicked with confidence. Wrongly.",
            "This is how it begins."
    };

    public ButtonExe() {
        setupWindow();
        setupUI();
        setupButtonAction();
    }

    private void setupWindow() {
        setTitle("Button.exe");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupUI() {
        gamePanel = new JPanel(null);
        gamePanel.setBackground(new Color(12, 12, 16));
        setContentPane(gamePanel);

        titleLabel = new JLabel("Button.exe", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setBounds(0, 30, 900, 60);
        gamePanel.add(titleLabel);

        scoreLabel = new JLabel("Score: 0 | Clicks: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(new Color(180, 180, 190));
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        scoreLabel.setBounds(0, 95, 900, 30);
        gamePanel.add(scoreLabel);

        messageLabel = new JLabel("Press the button. Nothing bad will happen.", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(130, 130, 145));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setBounds(0, 500, 900, 30);
        gamePanel.add(messageLabel);

        chaosButton = new JButton("DO NOT PRESS");
        chaosButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        chaosButton.setFocusPainted(false);
        chaosButton.setForeground(Color.WHITE);
        chaosButton.setBackground(new Color(120, 40, 255));
        chaosButton.setBorder(BorderFactory.createLineBorder(new Color(190, 160, 255), 2));
        chaosButton.setBounds(340, 240, 220, 70);
        gamePanel.add(chaosButton);
    }

    private void setupButtonAction() {
        chaosButton.addActionListener((ActionEvent e) -> {
            clicks++;
            int earned = random.nextInt(15) + 1;
            score += earned;

            updateLabels();
            moveButton();
            changeBackground();
            showRandomMessage(earned);
            randomButtonText();
        });
    }

    private void updateLabels() {
        scoreLabel.setText("Score: " + score + " | Clicks: " + clicks);
    }

    private void moveButton() {
        int buttonWidth = chaosButton.getWidth();
        int buttonHeight = chaosButton.getHeight();

        int maxX = gamePanel.getWidth() - buttonWidth - 30;
        int maxY = gamePanel.getHeight() - buttonHeight - 80;

        int x = random.nextInt(Math.max(maxX, 1));
        int y = 150 + random.nextInt(Math.max(maxY - 150, 1));

        chaosButton.setBounds(x, y, buttonWidth, buttonHeight);
    }

    private void changeBackground() {
        int red = random.nextInt(35);
        int green = random.nextInt(35);
        int blue = random.nextInt(55) + 10;

        gamePanel.setBackground(new Color(red, green, blue));
    }

    private void showRandomMessage(int earned) {
        String message = messages[random.nextInt(messages.length)];
        messageLabel.setText(message + "  +" + earned + " points");
    }

    private void randomButtonText() {
        String[] buttonTexts = {
                "DO NOT PRESS",
                "WHY AGAIN?",
                "BAD IDEA",
                "CLICK ME",
                "DON'T.",
                "TOO LATE",
                "RUN",
                "ONE MORE?"
        };

        chaosButton.setText(buttonTexts[random.nextInt(buttonTexts.length)]);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ButtonExe game = new ButtonExe();
            game.setVisible(true);
        });
    }
}
