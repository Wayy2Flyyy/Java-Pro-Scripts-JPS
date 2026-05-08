import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GameTemplate extends JFrame {

    /*
     * ============================================================
     *                  JAVA GAME TEMPLATE CONFIG
     * ============================================================
     *
     * Edit this section to make your own game.
     * You do not need to touch the engine code unless you want to.
     */

    static final class CFG {

        static final class WINDOW {
            static final String TITLE = "Config Quest";
            static final int WIDTH = 960;
            static final int HEIGHT = 640;
            static final int FPS = 60;
            static final boolean RESIZABLE = false;
        }

        static final class GAME {
            static final String GAME_NAME = "Config Quest";
            static final int STARTING_SCORE = 0;
            static final int WIN_SCORE = 300;
            static final boolean ENABLE_WIN_CONDITION = true;
            static final boolean ENABLE_PAUSE = true;
            static final boolean ENABLE_DEBUG_TOGGLE = true;
        }

        static final class WORLD {
            static final int HUD_HEIGHT = 70;
            static final int GRID_SIZE = 40;
            static final boolean SHOW_GRID = true;
            static final boolean SHOW_SHADOWS = true;
            static final Color BACKGROUND = new Color(18, 24, 34);
            static final Color GRID_COLOR = new Color(255, 255, 255, 16);
        }

        static final class PLAYER {
            static final String NAME = "Player";
            static final int START_X = WINDOW.WIDTH / 2;
            static final int START_Y = WINDOW.HEIGHT / 2;
            static final int SIZE = 34;
            static final double SPEED = 4.8;
            static final int MAX_HEALTH = 100;
            static final Color COLOR = new Color(80, 170, 255);
            static final Color EYE_COLOR = Color.WHITE;
        }

        static final class ENEMY {
            static final boolean ENABLED = true;
            static final int COUNT = 3;
            static final int SIZE = 34;
            static final double SPEED = 2.1;
            static final int DAMAGE = 10;
            static final int DAMAGE_COOLDOWN_MS = 550;
            static final boolean SPEED_UP_ON_LEVEL = true;
            static final double SPEED_GAIN_PER_LEVEL = 0.25;
            static final Color COLOR = new Color(255, 85, 95);
        }

        static final class COLLECTIBLE {
            static final int STARTING_COUNT = 12;
            static final int SIZE = 18;
            static final int SCORE_VALUE = 10;
            static final int HEAL_VALUE = 2;
            static final boolean RESPAWN_AFTER_PICKUP = true;
            static final Color COLOR = new Color(255, 210, 80);
        }

        static final class LEVELS {
            static final boolean ENABLED = true;
            static final int SCORE_PER_LEVEL = 60;
            static final double PLAYER_SPEED_GAIN = 0.25;
        }

        static final class CONTROLS {
            static final int UP = KeyEvent.VK_W;
            static final int DOWN = KeyEvent.VK_S;
            static final int LEFT = KeyEvent.VK_A;
            static final int RIGHT = KeyEvent.VK_D;
            static final int RESTART = KeyEvent.VK_R;
            static final int PAUSE = KeyEvent.VK_P;
            static final int DEBUG = KeyEvent.VK_F3;

            /*
             * Mouse modes:
             * TELEPORT      = click to teleport player
             * MOVE_TO_CLICK = click and player walks to target
             * DISABLED      = mouse movement disabled
             */
            static final MouseMode MOUSE_MODE = MouseMode.MOVE_TO_CLICK;
        }

        static final class UI {
            static final boolean SHOW_HUD = true;
            static final boolean SHOW_CONTROLS_TEXT = true;
            static final boolean SHOW_TARGET_MARKER = true;
            static final Color HUD_BACKGROUND = new Color(8, 10, 16, 230);
            static final Color TEXT_MAIN = Color.WHITE;
            static final Color TEXT_MUTED = new Color(190, 195, 210);
            static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 23);
            static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 15);
            static final Font BIG_FONT = new Font("Segoe UI", Font.BOLD, 56);
        }
    }

    enum MouseMode {
        TELEPORT,
        MOVE_TO_CLICK,
        DISABLED
    }

    /*
     * ============================================================
     *                        WINDOW START
     * ============================================================
     */

    public GameTemplate() {
        setTitle(CFG.WINDOW.TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(CFG.WINDOW.RESIZABLE);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        panel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameTemplate::new);
    }
}

