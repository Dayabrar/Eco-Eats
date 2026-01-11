import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Food Editor Dialog - Add/Edit Food Items
 * Complete nutrition data entry
 * Version: 1.0
 */
public class FoodEditorDialog extends JDialog {
    private JTextField nameField, foodGroupField, baseQuantityField;
    private JTextField caloriesField, proteinField, carbsField, fatsField, waterField;
    private JTextField calciumField, potassiumField, sodiumField, magnesiumField;
    private JTextField ironField, zincField, vitaminAField, vitaminDField;
    private JTextField vitaminEField, vitaminKField;
    private JButton saveButton, cancelButton;

    private DatabaseHelper.FoodItem existingFood;
    private int adminId;
    private boolean saved = false;

    public FoodEditorDialog(JFrame parent, DatabaseHelper.FoodItem food, int adminId) {
        super(parent, food == null ? "Add New Food" : "Edit Food Item", true);
        this.existingFood = food;
        this.adminId = adminId;

        setSize(600, 750);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel(food == null ? "Add New Food Item" : "Edit Food Item");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        // Basic Info Section
        formPanel.add(createSectionLabel("Basic Information"));
        formPanel.add(createFieldRow("Food Name:", nameField = new JTextField(food != null ? food.name : "")));
        formPanel.add(createFieldRow("Food Group:", foodGroupField = new JTextField(food != null ? food.foodGroup : "")));
        formPanel.add(createFieldRow("Base Quantity (g):", baseQuantityField = new JTextField(food != null ? String.valueOf(food.baseQuantity) : "100")));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Macronutrients Section
        formPanel.add(createSectionLabel("Macronutrients (per base quantity)"));
        formPanel.add(createFieldRow("Calories (kcal):", caloriesField = new JTextField(food != null ? String.valueOf(food.calories) : "0")));
        formPanel.add(createFieldRow("Protein (g):", proteinField = new JTextField(food != null ? String.valueOf(food.protein_g) : "0")));
        formPanel.add(createFieldRow("Carbohydrates (g):", carbsField = new JTextField(food != null ? String.valueOf(food.carbs_g) : "0")));
        formPanel.add(createFieldRow("Fats (g):", fatsField = new JTextField(food != null ? String.valueOf(food.fats_g) : "0")));
        formPanel.add(createFieldRow("Water (ml):", waterField = new JTextField(food != null ? String.valueOf(food.water_ml) : "0")));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Minerals Section
        formPanel.add(createSectionLabel("Minerals (per base quantity)"));
        formPanel.add(createFieldRow("Calcium (mg):", calciumField = new JTextField(food != null ? String.valueOf(food.calcium_mg) : "0")));
        formPanel.add(createFieldRow("Potassium (mg):", potassiumField = new JTextField(food != null ? String.valueOf(food.potassium_mg) : "0")));
        formPanel.add(createFieldRow("Sodium (mg):", sodiumField = new JTextField(food != null ? String.valueOf(food.sodium_mg) : "0")));
        formPanel.add(createFieldRow("Magnesium (mg):", magnesiumField = new JTextField(food != null ? String.valueOf(food.magnesium_mg) : "0")));
        formPanel.add(createFieldRow("Iron (mg):", ironField = new JTextField(food != null ? String.valueOf(food.iron_mg) : "0")));
        formPanel.add(createFieldRow("Zinc (mg):", zincField = new JTextField(food != null ? String.valueOf(food.zinc_mg) : "0")));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Vitamins Section
        formPanel.add(createSectionLabel("Vitamins (per base quantity)"));
        formPanel.add(createFieldRow("Vitamin A (IU):", vitaminAField = new JTextField(food != null ? String.valueOf(food.vitamin_a_iu) : "0")));
        formPanel.add(createFieldRow("Vitamin D (IU):", vitaminDField = new JTextField(food != null ? String.valueOf(food.vitamin_d_iu) : "0")));
        formPanel.add(createFieldRow("Vitamin E (IU):", vitaminEField = new JTextField(food != null ? String.valueOf(food.vitamin_e_iu) : "0")));
        formPanel.add(createFieldRow("Vitamin K (mcg):", vitaminKField = new JTextField(food != null ? String.valueOf(food.vitamin_k_mcg) : "0")));

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

        saveButton = new JButton(food == null ? "Add Food" : "Save Changes");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> saveFood());
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(76, 175, 80));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createFieldRow(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.setBorder(new EmptyBorder(3, 0, 3, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(180, 25));
        panel.add(label, BorderLayout.WEST);

        textField.setFont(new Font("Arial", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(textField, BorderLayout.CENTER);

        return panel;
    }

    private void saveFood() {
        try {
            // Validate and collect data
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Food name is required",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseHelper.FoodItem food = new DatabaseHelper.FoodItem();
            if (existingFood != null) {
                food.id = existingFood.id;
            }

            food.name = name;
            food.foodGroup = foodGroupField.getText().trim();
            food.baseQuantity = parseIntField(baseQuantityField, "Base Quantity");
            food.calories = parseIntField(caloriesField, "Calories");
            food.protein_g = parseDoubleField(proteinField, "Protein");
            food.carbs_g = parseDoubleField(carbsField, "Carbohydrates");
            food.fats_g = parseDoubleField(fatsField, "Fats");
            food.water_ml = parseIntField(waterField, "Water");
            food.calcium_mg = parseIntField(calciumField, "Calcium");
            food.potassium_mg = parseIntField(potassiumField, "Potassium");
            food.sodium_mg = parseIntField(sodiumField, "Sodium");
            food.magnesium_mg = parseIntField(magnesiumField, "Magnesium");
            food.iron_mg = parseDoubleField(ironField, "Iron");
            food.zinc_mg = parseDoubleField(zincField, "Zinc");
            food.vitamin_a_iu = parseIntField(vitaminAField, "Vitamin A");
            food.vitamin_d_iu = parseIntField(vitaminDField, "Vitamin D");
            food.vitamin_e_iu = parseDoubleField(vitaminEField, "Vitamin E");
            food.vitamin_k_mcg = parseDoubleField(vitaminKField, "Vitamin K");

            // Validate positive values
            if (food.baseQuantity <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Base quantity must be greater than 0",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Save to database
            boolean success;
            if (existingFood == null) {
                success = DatabaseHelper.addFoodItem(food, adminId);
            } else {
                success = DatabaseHelper.updateFoodItem(food, adminId);
            }

            if (success) {
                saved = true;
                JOptionPane.showMessageDialog(this,
                        existingFood == null ? "Food item added successfully!" : "Food item updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save food item. Food name might already exist.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            // Error already shown by parse methods
        }
    }

    private int parseIntField(JTextField field, String fieldName) throws NumberFormatException {
        String text = field.getText().trim();
        if (text.isEmpty()) return 0;
        try {
            int value = Integer.parseInt(text);
            if (value < 0) {
                JOptionPane.showMessageDialog(this,
                        fieldName + " cannot be negative",
                        "Invalid Value",
                        JOptionPane.ERROR_MESSAGE);
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            if (!text.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number for " + fieldName,
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
            throw e;
        }
    }

    private double parseDoubleField(JTextField field, String fieldName) throws NumberFormatException {
        String text = field.getText().trim();
        if (text.isEmpty()) return 0.0;
        try {
            double value = Double.parseDouble(text);
            if (value < 0) {
                JOptionPane.showMessageDialog(this,
                        fieldName + " cannot be negative",
                        "Invalid Value",
                        JOptionPane.ERROR_MESSAGE);
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            if (!text.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number for " + fieldName,
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
            throw e;
        }
    }

    public boolean isSaved() {
        return saved;
    }
}