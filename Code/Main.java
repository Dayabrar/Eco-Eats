import javax.swing.*;

/**
 * Eco-Eats Nutrition Tracker
 * Main Application Entry Point
 * Version: 1.0
 * Author: Eco-Eats Development Team
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database
        DatabaseHelper.initializeDatabase();
        DatabaseHelper.initializeFoodDatabase();

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginPage();
        });
    }
}