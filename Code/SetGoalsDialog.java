import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Set Goals Dialog - Nutrition Goals Configuration
 * Version: 1.0
 * Features: Edit all 15 nutrition goals, Input validation
 */
public class SetGoalsDialog extends JDialog {
    private JTextField caloriesField, proteinField, carbsField, fatsField, waterField;
    private JTextField calciumField, potassiumField, sodiumField, magnesiumField;
    private JTextField ironField, zincField, vitaminAField, vitaminDField;
    private JTextField vitaminEField, vitaminKField;
    private JButton saveButton, cancelButton;
    private int userId;
    private DatabaseHelper.NutritionGoals currentGoals;

    public SetGoalsDialog(JFrame parent, int userId, DatabaseHelper.NutritionGoals currentGoals) {
        super(parent, "Set Nutrition Goals", true);
        this.userId = userId;
        this.currentGoals = currentGoals;

        setSize(500, 700);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Set Nutrition Goals");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(45, 60, 35));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        // Macronutrients section
        formPanel.add(createSectionLabel("Macronutrients"));
        formPanel.add(createNutrientRow("Calories (kcal):", caloriesField = new JTextField(String.valueOf(currentGoals.calories)), "kcal"));
        formPanel.add(createNutrientRow("Protein (g):", proteinField = new JTextField(String.valueOf(currentGoals.protein_g)), "g"));
        formPanel.add(createNutrientRow("Carbohydrates (g):", carbsField = new JTextField(String.valueOf(currentGoals.carbs_g)), "g"));
        formPanel.add(createNutrientRow("Fats (g):", fatsField = new JTextField(String.valueOf(currentGoals.fats_g)), "g"));

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Water
        formPanel.add(createSectionLabel("Water"));
        formPanel.add(createNutrientRow("Water (ml):", waterField = new JTextField(String.valueOf(currentGoals.water_ml)), "ml"));

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Minerals section
        formPanel.add(createSectionLabel("Minerals"));
        formPanel.add(createNutrientRow("Calcium (mg):", calciumField = new JTextField(String.valueOf(currentGoals.calcium_mg)), "mg"));
        formPanel.add(createNutrientRow("Potassium (mg):", potassiumField = new JTextField(String.valueOf(currentGoals.potassium_mg)), "mg"));
        formPanel.add(createNutrientRow("Sodium (mg):", sodiumField = new JTextField(String.valueOf(currentGoals.sodium_mg)), "mg"));
        formPanel.add(createNutrientRow("Magnesium (mg):", magnesiumField = new JTextField(String.valueOf(currentGoals.magnesium_mg)), "mg"));
        formPanel.add(createNutrientRow("Iron (mg):", ironField = new JTextField(String.valueOf(currentGoals.iron_mg)), "mg"));
        formPanel.add(createNutrientRow("Zinc (mg):", zincField = new JTextField(String.valueOf(currentGoals.zinc_mg)), "mg"));

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Vitamins section
        formPanel.add(createSectionLabel("Vitamins"));
        formPanel.add(createNutrientRow("Vitamin A (IU):", vitaminAField = new JTextField(String.valueOf(currentGoals.vitamin_a_iu)), "IU"));
        formPanel.add(createNutrientRow("Vitamin D (IU):", vitaminDField = new JTextField(String.valueOf(currentGoals.vitamin_d_iu)), "IU"));
        formPanel.add(createNutrientRow("Vitamin E (IU):", vitaminEField = new JTextField(String.valueOf(currentGoals.vitamin_e_iu)), "IU"));
        formPanel.add(createNutrientRow("Vitamin K (mcg):", vitaminKField = new JTextField(String.valueOf(currentGoals.vitamin_k_mcg)), "mcg"));

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

        saveButton = new JButton("Save Goals");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(123, 141, 74));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        saveButton.addActionListener(e -> saveGoals());
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(123, 141, 74));
        label.setBorder(new EmptyBorder(10, 0, 5, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createNutrientRow(String labelText, JTextField textField, String unit) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(150, 25));
        panel.add(label, BorderLayout.WEST);

        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(textField, BorderLayout.CENTER);

        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        unitLabel.setForeground(Color.GRAY);
        unitLabel.setPreferredSize(new Dimension(40, 25));
        panel.add(unitLabel, BorderLayout.EAST);

        return panel;
    }

    private void saveGoals() {
        DatabaseHelper.NutritionGoals goals = new DatabaseHelper.NutritionGoals();
        goals.userId = userId;

        try {
            goals.calories = parseIntField(caloriesField);
            goals.protein_g = parseIntField(proteinField);
            goals.carbs_g = parseIntField(carbsField);
            goals.fats_g = parseIntField(fatsField);
            goals.water_ml = parseIntField(waterField);
            goals.calcium_mg = parseIntField(calciumField);
            goals.potassium_mg = parseIntField(potassiumField);
            goals.sodium_mg = parseIntField(sodiumField);
            goals.magnesium_mg = parseIntField(magnesiumField);
            goals.iron_mg = parseIntField(ironField);
            goals.zinc_mg = parseIntField(zincField);
            goals.vitamin_a_iu = parseIntField(vitaminAField);
            goals.vitamin_d_iu = parseIntField(vitaminDField);
            goals.vitamin_e_iu = parseIntField(vitaminEField);
            goals.vitamin_k_mcg = parseIntField(vitaminKField);

            if (hasInvalidValues(goals)) {
                JOptionPane.showMessageDialog(this,
                        "Please enter positive values for all goals.",
                        "Invalid Values",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = DatabaseHelper.updateUserGoals(goals);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Nutrition goals updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update goals. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for all fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseIntField(JTextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            return 0;
        }
        int value = Integer.parseInt(text);
        if (value < 0) {
            throw new NumberFormatException("Negative value");
        }
        return value;
    }

    private boolean hasInvalidValues(DatabaseHelper.NutritionGoals goals) {
        return goals.calories <= 0 || goals.protein_g <= 0 || goals.carbs_g <= 0 ||
                goals.fats_g <= 0 || goals.water_ml <= 0 || goals.calcium_mg <= 0 ||
                goals.potassium_mg <= 0 || goals.sodium_mg <= 0 || goals.magnesium_mg <= 0 ||
                goals.iron_mg <= 0 || goals.zinc_mg <= 0 || goals.vitamin_a_iu <= 0 ||
                goals.vitamin_d_iu <= 0 || goals.vitamin_e_iu <= 0 || goals.vitamin_k_mcg <= 0;
    }
}