package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * ClusteringGUI is the main graphical user interface for the Clustering game.
 * This class sets up the initial window with a specified size and title and centers it on the screen, serving as the starting point for the GUI.
 *
 * @author: Jesús Pinzón & Alison Valderrama
 * @version 1.2 (2024/11/16)
 */
public class ClusteringGUI extends JFrame {
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = screenSize.width / 2;
    private static final int HEIGHT = screenSize.height / 2;
    private static final Dimension windowSize = new Dimension(WIDTH, HEIGHT);

    private JMenuItem menuNew;
    private JPanel boardPanel;
    private JLabel moveCountLabel;
    private JLabel scoreLabel;
    private JLabel occupancyLabel;

    private int moves = 0;
    private int score = 0;
    private int boardWidth = 4;
    private int boardHeight = 4;
    private double occupancyRate = 0.7;

    private final Color[] TILE_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    private final Random random = new Random();

    /**
     * Constructor that sets up the initial configuration for the Clustering GUI.
     */
    private ClusteringGUI() {
        prepareElements();
    }

    private void prepareElements() {
        setTitle("Clustering");
        setSize(windowSize);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setJMenuBar(prepareMenuBar());
        prepareBoard();
    }

    private JMenuBar prepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");

        menuNew = new JMenuItem("New");
        menuNew.addActionListener(e -> JOptionPane.showMessageDialog(this, "UNDER CONSTRUCTION: New"));

        JMenuItem menuOpen = createFileChooserMenuItem("Open", JFileChooser.OPEN_DIALOG);
        JMenuItem menuSave = createFileChooserMenuItem("Save", JFileChooser.SAVE_DIALOG);

        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(e -> closeOption());

        menuFile.add(menuNew);
        menuFile.add(menuOpen);
        menuFile.add(menuSave);
        menuFile.addSeparator();
        menuFile.add(menuExit);
        menuBar.add(menuFile);

