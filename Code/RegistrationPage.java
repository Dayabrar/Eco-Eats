import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Eco-Eats Registration Page
 * Complete implementation with email verification
 * Version: 1.0 - Full Code
 */
public class RegistrationPage extends JFrame {
    // Form fields
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> activityComboBox;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    // UI components
    private JButton registerButton;
    private JLabel signInLabel;
    private JLabel passwordStrengthLabel;

    public RegistrationPage() {
        // Window setup
        setTitle("Eco-Eats Registration");
        setSize(1240, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);

        // ==================== LEFT PANEL (Green Info Side) ====================
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 390, 800);
        leftPanel.setBackground(new Color(153, 183, 34));
        leftPanel.setLayout(null);

        // Logo
        try {
            ImageIcon logoIcon = new ImageIcon("src/logo.png");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(140, 117, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setBounds(100, 20, 140, 140);
            leftPanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Logo image not found at: src/logo.png");
        }

        // ECO-EATS Title
        JLabel ecoEatsTitle = new JLabel("ECO-EATS");
        ecoEatsTitle.setFont(new Font("Arial", Font.BOLD, 31));
        ecoEatsTitle.setForeground(Color.WHITE);
        ecoEatsTitle.setBounds(37, 147, 350, 65);
        leftPanel.add(ecoEatsTitle);

        // Subtitle
        JLabel nutritionProfileLabel = new JLabel("Create Your Nutrition Profile");
        nutritionProfileLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nutritionProfileLabel.setForeground(new Color(40, 60, 30));
        nutritionProfileLabel.setBounds(37, 220, 330, 25);
        leftPanel.add(nutritionProfileLabel);

        // Tagline
        JLabel taglineLabel = new JLabel("personalized tracking for better health");
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        taglineLabel.setForeground(Color.BLACK);
        taglineLabel.setBounds(37, 250, 330, 20);
        leftPanel.add(taglineLabel);

        // Why Join Section
        JLabel whyJoinLabel = new JLabel("Why Join Eco-Eats ?");
        whyJoinLabel.setFont(new Font("Arial", Font.BOLD, 24));
        whyJoinLabel.setForeground(new Color(180, 60, 60));
        whyJoinLabel.setBounds(37, 310, 300, 30);
        leftPanel.add(whyJoinLabel);

        // Benefits list
        String[] benefits = {
                "Track daily nutrition goals",
                "Monitor calorie intake",
                "Get personalized insights",
                "View progress charts",
                "Set health targets",
                "Easy meal logging"
        };

        int yPos = 365;
        for (String benefit : benefits) {
            JLabel benefitLabel = new JLabel("• " + benefit);
            benefitLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            benefitLabel.setForeground(Color.BLACK);
            benefitLabel.setBounds(47, yPos, 320, 23);
            leftPanel.add(benefitLabel);
            yPos += 35;
        }

        // Testimonial
        JTextArea testimonialArea = new JTextArea(
                "Eco-Eats helped me lose 15kg by tracking my nutrition properly. The monthly charts showed exactly where I needed to improve!"
        );
        testimonialArea.setBounds(37, 590, 330, 60);
        testimonialArea.setFont(new Font("Serif", Font.ITALIC, 13));
        testimonialArea.setForeground(Color.BLACK);
        testimonialArea.setBackground(new Color(123, 141, 74));
        testimonialArea.setLineWrap(true);
        testimonialArea.setWrapStyleWord(true);
        testimonialArea.setEditable(false);
        testimonialArea.setFocusable(false);
        leftPanel.add(testimonialArea);

        JLabel testimonialAuthor = new JLabel("Sarah M., Eco-Eats User\"");
        testimonialAuthor.setFont(new Font("Arial", Font.PLAIN, 11));
        testimonialAuthor.setForeground(Color.BLACK);
        testimonialAuthor.setBounds(37, 655, 200, 15);
        leftPanel.add(testimonialAuthor);

        mainPanel.add(leftPanel);

        // ==================== RIGHT PANEL (Registration Form) ====================
        JPanel formPanel = new JPanel();
        formPanel.setBounds(403, 0, 837, 800);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);

        // Title
        JLabel createAccountTitle = new JLabel("Create Account");
        createAccountTitle.setFont(new Font("Arial", Font.BOLD, 32));
        createAccountTitle.setForeground(Color.BLACK);
        createAccountTitle.setBounds(253, 68, 300, 40);
        formPanel.add(createAccountTitle);

