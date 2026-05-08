import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;

public class CalculatorExe extends JFrame {

    private JTextField displayField;
    private JLabel expressionLabel;

    private String expression = "";
    private double lastAnswer = 0.0;

    private final DecimalFormat resultFormat = new DecimalFormat("0.############");

    public CalculatorExe() {
        setupWindow();
        setupUI();
    }

    private void setupWindow() {
        setTitle("Calculator.exe");
        setSize(520, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(new GradientPanel());
    }

    private void setupUI() {
        JPanel root = (JPanel) getContentPane();
        root.setLayout(new BorderLayout());
        root.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel topWrap = new JPanel(new BorderLayout());
        topWrap.setOpaque(false);

        JLabel title = new JLabel("Calculator.exe");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 6, 14, 6));
        topWrap.add(title, BorderLayout.NORTH);

        RoundedPanel displayPanel = new RoundedPanel(28, new Color(255, 255, 255, 26), new Color(255, 255, 255, 40));
        displayPanel.setLayout(new BorderLayout(0, 8));
        displayPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        expressionLabel = new JLabel(" ");
        expressionLabel.setForeground(new Color(185, 190, 210));
        expressionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        displayField = new JTextField("0");
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setFont(new Font("Segoe UI", Font.BOLD, 36));
        displayField.setForeground(Color.WHITE);
        displayField.setBackground(new Color(0, 0, 0, 0));
        displayField.setBorder(null);
        displayField.setOpaque(false);

        displayPanel.add(expressionLabel, BorderLayout.NORTH);
        displayPanel.add(displayField, BorderLayout.CENTER);

        topWrap.add(displayPanel, BorderLayout.CENTER);
        root.add(topWrap, BorderLayout.NORTH);

        JPanel buttonGrid = new JPanel(new GridLayout(6, 6, 10, 10));
        buttonGrid.setOpaque(false);
        buttonGrid.setBorder(new EmptyBorder(18, 0, 0, 0));

        addButton(buttonGrid, "C", ButtonType.DANGER, e -> clearAll());
        addButton(buttonGrid, "DEL", ButtonType.SECONDARY, e -> deleteLast());
        addButton(buttonGrid, "(", ButtonType.SECONDARY, e -> append("("));
        addButton(buttonGrid, ")", ButtonType.SECONDARY, e -> append(")"));
        addButton(buttonGrid, "%", ButtonType.SECONDARY, e -> append("%"));
        addButton(buttonGrid, "÷", ButtonType.OPERATOR, e -> append("/"));

        addButton(buttonGrid, "sin", ButtonType.FUNCTION, e -> append("sin("));
        addButton(buttonGrid, "cos", ButtonType.FUNCTION, e -> append("cos("));
        addButton(buttonGrid, "tan", ButtonType.FUNCTION, e -> append("tan("));
        addButton(buttonGrid, "log", ButtonType.FUNCTION, e -> append("log("));
        addButton(buttonGrid, "ln", ButtonType.FUNCTION, e -> append("ln("));
        addButton(buttonGrid, "√", ButtonType.FUNCTION, e -> append("sqrt("));

        addButton(buttonGrid, "7", ButtonType.NORMAL, e -> append("7"));
        addButton(buttonGrid, "8", ButtonType.NORMAL, e -> append("8"));
        addButton(buttonGrid, "9", ButtonType.NORMAL, e -> append("9"));
        addButton(buttonGrid, "×", ButtonType.OPERATOR, e -> append("*"));
        addButton(buttonGrid, "x²", ButtonType.FUNCTION, e -> squareCurrent());
        addButton(buttonGrid, "xʸ", ButtonType.FUNCTION, e -> append("^"));

        addButton(buttonGrid, "4", ButtonType.NORMAL, e -> append("4"));
        addButton(buttonGrid, "5", ButtonType.NORMAL, e -> append("5"));
        addButton(buttonGrid, "6", ButtonType.NORMAL, e -> append("6"));
        addButton(buttonGrid, "-", ButtonType.OPERATOR, e -> append("-"));
        addButton(buttonGrid, "1/x", ButtonType.FUNCTION, e -> reciprocalCurrent());
        addButton(buttonGrid, "π", ButtonType.FUNCTION, e -> append("pi"));

