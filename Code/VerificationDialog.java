import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Verification Dialog - Email OTP Verification
 * Version: 1.0
 * Features: 6-digit code input, Timer countdown, Resend functionality
 */
public class VerificationDialog extends JDialog {
    private JTextField codeField;
    private JButton verifyButton;
    private JButton resendButton;
    private JLabel timerLabel;
    private String email;
    private String purpose;
    private boolean verified = false;
    private Timer countdownTimer;
    private int secondsLeft = 600; // 10 minutes

    public VerificationDialog(JFrame parent, String email, String purpose) {
        super(parent, "Email Verification", true);
        this.email = email;
        this.purpose = purpose;

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Verify Your Email");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(123, 141, 74));
        titleLabel.setBounds(120, 20, 250, 30);
        mainPanel.add(titleLabel);

        // Instruction text
        JLabel instructionLabel = new JLabel("<html><center>We've sent a 6-digit verification code to:<br/><b>" + email + "</b></center></html>");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        instructionLabel.setForeground(Color.BLACK);
        instructionLabel.setBounds(50, 60, 350, 40);
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(instructionLabel);

        // Code label
        JLabel codeLabel = new JLabel("Enter Verification Code:");
        codeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        codeLabel.setForeground(Color.BLACK);
        codeLabel.setBounds(75, 115, 200, 20);
        mainPanel.add(codeLabel);

        // Code input field
        codeField = new JTextField();
        codeField.setBounds(75, 140, 300, 35);
        codeField.setFont(new Font("Arial", Font.BOLD, 20));
        codeField.setHorizontalAlignment(JTextField.CENTER);
        codeField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));
        mainPanel.add(codeField);

        // Timer label
        timerLabel = new JLabel("Code expires in: 10:00");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timerLabel.setForeground(new Color(200, 100, 100));
        timerLabel.setBounds(150, 180, 150, 20);
        mainPanel.add(timerLabel);

        // Verify button
        verifyButton = new JButton("Verify");
        verifyButton.setBounds(100, 210, 120, 35);
        verifyButton.setFont(new Font("Arial", Font.BOLD, 15));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setBackground(new Color(123, 141, 74));
        verifyButton.setFocusPainted(false);
        verifyButton.setBorder(BorderFactory.createEmptyBorder());
        verifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verifyButton.addActionListener(e -> verifyCode());
        mainPanel.add(verifyButton);

        // Resend button
        resendButton = new JButton("Resend Code");
        resendButton.setBounds(230, 210, 120, 35);
        resendButton.setFont(new Font("Arial", Font.BOLD, 15));
        resendButton.setForeground(Color.WHITE);
        resendButton.setBackground(new Color(100, 100, 100));
        resendButton.setFocusPainted(false);
        resendButton.setBorder(BorderFactory.createEmptyBorder());
        resendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resendButton.addActionListener(e -> resendCode());
        mainPanel.add(resendButton);

        add(mainPanel);
        startCountdown();
    }

    private void startCountdown() {
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsLeft--;
                int minutes = secondsLeft / 60;
                int seconds = secondsLeft % 60;
                timerLabel.setText(String.format("Code expires in: %d:%02d", minutes, seconds));

                if (secondsLeft <= 0) {
                    countdownTimer.stop();
                    timerLabel.setText("Code expired!");
                    timerLabel.setForeground(Color.RED);
                    verifyButton.setEnabled(false);
                    JOptionPane.showMessageDialog(VerificationDialog.this,
                            "Verification code has expired. Please request a new code.",
                            "Code Expired",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        countdownTimer.start();
    }

    private void verifyCode() {
        String enteredCode = codeField.getText().trim();

        if (enteredCode.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the verification code",
                    "Verification Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (enteredCode.length() != 6) {
            JOptionPane.showMessageDialog(this,
                    "Verification code must be 6 digits",
                    "Invalid Code",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (EmailVerification.verifyCode(email, enteredCode)) {
            verified = true;
            countdownTimer.stop();
            JOptionPane.showMessageDialog(this,
                    "Email verified successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid or expired verification code. Please try again.",
                    "Verification Failed",
                    JOptionPane.ERROR_MESSAGE);
            codeField.setText("");
        }
    }

    private void resendCode() {
        secondsLeft = 600;
        timerLabel.setForeground(new Color(200, 100, 100));
        verifyButton.setEnabled(true);

        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        new Thread(() -> {
            boolean sent = EmailVerification.sendVerificationCode(email, purpose);
            SwingUtilities.invokeLater(() -> {
                if (sent) {
                    JOptionPane.showMessageDialog(this,
                            "A new verification code has been sent to your email.",
                            "Code Sent",
                            JOptionPane.INFORMATION_MESSAGE);
                    codeField.setText("");
                    startCountdown();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to send verification code. Please check your internet connection.",
                            "Send Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }).start();
    }

    public boolean isVerified() {
        return verified;
    }

    @Override
    public void dispose() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        super.dispose();
    }
}