        // ==================== PERSONAL INFORMATION SECTION ====================
        JLabel personalInfoLabel = new JLabel("Personal Information");
        personalInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        personalInfoLabel.setForeground(new Color(76, 175, 80));
        personalInfoLabel.setBounds(253, 130, 200, 20);
        formPanel.add(personalInfoLabel);

        // Full Name
        JLabel fullNameLabel = new JLabel("Full Name");
        fullNameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        fullNameLabel.setForeground(Color.BLACK);
        fullNameLabel.setBounds(253, 160, 100, 20);
        formPanel.add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(253, 182, 342, 30);
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 13));
        fullNameField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        formPanel.add(fullNameField);

        // Email Address
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        emailLabel.setForeground(Color.BLACK);
        emailLabel.setBounds(253, 222, 100, 20);
        formPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(253, 244, 342, 30);
        emailField.setFont(new Font("Arial", Font.PLAIN, 13));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        formPanel.add(emailField);

        // Age
        JLabel ageLabel = new JLabel("Age");
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        ageLabel.setForeground(Color.BLACK);
        ageLabel.setBounds(253, 284, 100, 20);
        formPanel.add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(253, 306, 342, 30);
        ageField.setFont(new Font("Arial", Font.PLAIN, 13));
        ageField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        formPanel.add(ageField);

        // ==================== PROFILE DETAILS SECTION ====================
        JLabel profileDetailsLabel = new JLabel("Profile Details");
        profileDetailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        profileDetailsLabel.setForeground(new Color(76, 175, 80));
        profileDetailsLabel.setBounds(253, 348, 200, 20);
        formPanel.add(profileDetailsLabel);

        // Gender
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        genderLabel.setForeground(Color.BLACK);
        genderLabel.setBounds(253, 378, 100, 20);
        formPanel.add(genderLabel);

        String[] genderOptions = {"Select Gender", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genderOptions);
        genderComboBox.setBounds(253, 400, 342, 30);
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        genderComboBox.setBackground(Color.WHITE);
        formPanel.add(genderComboBox);

        // Activity Level
        JLabel activityLabel = new JLabel("Activity level");
        activityLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        activityLabel.setForeground(Color.BLACK);
        activityLabel.setBounds(253, 440, 100, 20);
        formPanel.add(activityLabel);

        String[] activityOptions = {"Select Activity Level", "Sedentary", "Lightly Active",
                "Moderately Active", "Very Active", "Extremely Active"};
        activityComboBox = new JComboBox<>(activityOptions);
        activityComboBox.setBounds(253, 462, 342, 30);
        activityComboBox.setFont(new Font("Arial", Font.PLAIN, 13));
        activityComboBox.setBackground(Color.WHITE);
        formPanel.add(activityComboBox);

        // ==================== SECURITY SECTION ====================
        JLabel securityLabel = new JLabel("Security");
        securityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        securityLabel.setForeground(new Color(76, 175, 80));
        securityLabel.setBounds(253, 504, 200, 20);
        formPanel.add(securityLabel);

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(253, 534, 100, 20);
        formPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(253, 556, 342, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));

        // Password strength indicator
        passwordStrengthLabel = new JLabel("");
        passwordStrengthLabel.setFont(new Font("Arial", Font.BOLD, 11));
        passwordStrengthLabel.setBounds(253, 588, 342, 15);
        formPanel.add(passwordStrengthLabel);

        // Real-time password strength checker
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updatePasswordStrength(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updatePasswordStrength(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updatePasswordStrength(); }

            private void updatePasswordStrength() {
                String pwd = new String(passwordField.getPassword());
                if (pwd.isEmpty()) {
                    passwordStrengthLabel.setText("");
                } else {
                    ValidationHelper.PasswordStrength strength = ValidationHelper.checkPasswordStrength(pwd);
                    passwordStrengthLabel.setText("Strength: " + strength.getLevelText() + " - " + strength.feedback);
                    passwordStrengthLabel.setForeground(strength.getLevelColor());
                }
            }
        });

        formPanel.add(passwordField);

        // Confirm Password
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        confirmPasswordLabel.setForeground(Color.BLACK);
        confirmPasswordLabel.setBounds(253, 610, 120, 20);
        formPanel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(253, 632, 342, 30);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 13));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        formPanel.add(confirmPasswordField);

        // ==================== REGISTER BUTTON ====================
        registerButton = new JButton("Create Nutrition Profile");
        registerButton.setBounds(295, 680, 260, 45);
        registerButton.setFont(new Font("Arial", Font.BOLD, 15));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(123, 141, 74));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder());
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(135, 153, 84));
            }
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(123, 141, 74));
            }
        });

        registerButton.addActionListener(e -> performRegistration());
        formPanel.add(registerButton);

        // ==================== SIGN IN LINK ====================
        JLabel alreadyAccountLabel = new JLabel("Already have an account?");
        alreadyAccountLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        alreadyAccountLabel.setForeground(Color.BLACK);
        alreadyAccountLabel.setBounds(320, 727, 165, 20);
        formPanel.add(alreadyAccountLabel);

        signInLabel = new JLabel("<html><u>Sign in</u></html>");
        signInLabel.setFont(new Font("Arial", Font.BOLD, 13));
        signInLabel.setForeground(new Color(100, 120, 60));
        signInLabel.setBounds(475, 727, 60, 20);
        signInLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openLoginPage();
            }
            public void mouseEntered(MouseEvent e) {
                signInLabel.setForeground(new Color(120, 140, 70));
            }
            public void mouseExited(MouseEvent e) {
                signInLabel.setForeground(new Color(100, 120, 60));
            }
        });
        formPanel.add(signInLabel);

        mainPanel.add(formPanel);
        add(mainPanel);
        setVisible(true);
    }

    // ==================== REGISTRATION LOGIC ====================
    private void performRegistration() {
        // Get all form values
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String activity = (String) activityComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // ========== VALIDATION ==========

        // Check if all fields are filled
        if (fullName.isEmpty() || email.isEmpty() || ageStr.isEmpty() ||
                gender.equals("Select Gender") || activity.equals("Select Activity Level") ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate name format
        if (!ValidationHelper.isValidName(fullName)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid name\n(Only letters and spaces, 2-50 characters)",
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        if (!ValidationHelper.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address\nExample: user@example.com",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate and parse age
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

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check password strength
        ValidationHelper.PasswordStrength strength = ValidationHelper.checkPasswordStrength(password);

        if (!ValidationHelper.isPasswordAcceptable(password)) {
            JOptionPane.showMessageDialog(this,
                    "Password is too weak!\n" + strength.feedback + "\n\n" +
                            ValidationHelper.getPasswordRequirements(),
                    "Weak Password",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Warn if password is weak but acceptable
        if (strength.level == ValidationHelper.StrengthLevel.WEAK ||
                strength.level == ValidationHelper.StrengthLevel.VERY_WEAK) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Your password is " + strength.getLevelText() + "!\n" +
                            strength.feedback + "\n\nFor better security, consider:\n" +
                            "• Using 8+ characters\n" +
                            "• Including uppercase, lowercase, numbers, and special characters\n\n" +
                            "Do you want to continue with this password?",
                    "Weak Password Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Check if email already exists
        if (DatabaseHelper.emailExists(email)) {
            JOptionPane.showMessageDialog(this,
                    "This email is already registered!\nPlease use a different email or try logging in.",
                    "Email Already Exists",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ========== EMAIL VERIFICATION ==========

        registerButton.setEnabled(false);
        registerButton.setText("Sending Code...");

        final int finalAge = age;

        // Send verification code in background thread
        new Thread(() -> {
            boolean sent = EmailVerification.sendVerificationCode(email, "registration");

            SwingUtilities.invokeLater(() -> {
                registerButton.setEnabled(true);
                registerButton.setText("Create Nutrition Profile");

                if (sent) {
                    // Show verification dialog
                    VerificationDialog dialog = new VerificationDialog(this, email, "registration");
                    dialog.setVisible(true);

                    if (dialog.isVerified()) {
                        // Save to database
                        boolean saved = DatabaseHelper.registerUser(
                                fullName, email, finalAge, gender, activity, password
                        );

                        if (saved) {
                            JOptionPane.showMessageDialog(this,
                                    "Registration Successful!\nYour account has been created. You can now login.",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            openLoginPage();
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Registration failed. Please try again or contact support.",
                                    "Database Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to send verification code. Please check your email and try again.",
                            "Email Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    // Navigate to login page
    private void openLoginPage() {
        new LoginPage();
        dispose();
    }
}