        addButton(buttonGrid, "1", ButtonType.NORMAL, e -> append("1"));
        addButton(buttonGrid, "2", ButtonType.NORMAL, e -> append("2"));
        addButton(buttonGrid, "3", ButtonType.NORMAL, e -> append("3"));
        addButton(buttonGrid, "+", ButtonType.OPERATOR, e -> append("+"));
        addButton(buttonGrid, "Ans", ButtonType.FUNCTION, e -> append(formatNumber(lastAnswer)));
        addButton(buttonGrid, "e", ButtonType.FUNCTION, e -> append("e"));

        addButton(buttonGrid, "±", ButtonType.SECONDARY, e -> toggleSign());
        addButton(buttonGrid, "0", ButtonType.NORMAL, e -> append("0"));
        addButton(buttonGrid, ".", ButtonType.NORMAL, e -> append("."));
        addButton(buttonGrid, "EXP", ButtonType.FUNCTION, e -> append("E"));
        addButton(buttonGrid, "abs", ButtonType.FUNCTION, e -> append("abs("));
        addButton(buttonGrid, "=", ButtonType.EQUALS, e -> evaluate());

        root.add(buttonGrid, BorderLayout.CENTER);

        bindKeyboard();
    }

    private void bindKeyboard() {
        JRootPane rootPane = getRootPane();
        bindKey(rootPane, "0", () -> append("0"));
        bindKey(rootPane, "1", () -> append("1"));
        bindKey(rootPane, "2", () -> append("2"));
        bindKey(rootPane, "3", () -> append("3"));
        bindKey(rootPane, "4", () -> append("4"));
        bindKey(rootPane, "5", () -> append("5"));
        bindKey(rootPane, "6", () -> append("6"));
        bindKey(rootPane, "7", () -> append("7"));
        bindKey(rootPane, "8", () -> append("8"));
        bindKey(rootPane, "9", () -> append("9"));
        bindKey(rootPane, ".", () -> append("."));
        bindKey(rootPane, "+", () -> append("+"));
        bindKey(rootPane, "-", () -> append("-"));
        bindKey(rootPane, "*", () -> append("*"));
        bindKey(rootPane, "/", () -> append("/"));
        bindKey(rootPane, "ENTER", this::evaluate);
        bindKey(rootPane, "BACK_SPACE", this::deleteLast);
        bindKey(rootPane, "ESCAPE", this::clearAll);
        bindKey(rootPane, "OPEN_BRACKET", () -> append("("));
        bindKey(rootPane, "CLOSE_BRACKET", () -> append(")"));
    }

    private void bindKey(JRootPane rootPane, String keyStroke, Runnable action) {
        String actionName = "action_" + keyStroke + "_" + Math.random();
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyStroke), actionName);
        rootPane.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void addButton(JPanel panel, String text, ButtonType type, java.awt.event.ActionListener action) {
        ModernButton button = new ModernButton(text, type);
        button.addActionListener(action);
        panel.add(button);
    }

    private void append(String value) {
        if (displayField.getText().equals("Error")) {
            clearAll();
        }

        expression += value;
        updateDisplay(false);
    }

    private void clearAll() {
        expression = "";
        expressionLabel.setText(" ");
        displayField.setText("0");
    }

    private void deleteLast() {
        if (displayField.getText().equals("Error")) {
            clearAll();
            return;
        }

        if (!expression.isEmpty()) {
            expression = expression.substring(0, expression.length() - 1);
            updateDisplay(false);
        }
    }

    private void toggleSign() {
        if (expression.isEmpty()) {
            expression = "-";
            updateDisplay(false);
            return;
        }

        try {
            double value = ExpressionEvaluator.evaluate(expression);
            expression = formatNumber(-value);
            updateDisplay(false);
        } catch (Exception ex) {
            expression = "-(" + expression + ")";
            updateDisplay(false);
        }
    }

    private void squareCurrent() {
        if (expression.isEmpty()) {
            return;
        }
        expression = "(" + expression + ")^2";
        updateDisplay(false);
    }

    private void reciprocalCurrent() {
        if (expression.isEmpty()) {
            expression = "1/(";
        } else {
            expression = "1/(" + expression + ")";
        }
        updateDisplay(false);
    }

    private void evaluate() {
        if (expression.isEmpty()) {
            return;
        }

        try {
            double result = ExpressionEvaluator.evaluate(expression);

            if (Double.isNaN(result) || Double.isInfinite(result)) {
                throw new IllegalArgumentException("Invalid result");
            }

            String oldExpression = expression;
            String formatted = formatNumber(result);

            lastAnswer = result;
            expressionLabel.setText(prettyExpression(oldExpression) + " =");
            expression = formatted;
            displayField.setText(formatted);
        } catch (Exception ex) {
            displayField.setText("Error");
            expressionLabel.setText("Invalid expression");
            expression = "";
        }
    }

    private void updateDisplay(boolean showRawExpression) {
        if (expression.isEmpty()) {
            displayField.setText("0");
            expressionLabel.setText(" ");
            return;
        }

        if (showRawExpression) {
            displayField.setText(expression);
            expressionLabel.setText(" ");
        } else {
            displayField.setText(prettyExpression(expression));
            expressionLabel.setText("Expression");
        }
    }

    private String prettyExpression(String expr) {
        return expr.replace("*", " × ")
                .replace("/", " ÷ ");
    }

    private String formatNumber(double value) {
        if (Math.abs(value) < 1e-12) {
            value = 0;
        }
        return resultFormat.format(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorExe app = new CalculatorExe();
            app.setVisible(true);
        });
    }

    enum ButtonType {
        NORMAL,
        SECONDARY,
        FUNCTION,
        OPERATOR,
        DANGER,
        EQUALS
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(18, 24, 40),
                    getWidth(), getHeight(), new Color(42, 26, 70)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 18));
            g2.fillOval(40, 70, 180, 180);
            g2.fillOval(300, 150, 220, 220);
            g2.fillOval(90, 520, 140, 140);

            g2.dispose();
            super.paintComponent(g);
        }

        public GradientPanel() {
            setOpaque(false);
        }
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color backgroundColor;
        private final Color borderColor;

        RoundedPanel(int radius, Color backgroundColor, Color borderColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class ModernButton extends JButton {
        private final ButtonType type;
        private boolean hovered = false;

        ModernButton(String text, ButtonType type) {
            super(text);
            this.type = type;

            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color base = getBaseColor();
            if (hovered) {
                base = brighten(base, 0.12);
            }

            g2.setColor(base);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);

            g2.setColor(new Color(255, 255, 255, 35));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);

            FontMetrics fm = g2.getFontMetrics(getFont());
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();

            g2.setFont(getFont());
            g2.setColor(getForeground());
            g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);

            g2.dispose();
        }

        private Color getBaseColor() {
            return switch (type) {
                case NORMAL -> new Color(255, 255, 255, 28);
                case SECONDARY -> new Color(86, 102, 132, 95);
                case FUNCTION -> new Color(109, 82, 171, 125);
                case OPERATOR -> new Color(114, 73, 230, 165);
                case DANGER -> new Color(190, 75, 90, 165);
                case EQUALS -> new Color(61, 156, 255, 185);
            };
        }

        private Color brighten(Color color, double factor) {
            int r = (int) Math.min(255, color.getRed() + (255 - color.getRed()) * factor);
            int g = (int) Math.min(255, color.getGreen() + (255 - color.getGreen()) * factor);
            int b = (int) Math.min(255, color.getBlue() + (255 - color.getBlue()) * factor);
            return new Color(r, g, b, color.getAlpha());
        }
    }

    static class ExpressionEvaluator {
        public static double evaluate(String expression) {
            Parser parser = new Parser(expression);
            double result = parser.parse();
            if (parser.hasRemaining()) {
                throw new IllegalArgumentException("Unexpected input");
            }
            return result;
        }

        private static class Parser {
            private final String input;
            private int pos = 0;

            Parser(String input) {
                this.input = input.replace(" ", "");
            }

            boolean hasRemaining() {
                skipWhitespace();
                return pos < input.length();
            }

            double parse() {
                return parseExpression();
            }

            private double parseExpression() {
                double value = parseTerm();

                while (true) {
                    if (match('+')) {
                        value += parseTerm();
                    } else if (match('-')) {
                        value -= parseTerm();
                    } else {
                        return value;
                    }
                }
            }

            private double parseTerm() {
                double value = parsePower();

                while (true) {
                    if (match('*')) {
                        value *= parsePower();
                    } else if (match('/')) {
                        double divisor = parsePower();
                        if (divisor == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        value /= divisor;
                    } else if (match('%')) {
                        double divisor = parsePower();
                        if (divisor == 0) {
                            throw new ArithmeticException("Modulo by zero");
                        }
                        value %= divisor;
                    } else {
                        return value;
                    }
                }
            }

            private double parsePower() {
                double value = parseUnary();

                if (match('^')) {
                    value = Math.pow(value, parsePower());
                }

                return value;
            }

            private double parseUnary() {
                if (match('+')) {
                    return parseUnary();
                }
                if (match('-')) {
                    return -parseUnary();
                }

                if (peekLetter()) {
                    String identifier = parseIdentifier();

                    if (identifier.equalsIgnoreCase("pi")) {
                        return Math.PI;
                    }

                    if (identifier.equalsIgnoreCase("e")) {
                        return Math.E;
                    }

                    if (match('(')) {
                        double argument = parseExpression();
                        expect(')');
                        return applyFunction(identifier, argument);
                    }

                    throw new IllegalArgumentException("Unknown identifier: " + identifier);
                }

                return parsePrimary();
            }

            private double parsePrimary() {
                if (match('(')) {
                    double value = parseExpression();
                    expect(')');
                    return value;
                }

                return parseNumber();
            }

            private double parseNumber() {
                int start = pos;
                boolean hasDigit = false;

                while (pos < input.length()) {
                    char c = input.charAt(pos);

                    if (Character.isDigit(c)) {
                        hasDigit = true;
                        pos++;
                    } else if (c == '.') {
                        pos++;
                    } else if ((c == 'E' || c == 'e') && hasDigit) {
                        pos++;
                        if (pos < input.length() && (input.charAt(pos) == '+' || input.charAt(pos) == '-')) {
                            pos++;
                        }
                    } else {
                        break;
                    }
                }

                if (start == pos) {
                    throw new IllegalArgumentException("Expected number at position " + pos);
                }

                return Double.parseDouble(input.substring(start, pos));
            }

            private double applyFunction(String function, double value) {
                return switch (function.toLowerCase()) {
                    case "sin" -> Math.sin(Math.toRadians(value));
                    case "cos" -> Math.cos(Math.toRadians(value));
                    case "tan" -> Math.tan(Math.toRadians(value));
                    case "log" -> Math.log10(value);
                    case "ln" -> Math.log(value);
                    case "sqrt" -> Math.sqrt(value);
                    case "abs" -> Math.abs(value);
                    default -> throw new IllegalArgumentException("Unknown function: " + function);
                };
            }

            private String parseIdentifier() {
                int start = pos;
                while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
                    pos++;
                }
                return input.substring(start, pos);
            }

            private boolean peekLetter() {
                return pos < input.length() && Character.isLetter(input.charAt(pos));
            }

            private boolean match(char expected) {
                skipWhitespace();
                if (pos < input.length() && input.charAt(pos) == expected) {
                    pos++;
                    return true;
                }
                return false;
            }

            private void expect(char expected) {
                if (!match(expected)) {
                    throw new IllegalArgumentException("Expected '" + expected + "'");
                }
            }

            private void skipWhitespace() {
                while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
                    pos++;
                }
            }
        }
    }
}
