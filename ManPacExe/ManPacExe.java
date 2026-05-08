import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ManPacExe extends JFrame {

    public ManPacExe() {
        setTitle("ManPac.exe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ManPacPanel panel = new ManPacPanel();
        add(panel);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        panel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManPacExe::new);
    }
}

class ManPacPanel extends JPanel implements ActionListener, KeyListener {

    private static final int TILE = 28;
    private static final int HUD_HEIGHT = 74;
    private static final int FPS_DELAY = 16;

    private final String[] RAW_MAP = {
            "#####################",
            "#P........#........H#",
            "#.###.###.#.###.###.#",
            "#o###.###.#.###.###o#",
            "#...................#",
            "#.###.#.#####.#.###.#",
            "#.....#...#...#.....#",
            "#####.###.#.###.#####",
            "#.........H.........#",
            "#.###.#.#####.#.###.#",
            "     .#...#...#.     ",
            "#####.#.#####.#.#####",
            "#.........H.........#",
            "#.###.#.#####.#.###.#",
            "#.....#...#...#.....#",
            "#.###.###.#.###.###.#",
            "#o..#.....H.....#..o#",
            "###.#.#.#####.#.#.###",
            "#H....#...#...#....H#",
            "#.........#.........#",
            "#####################"
    };

    private final int rows = RAW_MAP.length;
    private final int cols = RAW_MAP[0].length();
    private final int boardWidth = cols * TILE;
    private final int boardHeight = rows * TILE;
    private final int screenHeight = boardHeight + HUD_HEIGHT;

    private final Timer timer = new Timer(FPS_DELAY, this);
    private final Random random = new Random();

    private char[][] map;
    private Player player;
    private final ArrayList<Hunter> hunters = new ArrayList<>();

    private Direction wantedDir = Direction.NONE;

    private int score = 0;
    private int highScore = 0;
    private int lives = 3;
    private int level = 1;

    private int pelletsLeft = 0;
    private int totalPellets = 0;

    private int powerTicks = 0;
    private int powerCombo = 0;

    private int fruitTicks = 0;
    private int fruitCol = 10;
    private int fruitRow = 8;

    private int animationTick = 0;
    private int respawnSafeTicks = 120;
    private int levelStartTicks = 100;
    private int modeTicks = 0;

    private boolean gameOver = false;
    private boolean paused = false;
    private boolean fruitActive = false;
    private boolean fruitCollectedThisLevel = false;

    private String message = "READY";
    private int messageTicks = 100;

    public ManPacPanel() {
        setPreferredSize(new Dimension(boardWidth, screenHeight));
        setBackground(new Color(6, 7, 14));
        setFocusable(true);
        addKeyListener(this);
        loadLevel(true);
    }

    public void startGame() {
        requestFocusInWindow();
        timer.start();
    }

    private void loadLevel(boolean resetPlayerStats) {
        map = new char[rows][cols];
        hunters.clear();

        pelletsLeft = 0;
        totalPellets = 0;

        fruitActive = false;
        fruitCollectedThisLevel = false;

        powerTicks = 0;
        powerCombo = 0;
        modeTicks = 0;

        levelStartTicks = 100;
        respawnSafeTicks = 120;

        wantedDir = Direction.NONE;

        message = "READY";
        messageTicks = 100;

        int playerCol = 1;
        int playerRow = 1;
        int hunterIndex = 0;

        for (int row = 0; row < rows; row++) {
            String line = RAW_MAP[row];

            if (line.length() != cols) {
                throw new IllegalStateException("Map row " + row + " is not " + cols + " columns long.");
            }

            for (int col = 0; col < cols; col++) {
                char tile = line.charAt(col);

                if (tile == 'P') {
                    playerCol = col;
                    playerRow = row;
                    map[row][col] = ' ';
                } else if (tile == 'H') {
                    Hunter hunter = new Hunter(center(col), center(row), col, row, hunterIndex);
                    hunters.add(hunter);
                    hunterIndex++;
                    map[row][col] = ' ';
                } else {
                    map[row][col] = tile;

                    if (tile == '.' || tile == 'o') {
                        pelletsLeft++;
                        totalPellets++;
                    }
                }
            }
        }

        if (resetPlayerStats || player == null) {
            player = new Player(center(playerCol), center(playerRow), playerCol, playerRow);
        } else {
            player.resetToStart(center(playerCol), center(playerRow), playerCol, playerRow);
        }
    }

