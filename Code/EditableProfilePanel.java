import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Editable Profile Panel with BMI Calculator
 * Allows editing all fields except email and member since
 * Version: 2.0 - Added BMI calculation based on gender
 */
public class EditableProfilePanel extends JPanel {
    private DatabaseHelper.UserData currentUser;
    private JTextField fullNameField;
    private JLabel emailLabel;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> activityComboBox;
    private JLabel memberSinceLabel;
    private JButton editButton;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton changePasswordButton;
    private boolean editMode = false;

    // BMI fields
    private JTextField heightField;
    private JTextField weightField;
    private JLabel bmiValueLabel;
    private JLabel bmiCategoryLabel;
    private JLabel bmiStatusLabel;
    private JButton calculateBmiButton;

    public EditableProfilePanel(DatabaseHelper.UserData user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(45, 60, 35));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        changePasswordButton = createStyledButton("Change Password", new Color(255, 152, 0));
        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showChangePasswordDialog();
            }
        });
        buttonPanel.add(changePasswordButton);

        editButton = createStyledButton("Edit Profile", new Color(33, 150, 243));
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleEditMode();
            }
        });
        buttonPanel.add(editButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Profile content
        JPanel profileContent = new JPanel();
        profileContent.setLayout(new BoxLayout(profileContent, BoxLayout.Y_AXIS));
        profileContent.setBackground(Color.WHITE);
        profileContent.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Full Name
        profileContent.add(createFieldLabel("Full Name"));
        fullNameField = new JTextField(currentUser.fullName);
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        fullNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        fullNameField.setEditable(false);
        fullNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        profileContent.add(fullNameField);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email (Read-only)
        profileContent.add(createFieldLabel("Email (Cannot be changed)"));
        emailLabel = new JLabel(currentUser.email);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(Color.GRAY);
        emailLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        emailLabel.setOpaque(true);
        emailLabel.setBackground(new Color(245, 245, 245));
        profileContent.add(emailLabel);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Age
        profileContent.add(createFieldLabel("Age"));
        ageField = new JTextField(String.valueOf(currentUser.age));
        ageField.setFont(new Font("Arial", Font.PLAIN, 16));
        ageField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ageField.setEditable(false);
        ageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        profileContent.add(ageField);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Gender
        profileContent.add(createFieldLabel("Gender"));
        genderComboBox = new JComboBox<String>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setSelectedItem(currentUser.gender);
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        genderComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        genderComboBox.setEnabled(false);
        profileContent.add(genderComboBox);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Activity Level
        profileContent.add(createFieldLabel("Activity Level"));
        activityComboBox = new JComboBox<String>(new String[]{
                "Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extremely Active"
        });
        activityComboBox.setSelectedItem(currentUser.activityLevel);
        activityComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        activityComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        activityComboBox.setEnabled(false);
        profileContent.add(activityComboBox);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Member Since (Read-only)
        profileContent.add(createFieldLabel("Member Since (Cannot be changed)"));
        memberSinceLabel = new JLabel(currentUser.createdAt.toString().substring(0, 10));
        memberSinceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        memberSinceLabel.setForeground(Color.GRAY);
        memberSinceLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        memberSinceLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        memberSinceLabel.setOpaque(true);
        memberSinceLabel.setBackground(new Color(245, 245, 245));
        profileContent.add(memberSinceLabel);
        profileContent.add(Box.createRigidArea(new Dimension(0, 25)));

        // ==================== BMI SECTION ====================
        JLabel bmiSectionLabel = new JLabel("Body Mass Index (BMI) Calculator");
        bmiSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bmiSectionLabel.setForeground(new Color(123, 141, 74));
        bmiSectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileContent.add(bmiSectionLabel);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Height and Weight in a row
        JPanel bmiInputPanel = new JPanel();
        bmiInputPanel.setLayout(new BoxLayout(bmiInputPanel, BoxLayout.X_AXIS));
        bmiInputPanel.setBackground(Color.WHITE);
        bmiInputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        bmiInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Height
        JLabel heightLabel = new JLabel("Height (cm):");
        heightLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        heightLabel.setPreferredSize(new Dimension(100, 30));
        bmiInputPanel.add(heightLabel);

        heightField = new JTextField();
        heightField.setFont(new Font("Arial", Font.PLAIN, 16));
        heightField.setMaximumSize(new Dimension(150, 40));
        heightField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        bmiInputPanel.add(heightField);
        bmiInputPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        // Weight
        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        weightLabel.setPreferredSize(new Dimension(100, 30));
        bmiInputPanel.add(weightLabel);

        weightField = new JTextField();
        weightField.setFont(new Font("Arial", Font.PLAIN, 16));
        weightField.setMaximumSize(new Dimension(150, 40));
        weightField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        bmiInputPanel.add(weightField);

        profileContent.add(bmiInputPanel);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // Calculate BMI Button
        calculateBmiButton = createStyledButton("Calculate BMI", new Color(76, 175, 80));
        calculateBmiButton.setMaximumSize(new Dimension(200, 40));
        calculateBmiButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        calculateBmiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateBMI();
            }
        });
        profileContent.add(calculateBmiButton);
        profileContent.add(Box.createRigidArea(new Dimension(0, 15)));

        // BMI Results Panel
        JPanel bmiResultPanel = new JPanel();
        bmiResultPanel.setLayout(new BoxLayout(bmiResultPanel, BoxLayout.Y_AXIS));
        bmiResultPanel.setBackground(new Color(245, 250, 245));
        bmiResultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 200), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        bmiResultPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        bmiResultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bmiValueLabel = new JLabel("BMI: Not calculated");
        bmiValueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        bmiValueLabel.setForeground(new Color(45, 60, 35));
        bmiValueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bmiResultPanel.add(bmiValueLabel);
        bmiResultPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        bmiCategoryLabel = new JLabel("Category: -");
        bmiCategoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bmiCategoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bmiResultPanel.add(bmiCategoryLabel);
        bmiResultPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        bmiStatusLabel = new JLabel("Enter your height and weight to calculate BMI");
        bmiStatusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bmiStatusLabel.setForeground(Color.GRAY);
        bmiStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bmiResultPanel.add(bmiStatusLabel);

        profileContent.add(bmiResultPanel);

        // Save/Cancel buttons (initially hidden)
        profileContent.add(Box.createRigidArea(new Dimension(0, 20)));
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        savePanel.setOpaque(false);
        savePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        cancelButton = createStyledButton("Cancel", new Color(150, 150, 150));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelEdit();
            }
        });
        cancelButton.setVisible(false);
        savePanel.add(cancelButton);

        saveButton = createStyledButton("Save Changes", new Color(76, 175, 80));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });
        saveButton.setVisible(false);
        savePanel.add(saveButton);

        profileContent.add(savePanel);

        JScrollPane scrollPane = new JScrollPane(profileContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(45, 60, 35));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void calculateBMI() {
        String heightStr = heightField.getText().trim();
        String weightStr = weightField.getText().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both height and weight",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);

            if (height <= 0 || height > 300) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid height (1-300 cm)",
                        "Invalid Height",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (weight <= 0 || weight > 500) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid weight (1-500 kg)",
                        "Invalid Weight",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculate BMI
            double heightInMeters = height / 100.0;
            double bmi = weight / (heightInMeters * heightInMeters);

            // Get gender
            String gender = currentUser.gender;

            // Display results
            displayBMIResults(bmi, gender);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for height and weight",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayBMIResults(double bmi, String gender) {
        // Format BMI value
        String bmiText = String.format("BMI: %.1f", bmi);
        bmiValueLabel.setText(bmiText);

        // Determine category and recommendations based on gender
        String category = "";
        String status = "";
        Color categoryColor = Color.BLACK;

        if (gender.equalsIgnoreCase("Male")) {
            if (bmi < 18.5) {
                category = "Underweight";
                status = "Your BMI is below the healthy range for men. Consider consulting a nutritionist to gain weight healthily.";
                categoryColor = new Color(255, 152, 0);
            } else if (bmi >= 18.5 && bmi < 25) {
                category = "Normal (Healthy)";
                status = "Excellent! Your BMI is in the healthy range for men. Maintain your current lifestyle.";
                categoryColor = new Color(76, 175, 80);
            } else if (bmi >= 25 && bmi < 30) {
                category = "Overweight";
                status = "Your BMI is above the healthy range for men. Consider increasing physical activity and monitoring diet.";
                categoryColor = new Color(255, 193, 7);
            } else {
                category = "Obese";
                status = "Your BMI indicates obesity. It's recommended to consult a healthcare provider for a personalized plan.";
                categoryColor = new Color(244, 67, 54);
            }
        } else if (gender.equalsIgnoreCase("Female")) {
            if (bmi < 18.5) {
                category = "Underweight";
                status = "Your BMI is below the healthy range for women. Consider consulting a nutritionist to gain weight healthily.";
                categoryColor = new Color(255, 152, 0);
            } else if (bmi >= 18.5 && bmi < 24) {
                category = "Normal (Healthy)";
                status = "Excellent! Your BMI is in the healthy range for women. Maintain your current lifestyle.";
                categoryColor = new Color(76, 175, 80);
            } else if (bmi >= 24 && bmi < 29) {
                category = "Overweight";
                status = "Your BMI is above the healthy range for women. Consider increasing physical activity and monitoring diet.";
                categoryColor = new Color(255, 193, 7);
            } else {
                category = "Obese";
                status = "Your BMI indicates obesity. It's recommended to consult a healthcare provider for a personalized plan.";
                categoryColor = new Color(244, 67, 54);
            }
        } else {
            // Default/Other gender - use standard BMI ranges
            if (bmi < 18.5) {
                category = "Underweight";
                status = "Your BMI is below the healthy range. Consider consulting a healthcare provider.";
                categoryColor = new Color(255, 152, 0);
            } else if (bmi >= 18.5 && bmi < 25) {
                category = "Normal (Healthy)";
                status = "Your BMI is in the healthy range. Maintain your current lifestyle.";
                categoryColor = new Color(76, 175, 80);
            } else if (bmi >= 25 && bmi < 30) {
                category = "Overweight";
                status = "Your BMI is above the healthy range. Consider lifestyle modifications.";
                categoryColor = new Color(255, 193, 7);
            } else {
                category = "Obese";
                status = "Your BMI indicates obesity. Consult a healthcare provider for guidance.";
                categoryColor = new Color(244, 67, 54);
            }
        }

        bmiCategoryLabel.setText("Category: " + category);
        bmiCategoryLabel.setForeground(categoryColor);
        bmiStatusLabel.setText("<html><body style='width: 450px'>" + status + "</body></html>");
    }

    private void toggleEditMode() {
        editMode = !editMode;

        fullNameField.setEditable(editMode);
        fullNameField.setBackground(editMode ? Color.WHITE : new Color(245, 245, 245));
        ageField.setEditable(editMode);
        ageField.setBackground(editMode ? Color.WHITE : new Color(245, 245, 245));
        genderComboBox.setEnabled(editMode);
        activityComboBox.setEnabled(editMode);

        editButton.setVisible(!editMode);
        saveButton.setVisible(editMode);
        cancelButton.setVisible(editMode);
        changePasswordButton.setVisible(!editMode);

        if (editMode) {
            editButton.setText("Cancel");
        }
    }

    private void cancelEdit() {
        fullNameField.setText(currentUser.fullName);
        ageField.setText(String.valueOf(currentUser.age));
        genderComboBox.setSelectedItem(currentUser.gender);
        activityComboBox.setSelectedItem(currentUser.activityLevel);
        toggleEditMode();
    }

    private void saveProfile() {
        String fullName = fullNameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String activity = (String) activityComboBox.getSelectedItem();

        // Validation
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Full name cannot be empty",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationHelper.isValidName(fullName)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid name (only letters and spaces)",
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (!ValidationHelper.isValidAge(age)) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid age (10-120)",
                        "Invalid Age",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid age number",
                    "Invalid Age",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update database
        if (updateUserProfile(fullName, age, gender, activity)) {
            currentUser.fullName = fullName;
            currentUser.age = age;
            currentUser.gender = gender;
            currentUser.activityLevel = activity;

            JOptionPane.showMessageDialog(this,
                    "Profile updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            toggleEditMode();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update profile. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean updateUserProfile(String fullName, int age, String gender, String activity) {
        String sql = "UPDATE users SET full_name = ?, age = ?, gender = ?, activity_level = ? WHERE id = ?";
        try {
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fullName);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, activity);
            pstmt.setInt(5, currentUser.id);
            boolean result = pstmt.executeUpdate() > 0;
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.err.println("Error updating profile: " + e.getMessage());
            return false;
        }
    }

    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        JLabel strengthLabel = new JLabel("Strength: Not entered");
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 12));

        newPasswordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }

            private void updateStrength() {
                String pwd = new String(newPasswordField.getPassword());
                if (pwd.isEmpty()) {
                    strengthLabel.setText("Strength: Not entered");
                    strengthLabel.setForeground(Color.GRAY);
                } else {
                    ValidationHelper.PasswordStrength strength = ValidationHelper.checkPasswordStrength(pwd);
                    strengthLabel.setText("Strength: " + strength.getLevelText());
                    strengthLabel.setForeground(strength.getLevelColor());
                }
            }
        });

        panel.add(new JLabel("Current Password:"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        panel.add(new JLabel(""));
        panel.add(strengthLabel);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!DatabaseHelper.validateLogin(currentUser.email, currentPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Current password is incorrect",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ValidationHelper.isPasswordAcceptable(newPassword)) {
                JOptionPane.showMessageDialog(this,
                        "New password is too weak!\n" + ValidationHelper.getPasswordRequirements(),
                        "Weak Password",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Passwords do not match",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (DatabaseHelper.updatePassword(currentUser.email, newPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Password changed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to change password",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}