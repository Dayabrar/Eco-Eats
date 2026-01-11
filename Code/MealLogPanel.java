import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;

/**
 * Meal Log Panel - Complete Implementation with Calendar
 * Shows all consumed foods with delete functionality and date navigation
 * Version: 3.0 - Fixed: Can now add/delete food for any date with proper daily_logs sync
 */
public class MealLogPanel extends JPanel {
    private int userId;
    private JTable mealsTable;
    private DefaultTableModel tableModel;
    private JLabel totalCaloriesLabel;
    private JButton refreshButton;
    private JButton deleteMealButton;
    private JButton addFoodButton;
    private JButton todayButton;
    private JDateChooser dateChooser;
    private LocalDate selectedDate;
    private java.util.Map<Integer, Integer> rowToLogIdMap;
    private JFrame parentFrame;

    public MealLogPanel(int userId) {
        this.userId = userId;
        this.selectedDate = LocalDate.now();
        this.rowToLogIdMap = new java.util.HashMap<>();
        this.parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Meal Log");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 60, 35));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Date picker and buttons panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setOpaque(false);

        // Date picker
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        controlPanel.add(dateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setDate(java.sql.Date.valueOf(selectedDate));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(140, 30));
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 13));
        dateChooser.setMaxSelectableDate(new java.util.Date());
        dateChooser.addPropertyChangeListener("date", evt -> {
            if (dateChooser.getDate() != null) {
                selectedDate = new java.sql.Date(dateChooser.getDate().getTime()).toLocalDate();
                loadMealData();
            }
        });
        controlPanel.add(dateChooser);

        todayButton = createStyledButton("Today", new Color(76, 175, 80));
        todayButton.addActionListener(e -> {
            selectedDate = LocalDate.now();
            dateChooser.setDate(java.sql.Date.valueOf(selectedDate));
            loadMealData();
        });
        controlPanel.add(todayButton);

        addFoodButton = createStyledButton("➕ Add Food", new Color(123, 141, 74));
        addFoodButton.addActionListener(e -> showAddFoodDialog());
        controlPanel.add(addFoodButton);

        refreshButton = createStyledButton("🔄 Refresh", new Color(33, 150, 243));
        refreshButton.addActionListener(e -> loadMealData());
        controlPanel.add(refreshButton);

        deleteMealButton = createStyledButton("🗑 Delete Selected", new Color(244, 67, 54));
        deleteMealButton.addActionListener(e -> deleteSelectedMeal());
        controlPanel.add(deleteMealButton);

        headerPanel.add(controlPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Time", "Meal Type", "Food Item", "Quantity", "Calories", "Protein", "Carbs", "Fats"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        mealsTable = new JTable(tableModel);
        mealsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        mealsTable.setRowHeight(30);
        mealsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mealsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        mealsTable.getTableHeader().setBackground(new Color(123, 141, 74));
        mealsTable.getTableHeader().setForeground(Color.WHITE);

        // Column widths
        mealsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        mealsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        mealsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        mealsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        mealsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        mealsTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        mealsTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        mealsTable.getColumnModel().getColumn(7).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(mealsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(scrollPane, BorderLayout.CENTER);

        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(new CompoundBorder(
                new EmptyBorder(10, 0, 0, 0),
                new LineBorder(new Color(220, 220, 220), 1)
        ));

        totalCaloriesLabel = new JLabel("Total Calories: 0 kcal");
        totalCaloriesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalCaloriesLabel.setForeground(new Color(123, 141, 74));
        summaryPanel.add(totalCaloriesLabel);

        add(summaryPanel, BorderLayout.SOUTH);

        loadMealData();
    }

    private void loadMealData() {
        tableModel.setRowCount(0);
        rowToLogIdMap.clear();
        int totalCalories = 0;
        int rowIndex = 0;

        String sql = """
            SELECT fl.consumed_at, fl.meal_type, fi.name, fl.quantity, fl.unit,
                   ROUND((fi.calories * fl.quantity) / fi.base_quantity) as calories,
                   ROUND((fi.protein_g * fl.quantity) / fi.base_quantity, 1) as protein,
                   ROUND((fi.carbs_g * fl.quantity) / fi.base_quantity, 1) as carbs,
                   ROUND((fi.fats_g * fl.quantity) / fi.base_quantity, 1) as fats,
                   fl.id as log_id
            FROM food_logs fl
            JOIN food_items fi ON fl.food_item_id = fi.id
            WHERE fl.user_id = ? AND DATE(fl.consumed_at) = ?
            ORDER BY fl.consumed_at DESC
        """;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(selectedDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("consumed_at");
                String timeStr = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
                String mealType = rs.getString("meal_type");
                String foodName = rs.getString("name");
                String quantity = rs.getInt("quantity") + " " + rs.getString("unit");
                int calories = rs.getInt("calories");
                double protein = rs.getDouble("protein");
                double carbs = rs.getDouble("carbs");
                double fats = rs.getDouble("fats");
                int logId = rs.getInt("log_id");

                rowToLogIdMap.put(rowIndex, logId);

                tableModel.addRow(new Object[]{
                        timeStr, mealType, foodName, quantity,
                        calories + " kcal", protein + "g", carbs + "g", fats + "g"
                });

                totalCalories += calories;
                rowIndex++;
            }

            String dateDisplay = selectedDate.equals(LocalDate.now()) ?
                    "Today's Total: " : "Total for " + selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + ": ";
            totalCaloriesLabel.setText(dateDisplay + totalCalories + " kcal");

        } catch (SQLException e) {
            System.err.println("Error loading meal data: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Failed to load meal data",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddFoodDialog() {
        // Get parent frame
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: Could not find parent window",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a custom dialog for the selected date
        new AddFoodForDateDialog(frame, userId, selectedDate, this);
    }

    private void deleteSelectedMeal() {
        int selectedRow = mealsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a meal to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer logId = rowToLogIdMap.get(selectedRow);
        if (logId == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: Could not find meal ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String foodName = (String) tableModel.getValueAt(selectedRow, 2);

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete meal: " + foodName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (deleteMealAndRecalculate(logId)) {
                JOptionPane.showMessageDialog(this,
                        "Meal deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadMealData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete meal",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean deleteMealAndRecalculate(int logId) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Get the food log details before deleting
                String getFoodLogSql = "SELECT user_id, DATE(consumed_at) as log_date FROM food_logs WHERE id = ?";
                PreparedStatement getFoodLogStmt = conn.prepareStatement(getFoodLogSql);
                getFoodLogStmt.setInt(1, logId);
                ResultSet rs = getFoodLogStmt.executeQuery();

                if (!rs.next()) {
                    conn.rollback();
                    return false;
                }

                int userId = rs.getInt("user_id");
                java.sql.Date logDate = rs.getDate("log_date");
                getFoodLogStmt.close();

                // Delete the food log entry
                String deleteSql = "DELETE FROM food_logs WHERE id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, logId);
                deleteStmt.executeUpdate();
                deleteStmt.close();

                // Recalculate daily totals for that date
                recalculateDailyLog(conn, userId, logDate);

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error in delete transaction: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting meal: " + e.getMessage());
            return false;
        }
    }

    private void recalculateDailyLog(Connection conn, int userId, java.sql.Date logDate) throws SQLException {
        // Calculate totals from food_logs for the specific date
        String calculateSql = """
            SELECT 
                COALESCE(SUM(ROUND((fi.calories * fl.quantity) / fi.base_quantity)), 0) as total_calories,
                COALESCE(SUM(ROUND((fi.protein_g * fl.quantity) / fi.base_quantity)), 0) as total_protein,
                COALESCE(SUM(ROUND((fi.carbs_g * fl.quantity) / fi.base_quantity)), 0) as total_carbs,
                COALESCE(SUM(ROUND((fi.fats_g * fl.quantity) / fi.base_quantity)), 0) as total_fats,
                COALESCE(SUM(ROUND((fi.calcium_mg * fl.quantity) / fi.base_quantity)), 0) as total_calcium,
                COALESCE(SUM(ROUND((fi.potassium_mg * fl.quantity) / fi.base_quantity)), 0) as total_potassium,
                COALESCE(SUM(ROUND((fi.sodium_mg * fl.quantity) / fi.base_quantity)), 0) as total_sodium,
                COALESCE(SUM(ROUND((fi.magnesium_mg * fl.quantity) / fi.base_quantity)), 0) as total_magnesium,
                COALESCE(SUM(ROUND((fi.iron_mg * fl.quantity) / fi.base_quantity)), 0) as total_iron,
                COALESCE(SUM(ROUND((fi.zinc_mg * fl.quantity) / fi.base_quantity)), 0) as total_zinc,
                COALESCE(SUM(ROUND((fi.vitamin_a_iu * fl.quantity) / fi.base_quantity)), 0) as total_vit_a,
                COALESCE(SUM(ROUND((fi.vitamin_d_iu * fl.quantity) / fi.base_quantity)), 0) as total_vit_d,
                COALESCE(SUM(ROUND((fi.vitamin_e_iu * fl.quantity) / fi.base_quantity)), 0) as total_vit_e,
                COALESCE(SUM(ROUND((fi.vitamin_k_mcg * fl.quantity) / fi.base_quantity)), 0) as total_vit_k
            FROM food_logs fl
            JOIN food_items fi ON fl.food_item_id = fi.id
            WHERE fl.user_id = ? AND DATE(fl.consumed_at) = ?
        """;

        PreparedStatement calcStmt = conn.prepareStatement(calculateSql);
        calcStmt.setInt(1, userId);
        calcStmt.setDate(2, logDate);
        ResultSet rs = calcStmt.executeQuery();

        if (rs.next()) {
            // Get current water_ml from daily_logs (we don't want to lose this)
            String getWaterSql = "SELECT COALESCE(water_ml, 0) as water_ml FROM daily_logs WHERE user_id = ? AND log_date = ?";
            PreparedStatement getWaterStmt = conn.prepareStatement(getWaterSql);
            getWaterStmt.setInt(1, userId);
            getWaterStmt.setDate(2, logDate);
            ResultSet waterRs = getWaterStmt.executeQuery();

            int currentWater = 0;
            if (waterRs.next()) {
                currentWater = waterRs.getInt("water_ml");
            }
            getWaterStmt.close();

            // Update or insert daily_logs with recalculated values
            String updateSql = """
                INSERT INTO daily_logs (user_id, log_date, calories, protein_g, carbs_g, fats_g, water_ml,
                    calcium_mg, potassium_mg, sodium_mg, magnesium_mg, iron_mg, zinc_mg,
                    vitamin_a_iu, vitamin_d_iu, vitamin_e_iu, vitamin_k_mcg)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    calories = VALUES(calories),
                    protein_g = VALUES(protein_g),
                    carbs_g = VALUES(carbs_g),
                    fats_g = VALUES(fats_g),
                    calcium_mg = VALUES(calcium_mg),
                    potassium_mg = VALUES(potassium_mg),
                    sodium_mg = VALUES(sodium_mg),
                    magnesium_mg = VALUES(magnesium_mg),
                    iron_mg = VALUES(iron_mg),
                    zinc_mg = VALUES(zinc_mg),
                    vitamin_a_iu = VALUES(vitamin_a_iu),
                    vitamin_d_iu = VALUES(vitamin_d_iu),
                    vitamin_e_iu = VALUES(vitamin_e_iu),
                    vitamin_k_mcg = VALUES(vitamin_k_mcg)
            """;

            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, userId);
            updateStmt.setDate(2, logDate);
            updateStmt.setInt(3, rs.getInt("total_calories"));
            updateStmt.setInt(4, rs.getInt("total_protein"));
            updateStmt.setInt(5, rs.getInt("total_carbs"));
            updateStmt.setInt(6, rs.getInt("total_fats"));
            updateStmt.setInt(7, currentWater); // Preserve water intake
            updateStmt.setInt(8, rs.getInt("total_calcium"));
            updateStmt.setInt(9, rs.getInt("total_potassium"));
            updateStmt.setInt(10, rs.getInt("total_sodium"));
            updateStmt.setInt(11, rs.getInt("total_magnesium"));
            updateStmt.setInt(12, rs.getInt("total_iron"));
            updateStmt.setInt(13, rs.getInt("total_zinc"));
            updateStmt.setInt(14, rs.getInt("total_vit_a"));
            updateStmt.setInt(15, rs.getInt("total_vit_d"));
            updateStmt.setInt(16, rs.getInt("total_vit_e"));
            updateStmt.setInt(17, rs.getInt("total_vit_k"));
            updateStmt.executeUpdate();
            updateStmt.close();
        }

        calcStmt.close();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public void refresh() {
        selectedDate = LocalDate.now();
        dateChooser.setDate(java.sql.Date.valueOf(selectedDate));
        loadMealData();
    }

    // Inner class for adding food to a specific date
    private static class AddFoodForDateDialog extends JDialog {
        private JComboBox<String> foodComboBox;
        private JTextField quantityField;
        private JComboBox<String> unitComboBox;
        private JComboBox<String> mealTypeComboBox;
        private JButton searchButton;
        private JButton addButton;
        private JButton cancelButton;
        private JTextArea nutritionPreview;
        private int userId;
        private LocalDate targetDate;
        private MealLogPanel parentPanel;

        public AddFoodForDateDialog(JFrame parent, int userId, LocalDate targetDate, MealLogPanel parentPanel) {
            super(parent, "Add Food - " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")), true);
            this.userId = userId;
            this.targetDate = targetDate;
            this.parentPanel = parentPanel;

            setSize(500, 520);
            setLocationRelativeTo(parent);
            setResizable(false);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(Color.WHITE);

            // Title
            JLabel titleLabel = new JLabel("Add Food for " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setForeground(new Color(45, 60, 35));
            titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            // Form panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setBackground(Color.WHITE);

            // Food selection
            formPanel.add(createLabel("Select Food:"));
            JPanel foodPanel = new JPanel(new BorderLayout(10, 0));
            foodPanel.setBackground(Color.WHITE);
            foodPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

            foodComboBox = new JComboBox<String>();
            foodComboBox.setEditable(true);
            foodComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            foodPanel.add(foodComboBox, BorderLayout.CENTER);

            searchButton = new JButton("🔍");
            searchButton.setFont(new Font("Arial", Font.PLAIN, 12));
            searchButton.setPreferredSize(new Dimension(40, 30));
            searchButton.addActionListener(e -> searchFoods());
            foodPanel.add(searchButton, BorderLayout.EAST);

            formPanel.add(foodPanel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            // Meal type
            formPanel.add(createLabel("Meal Type:"));
            mealTypeComboBox = new JComboBox<String>(new String[]{"Breakfast", "Lunch", "Dinner", "Snack"});
            mealTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            mealTypeComboBox.setBackground(Color.WHITE);
            mealTypeComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            formPanel.add(mealTypeComboBox);
            formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

            // Quantity input
            formPanel.add(createLabel("Quantity:"));
            JPanel quantityPanel = new JPanel(new BorderLayout(10, 0));
            quantityPanel.setBackground(Color.WHITE);
            quantityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

            quantityField = new JTextField("100");
            quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
            quantityPanel.add(quantityField, BorderLayout.CENTER);

            unitComboBox = new JComboBox<String>(new String[]{"grams", "ml", "pieces", "cups"});
            unitComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
            unitComboBox.setPreferredSize(new Dimension(80, 30));
            quantityPanel.add(unitComboBox, BorderLayout.EAST);

            formPanel.add(quantityPanel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

            // Nutrition preview
            formPanel.add(createLabel("Nutrition Preview:"));
            nutritionPreview = new JTextArea();
            nutritionPreview.setFont(new Font("Arial", Font.PLAIN, 11));
            nutritionPreview.setEditable(false);
            nutritionPreview.setBackground(new Color(245, 245, 245));
            nutritionPreview.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            nutritionPreview.setLineWrap(true);
            nutritionPreview.setWrapStyleWord(true);
            nutritionPreview.setText("Select a food to see nutrition information");

            JScrollPane previewScroll = new JScrollPane(nutritionPreview);
            previewScroll.setPreferredSize(new Dimension(0, 120));
            previewScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            formPanel.add(previewScroll);

            mainPanel.add(formPanel, BorderLayout.CENTER);

            // Buttons panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

            cancelButton = new JButton("Cancel");
            cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
            cancelButton.setBackground(new Color(200, 200, 200));
            cancelButton.setForeground(Color.BLACK);
            cancelButton.setFocusPainted(false);
            cancelButton.setBorder(new EmptyBorder(10, 20, 10, 20));
            cancelButton.addActionListener(e -> dispose());
            buttonPanel.add(cancelButton);

            addButton = new JButton("Add Food");
            addButton.setFont(new Font("Arial", Font.BOLD, 14));
            addButton.setBackground(new Color(123, 141, 74));
            addButton.setForeground(Color.WHITE);
            addButton.setFocusPainted(false);
            addButton.setBorder(new EmptyBorder(10, 20, 10, 20));
            addButton.addActionListener(e -> addFood());
            buttonPanel.add(addButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(mainPanel);

            // Load initial data and add listeners
            updateFoodComboBox("");

            foodComboBox.addActionListener(e -> updateNutritionPreview());

            quantityField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) { updateNutritionPreview(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { updateNutritionPreview(); }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { updateNutritionPreview(); }
            });

            setVisible(true);
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(new Color(45, 60, 35));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            return label;
        }

        private void searchFoods() {
            String query = "";
            Object editorItem = foodComboBox.getEditor().getItem();
            if (editorItem != null) {
                query = editorItem.toString();
            }
            updateFoodComboBox(query);
        }

        private void updateFoodComboBox(String query) {
            foodComboBox.removeAllItems();
            List<DatabaseHelper.FoodItem> foods = DatabaseHelper.searchFoods(query);

            if (foods.isEmpty()) {
                foodComboBox.addItem("No foods found");
            } else {
                for (DatabaseHelper.FoodItem food : foods) {
                    foodComboBox.addItem(food.name);
                }
            }
        }

        private void updateNutritionPreview() {
            Object editorItem = foodComboBox.getEditor().getItem();
            if (editorItem == null) {
                nutritionPreview.setText("Select a food to see nutrition information");
                return;
            }

            String selectedFood = editorItem.toString();
            DatabaseHelper.FoodItem foodItem = DatabaseHelper.getFoodItem(selectedFood);

            if (foodItem == null) {
                nutritionPreview.setText("Select a food to see nutrition information");
                return;
            }

            try {
                String quantityText = quantityField.getText().trim();
                if (quantityText.isEmpty()) {
                    nutritionPreview.setText("Please enter a quantity");
                    return;
                }

                int quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    nutritionPreview.setText("Please enter a positive quantity");
                    return;
                }

                DatabaseHelper.NutritionData nutrition = foodItem.calculateNutrition(quantity);

                String preview = String.format(
                        "Nutrition for %dg of %s:\n\n" +
                                "Calories: %d kcal\n" +
                                "Protein: %dg\n" +
                                "Carbs: %dg\n" +
                                "Fats: %dg\n" +
                                "Calcium: %dmg\n" +
                                "Iron: %dmg\n" +
                                "Potassium: %dmg\n" +
                                "Vitamin A: %dIU\n" +
                                "Vitamin D: %dIU",
                        quantity, foodItem.name,
                        nutrition.calories,
                        nutrition.protein_g,
                        nutrition.carbs_g,
                        nutrition.fats_g,
                        nutrition.calcium_mg,
                        nutrition.iron_mg,
                        nutrition.potassium_mg,
                        nutrition.vitamin_a_iu,
                        nutrition.vitamin_d_iu
                );

                nutritionPreview.setText(preview);

            } catch (NumberFormatException e) {
                nutritionPreview.setText("Please enter a valid quantity");
            }
        }

        private void addFood() {
            Object editorItem = foodComboBox.getEditor().getItem();
            if (editorItem == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a food item",
                        "Invalid Food",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedFood = editorItem.toString();
            DatabaseHelper.FoodItem foodItem = DatabaseHelper.getFoodItem(selectedFood);

            if (foodItem == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a valid food item",
                        "Invalid Food",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String quantityText = quantityField.getText().trim();
                if (quantityText.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a quantity",
                            "Invalid Quantity",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a positive quantity",
                            "Invalid Quantity",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String mealType = (String) mealTypeComboBox.getSelectedItem();
                String unit = (String) unitComboBox.getSelectedItem();

                // Log food for the specific target date
                boolean logged = logFoodForDate(foodItem.id, quantity, unit, mealType);

                if (logged) {
                    // Update daily nutrition totals for the target date
                    DatabaseHelper.NutritionData nutrition = foodItem.calculateNutrition(quantity);
                    boolean nutritionAdded = addNutritionForDate(nutrition);

                    if (nutritionAdded) {
                        JOptionPane.showMessageDialog(this,
                                String.format("Added %d%s of %s to your %s meal on %s!",
                                        quantity, unit, foodItem.name, mealType,
                                        targetDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))),
                                "Food Added",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Refresh the parent panel
                        if (parentPanel != null) {
                            parentPanel.loadMealData();
                        }

                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to add nutrition data.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to log food. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid quantity",
                        "Invalid Quantity",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean logFoodForDate(int foodItemId, int quantity, String unit, String mealType) {
            // Create timestamp for the target date
            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(
                    targetDate.atTime(java.time.LocalTime.now())
            );

            String sql = "INSERT INTO food_logs (user_id, food_item_id, quantity, unit, meal_type, consumed_at) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, foodItemId);
                pstmt.setInt(3, quantity);
                pstmt.setString(4, unit);
                pstmt.setString(5, mealType);
                pstmt.setTimestamp(6, timestamp);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.err.println("Log food consumption error: " + e.getMessage());
                return false;
            }
        }

        private boolean addNutritionForDate(DatabaseHelper.NutritionData data) {
            java.sql.Date sqlDate = java.sql.Date.valueOf(targetDate);

            String sql = """
                INSERT INTO daily_logs (user_id, log_date, calories, protein_g, carbs_g, fats_g, water_ml,
                calcium_mg, potassium_mg, sodium_mg, magnesium_mg, iron_mg, zinc_mg,
                vitamin_a_iu, vitamin_d_iu, vitamin_e_iu, vitamin_k_mcg)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                calories=calories+VALUES(calories), protein_g=protein_g+VALUES(protein_g),
                carbs_g=carbs_g+VALUES(carbs_g), fats_g=fats_g+VALUES(fats_g),
                water_ml=water_ml+VALUES(water_ml), calcium_mg=calcium_mg+VALUES(calcium_mg),
                potassium_mg=potassium_mg+VALUES(potassium_mg), sodium_mg=sodium_mg+VALUES(sodium_mg),
                magnesium_mg=magnesium_mg+VALUES(magnesium_mg), iron_mg=iron_mg+VALUES(iron_mg),
                zinc_mg=zinc_mg+VALUES(zinc_mg), vitamin_a_iu=vitamin_a_iu+VALUES(vitamin_a_iu),
                vitamin_d_iu=vitamin_d_iu+VALUES(vitamin_d_iu), vitamin_e_iu=vitamin_e_iu+VALUES(vitamin_e_iu),
                vitamin_k_mcg=vitamin_k_mcg+VALUES(vitamin_k_mcg)
            """;

            try (Connection conn = DatabaseHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                pstmt.setDate(2, sqlDate);
                pstmt.setInt(3, data.calories);
                pstmt.setInt(4, data.protein_g);
                pstmt.setInt(5, data.carbs_g);
                pstmt.setInt(6, data.fats_g);
                pstmt.setInt(7, data.water_ml);
                pstmt.setInt(8, data.calcium_mg);
                pstmt.setInt(9, data.potassium_mg);
                pstmt.setInt(10, data.sodium_mg);
                pstmt.setInt(11, data.magnesium_mg);
                pstmt.setInt(12, data.iron_mg);
                pstmt.setInt(13, data.zinc_mg);
                pstmt.setInt(14, data.vitamin_a_iu);
                pstmt.setInt(15, data.vitamin_d_iu);
                pstmt.setInt(16, data.vitamin_e_iu);
                pstmt.setInt(17, data.vitamin_k_mcg);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.err.println("Add nutrition data error: " + e.getMessage());
                return false;
            }
        }
    }
}