    private void restartWholeGame() {
        score = 0;
        lives = 3;
        level = 1;
        gameOver = false;
        paused = false;
        loadLevel(true);
    }

    private void nextLevel() {
        level++;
        message = "LEVEL " + level;
        messageTicks = 120;
        loadLevel(false);
    }

    private void resetPositionsAfterHit() {
        player.resetToStart(center(player.startCol), center(player.startRow), player.startCol, player.startRow);

        wantedDir = Direction.NONE;
        powerTicks = 0;
        powerCombo = 0;

        respawnSafeTicks = 150;
        levelStartTicks = 80;

        message = "WATCH OUT";
        messageTicks = 90;

        for (Hunter hunter : hunters) {
            hunter.reset();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused && !gameOver) {
            updateGame();
        }

        repaint();
    }

    private void updateGame() {
        animationTick++;
        modeTicks++;

        if (messageTicks > 0) {
            messageTicks--;
        }

        if (respawnSafeTicks > 0) {
            respawnSafeTicks--;
        }

        if (levelStartTicks > 0) {
            levelStartTicks--;
            return;
        }

        if (powerTicks > 0) {
            powerTicks--;

            if (powerTicks == 0) {
                powerCombo = 0;
                message = "POWER OFF";
                messageTicks = 60;
            }
        }

        if (fruitTicks > 0) {
            fruitTicks--;

            if (fruitTicks == 0) {
                fruitActive = false;
            }
        }

        updatePlayer();
        eatCurrentTile();
        updateFruitSpawn();
        updateHunters();
        checkHunterCollisions();

        if (pelletsLeft <= 0) {
            nextLevel();
        }

        highScore = Math.max(highScore, score);
    }

    private void updatePlayer() {
        if (isNearTileCenter(player.x, player.y, player.speed + 1.2)) {
            snapToTileCenter(player);

            if (canMove(player, wantedDir)) {
                player.dir = wantedDir;
            }

            if (!canMove(player, player.dir)) {
                player.dir = Direction.NONE;
            }
        }

        player.x += player.dir.dx * player.speed;
        player.y += player.dir.dy * player.speed;

        wrapEntity(player);
    }

    private void updateHunters() {
        for (Hunter hunter : hunters) {
            double speed = hunter.getCurrentSpeed(level, powerTicks > 0);

            if (hunter.eaten && distance(hunter.x, hunter.y, center(hunter.startCol), center(hunter.startRow)) < 5) {
                hunter.eaten = false;
                hunter.dir = Direction.NONE;
                continue;
            }

            if (isNearTileCenter(hunter.x, hunter.y, speed + 1.0)) {
                snapToTileCenter(hunter);
                hunter.dir = chooseHunterDirection(hunter);
            }

            hunter.x += hunter.dir.dx * speed;
            hunter.y += hunter.dir.dy * speed;

            wrapEntity(hunter);
        }
    }

    private Direction chooseHunterDirection(Hunter hunter) {
        List<Direction> valid = new ArrayList<>();

        for (Direction dir : Direction.movementDirections()) {
            if (canMove(hunter, dir)) {
                valid.add(dir);
            }
        }

        if (valid.isEmpty()) {
            return Direction.NONE;
        }

        Direction reverse = hunter.dir.opposite();

        if (valid.size() > 1 && !hunter.eaten) {
            valid.remove(reverse);
        }

        if (powerTicks > 0 && !hunter.eaten) {
            if (random.nextInt(100) < 45) {
                return valid.get(random.nextInt(valid.size()));
            }

            return directionFarthestFromPlayer(hunter, valid);
        }

        Point target = hunterTargetTile(hunter);

        Direction best = valid.get(0);
        double bestDistance = Double.MAX_VALUE;

        for (Direction dir : valid) {
            double nextX = hunter.x + dir.dx * TILE;
            double nextY = hunter.y + dir.dy * TILE;
            double distance = distance(nextX, nextY, center(target.x), center(target.y));

            if (distance < bestDistance) {
                bestDistance = distance;
                best = dir;
            }
        }

        return best;
    }