/*
 * ============================================================
 *                          GAME PANEL
 * ============================================================
 */

class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {

    private final Timer timer;
    private final Random random = new Random();

    private Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Collectible> collectibles = new ArrayList<>();

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private boolean paused = false;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private boolean debugVisible = false;

    private int score;
    private int level;
    private long lastDamageTime;

    private boolean targetActive = false;
    private double targetX;
    private double targetY;

    GamePanel() {
        int delay = Math.max(1, 1000 / GameTemplate.CFG.WINDOW.FPS);
        timer = new Timer(delay, this);

        setPreferredSize(new Dimension(GameTemplate.CFG.WINDOW.WIDTH, GameTemplate.CFG.WINDOW.HEIGHT));
        setFocusable(true);
        setBackground(GameTemplate.CFG.WORLD.BACKGROUND);

        addKeyListener(this);
        addMouseListener(this);

        resetGame();
    }

    void startGame() {
        requestFocusInWindow();
        timer.start();
    }

    private void resetGame() {
        score = GameTemplate.CFG.GAME.STARTING_SCORE;
        level = 1;
        paused = false;
        gameOver = false;
        gameWon = false;
        targetActive = false;
        lastDamageTime = 0;

        player = new Player(
                GameTemplate.CFG.PLAYER.START_X,
                GameTemplate.CFG.PLAYER.START_Y,
                GameTemplate.CFG.PLAYER.SIZE,
                GameTemplate.CFG.PLAYER.SPEED,
                GameTemplate.CFG.PLAYER.MAX_HEALTH
        );

        enemies.clear();
        collectibles.clear();

        if (GameTemplate.CFG.ENEMY.ENABLED) {
            for (int i = 0; i < GameTemplate.CFG.ENEMY.COUNT; i++) {
                spawnEnemy();
            }
        }

        for (int i = 0; i < GameTemplate.CFG.COLLECTIBLE.STARTING_COUNT; i++) {
            spawnCollectible();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private void updateGame() {
        if (paused || gameOver || gameWon) {
            return;
        }

        updatePlayer();
        updateEnemies();
        checkCollectibles();
        checkEnemyCollisions();
        updateLevel();
        checkWinCondition();
    }

    private void updatePlayer() {
        double moveX = 0;
        double moveY = 0;

        if (up) moveY -= 1;
        if (down) moveY += 1;
        if (left) moveX -= 1;
        if (right) moveX += 1;

        boolean keyboardMoving = moveX != 0 || moveY != 0;

        if (keyboardMoving) {
            targetActive = false;

            double length = Math.sqrt(moveX * moveX + moveY * moveY);

            player.x += (moveX / length) * player.speed;
            player.y += (moveY / length) * player.speed;
        } else if (targetActive && GameTemplate.CFG.CONTROLS.MOUSE_MODE == GameTemplate.MouseMode.MOVE_TO_CLICK) {
            movePlayerToTarget();
        }

        clampPlayer();
    }

    private void movePlayerToTarget() {
        double dx = targetX - player.x;
        double dy = targetY - player.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= player.speed) {
            player.x = targetX;
            player.y = targetY;
            targetActive = false;
            return;
        }

        player.x += (dx / distance) * player.speed;
        player.y += (dy / distance) * player.speed;
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            double dx = player.x - enemy.x;
            double dy = player.y - enemy.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                enemy.x += (dx / distance) * enemy.speed;
                enemy.y += (dy / distance) * enemy.speed;
            }

            clampEnemy(enemy);
        }
    }

    private void checkCollectibles() {
        Rectangle playerBox = player.getBounds();

        for (int i = collectibles.size() - 1; i >= 0; i--) {
            Collectible item = collectibles.get(i);

            if (playerBox.intersects(item.getBounds())) {
                collectibles.remove(i);

                score += GameTemplate.CFG.COLLECTIBLE.SCORE_VALUE;
                player.health += GameTemplate.CFG.COLLECTIBLE.HEAL_VALUE;

                if (player.health > player.maxHealth) {
                    player.health = player.maxHealth;
                }

                if (GameTemplate.CFG.COLLECTIBLE.RESPAWN_AFTER_PICKUP) {
                    spawnCollectible();
                }
            }
        }
    }

    private void checkEnemyCollisions() {
        long now = System.currentTimeMillis();

        if (now - lastDamageTime < GameTemplate.CFG.ENEMY.DAMAGE_COOLDOWN_MS) {
            return;
        }

        Rectangle playerBox = player.getBounds();

        for (Enemy enemy : enemies) {
            if (playerBox.intersects(enemy.getBounds())) {
                player.health -= GameTemplate.CFG.ENEMY.DAMAGE;
                lastDamageTime = now;

                if (player.health <= 0) {
                    player.health = 0;
                    gameOver = true;
                }

                return;
            }
        }
    }

    private void updateLevel() {
        if (!GameTemplate.CFG.LEVELS.ENABLED) {
            return;
        }

        int newLevel = Math.max(1, (score / GameTemplate.CFG.LEVELS.SCORE_PER_LEVEL) + 1);

        if (newLevel > level) {
            int gainedLevels = newLevel - level;
            level = newLevel;

            player.speed += GameTemplate.CFG.LEVELS.PLAYER_SPEED_GAIN * gainedLevels;

            if (GameTemplate.CFG.ENEMY.SPEED_UP_ON_LEVEL) {
                for (Enemy enemy : enemies) {
                    enemy.speed += GameTemplate.CFG.ENEMY.SPEED_GAIN_PER_LEVEL * gainedLevels;
                }
            }
        }
    }

    private void checkWinCondition() {
        if (!GameTemplate.CFG.GAME.ENABLE_WIN_CONDITION) {
            return;
        }

        if (score >= GameTemplate.CFG.GAME.WIN_SCORE) {
            gameWon = true;
        }
    }

    private void spawnCollectible() {
        int size = GameTemplate.CFG.COLLECTIBLE.SIZE;
        int x = randomBetween(40, GameTemplate.CFG.WINDOW.WIDTH - 60);
        int y = randomBetween(GameTemplate.CFG.WORLD.HUD_HEIGHT + 30, GameTemplate.CFG.WINDOW.HEIGHT - 60);

        collectibles.add(new Collectible(x, y, size));
    }

    private void spawnEnemy() {
        int size = GameTemplate.CFG.ENEMY.SIZE;
        int x;
        int y;

        do {
            x = randomBetween(40, GameTemplate.CFG.WINDOW.WIDTH - 60);
            y = randomBetween(GameTemplate.CFG.WORLD.HUD_HEIGHT + 30, GameTemplate.CFG.WINDOW.HEIGHT - 60);
        } while (distance(x, y, player.x, player.y) < 180);

        enemies.add(new Enemy(x, y, size, GameTemplate.CFG.ENEMY.SPEED));
    }

    private void clampPlayer() {
        player.x = clamp(player.x, 0, GameTemplate.CFG.WINDOW.WIDTH - player.size);
        player.y = clamp(player.y, GameTemplate.CFG.WORLD.HUD_HEIGHT, GameTemplate.CFG.WINDOW.HEIGHT - player.size);
    }

    private void clampEnemy(Enemy enemy) {
        enemy.x = clamp(enemy.x, 0, GameTemplate.CFG.WINDOW.WIDTH - enemy.size);
        enemy.y = clamp(enemy.y, GameTemplate.CFG.WORLD.HUD_HEIGHT, GameTemplate.CFG.WINDOW.HEIGHT - enemy.size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        enableQuality(g2);

        drawWorld(g2);
        drawTarget(g2);
        drawCollectibles(g2);
        drawEnemies(g2);
        drawPlayer(g2);

        if (GameTemplate.CFG.UI.SHOW_HUD) {
            drawHUD(g2);
        }

        if (debugVisible) {
            drawDebug(g2);
        }

        if (paused) {
            drawCenterMessage(g2, "PAUSED", "Press P to continue");
        }

        if (gameOver) {
            drawCenterMessage(g2, "GAME OVER", "Press R to restart");
        }

        if (gameWon) {
            drawCenterMessage(g2, "YOU WIN", "Press R to restart");
        }

        g2.dispose();
    }

    private void enableQuality(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private void drawWorld(Graphics2D g2) {
        g2.setColor(GameTemplate.CFG.WORLD.BACKGROUND);
        g2.fillRect(0, 0, GameTemplate.CFG.WINDOW.WIDTH, GameTemplate.CFG.WINDOW.HEIGHT);

        if (!GameTemplate.CFG.WORLD.SHOW_GRID) {
            return;
        }

        g2.setColor(GameTemplate.CFG.WORLD.GRID_COLOR);

        for (int x = 0; x < GameTemplate.CFG.WINDOW.WIDTH; x += GameTemplate.CFG.WORLD.GRID_SIZE) {
            g2.drawLine(x, GameTemplate.CFG.WORLD.HUD_HEIGHT, x, GameTemplate.CFG.WINDOW.HEIGHT);
        }

        for (int y = GameTemplate.CFG.WORLD.HUD_HEIGHT; y < GameTemplate.CFG.WINDOW.HEIGHT; y += GameTemplate.CFG.WORLD.GRID_SIZE) {
            g2.drawLine(0, y, GameTemplate.CFG.WINDOW.WIDTH, y);
        }
    }

    private void drawTarget(Graphics2D g2) {
        if (!targetActive || !GameTemplate.CFG.UI.SHOW_TARGET_MARKER) {
            return;
        }

        g2.setColor(new Color(255, 255, 255, 130));
        int x = (int) targetX;
        int y = (int) targetY;

        g2.drawOval(x - 14, y - 14, 28, 28);
        g2.drawLine(x - 20, y, x + 20, y);
        g2.drawLine(x, y - 20, x, y + 20);
    }

    private void drawPlayer(Graphics2D g2) {
        if (GameTemplate.CFG.WORLD.SHOW_SHADOWS) {
            g2.setColor(new Color(0, 0, 0, 85));
            g2.fillOval((int) player.x - 5, (int) player.y + player.size - 6, player.size + 10, 12);
        }

        g2.setColor(GameTemplate.CFG.PLAYER.COLOR);
        g2.fillRoundRect((int) player.x, (int) player.y, player.size, player.size, 12, 12);

        g2.setColor(GameTemplate.CFG.PLAYER.EYE_COLOR);
        g2.fillOval((int) player.x + 8, (int) player.y + 9, 5, 5);
        g2.fillOval((int) player.x + 21, (int) player.y + 9, 5, 5);
    }

    private void drawEnemies(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            if (GameTemplate.CFG.WORLD.SHOW_SHADOWS) {
                g2.setColor(new Color(0, 0, 0, 85));
                g2.fillOval((int) enemy.x - 5, (int) enemy.y + enemy.size - 6, enemy.size + 10, 12);
            }

            g2.setColor(GameTemplate.CFG.ENEMY.COLOR);
            g2.fillRoundRect((int) enemy.x, (int) enemy.y, enemy.size, enemy.size, 12, 12);

            g2.setColor(Color.BLACK);
            g2.fillOval((int) enemy.x + 8, (int) enemy.y + 10, 5, 5);
            g2.fillOval((int) enemy.x + 21, (int) enemy.y + 10, 5, 5);
        }
    }

    private void drawCollectibles(Graphics2D g2) {
        for (Collectible item : collectibles) {
            if (GameTemplate.CFG.WORLD.SHOW_SHADOWS) {
                g2.setColor(new Color(0, 0, 0, 70));
                g2.fillOval(item.x - 2, item.y + item.size - 4, item.size + 4, 8);
            }

            g2.setColor(GameTemplate.CFG.COLLECTIBLE.COLOR);
            g2.fillOval(item.x, item.y, item.size, item.size);

            g2.setColor(new Color(255, 255, 255, 150));
            g2.fillOval(item.x + 4, item.y + 3, 6, 5);
        }
    }

    private void drawHUD(Graphics2D g2) {
        g2.setColor(GameTemplate.CFG.UI.HUD_BACKGROUND);
        g2.fillRect(0, 0, GameTemplate.CFG.WINDOW.WIDTH, GameTemplate.CFG.WORLD.HUD_HEIGHT);

        g2.setFont(GameTemplate.CFG.UI.TITLE_FONT);
        g2.setColor(GameTemplate.CFG.UI.TEXT_MAIN);
        g2.drawString(GameTemplate.CFG.GAME.GAME_NAME, 20, 42);

        g2.setFont(GameTemplate.CFG.UI.NORMAL_FONT);
        g2.setColor(GameTemplate.CFG.UI.TEXT_MUTED);

        g2.drawString("Score: " + score, 230, 42);
        g2.drawString("Health: " + player.health + "/" + player.maxHealth, 340, 42);
        g2.drawString("Level: " + level, 495, 42);

        if (GameTemplate.CFG.UI.SHOW_CONTROLS_TEXT) {
            g2.drawString("WASD Move | Mouse " + GameTemplate.CFG.CONTROLS.MOUSE_MODE + " | R Restart | P Pause | F3 Debug", 590, 42);
        }
    }

    private void drawDebug(Graphics2D g2) {
        int x = 15;
        int y = GameTemplate.CFG.WORLD.HUD_HEIGHT + 20;

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRoundRect(x, y, 260, 150, 16, 16);

        g2.setFont(new Font("Consolas", Font.PLAIN, 13));
        g2.setColor(Color.WHITE);

        g2.drawString("DEBUG", x + 15, y + 25);
        g2.drawString("Player X: " + (int) player.x, x + 15, y + 50);
        g2.drawString("Player Y: " + (int) player.y, x + 15, y + 70);
        g2.drawString("Player Speed: " + round(player.speed), x + 15, y + 90);
        g2.drawString("Enemies: " + enemies.size(), x + 15, y + 110);
        g2.drawString("Collectibles: " + collectibles.size(), x + 15, y + 130);
    }

    private void drawCenterMessage(Graphics2D g2, String title, String subtitle) {
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, GameTemplate.CFG.WINDOW.WIDTH, GameTemplate.CFG.WINDOW.HEIGHT);

        g2.setFont(GameTemplate.CFG.UI.BIG_FONT);
        g2.setColor(Color.WHITE);

        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (GameTemplate.CFG.WINDOW.WIDTH - titleWidth) / 2, GameTemplate.CFG.WINDOW.HEIGHT / 2 - 20);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        g2.setColor(new Color(220, 220, 230));

        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.drawString(subtitle, (GameTemplate.CFG.WINDOW.WIDTH - subtitleWidth) / 2, GameTemplate.CFG.WINDOW.HEIGHT / 2 + 25);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == GameTemplate.CFG.CONTROLS.UP) up = true;
        if (code == GameTemplate.CFG.CONTROLS.DOWN) down = true;
        if (code == GameTemplate.CFG.CONTROLS.LEFT) left = true;
        if (code == GameTemplate.CFG.CONTROLS.RIGHT) right = true;

        if (code == GameTemplate.CFG.CONTROLS.RESTART) {
            resetGame();
        }

        if (code == GameTemplate.CFG.CONTROLS.PAUSE && GameTemplate.CFG.GAME.ENABLE_PAUSE) {
            if (!gameOver && !gameWon) {
                paused = !paused;
            }
        }

        if (code == GameTemplate.CFG.CONTROLS.DEBUG && GameTemplate.CFG.GAME.ENABLE_DEBUG_TOGGLE) {
            debugVisible = !debugVisible;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == GameTemplate.CFG.CONTROLS.UP) up = false;
        if (code == GameTemplate.CFG.CONTROLS.DOWN) down = false;
        if (code == GameTemplate.CFG.CONTROLS.LEFT) left = false;
        if (code == GameTemplate.CFG.CONTROLS.RIGHT) right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();

        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        if (gameOver || gameWon || paused) {
            return;
        }

        if (GameTemplate.CFG.CONTROLS.MOUSE_MODE == GameTemplate.MouseMode.DISABLED) {
            return;
        }

        double clickedX = clamp(e.getX() - player.size / 2.0, 0, GameTemplate.CFG.WINDOW.WIDTH - player.size);
        double clickedY = clamp(e.getY() - player.size / 2.0, GameTemplate.CFG.WORLD.HUD_HEIGHT, GameTemplate.CFG.WINDOW.HEIGHT - player.size);

        if (GameTemplate.CFG.CONTROLS.MOUSE_MODE == GameTemplate.MouseMode.TELEPORT) {
            player.x = clickedX;
            player.y = clickedY;
            targetActive = false;
        }

        if (GameTemplate.CFG.CONTROLS.MOUSE_MODE == GameTemplate.MouseMode.MOVE_TO_CLICK) {
            targetX = clickedX;
            targetY = clickedY;
            targetActive = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private int randomBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private String round(double value) {
        return String.format("%.2f", value);
    }
}

/*
 * ============================================================
 *                            ENTITIES
 * ============================================================
 */

class Player {
    double x;
    double y;
    int size;
    double speed;
    int health;
    int maxHealth;

    Player(double x, double y, int size, double speed, int maxHealth) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }
}

class Enemy {
    double x;
    double y;
    int size;
    double speed;

    Enemy(double x, double y, int size, double speed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.speed = speed;
    }

    Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }
}

class Collectible {
    int x;
    int y;
    int size;

    Collectible(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }
}
