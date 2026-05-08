import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class CalculatorExe extends JFrame {

    private JTextField display;
    private JLabel topLabel;

    private double firstValue = 0;
    private String operator = "";
    private boolean startNewNumber = true;
    private boolean hasDecimal = false;

    private final DecimalFormat format = new DecimalFormat("0.##########");

    public CalculatorExe() {
        setupWindow();
        setupUI();
    }

    private void setupWindow() {
        setTitle("Calculator.exe");
        setSize(420, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(14, 14, 18));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(root);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Calculator.exe");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        topPanel.add(title, BorderLayout.NORTH);

        topLabel = new JLabel(" ");
        topLabel.setForeground(new Color(130, 130, 145));
        topLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        topLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(topLabel, BorderLayout.CENTER);

        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(new Color(22, 22, 28));
        display.setForeground(Color.WHITE);
        display.setCaretColor(Color.WHITE);
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 78), 1),
                new EmptyBorder(18, 18, 18, 18)
        ));
        display.setFont(new Font("Segoe UI", Font.BOLD, 34));
        topPanel.add(display, BorderLayout.SOUTH);

        root.add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 12, 12));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(18, 0, 0, 0));

        addButton(buttonPanel, "C", new Color(95, 55, 55), Color.WHITE, e -> clearAll());
        addButton(buttonPanel, "DEL", new Color(80, 80, 95), Color.WHITE, e -> deleteLast());
        addButton(buttonPanel, "%", new Color(80, 80, 95), Color.WHITE, e -> applyPercent());
        addButton(buttonPanel, "÷", new Color(110, 80, 200), Color.WHITE, e -> setOperator("/"));

        addButton(buttonPanel, "7", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("7"));
        addButton(buttonPanel, "8", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("8"));
        addButton(buttonPanel, "9", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("9"));
        addButton(buttonPanel, "×", new Color(110, 80, 200), Color.WHITE, e -> setOperator("*"));

        addButton(buttonPanel, "4", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("4"));
        addButton(buttonPanel, "5", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("5"));
        addButton(buttonPanel, "6", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("6"));
        addButton(buttonPanel, "-", new Color(110, 80, 200), Color.WHITE, e -> setOperator("-"));

        addButton(buttonPanel, "1", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("1"));
        addButton(buttonPanel, "2", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("2"));
        addButton(buttonPanel, "3", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("3"));
        addButton(buttonPanel, "+", new Color(110, 80, 200), Color.WHITE, e -> setOperator("+"));

        addButton(buttonPanel, "±", new Color(80, 80, 95), Color.WHITE, e -> toggleSign());
        addButton(buttonPanel, "0", new Color(38, 38, 48), Color.WHITE, e -> appendNumber("0"));
        addButton(buttonPanel, ".", new Color(80, 80, 95), Color.WHITE, e -> appendDecimal());
        addButton(buttonPanel, "=", new Color(70, 140, 255), Color.WHITE, e -> calculateResult());

        root.add(buttonPanel, BorderLayout.CENTER);
    }

    private void addButton(JPanel panel, String text, Color bg, Color fg, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createLineBorder(new Color(65, 65, 85), 1));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(brighten(bg, 0.10));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bg);
            }
        });
        panel.add(button);
    }

    private Color brighten(Color color, double factor) {
        int r = (int) Math.min(255, color.getRed() + (255 - color.getRed()) * factor);
        int g = (int) Math.min(255, color.getGreen() + (255 - color.getGreen()) * factor);
        int b = (int) Math.min(255, color.getBlue() + (255 - color.getBlue()) * factor);
        return new Color(r, g, b);
    }

    private void appendNumber(String number) {
        if (startNewNumber) {
            display.setText(number);
            startNewNumber = false;
            hasDecimal = false;
            return;
        }

        if (display.getText().equals("0")) {
            display.setText(number);
        } else {
            display.setText(display.getText() + number);
        }
    }

    private void appendDecimal() {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
            hasDecimal = true;
            return;
        }

        if (!hasDecimal && !display.getText().contains(".")) {
            display.setText(display.getText() + ".");
            hasDecimal = true;
        }
    }

    private void setOperator(String op) {
        try {
            if (!operator.isEmpty() && !startNewNumber) {
                calculateResult();
            }

            firstValue = Double.parseDouble(display.getText());
            operator = op;
            startNewNumber = true;
            hasDecimal = false;
            topLabel.setText(formatNumber(firstValue) + " " + getPrettyOperator(operator));
        } catch (Exception ex) {
            showError();
        }
    }

    private void calculateResult() {
        if (operator.isEmpty()) {
            return;
        }

        try {
            double secondValue = Double.parseDouble(display.getText());
            double result;

            switch (operator) {
                case "+" -> result = firstValue + secondValue;
                case "-" -> result = firstValue - secondValue;
                case "*" -> result = firstValue * secondValue;
                case "/" -> {
                    if (secondValue == 0) {
                        display.setText("Cannot divide by 0");
                        topLabel.setText("Math error");
                        operator = "";
                        startNewNumber = true;
                        hasDecimal = false;
                        return;
                    }
                    result = firstValue / secondValue;
                }
                default -> {
                    return;
                }
            }

            topLabel.setText(formatNumber(firstValue) + " " + getPrettyOperator(operator) + " " + formatNumber(secondValue) + " =");
            display.setText(formatNumber(result));
            operator = "";
            firstValue = result;
            startNewNumber = true;
            hasDecimal = display.getText().contains(".");
        } catch (Exception ex) {
            showError();
        }
    }

    private void applyPercent() {
        try {
            double current = Double.parseDouble(display.getText());
            current = current / 100.0;
            display.setText(formatNumber(current));
            startNewNumber = true;
            hasDecimal = display.getText().contains(".");
        } catch (Exception ex) {
            showError();
        }
    }

    private void toggleSign() {
        try {
            double current = Double.parseDouble(display.getText());
            current = -current;
            display.setText(formatNumber(current));
        } catch (Exception ex) {
            showError();
        }
    }

    private void deleteLast() {
        if (startNewNumber || display.getText().equals("0")) {
            return;
        }

        String text = display.getText();

        if (text.equals("Cannot divide by 0") || text.equals("Error")) {
            clearAll();
            return;
        }

        if (text.length() == 1 || (text.length() == 2 && text.startsWith("-"))) {
            display.setText("0");
            startNewNumber = true;
            hasDecimal = false;
            return;
        }

        text = text.substring(0, text.length() - 1);
        display.setText(text);
        hasDecimal = text.contains(".");
    }

    private void clearAll() {
        display.setText("0");
        topLabel.setText(" ");
        firstValue = 0;
        operator = "";
        startNewNumber = true;
        hasDecimal = false;
    }

    private String formatNumber(double value) {
        return format.format(value);
    }

    private String getPrettyOperator(String op) {
        return switch (op) {
            case "/" -> "÷";
            case "*" -> "×";
            default -> op;
        };
    }

    private void showError() {
        display.setText("Error");
        topLabel.setText("Something went wrong");
        operator = "";
        startNewNumber = true;
        hasDecimal = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorExe app = new CalculatorExe();
            app.setVisible(true);
        });
    }
}