    private Direction directionFarthestFromPlayer(Hunter hunter, List<Direction> valid) {
        Direction best = valid.get(0);
        double bestDistance = -1;

        for (Direction dir : valid) {
            double nextX = hunter.x + dir.dx * TILE;
            double nextY = hunter.y + dir.dy * TILE;
            double distance = distance(nextX, nextY, player.x, player.y);

            if (distance > bestDistance) {
                bestDistance = distance;
                best = dir;
            }
        }

        return best;
    }

    private Point hunterTargetTile(Hunter hunter) {
        if (hunter.eaten) {
            return new Point(hunter.startCol, hunter.startRow);
        }

        if (currentHunterMode() == HunterMode.SCATTER) {
            return switch (hunter.personality % 4) {
                case 0 -> new Point(1, 1);
                case 1 -> new Point(cols - 2, 1);
                case 2 -> new Point(1, rows - 2);
                default -> new Point(cols - 2, rows - 2);
            };
        }

        int playerCol = colFromX(player.x);
        int playerRow = rowFromY(player.y);

        return switch (hunter.personality % 5) {
            case 0 -> new Point(playerCol, playerRow);
            case 1 -> new Point(playerCol + player.dir.dx * 4, playerRow + player.dir.dy * 4);
            case 2 -> new Point(playerCol - player.dir.dx * 3, playerRow - player.dir.dy * 3);
            case 3 -> new Point(playerCol + random.nextInt(5) - 2, playerRow + random.nextInt(5) - 2);
            default -> new Point(playerCol, playerRow + 2);
        };
    }

    private HunterMode currentHunterMode() {
        int cycle = modeTicks % (60 * 27);
        return cycle < 60 * 7 ? HunterMode.SCATTER : HunterMode.CHASE;
    }

    private void eatCurrentTile() {
        int col = colFromX(player.x);
        int row = rowFromY(player.y);

        if (!insideMap(row, col)) {
            return;
        }

        if (map[row][col] == '.') {
            map[row][col] = ' ';
            score += 10;
            pelletsLeft--;
        } else if (map[row][col] == 'o') {
            map[row][col] = ' ';
            score += 50;
            pelletsLeft--;
            powerTicks = 540;
            powerCombo = 0;
            message = "POWER MODE";
            messageTicks = 90;
        }

        if (fruitActive && col == fruitCol && row == fruitRow) {
            fruitActive = false;
            fruitTicks = 0;

            int fruitScore = 250 + level * 50;
            score += fruitScore;

            message = "+" + fruitScore + " FRUIT";
            messageTicks = 90;
        }
    }

    private void updateFruitSpawn() {
        if (fruitCollectedThisLevel || fruitActive) {
            return;
        }

        int eaten = totalPellets - pelletsLeft;

        if (eaten >= totalPellets / 2) {
            fruitActive = true;
            fruitCollectedThisLevel = true;
            fruitTicks = 60 * 9;
            message = "BONUS FRUIT";
            messageTicks = 90;
        }
    }

    private void checkHunterCollisions() {
        if (respawnSafeTicks > 0) {
            return;
        }

        Rectangle playerBox = player.getBounds();

        for (Hunter hunter : hunters) {
            if (hunter.eaten) {
                continue;
            }

            if (!playerBox.intersects(hunter.getBounds())) {
                continue;
            }

            if (powerTicks > 0) {
                powerCombo++;

                int hunterScore = 200 * (int) Math.pow(2, Math.min(powerCombo - 1, 3));
                score += hunterScore;

                hunter.eaten = true;
                hunter.dir = Direction.NONE;

                message = "+" + hunterScore;
                messageTicks = 55;
            } else {
                lives--;

                if (lives <= 0) {
                    lives = 0;
                    gameOver = true;
                    message = "GAME OVER";
                    messageTicks = 9999;
                } else {
                    resetPositionsAfterHit();
                }

                return;
            }
        }
    }