        return menuBar;
    }

    private JMenuItem createFileChooserMenuItem(String title, int dialogType) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = dialogType == JFileChooser.OPEN_DIALOG ?
                    fileChooser.showOpenDialog(this) :
                    fileChooser.showSaveDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "UNDER CONSTRUCTION: " + fileChooser.getSelectedFile().getName());
            }
        });
        return menuItem;
    }

    private void prepareBoard() {
        setLayout(new BorderLayout());

        JPanel leftPanel = createLeftPanel();
        boardPanel = new JPanel(new GridLayout(boardHeight, boardWidth, 2, 2));
        JPanel rightPanel = createRightPanel();
        JPanel bottomPanel = createBottomPanel();

        initializeBoard();

        add(leftPanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnSetColors = new JButton("Set Tiles Colors");
        JButton btnSetBoard = new JButton("Set Board Game");
        JButton btnSetOccupancy = new JButton("Set Occupancy Rate");

        btnSetColors.addActionListener(e -> JOptionPane.showMessageDialog(this, "Select a tile to change its color"));
        btnSetBoard.addActionListener(e -> setBoardDimensions());
        btnSetOccupancy.addActionListener(e -> setOccupancyRate());

        panel.add(btnSetColors);
        panel.add(btnSetBoard);
        panel.add(btnSetOccupancy);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel movementPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        Font buttonFont = new Font("Arial", Font.BOLD, 35);

        JButton btnUp = createDirectionButton("↑", buttonFont, "Up");
        JButton btnDown = createDirectionButton("↓", buttonFont, "Down");
        JButton btnLeft = createDirectionButton("←", buttonFont, "Left");
        JButton btnRight = createDirectionButton("→", buttonFont, "Right");

        movementPanel.add(new JLabel());
        movementPanel.add(btnUp);
        movementPanel.add(new JLabel());
        movementPanel.add(btnLeft);
        movementPanel.add(new JLabel());
        movementPanel.add(btnRight);
        movementPanel.add(new JLabel());
        movementPanel.add(btnDown);
        movementPanel.add(new JLabel());

        panel.add(movementPanel, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("REFRESH");
        btnRefresh.addActionListener(e -> refreshGame());
        panel.add(btnRefresh, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createDirectionButton(String text, Font font, String direction) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.addActionListener(e -> move(direction));
        return button;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        moveCountLabel = new JLabel("Moves: 0");
        scoreLabel = new JLabel("Score: 0");
        occupancyLabel = new JLabel(String.format("Occupancy: %.1f%%", occupancyRate * 100));

        panel.add(moveCountLabel);
        panel.add(scoreLabel);
        panel.add(occupancyLabel);

        return panel;
    }

    private void setOccupancyRate() {
        String input = JOptionPane.showInputDialog(this, "Enter occupancy rate (1-100):", "Set Occupancy Rate", JOptionPane.QUESTION_MESSAGE);

        try {
            int rate = Integer.parseInt(input);
            if (rate > 0 && rate <= 100) {
                occupancyRate = rate / 100.0;
                refreshGame();
            } else {
                showErrorMessage("Please enter a number between 1 and 100");
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid number");
        }
    }

    private void initializeBoard() {
        boardPanel.removeAll();
        int totalTiles = boardWidth * boardHeight;
        int occupiedTiles = (int) (totalTiles * occupancyRate);
        boolean[] occupied = new boolean[totalTiles];

        for (int i = 0; i < occupiedTiles; i++) {
            int pos;
            do {
                pos = random.nextInt(totalTiles);
            } while (occupied[pos]);
            occupied[pos] = true;
        }

        for (int i = 0; i < totalTiles; i++) {
            JButton tile = new JButton();
            tile.setOpaque(true);
            tile.setPreferredSize(new Dimension(50, 50));

            if (occupied[i]) {
                tile.setBackground(TILE_COLORS[random.nextInt(TILE_COLORS.length)]);
                tile.addActionListener(e -> cycleTileColor(tile));
            } else {
                tile.setEnabled(false);
                tile.setBackground(Color.WHITE);
            }

            boardPanel.add(tile);
        }

        occupancyLabel.setText(String.format("Occupancy: %.1f%%", occupancyRate * 100));
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void cycleTileColor(JButton tile) {
        Color currentColor = tile.getBackground();
        int nextColorIndex = (java.util.Arrays.asList(TILE_COLORS).indexOf(currentColor) + 1) % TILE_COLORS.length;
        tile.setBackground(TILE_COLORS[nextColorIndex]);
    }

    private void setBoardDimensions() {
        JTextField widthField = new JTextField(5);
        JTextField heightField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Width (3-20):"));
        panel.add(widthField);
        panel.add(new JLabel("Height (3-20):"));
        panel.add(heightField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Set Board Dimensions", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int newWidth = Integer.parseInt(widthField.getText().trim());
                int newHeight = Integer.parseInt(heightField.getText().trim());

                if (isValidDimension(newWidth) && isValidDimension(newHeight)) {
                    boardWidth = newWidth;
                    boardHeight = newHeight;
                    boardPanel.setLayout(new GridLayout(boardHeight, boardWidth, 2, 2));
                    initializeBoard();
                } else {
                    showErrorMessage("Width and height must be between 3 and 20");
                }
            } catch (NumberFormatException e) {
                showErrorMessage("Please enter valid integers");
            }
        }
    }

    private boolean isValidDimension(int value) {
        return value >= 3 && value <= 20;
    }

    private void refreshGame() {
        moves = 0;
        score = 0;
        initializeBoard();
        moveCountLabel.setText("Moves: 0");
        scoreLabel.setText("Score: 0");
    }

    private void move(String direction) {
        moves++;
        score += random.nextInt(10); // Example scoring logic
        moveCountLabel.setText("Moves: " + moves);
        scoreLabel.setText("Score: " + score);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void closeOption() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClusteringGUI::new);
    }
}
