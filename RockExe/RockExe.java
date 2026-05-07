import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class RockExe extends JFrame {

    private final Random random = new Random();

    private JPanel mainPanel;

    private JLabel titleLabel;
    private JLabel rockLabel;
    private JLabel messageLabel;

    private JLabel happinessLabel;
    private JLabel cleanlinessLabel;
    private JLabel energyLabel;
    private JLabel boredomLabel;
    private JLabel respectLabel;

    private int happiness = 50;
    private int cleanliness = 50;
    private int energy = 50;
    private int boredom = 50;
    private int respect = 0;

    public RockExe() {
        setupWindow();
        setupInterface();
        updateStats();
    }

    private void setupWindow() {
        setTitle("Rock.exe");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupInterface() {
        mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(14, 14, 18));
        setContentPane(mainPanel);

        titleLabel = new JLabel("Rock.exe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 25, 850, 55);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("A deeply serious pet rock simulator.", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(150, 150, 165));
        subtitleLabel.setBounds(0, 80, 850, 30);
        mainPanel.add(subtitleLabel);

        rockLabel = new JLabel("🪨", SwingConstants.CENTER);
        rockLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 110));
        rockLabel.setBounds(305, 135, 240, 140);
        mainPanel.add(rockLabel);

        messageLabel = new JLabel("The rock is doing rock things.", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        messageLabel.setForeground(new Color(205, 205, 215));
        messageLabel.setBounds(50, 285, 750, 35);
        mainPanel.add(messageLabel);

        createStatsPanel();
        createActionButtons();
    }

    private void createStatsPanel() {
        JPanel statsPanel = new JPanel(null);
        statsPanel.setBackground(new Color(24, 24, 32));
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 75)));
        statsPanel.setBounds(55, 345, 330, 170);
        mainPanel.add(statsPanel);

        JLabel statsTitle = new JLabel("Rock Stats");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setForeground(Color.WHITE);
        statsTitle.setBounds(20, 10, 250, 30);
        statsPanel.add(statsTitle);

        happinessLabel = createStatLabel(20, 45);
        cleanlinessLabel = createStatLabel(20, 68);
        energyLabel = createStatLabel(20, 91);
        boredomLabel = createStatLabel(20, 114);
        respectLabel = createStatLabel(20, 137);

        statsPanel.add(happinessLabel);
        statsPanel.add(cleanlinessLabel);
        statsPanel.add(energyLabel);
        statsPanel.add(boredomLabel);
        statsPanel.add(respectLabel);
    }

    private JLabel createStatLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(new Color(210, 210, 220));
        label.setBounds(x, y, 280, 22);
        return label;
    }

    private void createActionButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBackground(new Color(14, 14, 18));
        buttonPanel.setBounds(430, 345, 360, 170);
        mainPanel.add(buttonPanel);

        buttonPanel.add(createButton("Feed Rock", this::feedRock));
        buttonPanel.add(createButton("Wash Rock", this::washRock));
        buttonPanel.add(createButton("Talk To Rock", this::talkToRock));
        buttonPanel.add(createButton("Insult Rock", this::insultRock));
        buttonPanel.add(createButton("Let Rock Sleep", this::sleepRock));
        buttonPanel.add(createButton("Praise Rock", this::praiseRock));
        buttonPanel.add(createButton("Throw Rock", this::throwRock));
        buttonPanel.add(createButton("Stare At Rock", this::stareAtRock));
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 45, 60));
        button.setBorder(BorderFactory.createLineBorder(new Color(95, 95, 120)));
        button.addActionListener(action);
        return button;
    }

    private void feedRock(ActionEvent event) {
        happiness += randomValue(3, 8);
        energy += randomValue(1, 4);
        cleanliness -= randomValue(1, 5);
        boredom += randomValue(1, 3);

        showMessage(randomMessage(
                "You fed the rock. It did not eat.",
                "The rock accepts your imaginary food.",
                "The rock looks exactly the same.",
                "The rock gained emotional calories."
        ));

        afterAction();
    }

    private void washRock(ActionEvent event) {
        cleanliness += randomValue(8, 15);
        happiness += randomValue(1, 4);
        energy -= randomValue(1, 3);

        showMessage(randomMessage(
                "The rock is now slightly less dusty.",
                "You cleaned the rock. It remains unimpressed.",
                "The rock shines with unnecessary confidence.",
                "The rock is cleaner than your decisions."
        ));

        afterAction();
    }

    private void talkToRock(ActionEvent event) {
        happiness += randomValue(2, 7);
        boredom -= randomValue(4, 9);
        respect += randomValue(0, 2);

        showMessage(randomMessage(
                "You spoke to the rock. The rock listened aggressively.",
                "The rock says nothing, but somehow says everything.",
                "The rock now knows too much.",
                "You and the rock shared a powerful silence."
        ));

        afterAction();
    }

    private void insultRock(ActionEvent event) {
        happiness -= randomValue(6, 12);
        respect -= randomValue(1, 4);
        boredom -= randomValue(1, 4);

        showMessage(randomMessage(
                "The rock remembers that.",
                "You insulted the rock. Bold move.",
                "The rock is emotionally unavailable.",
                "The rock judges you harder now."
        ));

        afterAction();
    }

    private void sleepRock(ActionEvent event) {
        energy += randomValue(10, 20);
        boredom += randomValue(3, 7);
        happiness += randomValue(1, 4);

        showMessage(randomMessage(
                "The rock sleeps. Probably.",
                "The rock entered deep geological rest.",
                "The rock has been asleep for 40 million years already.",
                "The rock is now fully recharged and still useless."
        ));

        afterAction();
    }

    private void praiseRock(ActionEvent event) {
        happiness += randomValue(5, 12);
        respect += randomValue(2, 6);
        boredom -= randomValue(2, 5);

        showMessage(randomMessage(
                "The rock accepts your praise with terrifying confidence.",
                "The rock feels powerful.",
                "The rock's ego has increased.",
                "The rock now believes it is the main character."
        ));

        afterAction();
    }

    private void throwRock(ActionEvent event) {
        happiness -= randomValue(10, 20);
        energy -= randomValue(5, 12);
        cleanliness -= randomValue(5, 12);
        respect += randomValue(1, 5);
        boredom -= randomValue(8, 15);

        showMessage(randomMessage(
                "You threw the rock. The rock enjoyed the violence.",
                "The rock travelled three feet and learned nothing.",
                "The rock has seen the floor. It was not impressed.",
                "The rock has entered combat mode."
        ));

        shakeRock();
        afterAction();
    }

    private void stareAtRock(ActionEvent event) {
        boredom -= randomValue(1, 5);
        respect += randomValue(0, 3);
        happiness += randomValue(0, 4);

        showMessage(randomMessage(
                "You stared at the rock. The rock stared back.",
                "This became uncomfortable very quickly.",
                "The rock blinked. Maybe.",
                "You have formed a strange bond with the rock."
        ));

        afterAction();
    }

    private void afterAction() {
        naturalDecay();
        clampStats();
        updateStats();
        updateRockMood();
    }

    private void naturalDecay() {
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

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private int randomValue(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private String randomMessage(String... messages) {
        return messages[random.nextInt(messages.length)];
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    private void updateStats() {
        happinessLabel.setText("Happiness: " + happiness + "/100");
        cleanlinessLabel.setText("Cleanliness: " + cleanliness + "/100");
        energyLabel.setText("Energy: " + energy + "/100");
        boredomLabel.setText("Boredom: " + boredom + "/100");
        respectLabel.setText("Respect: " + respect);
    }

    private void updateRockMood() {
        if (happiness >= 80 && respect >= 20) {
            rockLabel.setText("💎");
            titleLabel.setText("Rock.exe — Ascended Rock");
        } else if (happiness <= 20) {
            rockLabel.setText("🪨");
            titleLabel.setText("Rock.exe — Depressed Rock");
        } else if (boredom >= 80) {
            rockLabel.setText("🧱");
            titleLabel.setText("Rock.exe — Bored Rock");
        } else {
            rockLabel.setText("🪨");
            titleLabel.setText("Rock.exe");
        }
    }

    private void shakeRock() {
        int originalX = rockLabel.getX();
        int originalY = rockLabel.getY();

        Timer timer = new Timer(35, null);
        final int[] count = {0};

        timer.addActionListener(e -> {
            int offsetX = random.nextInt(17) - 8;
            int offsetY = random.nextInt(17) - 8;

            rockLabel.setLocation(originalX + offsetX, originalY + offsetY);

            count[0]++;

            if (count[0] >= 12) {
                rockLabel.setLocation(originalX, originalY);
                timer.stop();
            }
        });

        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RockExe app = new RockExe();
            app.setVisible(true);
        });
    }
}