    private boolean canMove(Entity entity, Direction dir) {
        if (dir == Direction.NONE) {
            return true;
        }

        int col = colFromX(entity.x);
        int row = rowFromY(entity.y);

        if (!insideMap(row, col)) {
            return dir.dy == 0;
        }

        int nextCol = col + dir.dx;
        int nextRow = row + dir.dy;

        if (nextCol < 0 || nextCol >= cols) {
            return dir.dy == 0 && map[row][col] != '#';
        }

        if (nextRow < 0 || nextRow >= rows) {
            return false;
        }

        return map[nextRow][nextCol] != '#';
    }

    private boolean isNearTileCenter(double x, double y, double tolerance) {
        int col = colFromX(x);
        int row = rowFromY(y);

        if (!insideMap(row, col)) {
            return false;
        }

        return Math.abs(x - center(col)) <= tolerance && Math.abs(y - center(row)) <= tolerance;
    }

    private void snapToTileCenter(Entity entity) {
        int col = colFromX(entity.x);
        int row = rowFromY(entity.y);

        if (insideMap(row, col)) {
            entity.x = center(col);
            entity.y = center(row);
        }
    }

    private void wrapEntity(Entity entity) {
        if (entity.x < -TILE / 2.0) {
            entity.x = boardWidth + TILE / 2.0;
        } else if (entity.x > boardWidth + TILE / 2.0) {
            entity.x = -TILE / 2.0;
        }
    }

    private int center(int tileIndex) {
        return tileIndex * TILE + TILE / 2;
    }

    private int colFromX(double x) {
        return (int) Math.floor(x / TILE);
    }

    private int rowFromY(double y) {
        return (int) Math.floor(y / TILE);
    }

    private boolean insideMap(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        enableQuality(g2);

        drawBackground(g2);
        drawMaze(g2);
        drawPellets(g2);
        drawFruit(g2);
        drawHunters(g2);
        drawPlayer(g2);
        drawHUD(g2);
        drawMessage(g2);

        if (paused) {
            drawCenterOverlay(g2, "PAUSED", "Press P to continue");
        }

        if (gameOver) {
            drawCenterOverlay(g2, "GAME OVER", "Press R to restart");
        }

        g2.dispose();
    }

    private void enableQuality(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(new Color(5, 6, 13));
        g2.fillRect(0, 0, boardWidth, screenHeight);

        GradientPaint glow = new GradientPaint(
                0,
                HUD_HEIGHT,
                new Color(14, 21, 52),
                boardWidth,
                screenHeight,
                new Color(3, 4, 9)
        );

        g2.setPaint(glow);
        g2.fillRect(0, HUD_HEIGHT, boardWidth, boardHeight);
    }

    private void drawMaze(Graphics2D g2) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (map[row][col] != '#') {
                    continue;
                }

                int x = col * TILE;
                int y = HUD_HEIGHT + row * TILE;

                g2.setColor(new Color(22, 67, 170));
                g2.fillRoundRect(x + 2, y + 2, TILE - 4, TILE - 4, 10, 10);

