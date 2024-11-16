package domain;

/**
 * ClusteringException is a custom exception class for handling specific errors in the Clustering game.
 * 
 * @author: Jesús Pinzón & Alison Valderrama
 * @version 1.0 (2024/11/07)
 */
public class ClusteringException extends Exception {

    // Predefined exception messages
    public static final String INVALID_WIDTH = "The board width is invalid.";
    public static final String INVALID_HEIGHT = "The board height is invalid.";
    public static final String INVALID_PERCENTAGE = "The tile occupancy percentage is invalid.";
    public static final String INVALID_TILT = "The indicated tilt is invalid.";
    public static final String NO_FOUND_FILE_PATH = "The directory for the specified file was not found.";
    public static final String NO_FOUND_GAME_FILE = "The game file was not found at the specified path.";

    /**
     * Constructs a ClusteringException with the specified detail message.
     * @param message The detail message of the exception.
     */
    public ClusteringException(String message) {
        super(message);
    }
}

