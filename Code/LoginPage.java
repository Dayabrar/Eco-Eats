import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login Page - User Authentication Interface with Admin Support
 * Version: 2.0 - Added Admin Panel routing
 * Features: Email/Password login, Forgot password, Registration link, Admin detection
 */
public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox keepLoggedInCheckBox;
    private JButton loginButton;
    private JLabel createAccountLabel;

    public LoginPage() {
        setTitle("Eco-Eats Login");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);

        // Left Panel (Green Side)
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 456, 800);
        leftPanel.setBackground(new Color(153, 183, 34));
        leftPanel.setLayout(null);

        // Logo at top
        try {
            ImageIcon logoIcon = new ImageIcon("src/logo.png");
            Image scaledLogo = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setBounds(188, 60, 80, 80);
            leftPanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Logo not found at: src/logo.png");
        }

        // ECO-EATS Title
        JLabel ecoEatsTitle = new JLabel("Eco-Eats");
        ecoEatsTitle.setFont(new Font("Serif", Font.BOLD, 62));
        ecoEatsTitle.setForeground(Color.WHITE);
        ecoEatsTitle.setBounds(95, 170, 300, 70);
        leftPanel.add(ecoEatsTitle);

        // Smart Nutrition Tracker
        JLabel smartTrackerLabel = new JLabel("Smart Nutrition Tracker");
        smartTrackerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        smartTrackerLabel.setForeground(new Color(40, 60, 30));
        smartTrackerLabel.setBounds(95, 245, 280, 25);
        leftPanel.add(smartTrackerLabel);

        // Track · Analyze · Improve
        JLabel taglineLabel = new JLabel("Track · Analyze · Improve");
        taglineLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        taglineLabel.setForeground(Color.BLACK);
        taglineLabel.setBounds(120, 275, 250, 20);
        leftPanel.add(taglineLabel);

        // Start Your Nutrition Journey:
        JLabel journeyLabel = new JLabel("Start Your Nutrition Journey :");
        journeyLabel.setFont(new Font("Arial", Font.BOLD, 18));
        journeyLabel.setForeground(new Color(180, 60, 60));
        journeyLabel.setBounds(80, 345, 300, 25);
        leftPanel.add(journeyLabel);

        // Journey points
        String[] journeyPoints = {
                "• Track daily calories & macros",
                "• Monitor monthly progress",
                "• Get personalized insights",
                "• Achieve your health goals"
        };

        int yPos = 385;
        for (String point : journeyPoints) {
            JLabel pointLabel = new JLabel(point);
            pointLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            pointLabel.setForeground(Color.BLACK);
            pointLabel.setBounds(100, yPos, 300, 20);
            leftPanel.add(pointLabel);
            yPos += 30;
        }

        // Testimonial
        JTextArea testimonialArea = new JTextArea(
                "What I love most about Eco-Eats is that it doesn't just track calories – it also encourages sustainable eating choices. I'm hitting my nutrition targets and reducing my carbon footprint at the same time!"
        );
        testimonialArea.setBounds(80, 540, 300, 100);
        testimonialArea.setFont(new Font("Serif", Font.ITALIC, 13));
        testimonialArea.setForeground(Color.BLACK);
        testimonialArea.setBackground(new Color(153, 183, 34));
        testimonialArea.setLineWrap(true);
        testimonialArea.setWrapStyleWord(true);
        testimonialArea.setEditable(false);
        testimonialArea.setFocusable(false);
        leftPanel.add(testimonialArea);

        JLabel testimonialAuthor = new JLabel("Mr. Alex, Eco-Eats User\"");
        testimonialAuthor.setFont(new Font("Arial", Font.PLAIN, 12));
        testimonialAuthor.setForeground(Color.BLACK);
        testimonialAuthor.setBounds(80, 645, 200, 20);
        leftPanel.add(testimonialAuthor);

        mainPanel.add(leftPanel);

        // Right Panel (White Side - Login Form)
        JPanel formPanel = new JPanel();
        formPanel.setBounds(456, 0, 824, 800);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);

        // Welcome Back Title
        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBounds(240, 125, 400, 60);
        formPanel.add(welcomeLabel);

        // Email Address Label
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setForeground(Color.BLACK);
        emailLabel.setBounds(220, 230, 150, 20);
        formPanel.add(emailLabel);

        // Email Field
        emailField = new JTextField();
        emailField.setBounds(220, 260, 390, 45);
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));
        emailField.setBackground(Color.WHITE);
        //emailField.setForeground(Color.GREEN);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        formPanel.add(emailField);

        // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(220, 325, 150, 20);
        formPanel.add(passwordLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(220, 355, 390, 45);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        formPanel.add(passwordField);

        // Keep me logged in checkbox
        keepLoggedInCheckBox = new JCheckBox("Keep me logged in");
        keepLoggedInCheckBox.setBounds(220, 415, 170, 25);
        keepLoggedInCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        keepLoggedInCheckBox.setForeground(Color.BLACK);
        keepLoggedInCheckBox.setBackground(Color.WHITE);
        keepLoggedInCheckBox.setFocusPainted(false);
        formPanel.add(keepLoggedInCheckBox);

        // Forget Password Link
        JLabel forgetPasswordLabel = new JLabel("Forget Password?");
        forgetPasswordLabel.setBounds(490, 418, 130, 20);
        forgetPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        forgetPasswordLabel.setForeground(new Color(139, 69, 19));
        forgetPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgetPasswordLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleForgotPassword();
            }
            public void mouseEntered(MouseEvent e) {
                forgetPasswordLabel.setForeground(new Color(160, 82, 22));
            }
            public void mouseExited(MouseEvent e) {
                forgetPasswordLabel.setForeground(new Color(139, 69, 19));
            }
        });
        formPanel.add(forgetPasswordLabel);

        // Start Tracking Button (Dark Green, Rounded)
        loginButton = new JButton("Start Tracking") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2d.setColor(new Color(55, 85, 50));
                } else {
                    g2d.setColor(new Color(45, 75, 45));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };
        loginButton.setBounds(285, 465, 260, 55);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> performLogin());
        formPanel.add(loginButton);

        // New to Eco-Eats? Create Account
        JLabel newUserLabel = new JLabel("New to Eco-Eats?");
        newUserLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        newUserLabel.setForeground(Color.BLACK);
        newUserLabel.setBounds(292, 540, 140, 20);
        formPanel.add(newUserLabel);

        createAccountLabel = new JLabel("<html><u>Create Account</u></html>");
        createAccountLabel.setFont(new Font("Arial", Font.BOLD, 15));
        createAccountLabel.setForeground(new Color(139, 69, 19));
        createAccountLabel.setBounds(432, 540, 130, 20);
        createAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openRegistrationPage();
            }
            public void mouseEntered(MouseEvent e) {
                createAccountLabel.setForeground(new Color(160, 82, 22));
            }
            public void mouseExited(MouseEvent e) {
                createAccountLabel.setForeground(new Color(139, 69, 19));
            }
        });
        formPanel.add(createAccountLabel);

        mainPanel.add(formPanel);
        add(mainPanel);
        setVisible(true);
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationHelper.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address\nExample: user@example.com",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        new Thread(() -> {
            boolean isValid = DatabaseHelper.validateLogin(email, password);

            SwingUtilities.invokeLater(() -> {
                loginButton.setEnabled(true);
                loginButton.setText("Start Tracking");

                if (isValid) {
                    DatabaseHelper.UserData user = DatabaseHelper.getUserByEmail(email);

                    if (user != null) {
                        // Check user role and redirect accordingly
                        if ("admin".equals(user.role)) {
                            new AdminPanel(user);
                        } else {
                            new Dashboard(user);
                        }
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid email or password. Please try again.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                }
            });
        }).start();
    }

    private void openRegistrationPage() {
        new RegistrationPage();
        dispose();
    }

    private void handleForgotPassword() {
        String email = JOptionPane.showInputDialog(this,
                "Enter your registered email address:",
                "Forgot Password",
                JOptionPane.QUESTION_MESSAGE);

        if (email == null || email.trim().isEmpty()) {
            return;
        }

        email = email.trim();

        if (!ValidationHelper.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address\nExample: user@example.com",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!DatabaseHelper.emailExists(email)) {
            JOptionPane.showMessageDialog(this,
                    "Email not found. Please check your email or register a new account.",
                    "Email Not Found",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String userEmail = email;

        new Thread(() -> {
            boolean sent = EmailVerification.sendVerificationCode(userEmail, "password_reset");
            SwingUtilities.invokeLater(() -> {
                if (sent) {
                    VerificationDialog dialog = new VerificationDialog(this, userEmail, "password_reset");
                    dialog.setVisible(true);

                    if (dialog.isVerified()) {
                        showPasswordResetDialog(userEmail);
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

    private void showPasswordResetDialog(String email) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
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

        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        panel.add(new JLabel(""));
        panel.add(strengthLabel);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Reset Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Password fields cannot be empty",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            ValidationHelper.PasswordStrength strength = ValidationHelper.checkPasswordStrength(newPassword);
            if (!ValidationHelper.isPasswordAcceptable(newPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Password is too weak!\n" + strength.feedback + "\n\n" +
                                ValidationHelper.getPasswordRequirements(),
                        "Weak Password",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Passwords do not match",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (strength.level == ValidationHelper.StrengthLevel.WEAK ||
                    strength.level == ValidationHelper.StrengthLevel.VERY_WEAK) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Your password is " + strength.getLevelText() + "!\n" +
                                strength.feedback + "\n\nDo you want to continue anyway?",
                        "Weak Password Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            boolean updated = DatabaseHelper.updatePassword(email, newPassword);

            if (updated) {
                JOptionPane.showMessageDialog(this,
                        "Password reset successful! You can now login with your new password.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to reset password. Please try again or contact support.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
