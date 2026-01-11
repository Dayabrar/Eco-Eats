import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Add Food Dialog - Fixed Version
 * Version: 2.0 - NullPointerException Fixed
 */
public class AddFoodDialog extends JDialog {
    private JComboBox<String> foodComboBox;
    private JTextField quantityField;
    private JComboBox<String> unitComboBox;
    private JComboBox<String> mealTypeComboBox;
    private JButton searchButton;
    private JButton addButton;
    private JButton cancelButton;
    private JTextArea nutritionPreview;
    private int userId;
    private JFrame parent;

    public AddFoodDialog(JFrame parent, int userId) {
        super(parent, "Add Food", true);
        this.parent = parent;
        this.userId = userId;

        setSize(500, 520);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Add Food Item");
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
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchFoods();
            }
        });
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
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);

        addButton = new JButton("Add Food");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(123, 141, 74));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addFood();
            }
        });
        buttonPanel.add(addButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // NOW load initial data and add listeners AFTER all components are created
        updateFoodComboBox("");

        // Add listeners
        foodComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateNutritionPreview();
            }
        });

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

            // First, log the food consumption
            boolean logged = DatabaseHelper.logFoodConsumption(userId, foodItem.id, quantity, unit, mealType);

            if (logged) {
                // Then update the daily nutrition totals
                DatabaseHelper.NutritionData nutrition = foodItem.calculateNutrition(quantity);
                boolean nutritionAdded = DatabaseHelper.addNutritionData(userId, nutrition);

                if (nutritionAdded) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Added %d%s of %s to your %s meal!",
                                    quantity, unit, foodItem.name, mealType),
                            "Food Added",
                            JOptionPane.INFORMATION_MESSAGE);
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
}