                g2.setColor(new Color(98, 165, 255, 90));
                g2.drawRoundRect(x + 5, y + 5, TILE - 10, TILE - 10, 8, 8);
            }
        }
    }

    private void drawPellets(Graphics2D g2) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char tile = map[row][col];

                int cx = center(col);
                int cy = HUD_HEIGHT + center(row);

                if (tile == '.') {
                    g2.setColor(new Color(255, 235, 165));
                    g2.fillOval(cx - 3, cy - 3, 6, 6);
                } else if (tile == 'o') {
                    int pulse = 2 + (int) (Math.abs(Math.sin(animationTick * 0.13)) * 3);

                    g2.setColor(new Color(255, 235, 165));
                    g2.fillOval(cx - 8 - pulse, cy - 8 - pulse, 16 + pulse * 2, 16 + pulse * 2);
                }
            }
        }
    }

    private void drawFruit(Graphics2D g2) {
        if (!fruitActive) {
            return;
        }

        int cx = center(fruitCol);
        int cy = HUD_HEIGHT + center(fruitRow);
        int bounce = (int) (Math.sin(animationTick * 0.20) * 2);

        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillOval(cx - 11, cy + 7, 22, 7);

        g2.setColor(new Color(255, 70, 90));
        g2.fillOval(cx - 9, cy - 8 + bounce, 18, 18);

        g2.setColor(new Color(80, 220, 120));
        g2.fillOval(cx + 2, cy - 13 + bounce, 9, 6);
    }

    private void drawPlayer(Graphics2D g2) {
        int size = 24;
        int x = (int) player.x - size / 2;
        int y = HUD_HEIGHT + (int) player.y - size / 2;

        if (respawnSafeTicks > 0 && animationTick % 12 < 6) {
            return;
        }

        int mouth = 18 + (int) (Math.abs(Math.sin(animationTick * 0.25)) * 26);

        int startAngle = switch (player.dir) {
            case RIGHT -> mouth;
            case LEFT -> 180 + mouth;
            case UP -> 90 + mouth;
            case DOWN -> 270 + mouth;
            default -> 28;
        };

        g2.setColor(new Color(255, 213, 45));
        g2.fillArc(x, y, size, size, startAngle, 360 - mouth * 2);

        g2.setColor(new Color(255, 255, 255, 80));
        g2.fillOval(x + 6, y + 5, 7, 5);
    }

    private void drawHunters(Graphics2D g2) {
        for (Hunter hunter : hunters) {
            int size = 24;
            int x = (int) hunter.x - size / 2;
            int y = HUD_HEIGHT + (int) hunter.y - size / 2;

            Color bodyColor;

            if (hunter.eaten) {
                bodyColor = new Color(140, 145, 160, 160);
            } else if (powerTicks > 0) {
                boolean flash = powerTicks < 150 && animationTick % 18 < 9;
                bodyColor = flash ? new Color(245, 245, 255) : new Color(70, 210, 255);
            } else {
                bodyColor = hunter.baseColor;
            }

            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillOval(x - 2, y + size - 6, size + 4, 9);

            g2.setColor(bodyColor);
            g2.fillRoundRect(x, y, size, size, 12, 12);
            g2.fillOval(x, y - 4, size, size);

            g2.setColor(Color.WHITE);
            g2.fillOval(x + 5, y + 7, 7, 7);
            g2.fillOval(x + 14, y + 7, 7, 7);

            g2.setColor(Color.BLACK);

            int eyeOffsetX = hunter.dir.dx;
            int eyeOffsetY = hunter.dir.dy;

            g2.fillOval(x + 7 + eyeOffsetX, y + 9 + eyeOffsetY, 3, 3);
            g2.fillOval(x + 16 + eyeOffsetX, y + 9 + eyeOffsetY, 3, 3);
        }
    }

    private void drawHUD(Graphics2D g2) {
        g2.setColor(new Color(7, 9, 18));
        g2.fillRect(0, 0, boardWidth, HUD_HEIGHT);

        g2.setColor(new Color(255, 255, 255, 25));
        g2.drawLine(0, HUD_HEIGHT - 1, boardWidth, HUD_HEIGHT - 1);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 25));
        g2.setColor(Color.WHITE);
        g2.drawString("ManPac.exe", 18, 39);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.setColor(new Color(218, 222, 235));

        g2.drawString("Score: " + score, 190, 37);
        g2.drawString("High: " + highScore, 290, 37);
        g2.drawString("Lives: " + lives, 390, 37);
        g2.drawString("Level: " + level, 472, 37);

        if (powerTicks > 0) {
            g2.setColor(new Color(90, 225, 255));
            g2.drawString("Power: " + Math.max(1, powerTicks / 60) + "s", 552, 37);
        } else {
            g2.setColor(currentHunterMode() == HunterMode.SCATTER
                    ? new Color(170, 190, 255)
                    : new Color(255, 150, 150));

            g2.drawString("Mode: " + currentHunterMode(), 552, 37);
        }

        g2.setColor(new Color(170, 175, 190));
        g2.drawString(
                "WASD / Arrows = Move | P = Pause | R = Restart | Eat pellets, use power orbs, dodge hunters",
                18,
                63
        );
    }

    private void drawMessage(Graphics2D g2) {
        if (messageTicks <= 0 || message == null || message.isBlank()) {
            return;
        }

        g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
        g2.setColor(new Color(255, 255, 255, 220));

        int textWidth = g2.getFontMetrics().stringWidth(message);

        g2.drawString(message, (boardWidth - textWidth) / 2, HUD_HEIGHT + boardHeight / 2);
    }

    private void drawCenterOverlay(Graphics2D g2, String title, String subtitle) {
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, boardWidth, screenHeight);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 54));
        g2.setColor(Color.WHITE);

        int titleWidth = g2.getFontMetrics().stringWidth(title);

        g2.drawString(title, (boardWidth - titleWidth) / 2, screenHeight / 2 - 24);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 21));
        g2.setColor(new Color(220, 225, 240));

        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);

        g2.drawString(subtitle, (boardWidth - subtitleWidth) / 2, screenHeight / 2 + 22);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            wantedDir = Direction.UP;
        }

        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            wantedDir = Direction.DOWN;
        }

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            wantedDir = Direction.LEFT;
        }

        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            wantedDir = Direction.RIGHT;
        }

        if (code == KeyEvent.VK_P && !gameOver) {
            paused = !paused;
        }

        if (code == KeyEvent.VK_R) {
            restartWholeGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    enum HunterMode {
        CHASE,
        SCATTER
    }

    enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0),
        NONE(0, 0);

        final int dx;
        final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
                case NONE -> NONE;
            };
        }

        static Direction[] movementDirections() {
            return new Direction[]{UP, DOWN, LEFT, RIGHT};
        }
    }

    static class Entity {
        double x;
        double y;
        double speed;
        Direction dir = Direction.NONE;

        Rectangle getBounds() {
            return new Rectangle((int) x - 10, (int) y - 10, 20, 20);
        }
    }

    static class Player extends Entity {
        int startCol;
        int startRow;

        Player(double x, double y, int startCol, int startRow) {
            resetToStart(x, y, startCol, startRow);
        }

        void resetToStart(double x, double y, int startCol, int startRow) {
            this.x = x;
            this.y = y;
            this.startCol = startCol;
            this.startRow = startRow;
            this.speed = 2.85;
            this.dir = Direction.NONE;
        }
    }

    class Hunter extends Entity {
        final int startCol;
        final int startRow;
        final int personality;
        final Color baseColor;

        boolean eaten = false;

        Hunter(double x, double y, int startCol, int startRow, int personality) {
            this.x = x;
            this.y = y;
            this.startCol = startCol;
            this.startRow = startRow;
            this.personality = personality;
            this.baseColor = pickColor(personality);
            this.speed = 2.05;
        }

        void reset() {
            this.x = center(startCol);
            this.y = center(startRow);
            this.dir = Direction.NONE;
            this.eaten = false;
        }

        double getCurrentSpeed(int level, boolean frightened) {
            if (eaten) {
                return 3.45 + level * 0.04;
            }

            if (frightened) {
                return 1.55 + level * 0.02;
            }

            return 2.00 + Math.min(0.65, level * 0.06);
        }

        private Color pickColor(int index) {
            Color[] colors = {
                    new Color(255, 75, 95),
                    new Color(255, 145, 55),
                    new Color(210, 80, 255),
                    new Color(90, 220, 150),
                    new Color(255, 90, 190),
                    new Color(90, 160, 255)
            };

            return colors[index % colors.length];
        }
    }
}
