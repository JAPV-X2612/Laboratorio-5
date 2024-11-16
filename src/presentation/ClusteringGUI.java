package presentation;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * ClusteringGUI is the main graphical user interface for the Clustering game.
 * This class sets up the initial window with a specified size and title and centers it on the screen, serving as the starting point for the GUI.
 * 
 * @author: Jesús Pinzón & Alison Valderrama
 * @version 1.0 (2024/11/07)
 */
public class ClusteringGUI extends JFrame {

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = screenSize.width / 2;
    private static final int HEIGHT = screenSize.height / 2;
    private static final Dimension windowSize = new Dimension(WIDTH / 2, HEIGHT / 2); // Adjusted to a quarter size

    /**
     * Constructor that sets up the initial configuration for the Clustering GUI.
     */
    private ClusteringGUI() {
        prepareElements();
    }

    /**
     * Prepares the basic elements of the window, including setting the title, size and location on the screen.
     */
    private void prepareElements() {
        setTitle("Clustering");
        setSize(windowSize);
        setLocation((screenSize.width - WIDTH / 2) / 2, (screenSize.height - HEIGHT / 2) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * The main method that creates an instance of ClusteringGUI and displays it.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        ClusteringGUI clustering = new ClusteringGUI();
        clustering.setVisible(true);
    }
}