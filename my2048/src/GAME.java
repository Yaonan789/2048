import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GAME extends JPanel {
    // 游戏状态枚举，分别表示游戏的四种状态：开始、赢了、进行中、结束
    enum State {
        start, won, running, over
    }

    // 定义颜色表，每个颜色表示不同数值的方块背景颜色
    final Color[] colorTable = {
            new Color(0x701710), new Color(0xFFE4C3), new Color(0xfff4d3),
            new Color(0xffdac3), new Color(0xe7b08e), new Color(0xe7bf8e),
            new Color(0xffc4c3), new Color(0xE7948e), new Color(0xbe7e56),
            new Color(0xbe5e56), new Color(0x9c3931), new Color(0x701710)
    };

    // 定义目标值，游戏结束条件为达到2048
    final static int target = 2048;

    // 记录当前游戏的最高值和得分
    static int highest;
    static int score;

    // 定义颜色，用于绘制棋盘背景和空格子
    private Color gridColor = new Color(0xBBADA0);
    private Color emptyColor = new Color(0xCDC1B4);
    private Color startColor = new Color(0xFFEBCD);

    // 随机数生成器，用于产生随机方块
    private Random rand = new Random();

    // 二维数组表示方块
    private Tile[][] tiles;

    // 棋盘的边长，默认为4x4
    private int side = 4;

    // 当前游戏的状态
    private State gamestate = State.start;

    // 是否在检查可用的移动
    private boolean checkingAvailableMoves;

    // 构造函数，初始化游戏界面
    public GAME() {
        setPreferredSize(new Dimension(900, 700));  // 设置界面大小
        setBackground(new Color(0xFAF8EF));         // 设置背景颜色
        setFont(new Font("SansSerif", Font.BOLD, 48));  // 设置字体
        setFocusable(true);                         // 设置焦点

        // 添加鼠标点击监听器，点击开始游戏
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startGame();  // 开始游戏
                repaint();    // 重绘界面
            }
        });

        // 添加键盘监听器，控制方向键移动方块
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 根据方向键移动方块
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        moveUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        moveDown();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRight();
                        break;
                }
                repaint();  // 移动后重绘界面
            }
        });
    }

    // 重写paintComponent方法，绘制游戏界面
    @Override
    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);  // 设置抗锯齿
        drawGrid(g);  // 绘制棋盘
    }

    // 开始游戏，初始化方块
    void startGame() {
        if (gamestate != State.running) {
            score = 0;         // 重置得分
            highest = 0;       // 重置最高方块值
            gamestate = State.running;  // 设置游戏状态为进行中
            tiles = new Tile[side][side];  // 初始化方块
            addRandomTile();  // 随机添加两个方块
            addRandomTile();
        }
    }

    // 绘制棋盘，包括方块及游戏信息
    void drawGrid(Graphics2D g) {
        g.setColor(gridColor);
        g.fillRoundRect(200, 100, 499, 499, 15, 15);  // 绘制棋盘背景
        if (gamestate == State.running) {
            // 如果游戏正在进行，绘制所有方块
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    if (tiles[r][c] == null) {
                        g.setColor(emptyColor);  // 空方块
                        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);
                    } else {
                        drawTile(g, r, c);  // 绘制实际存在的方块
                    }
                }
            }
        } else {
            // 如果游戏未进行，显示开始或结束画面
            g.setColor(startColor);
            g.fillRoundRect(215, 115, 469, 469, 7, 7);
            g.setColor(gridColor.darker());
            g.setFont(new Font("SansSerif", Font.BOLD, 128));
            g.drawString("2048", 310, 270);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            if (gamestate == State.won) {
                g.drawString("you made it!", 390, 350);  // 赢了
            } else if (gamestate == State.over)
                g.drawString("game over", 400, 350);  // 游戏结束
            g.setColor(gridColor);
            g.drawString("click to start a new game", 330, 470);  // 提示点击开始新游戏
            g.drawString("(use arrow keys to move tiles)", 310, 530);  // 提示使用方向键移动方块
        }
    }

    // 绘制单个方块
    void drawTile(Graphics2D g, int r, int c) {
        int value = tiles[r][c].getValue();  // 获取方块的值
        g.setColor(colorTable[(int) (Math.log(value) / Math.log(2)) + 1]);  // 根据方块值设置颜色
        g.fillRoundRect(215 + c * 121, 115 + r * 121, 106, 106, 7, 7);
        String s = String.valueOf(value);  // 方块的数字
        g.setColor(value < 128 ? colorTable[0] : colorTable[1]);  // 根据数值调整字体颜色
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int dec = fm.getDescent();
        int x = 215 + c * 121 + (106 - fm.stringWidth(s)) / 2;
        int y = 115 + r * 121 + (asc + (106 - (asc + dec)) / 2);
        g.drawString(s, x, y);  // 绘制方块上的数字
    }

    // 添加一个随机方块（2或4）
    private void addRandomTile() {
        int pos = rand.nextInt(side * side);  // 随机位置
        int row, col;
        do {
            pos = (pos + 1) % (side * side);
            row = pos / side;
            col = pos % side;
        } while (tiles[row][col] != null);  // 确保该位置为空
        int val = rand.nextInt(10) == 0 ? 4 : 2;  // 10%的概率生成4，其他生成2
        tiles[row][col] = new Tile(val);
    }

    // 移动方块的逻辑，基于输入的方向
    private boolean move(int countDownFrom, int yIncr, int xIncr) {
        boolean moved = false;
        for (int i = 0; i < side * side; i++) {
            int j = Math.abs(countDownFrom - i);
            int r = j / side;
            int c = j % side;
            if (tiles[r][c] == null)
                continue;
            int nextR = r + yIncr;
            int nextC = c + xIncr;
            while (nextR >= 0 && nextR < side && nextC >= 0 && nextC < side) {
                Tile next = tiles[nextR][nextC];
                Tile curr = tiles[r][c];
                if (next == null) {
                    if (checkingAvailableMoves)
                        return true;
                    tiles[nextR][nextC] = curr;
                    tiles[r][c] = null;
                    r = nextR;
                    c = nextC;
                    nextR += yIncr;
                    nextC += xIncr;
                    moved = true;
                } else if (next.canMergeWith(curr)) {
                    if (checkingAvailableMoves)
                        return true;
                    int value = next.mergeWith(curr);  // 合并两个相同的方块
                    if (value > highest)
                        highest = value;
                    score += value;
                    tiles[r][c] = null;
                    moved = true;
                    break;
                } else
                    break;
            }
        }
        if (moved) {
            if (highest < target) {
                clearMerged();  // 清除合并标记
                addRandomTile();  // 每次移动后添加新方块
                if (!movesAvailable()) {
                    gamestate = State.over;  // 如果没有可用移动，游戏结束
                }
            } else if (highest == target)
                gamestate = State.won;  // 达到2048，游戏胜利
        }
        return moved;
    }

    // 四个方向的移动函数
    boolean moveUp() {
        return move(0, -1, 0);
    }

    boolean moveDown() {
        return move(side * side - 1, 1, 0);
    }

    boolean moveLeft() {
        return move(0, 0, -1);
    }

    boolean moveRight() {
        return move(side * side - 1, 0, 1);
    }

    // 清除方块的合并标记
    void clearMerged() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile != null)
                    tile.setMerged(false);
    }

    // 判断是否还有可用的移动
    boolean movesAvailable() {
        checkingAvailableMoves = true;
        boolean hasMoves = moveUp() || moveDown() || moveLeft() || moveRight();
        checkingAvailableMoves = false;
        return hasMoves;
    }

    // 主函数，启动游戏
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setTitle("2048");
            f.setResizable(true);
            f.add(new GAME(), BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}

// 方块类，包含方块的数值及合并状态
class Tile {
    private boolean merged;  // 记录方块是否合并过
    private int value;       // 方块的值

    Tile(int val) {
        value = val;
    }

    int getValue() {
        return value;
    }

    void setMerged(boolean m) {
        merged = m;
    }

    // 判断两个方块是否可以合并
    boolean canMergeWith(Tile other) {
        return !merged && other != null && !other.merged && value == other.getValue();
    }

    // 合并两个方块，并返回合并后的值
    int mergeWith(Tile other) {
        if (canMergeWith(other)) {
            value *= 2;
            merged = true;
            return value;
        }
        return -1;
    }
}
