import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Email Verification Service
 * Version: 1.0
 * Features: OTP generation, Email sending, Code verification
 */
public class EmailVerification {
    private static Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private static Map<String, Long> codeTimestamps = new ConcurrentHashMap<>();

    private static final String SENDER_EMAIL = "abrarmddayan120@gmail.com";
    private static final String SENDER_PASSWORD = "peuj cadx skty sskr";
    private static final int CODE_EXPIRY_MINUTES = 10;

    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static boolean sendVerificationCode(String recipientEmail, String purpose) {
        String code = generateVerificationCode();
        verificationCodes.put(recipientEmail, code);
        codeTimestamps.put(recipientEmail, System.currentTimeMillis());

        String subject = purpose.equals("registration")
                ? "Eco-Eats - Verify Your Email"
                : "Eco-Eats - Password Reset Code";

        String body = buildEmailBody(code, purpose);
        return sendEmail(recipientEmail, subject, body);
    }

    private static String buildEmailBody(String code, String purpose) {
        if (purpose.equals("registration")) {
            return "<!DOCTYPE html>" +
                    "<html><body style='font-family: Arial, sans-serif;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                    "<h2 style='color: #7B8D4A;'>Welcome to Eco-Eats!</h2>" +
                    "<p>Thank you for registering with Eco-Eats. To complete your registration, please use the verification code below:</p>" +
                    "<div style='background-color: #f4f4f4; padding: 15px; text-align: center; font-size: 32px; font-weight: bold; letter-spacing: 5px; margin: 20px 0;'>" +
                    code +
                    "</div>" +
                    "<p>This code will expire in " + CODE_EXPIRY_MINUTES + " minutes.</p>" +
                    "<p>If you didn't request this code, please ignore this email.</p>" +
                    "<hr style='margin: 30px 0;'>" +
                    "<p style='color: #666; font-size: 12px;'>This is an automated message from Eco-Eats. Please do not reply to this email.</p>" +
                    "</div></body></html>";
        } else {
            return "<!DOCTYPE html>" +
                    "<html><body style='font-family: Arial, sans-serif;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                    "<h2 style='color: #7B8D4A;'>Password Reset Request</h2>" +
                    "<p>We received a request to reset your Eco-Eats account password. Use the code below to proceed:</p>" +
                    "<div style='background-color: #f4f4f4; padding: 15px; text-align: center; font-size: 32px; font-weight: bold; letter-spacing: 5px; margin: 20px 0;'>" +
                    code +
                    "</div>" +
                    "<p>This code will expire in " + CODE_EXPIRY_MINUTES + " minutes.</p>" +
                    "<p>If you didn't request a password reset, please ignore this email or contact support if you have concerns.</p>" +
                    "<hr style='margin: 30px 0;'>" +
                    "<p style='color: #666; font-size: 12px;'>This is an automated message from Eco-Eats. Please do not reply to this email.</p>" +
                    "</div></body></html>";
        }
    }

    private static boolean sendEmail(String recipientEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Verification code sent to: " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyCode(String email, String code) {
        if (!verificationCodes.containsKey(email)) {
            return false;
        }

        long timestamp = codeTimestamps.get(email);
        long currentTime = System.currentTimeMillis();
        long elapsedMinutes = (currentTime - timestamp) / (1000 * 60);

        if (elapsedMinutes > CODE_EXPIRY_MINUTES) {
            verificationCodes.remove(email);
            codeTimestamps.remove(email);
            return false;
        }

        String storedCode = verificationCodes.get(email);
        if (storedCode.equals(code)) {
            verificationCodes.remove(email);
            codeTimestamps.remove(email);
            return true;
        }

        return false;
    }

    public static void removeCode(String email) {
        verificationCodes.remove(email);
        codeTimestamps.remove(email);
    }

    public static boolean hasCode(String email) {
        return verificationCodes.containsKey(email);
    }
}