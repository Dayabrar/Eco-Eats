import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Editable Profile Panel with BMI Calculator - FINAL VERSION
 * Version: 2.2 - Perfect spacing and alignment
 *
 * CHANGES MADE:
 * 1. Changed BMI input panel from GridLayout to GridBagLayout for better control
 * 2. Added proper insets (spacing) between labels and text fields
 * 3. Set weightx for proper horizontal distribution
 * 4. Labels are now closer to their respective input fields
 * 5. Added padding between Height and Weight sections (20px)
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

        // Main content with proper constraints
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(Color.WHITE);
        contentWrapper.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // Profile content with GridBagLayout for better control
        JPanel profileContent = new JPanel(new GridBagLayout());
        profileContent.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Full Name
        profileContent.add(createFieldLabel("Full Name"), gbc);
        gbc.gridy++;

        fullNameField = new JTextField(currentUser.fullName);
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        fullNameField.setPreferredSize(new Dimension(0, 40));
        fullNameField.setEditable(false);
        fullNameField.setBackground(new Color(245, 245, 245));
        fullNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        profileContent.add(fullNameField, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // Email (Read-only)
        profileContent.add(createFieldLabel("Email (Cannot be changed)"), gbc);
        gbc.gridy++;

        emailLabel = new JLabel(currentUser.email);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(Color.GRAY);
        emailLabel.setPreferredSize(new Dimension(0, 40));
        emailLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        emailLabel.setOpaque(true);
        emailLabel.setBackground(new Color(245, 245, 245));
        profileContent.add(emailLabel, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // Age
        profileContent.add(createFieldLabel("Age"), gbc);
        gbc.gridy++;

        ageField = new JTextField(String.valueOf(currentUser.age));
        ageField.setFont(new Font("Arial", Font.PLAIN, 16));
        ageField.setPreferredSize(new Dimension(0, 40));
        ageField.setEditable(false);
        ageField.setBackground(new Color(245, 245, 245));
        ageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        profileContent.add(ageField, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // Gender
        profileContent.add(createFieldLabel("Gender"), gbc);
        gbc.gridy++;

        genderComboBox = new JComboBox<String>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setSelectedItem(currentUser.gender);
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        genderComboBox.setPreferredSize(new Dimension(0, 40));
        genderComboBox.setEnabled(false);
        profileContent.add(genderComboBox, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // Activity Level
        profileContent.add(createFieldLabel("Activity Level"), gbc);
        gbc.gridy++;

        activityComboBox = new JComboBox<String>(new String[]{
                "Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extremely Active"
        });
        activityComboBox.setSelectedItem(currentUser.activityLevel);
        activityComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        activityComboBox.setPreferredSize(new Dimension(0, 40));
        activityComboBox.setEnabled(false);
        profileContent.add(activityComboBox, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // Member Since (Read-only)
        profileContent.add(createFieldLabel("Member Since (Cannot be changed)"), gbc);
        gbc.gridy++;

        memberSinceLabel = new JLabel(currentUser.createdAt.toString().substring(0, 10));
        memberSinceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        memberSinceLabel.setForeground(Color.GRAY);
        memberSinceLabel.setPreferredSize(new Dimension(0, 40));
        memberSinceLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        memberSinceLabel.setOpaque(true);
        memberSinceLabel.setBackground(new Color(245, 245, 245));
        profileContent.add(memberSinceLabel, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(20), gbc);
        gbc.gridy++;

        // ==================== BMI SECTION ====================
        JLabel bmiSectionLabel = new JLabel("Body Mass Index (BMI) Calculator");
        bmiSectionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bmiSectionLabel.setForeground(new Color(123, 141, 74));
        profileContent.add(bmiSectionLabel, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // ============================================================
        // CHANGED: Height and Weight panel with better spacing
        // OLD: Used GridLayout(1, 4, 10, 0) - equal spacing for all
        // NEW: Using GridBagLayout with custom insets for closer labels
        // ============================================================
        JPanel bmiInputPanel = new JPanel(new GridBagLayout());
        bmiInputPanel.setBackground(Color.WHITE);
        bmiInputPanel.setPreferredSize(new Dimension(0, 40));

        GridBagConstraints bmiGbc = new GridBagConstraints();
        bmiGbc.fill = GridBagConstraints.HORIZONTAL;
        bmiGbc.insets = new Insets(0, 0, 0, 5); // Small gap after label
        bmiGbc.gridy = 0;

        // Height label
        bmiGbc.gridx = 0;
        bmiGbc.weightx = 0.0; // Don't stretch
        JLabel heightLabel = new JLabel("Height (cm):");
        heightLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bmiInputPanel.add(heightLabel, bmiGbc);

        // Height field - close to label
        bmiGbc.gridx = 1;
        bmiGbc.weightx = 0.3; // Allow stretching
        bmiGbc.insets = new Insets(0, 5, 0, 20); // 5px from label, 20px margin right
        heightField = new JTextField();
        heightField.setFont(new Font("Arial", Font.PLAIN, 16));
        heightField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        bmiInputPanel.add(heightField, bmiGbc);

        // Weight label
        bmiGbc.gridx = 2;
        bmiGbc.weightx = 0.0; // Don't stretch
        bmiGbc.insets = new Insets(0, 20, 0, 5); // 20px margin left, 5px before field
        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bmiInputPanel.add(weightLabel, bmiGbc);

        // Weight field - close to label
        bmiGbc.gridx = 3;
        bmiGbc.weightx = 0.3; // Allow stretching
        bmiGbc.insets = new Insets(0, 5, 0, 0); // 5px from label
        weightField = new JTextField();
        weightField.setFont(new Font("Arial", Font.PLAIN, 16));
        weightField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        bmiInputPanel.add(weightField, bmiGbc);

        profileContent.add(bmiInputPanel, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // Calculate BMI Button
        calculateBmiButton = createStyledButton("Calculate BMI", new Color(76, 175, 80));
        calculateBmiButton.setPreferredSize(new Dimension(200, 40));
        calculateBmiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateBMI();
            }
        });

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(calculateBmiButton);
        profileContent.add(buttonWrapper, gbc);
        gbc.gridy++;

        profileContent.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy++;

        // BMI Results Panel with fixed size
        JPanel bmiResultPanel = new JPanel();
        bmiResultPanel.setLayout(new BoxLayout(bmiResultPanel, BoxLayout.Y_AXIS));
        bmiResultPanel.setBackground(new Color(245, 250, 245));
        bmiResultPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 200), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        bmiResultPanel.setPreferredSize(new Dimension(0, 120));

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

        profileContent.add(bmiResultPanel, gbc);
        gbc.gridy++;

        // Save/Cancel buttons
        profileContent.add(Box.createVerticalStrut(15), gbc);
        gbc.gridy++;

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        savePanel.setOpaque(false);

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

        profileContent.add(savePanel, gbc);

        // Add vertical glue at the end
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        profileContent.add(Box.createVerticalGlue(), gbc);

        contentWrapper.add(profileContent, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(45, 60, 35));
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
                status = "Your BMI is below the healthy range for men. Consider consulting a nutritionist.";
                categoryColor = new Color(255, 152, 0);
            } else if (bmi >= 18.5 && bmi < 25) {
                category = "Normal (Healthy)";
                status = "Excellent! Your BMI is in the healthy range for men.";
                categoryColor = new Color(76, 175, 80);
            } else if (bmi >= 25 && bmi < 30) {
                category = "Overweight";
                status = "Your BMI is above the healthy range. Consider increasing physical activity.";
                categoryColor = new Color(255, 193, 7);
            } else {
                category = "Obese";
                status = "Consider consulting a healthcare provider for a personalized plan.";
                categoryColor = new Color(244, 67, 54);
            }
        } else if (gender.equalsIgnoreCase("Female")) {
            if (bmi < 18.5) {
                category = "Underweight";
                status = "Your BMI is below the healthy range for women. Consider consulting a nutritionist.";
                categoryColor = new Color(255, 152, 0);
            } else if (bmi >= 18.5 && bmi < 24) {
                category = "Normal (Healthy)";
                status = "Excellent! Your BMI is in the healthy range for women.";
                categoryColor = new Color(76, 175, 80);
            } else if (bmi >= 24 && bmi < 29) {
                category = "Overweight";
                status = "Your BMI is above the healthy range. Consider lifestyle modifications.";
                categoryColor = new Color(255, 193, 7);
            } else {
                category = "Obese";
                status = "Consider consulting a healthcare provider for guidance.";
                categoryColor = new Color(244, 67, 54);
            }
        } else {
            // Default/Other gender
            if (bmi < 18.5) {
                category = "Underweight";
                status = "Your BMI is below the healthy range.";
                categoryColor = new Color(255, 152, 0);
            } else if (bmi >= 18.5 && bmi < 25) {
                category = "Normal (Healthy)";
                status = "Your BMI is in the healthy range.";
                categoryColor = new Color(76, 175, 80);
            } else if (bmi >= 25 && bmi < 30) {
                category = "Overweight";
                status = "Your BMI is above the healthy range.";
                categoryColor = new Color(255, 193, 7);
            } else {
                category = "Obese";
                status = "Consider consulting a healthcare provider.";
                categoryColor = new Color(244, 67, 54);
            }
        }

        bmiCategoryLabel.setText("Category: " + category);
        bmiCategoryLabel.setForeground(categoryColor);
        bmiStatusLabel.setText("<html><body style='width: 100%'>" + status + "</body></html>");
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
