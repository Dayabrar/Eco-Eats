import java.util.regex.*;

/**
 * Validation Helper - Input Validation Utilities
 * Version: 1.0
 * Features: Email validation, Password strength checking, Name/Age validation
 */
public class ValidationHelper {

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email.trim());
        return matcher.matches();
    }

    public static PasswordStrength checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return new PasswordStrength(StrengthLevel.VERY_WEAK, "Password cannot be empty");
        }

        int score = 0;
        String feedback = "";

        if (password.length() < 6) {
            return new PasswordStrength(StrengthLevel.VERY_WEAK, "Password must be at least 6 characters long");
        } else if (password.length() >= 6 && password.length() < 8) {
            score += 1;
            feedback = "Password is too short. Use at least 8 characters";
        } else if (password.length() >= 8 && password.length() < 12) {
            score += 2;
        } else {
            score += 3;
        }

        if (password.matches(".*[a-z].*")) {
            score += 1;
        } else {
            feedback = "Add lowercase letters for stronger password";
        }

        if (password.matches(".*[A-Z].*")) {
            score += 1;
        } else {
            if (feedback.isEmpty()) {
                feedback = "Add uppercase letters for stronger password";
            }
        }

        if (password.matches(".*\\d.*")) {
            score += 1;
        } else {
            if (feedback.isEmpty()) {
                feedback = "Add numbers for stronger password";
            }
        }

        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>_\\-+=\\[\\]\\\\;'/`~].*")) {
            score += 2;
        } else {
            if (feedback.isEmpty()) {
                feedback = "Add special characters (!@#$%^&*) for stronger password";
            }
        }

        if (password.matches(".*(.)\\1{2,}.*")) {
            score -= 1;
            feedback = "Avoid repeated characters";
        }

        if (password.matches("(?i).*(password|12345|qwerty|admin|letmein|welcome).*")) {
            score -= 2;
            feedback = "Avoid common passwords";
        }

        StrengthLevel level;
        if (score <= 2) {
            level = StrengthLevel.VERY_WEAK;
            if (feedback.isEmpty()) feedback = "Very weak password. Add more variety";
        } else if (score <= 4) {
            level = StrengthLevel.WEAK;
            if (feedback.isEmpty()) feedback = "Weak password. Consider adding more characters";
        } else if (score <= 6) {
            level = StrengthLevel.MEDIUM;
            if (feedback.isEmpty()) feedback = "Medium strength. Add special characters for better security";
        } else if (score <= 8) {
            level = StrengthLevel.STRONG;
            if (feedback.isEmpty()) feedback = "Strong password!";
        } else {
            level = StrengthLevel.VERY_STRONG;
            feedback = "Excellent! Very strong password";
        }

        return new PasswordStrength(level, feedback);
    }

    public static boolean isPasswordAcceptable(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        return true;
    }

    public static String getPasswordRequirements() {
        return "Password Requirements:\n" +
                "• Minimum 6 characters (8+ recommended)\n" +
                "• Include uppercase and lowercase letters\n" +
                "• Include at least one number\n" +
                "• Include special characters (!@#$%^&*) for best security\n" +
                "• Avoid common words or patterns";
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s]{2,50}$");
    }

    public static boolean isValidAge(int age) {
        return age >= 10 && age <= 120;
    }

    public enum StrengthLevel {
        VERY_WEAK, WEAK, MEDIUM, STRONG, VERY_STRONG
    }

    public static class PasswordStrength {
        public final StrengthLevel level;
        public final String feedback;

        public PasswordStrength(StrengthLevel level, String feedback) {
            this.level = level;
            this.feedback = feedback;
        }

        public String getLevelText() {
            switch (level) {
                case VERY_WEAK: return "Very Weak";
                case WEAK: return "Weak";
                case MEDIUM: return "Medium";
                case STRONG: return "Strong";
                case VERY_STRONG: return "Very Strong";
                default: return "Unknown";
            }
        }

        public java.awt.Color getLevelColor() {
            switch (level) {
                case VERY_WEAK: return new java.awt.Color(220, 53, 69);
                case WEAK: return new java.awt.Color(255, 133, 27);
                case MEDIUM: return new java.awt.Color(255, 193, 7);
                case STRONG: return new java.awt.Color(40, 167, 69);
                case VERY_STRONG: return new java.awt.Color(0, 123, 85);
                default: return java.awt.Color.GRAY;
            }
        }
    }
}