import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.time.LocalDate;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ecoeats_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    public static void initializeDatabase() {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                full_name VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                age INT NOT NULL,
                gender VARCHAR(20) NOT NULL,
                activity_level VARCHAR(50) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role ENUM('user', 'admin') DEFAULT 'user',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        String createUserGoalsTable = """
            CREATE TABLE IF NOT EXISTS user_goals (
                id INT PRIMARY KEY AUTO_INCREMENT,
                user_id INT UNIQUE,
                calories INT DEFAULT 2000,
                protein_g INT DEFAULT 150,
                carbs_g INT DEFAULT 250,
                fats_g INT DEFAULT 65,
                water_ml INT DEFAULT 2000,
                calcium_mg INT DEFAULT 1000,
                potassium_mg INT DEFAULT 3500,
                sodium_mg INT DEFAULT 2300,
                magnesium_mg INT DEFAULT 400,
                iron_mg INT DEFAULT 18,
                zinc_mg INT DEFAULT 11,
                vitamin_a_iu INT DEFAULT 5000,
                vitamin_d_iu INT DEFAULT 600,
                vitamin_e_iu INT DEFAULT 22,
                vitamin_k_mcg INT DEFAULT 120,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
            )
        """;

        String createDailyLogsTable = """
            CREATE TABLE IF NOT EXISTS daily_logs (
                id INT PRIMARY KEY AUTO_INCREMENT,
                user_id INT,
                log_date DATE DEFAULT (CURRENT_DATE),
                calories INT DEFAULT 0,
                protein_g INT DEFAULT 0,
                carbs_g INT DEFAULT 0,
                fats_g INT DEFAULT 0,
                water_ml INT DEFAULT 0,
                calcium_mg INT DEFAULT 0,
                potassium_mg INT DEFAULT 0,
                sodium_mg INT DEFAULT 0,
                magnesium_mg INT DEFAULT 0,
                iron_mg INT DEFAULT 0,
                zinc_mg INT DEFAULT 0,
                vitamin_a_iu INT DEFAULT 0,
                vitamin_d_iu INT DEFAULT 0,
                vitamin_e_iu INT DEFAULT 0,
                vitamin_k_mcg INT DEFAULT 0,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                UNIQUE KEY unique_user_date (user_id, log_date),
                INDEX idx_user_date (user_id, log_date)
            )
        """;

        String createFoodItemsTable = """
            CREATE TABLE IF NOT EXISTS food_items (
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(100) UNIQUE NOT NULL,
                food_group VARCHAR(50),
                base_quantity INT DEFAULT 100,
                calories INT DEFAULT 0,
                protein_g DECIMAL(8,2) DEFAULT 0,
                carbs_g DECIMAL(8,2) DEFAULT 0,
                fats_g DECIMAL(8,2) DEFAULT 0,
                water_ml INT DEFAULT 0,
                calcium_mg INT DEFAULT 0,
                potassium_mg INT DEFAULT 0,
                sodium_mg INT DEFAULT 0,
                magnesium_mg INT DEFAULT 0,
                iron_mg DECIMAL(8,2) DEFAULT 0,
                zinc_mg DECIMAL(8,2) DEFAULT 0,
                vitamin_a_iu INT DEFAULT 0,
                vitamin_d_iu INT DEFAULT 0,
                vitamin_e_iu DECIMAL(8,2) DEFAULT 0,
                vitamin_k_mcg DECIMAL(8,2) DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_name (name)
            )
        """;

        String createFoodLogsTable = """
            CREATE TABLE IF NOT EXISTS food_logs (
                id INT PRIMARY KEY AUTO_INCREMENT,
                user_id INT,
                food_item_id INT,
                quantity INT NOT NULL,
                unit VARCHAR(20) DEFAULT 'grams',
                consumed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                meal_type ENUM('Breakfast', 'Lunch', 'Dinner', 'Snack') DEFAULT 'Snack',
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (food_item_id) REFERENCES food_items(id) ON DELETE CASCADE,
                INDEX idx_user_consumed (user_id, consumed_at),
                INDEX idx_user_date (user_id, consumed_at)
            )
        """;

        String createAdminLogsTable = """
            CREATE TABLE IF NOT EXISTS admin_logs (
                id INT PRIMARY KEY AUTO_INCREMENT,
                admin_id INT NOT NULL,
                action_type ENUM('CREATE', 'UPDATE', 'DELETE') NOT NULL,
                target_table VARCHAR(50) NOT NULL,
                target_id INT,
                action_details TEXT,
                performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE,
                INDEX idx_admin_date (admin_id, performed_at)
            )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createUserGoalsTable);
            stmt.execute(createDailyLogsTable);
            stmt.execute(createFoodItemsTable);
            stmt.execute(createFoodLogsTable);
            stmt.execute(createAdminLogsTable);

            // Create default admin if not exists
            createDefaultAdmin();

            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    private static void createDefaultAdmin() {
        String checkSql = "SELECT COUNT(*) FROM users WHERE role = 'admin'";
        String insertSql = "INSERT INTO users (full_name, email, age, gender, activity_level, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                // No admin exists, create default one
                PreparedStatement pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, "System Administrator");
                pstmt.setString(2, "admin@ecoeats.com");
                pstmt.setInt(3, 30);
                pstmt.setString(4, "Other");
                pstmt.setString(5, "Moderately Active");
                pstmt.setString(6, hashPassword("Admin@123"));
                pstmt.setString(7, "admin");
                pstmt.executeUpdate();
                System.out.println("Default admin created: admin@ecoeats.com / Admin@123");
                pstmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Error creating default admin: " + e.getMessage());
        }
    }

    public static void initializeFoodDatabase() {
        System.out.println("Food database ready for insertion");
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean registerUser(String fullName, String email, int age,
                                       String gender, String activityLevel, String password, String role) {
        String sql = "INSERT INTO users (full_name, email, age, gender, activity_level, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.setString(4, gender);
            pstmt.setString(5, activityLevel);
            pstmt.setString(6, hashPassword(password));
            pstmt.setString(7, role);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    if (role.equals("user")) {
                        createDefaultGoals(userId);
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
        }
        return false;
    }

    private static void createDefaultGoals(int userId) {
        String sql = "INSERT INTO user_goals (user_id) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Goals creation error: " + e.getMessage());
        }
    }

    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Email check error: " + e.getMessage());
        }
        return false;
    }

    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                String inputHash = hashPassword(password);
                return storedHash.equals(inputHash);
            }
        } catch (SQLException e) {
            System.err.println("Login validation error: " + e.getMessage());
        }
        return false;
    }

    public static UserData getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UserData user = new UserData();
                user.id = rs.getInt("id");
                user.fullName = rs.getString("full_name");
                user.email = rs.getString("email");
                user.age = rs.getInt("age");
                user.gender = rs.getString("gender");
                user.activityLevel = rs.getString("activity_level");
                user.role = rs.getString("role");
                user.createdAt = rs.getTimestamp("created_at");
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Get user error: " + e.getMessage());
        }
        return null;
    }

    public static boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashPassword(newPassword));
            pstmt.setString(2, email);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update password error: " + e.getMessage());
            return false;
        }
    }

    public static NutritionGoals getUserGoals(int userId) {
        String sql = "SELECT * FROM user_goals WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                NutritionGoals goals = new NutritionGoals();
                goals.userId = userId;
                goals.calories = rs.getInt("calories");
                goals.protein_g = rs.getInt("protein_g");
                goals.carbs_g = rs.getInt("carbs_g");
                goals.fats_g = rs.getInt("fats_g");
                goals.water_ml = rs.getInt("water_ml");
                goals.calcium_mg = rs.getInt("calcium_mg");
                goals.potassium_mg = rs.getInt("potassium_mg");
                goals.sodium_mg = rs.getInt("sodium_mg");
                goals.magnesium_mg = rs.getInt("magnesium_mg");
                goals.iron_mg = rs.getInt("iron_mg");
                goals.zinc_mg = rs.getInt("zinc_mg");
                goals.vitamin_a_iu = rs.getInt("vitamin_a_iu");
                goals.vitamin_d_iu = rs.getInt("vitamin_d_iu");
                goals.vitamin_e_iu = rs.getInt("vitamin_e_iu");
                goals.vitamin_k_mcg = rs.getInt("vitamin_k_mcg");
                return goals;
            }
        } catch (SQLException e) {
            System.err.println("Get goals error: " + e.getMessage());
        }
        return null;
    }

    public static DailyLog getTodayLog(int userId) {
        String sql = "SELECT * FROM daily_logs WHERE user_id = ? AND log_date = CURRENT_DATE";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractDailyLogFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get today log error: " + e.getMessage());
        }
        DailyLog log = new DailyLog();
        log.userId = userId;
        return log;
    }

    public static DailyLog getLogForDate(int userId, LocalDate date) {
        String sql = "SELECT * FROM daily_logs WHERE user_id = ? AND log_date = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractDailyLogFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get log for date error: " + e.getMessage());
        }
        DailyLog log = new DailyLog();
        log.userId = userId;
        return log;
    }

    private static DailyLog extractDailyLogFromResultSet(ResultSet rs) throws SQLException {
        DailyLog log = new DailyLog();
        log.userId = rs.getInt("user_id");
        log.calories = rs.getInt("calories");
        log.protein_g = rs.getInt("protein_g");
        log.carbs_g = rs.getInt("carbs_g");
        log.fats_g = rs.getInt("fats_g");
        log.water_ml = rs.getInt("water_ml");
        log.calcium_mg = rs.getInt("calcium_mg");
        log.potassium_mg = rs.getInt("potassium_mg");
        log.sodium_mg = rs.getInt("sodium_mg");
        log.magnesium_mg = rs.getInt("magnesium_mg");
        log.iron_mg = rs.getInt("iron_mg");
        log.zinc_mg = rs.getInt("zinc_mg");
        log.vitamin_a_iu = rs.getInt("vitamin_a_iu");
        log.vitamin_d_iu = rs.getInt("vitamin_d_iu");
        log.vitamin_e_iu = rs.getInt("vitamin_e_iu");
        log.vitamin_k_mcg = rs.getInt("vitamin_k_mcg");
        return log;
    }

    public static boolean updateUserGoals(NutritionGoals goals) {
        String sql = """
            INSERT INTO user_goals (user_id, calories, protein_g, carbs_g, fats_g, water_ml, 
            calcium_mg, potassium_mg, sodium_mg, magnesium_mg, iron_mg, zinc_mg,
            vitamin_a_iu, vitamin_d_iu, vitamin_e_iu, vitamin_k_mcg)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            calories=VALUES(calories), protein_g=VALUES(protein_g), carbs_g=VALUES(carbs_g),
            fats_g=VALUES(fats_g), water_ml=VALUES(water_ml), calcium_mg=VALUES(calcium_mg),
            potassium_mg=VALUES(potassium_mg), sodium_mg=VALUES(sodium_mg),
            magnesium_mg=VALUES(magnesium_mg), iron_mg=VALUES(iron_mg), zinc_mg=VALUES(zinc_mg),
            vitamin_a_iu=VALUES(vitamin_a_iu), vitamin_d_iu=VALUES(vitamin_d_iu),
            vitamin_e_iu=VALUES(vitamin_e_iu), vitamin_k_mcg=VALUES(vitamin_k_mcg)
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goals.userId);
            pstmt.setInt(2, goals.calories);
            pstmt.setInt(3, goals.protein_g);
            pstmt.setInt(4, goals.carbs_g);
            pstmt.setInt(5, goals.fats_g);
            pstmt.setInt(6, goals.water_ml);
            pstmt.setInt(7, goals.calcium_mg);
            pstmt.setInt(8, goals.potassium_mg);
            pstmt.setInt(9, goals.sodium_mg);
            pstmt.setInt(10, goals.magnesium_mg);
            pstmt.setInt(11, goals.iron_mg);
            pstmt.setInt(12, goals.zinc_mg);
            pstmt.setInt(13, goals.vitamin_a_iu);
            pstmt.setInt(14, goals.vitamin_d_iu);
            pstmt.setInt(15, goals.vitamin_e_iu);
            pstmt.setInt(16, goals.vitamin_k_mcg);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Update goals error: " + e.getMessage());
            return false;
        }
    }

    public static boolean addNutritionData(int userId, NutritionData data) {
        String sql = """
            INSERT INTO daily_logs (user_id, calories, protein_g, carbs_g, fats_g, water_ml,
            calcium_mg, potassium_mg, sodium_mg, magnesium_mg, iron_mg, zinc_mg,
            vitamin_a_iu, vitamin_d_iu, vitamin_e_iu, vitamin_k_mcg)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
            calories=calories+VALUES(calories), protein_g=protein_g+VALUES(protein_g),
            carbs_g=carbs_g+VALUES(carbs_g), fats_g=fats_g+VALUES(fats_g),
            water_ml=water_ml+VALUES(water_ml), calcium_mg=calcium_mg+VALUES(calcium_mg),
            potassium_mg=potassium_mg+VALUES(potassium_mg), sodium_mg=sodium_mg+VALUES(sodium_mg),
            magnesium_mg=magnesium_mg+VALUES(magnesium_mg), iron_mg=iron_mg+VALUES(iron_mg),
            zinc_mg=zinc_mg+VALUES(zinc_mg), vitamin_a_iu=vitamin_a_iu+VALUES(vitamin_a_iu),
            vitamin_d_iu=vitamin_d_iu+VALUES(vitamin_d_iu), vitamin_e_iu=vitamin_e_iu+VALUES(vitamin_e_iu),
            vitamin_k_mcg=vitamin_k_mcg+VALUES(vitamin_k_mcg)
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, data.calories);
            pstmt.setInt(3, data.protein_g);
            pstmt.setInt(4, data.carbs_g);
            pstmt.setInt(5, data.fats_g);
            pstmt.setInt(6, data.water_ml);
            pstmt.setInt(7, data.calcium_mg);
            pstmt.setInt(8, data.potassium_mg);
            pstmt.setInt(9, data.sodium_mg);
            pstmt.setInt(10, data.magnesium_mg);
            pstmt.setInt(11, data.iron_mg);
            pstmt.setInt(12, data.zinc_mg);
            pstmt.setInt(13, data.vitamin_a_iu);
            pstmt.setInt(14, data.vitamin_d_iu);
            pstmt.setInt(15, data.vitamin_e_iu);
            pstmt.setInt(16, data.vitamin_k_mcg);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Add nutrition data error: " + e.getMessage());
            return false;
        }
    }

    public static boolean resetDailyLog(int userId) {
        String sql = "DELETE FROM daily_logs WHERE user_id = ? AND log_date = CURRENT_DATE";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Reset daily log error: " + e.getMessage());
            return false;
        }
    }

    public static List<FoodItem> searchFoods(String query) {
        List<FoodItem> foods = new ArrayList<>();
        String sql = query.isEmpty() ?
                "SELECT * FROM food_items LIMIT 50" :
                "SELECT * FROM food_items WHERE name LIKE ? LIMIT 50";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (!query.isEmpty()) {
                pstmt.setString(1, "%" + query + "%");
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                foods.add(extractFoodItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Search foods error: " + e.getMessage());
        }
        return foods;
    }

    public static FoodItem getFoodItem(String name) {
        String sql = "SELECT * FROM food_items WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractFoodItemFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get food item error: " + e.getMessage());
        }
        return null;
    }

    public static FoodItem getFoodItemById(int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractFoodItemFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Get food item by ID error: " + e.getMessage());
        }
        return null;
    }

    private static FoodItem extractFoodItemFromResultSet(ResultSet rs) throws SQLException {
        FoodItem food = new FoodItem();
        food.id = rs.getInt("id");
        food.name = rs.getString("name");
        food.foodGroup = rs.getString("food_group");
        food.baseQuantity = rs.getInt("base_quantity");
        food.calories = rs.getInt("calories");
        food.protein_g = rs.getDouble("protein_g");
        food.carbs_g = rs.getDouble("carbs_g");
        food.fats_g = rs.getDouble("fats_g");
        food.water_ml = rs.getInt("water_ml");
        food.calcium_mg = rs.getInt("calcium_mg");
        food.potassium_mg = rs.getInt("potassium_mg");
        food.sodium_mg = rs.getInt("sodium_mg");
        food.magnesium_mg = rs.getInt("magnesium_mg");
        food.iron_mg = rs.getDouble("iron_mg");
        food.zinc_mg = rs.getDouble("zinc_mg");
        food.vitamin_a_iu = rs.getInt("vitamin_a_iu");
        food.vitamin_d_iu = rs.getInt("vitamin_d_iu");
        food.vitamin_e_iu = rs.getDouble("vitamin_e_iu");
        food.vitamin_k_mcg = rs.getDouble("vitamin_k_mcg");
        return food;
    }

    public static boolean logFoodConsumption(int userId, int foodItemId, int quantity, String unit, String mealType) {
        String sql = "INSERT INTO food_logs (user_id, food_item_id, quantity, unit, meal_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, foodItemId);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, unit);
            pstmt.setString(5, mealType);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Log food consumption error: " + e.getMessage());
            return false;
        }
    }

    public static Map<String, Double> getAverageNutrition(int userId, int days) {
        Map<String, Double> averages = new HashMap<>();

        String sql = """
            SELECT 
                AVG(calories) as avg_calories, 
                AVG(protein_g) as avg_protein, 
                AVG(carbs_g) as avg_carbs,
                AVG(fats_g) as avg_fats, 
                AVG(water_ml) as avg_water, 
                AVG(calcium_mg) as avg_calcium,
                AVG(potassium_mg) as avg_potassium, 
                AVG(sodium_mg) as avg_sodium, 
                AVG(magnesium_mg) as avg_magnesium,
                AVG(iron_mg) as avg_iron, 
                AVG(zinc_mg) as avg_zinc, 
                AVG(vitamin_a_iu) as avg_vit_a,
                AVG(vitamin_d_iu) as avg_vit_d, 
                AVG(vitamin_e_iu) as avg_vit_e, 
                AVG(vitamin_k_mcg) as avg_vit_k
            FROM daily_logs 
            WHERE user_id = ? 
            AND log_date >= DATE_SUB(CURRENT_DATE, INTERVAL ? DAY)
            AND log_date <= CURRENT_DATE
        """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, days - 1);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                averages.put("calories", rs.getDouble("avg_calories"));
                averages.put("protein_g", rs.getDouble("avg_protein"));
                averages.put("carbs_g", rs.getDouble("avg_carbs"));
                averages.put("fats_g", rs.getDouble("avg_fats"));
                averages.put("water_ml", rs.getDouble("avg_water"));
                averages.put("calcium_mg", rs.getDouble("avg_calcium"));
                averages.put("potassium_mg", rs.getDouble("avg_potassium"));
                averages.put("sodium_mg", rs.getDouble("avg_sodium"));
                averages.put("magnesium_mg", rs.getDouble("avg_magnesium"));
                averages.put("iron_mg", rs.getDouble("avg_iron"));
                averages.put("zinc_mg", rs.getDouble("avg_zinc"));
                averages.put("vitamin_a_iu", rs.getDouble("avg_vit_a"));
                averages.put("vitamin_d_iu", rs.getDouble("avg_vit_d"));
                averages.put("vitamin_e_iu", rs.getDouble("avg_vit_e"));
                averages.put("vitamin_k_mcg", rs.getDouble("avg_vit_k"));
            }
        } catch (SQLException e) {
            System.err.println("Get average nutrition error: " + e.getMessage());
        }
        return averages;
    }

    // ==================== ADMIN METHODS ====================

    public static List<UserData> getAllUsers() {
        List<UserData> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UserData user = new UserData();
                user.id = rs.getInt("id");
                user.fullName = rs.getString("full_name");
                user.email = rs.getString("email");
                user.age = rs.getInt("age");
                user.gender = rs.getString("gender");
                user.activityLevel = rs.getString("activity_level");
                user.role = rs.getString("role");
                user.createdAt = rs.getTimestamp("created_at");
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Get all users error: " + e.getMessage());
        }
        return users;
    }

    public static boolean deleteUser(int userId, int adminId) {
        String sql = "DELETE FROM users WHERE id = ? AND role != 'admin'";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logAdminAction(adminId, "DELETE", "users", userId, "Deleted user ID: " + userId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Delete user error: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateUser(int userId, String fullName, int age, String gender,
                                     String activityLevel, int adminId) {
        String sql = "UPDATE users SET full_name = ?, age = ?, gender = ?, activity_level = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, activityLevel);
            pstmt.setInt(5, userId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logAdminAction(adminId, "UPDATE", "users", userId,
                        "Updated user: " + fullName);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Update user error: " + e.getMessage());
        }
        return false;
    }

    public static List<FoodItem> getAllFoods() {
        List<FoodItem> foods = new ArrayList<>();
        String sql = "SELECT * FROM food_items ORDER BY name ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                foods.add(extractFoodItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Get all foods error: " + e.getMessage());
        }
        return foods;
    }

    public static boolean addFoodItem(FoodItem food, int adminId) {
        String sql = """
            INSERT INTO food_items (name, food_group, base_quantity, calories, protein_g, 
            carbs_g, fats_g, water_ml, calcium_mg, potassium_mg, sodium_mg, magnesium_mg,
            iron_mg, zinc_mg, vitamin_a_iu, vitamin_d_iu, vitamin_e_iu, vitamin_k_mcg)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, food.name);
            pstmt.setString(2, food.foodGroup);
            pstmt.setInt(3, food.baseQuantity);
            pstmt.setInt(4, food.calories);
            pstmt.setDouble(5, food.protein_g);
            pstmt.setDouble(6, food.carbs_g);
            pstmt.setDouble(7, food.fats_g);
            pstmt.setInt(8, food.water_ml);
            pstmt.setInt(9, food.calcium_mg);
            pstmt.setInt(10, food.potassium_mg);
            pstmt.setInt(11, food.sodium_mg);
            pstmt.setInt(12, food.magnesium_mg);
            pstmt.setDouble(13, food.iron_mg);
            pstmt.setDouble(14, food.zinc_mg);
            pstmt.setInt(15, food.vitamin_a_iu);
            pstmt.setInt(16, food.vitamin_d_iu);
            pstmt.setDouble(17, food.vitamin_e_iu);
            pstmt.setDouble(18, food.vitamin_k_mcg);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int foodId = rs.getInt(1);
                    logAdminAction(adminId, "CREATE", "food_items", foodId,
                            "Added food: " + food.name);
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Add food item error: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateFoodItem(FoodItem food, int adminId) {
        String sql = """
            UPDATE food_items SET name = ?, food_group = ?, base_quantity = ?, calories = ?,
            protein_g = ?, carbs_g = ?, fats_g = ?, water_ml = ?, calcium_mg = ?,
            potassium_mg = ?, sodium_mg = ?, magnesium_mg = ?, iron_mg = ?, zinc_mg = ?,
            vitamin_a_iu = ?, vitamin_d_iu = ?, vitamin_e_iu = ?, vitamin_k_mcg = ?
            WHERE id = ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, food.name);
            pstmt.setString(2, food.foodGroup);
            pstmt.setInt(3, food.baseQuantity);
            pstmt.setInt(4, food.calories);
            pstmt.setDouble(5, food.protein_g);
            pstmt.setDouble(6, food.carbs_g);
            pstmt.setDouble(7, food.fats_g);
            pstmt.setInt(8, food.water_ml);
            pstmt.setInt(9, food.calcium_mg);
            pstmt.setInt(10, food.potassium_mg);
            pstmt.setInt(11, food.sodium_mg);
            pstmt.setInt(12, food.magnesium_mg);
            pstmt.setDouble(13, food.iron_mg);
            pstmt.setDouble(14, food.zinc_mg);
            pstmt.setInt(15, food.vitamin_a_iu);
            pstmt.setInt(16, food.vitamin_d_iu);
            pstmt.setDouble(17, food.vitamin_e_iu);
            pstmt.setDouble(18, food.vitamin_k_mcg);
            pstmt.setInt(19, food.id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logAdminAction(adminId, "UPDATE", "food_items", food.id,
                        "Updated food: " + food.name);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Update food item error: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteFoodItem(int foodId, int adminId) {
        String sql = "DELETE FROM food_items WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, foodId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logAdminAction(adminId, "DELETE", "food_items", foodId,
                        "Deleted food ID: " + foodId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Delete food item error: " + e.getMessage());
        }
        return false;
    }

    private static void logAdminAction(int adminId, String actionType, String targetTable,
                                       Integer targetId, String details) {
        String sql = "INSERT INTO admin_logs (admin_id, action_type, target_table, target_id, action_details) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            pstmt.setString(2, actionType);
            pstmt.setString(3, targetTable);
            if (targetId != null) {
                pstmt.setInt(4, targetId);
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setString(5, details);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Log admin action error: " + e.getMessage());
        }
    }

    public static Map<String, Integer> getSystemStats() {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role='user'");
            if (rs.next()) stats.put("totalUsers", rs.getInt(1));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM food_items");
            if (rs.next()) stats.put("totalFoods", rs.getInt(1));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM food_logs WHERE DATE(consumed_at) = CURRENT_DATE");
            if (rs.next()) stats.put("todayLogs", rs.getInt(1));

            rs = stmt.executeQuery("SELECT COUNT(*) FROM admin_logs WHERE DATE(performed_at) = CURRENT_DATE");
            if (rs.next()) stats.put("todayActions", rs.getInt(1));

        } catch (SQLException e) {
            System.err.println("Get system stats error: " + e.getMessage());
        }
        return stats;
    }

    // Inner classes
    public static class UserData {
        public int id;
        public String fullName;
        public String email;
        public int age;
        public String gender;
        public String activityLevel;
        public String role;
        public Timestamp createdAt;
    }

    public static class NutritionGoals {
        public int userId;
        public int calories;
        public int protein_g;
        public int carbs_g;
        public int fats_g;
        public int water_ml;
        public int calcium_mg;
        public int potassium_mg;
        public int sodium_mg;
        public int magnesium_mg;
        public int iron_mg;
        public int zinc_mg;
        public int vitamin_a_iu;
        public int vitamin_d_iu;
        public int vitamin_e_iu;
        public int vitamin_k_mcg;
    }

    public static class DailyLog {
        public int userId;
        public int calories;
        public int protein_g;
        public int carbs_g;
        public int fats_g;
        public int water_ml;
        public int calcium_mg;
        public int potassium_mg;
        public int sodium_mg;
        public int magnesium_mg;
        public int iron_mg;
        public int zinc_mg;
        public int vitamin_a_iu;
        public int vitamin_d_iu;
        public int vitamin_e_iu;
        public int vitamin_k_mcg;
    }

    public static class NutritionData {
        public int calories;
        public int protein_g;
        public int carbs_g;
        public int fats_g;
        public int water_ml;
        public int calcium_mg;
        public int potassium_mg;
        public int sodium_mg;
        public int magnesium_mg;
        public int iron_mg;
        public int zinc_mg;
        public int vitamin_a_iu;
        public int vitamin_d_iu;
        public int vitamin_e_iu;
        public int vitamin_k_mcg;
    }

    public static class FoodItem {
        public int id;
        public String name;
        public String foodGroup;
        public int baseQuantity;
        public int calories;
        public double protein_g;
        public double carbs_g;
        public double fats_g;
        public int water_ml;
        public int calcium_mg;
        public int potassium_mg;
        public int sodium_mg;
        public int magnesium_mg;
        public double iron_mg;
        public double zinc_mg;
        public int vitamin_a_iu;
        public int vitamin_d_iu;
        public double vitamin_e_iu;
        public double vitamin_k_mcg;

        public NutritionData calculateNutrition(int quantity) {
            double multiplier = (double) quantity / baseQuantity;
            NutritionData nutrition = new NutritionData();
            nutrition.calories = (int) (calories * multiplier);
            nutrition.protein_g = (int) (protein_g * multiplier);
            nutrition.carbs_g = (int) (carbs_g * multiplier);
            nutrition.fats_g = (int) (fats_g * multiplier);
            nutrition.water_ml = (int) (water_ml * multiplier);
            nutrition.calcium_mg = (int) (calcium_mg * multiplier);
            nutrition.potassium_mg = (int) (potassium_mg * multiplier);
            nutrition.sodium_mg = (int) (sodium_mg * multiplier);
            nutrition.magnesium_mg = (int) (magnesium_mg * multiplier);
            nutrition.iron_mg = (int) (iron_mg * multiplier);
            nutrition.zinc_mg = (int) (zinc_mg * multiplier);
            nutrition.vitamin_a_iu = (int) (vitamin_a_iu * multiplier);
            nutrition.vitamin_d_iu = (int) (vitamin_d_iu * multiplier);
            nutrition.vitamin_e_iu = (int) (vitamin_e_iu * multiplier);
            nutrition.vitamin_k_mcg = (int) (vitamin_k_mcg * multiplier);
            return nutrition;
        }
    }
}