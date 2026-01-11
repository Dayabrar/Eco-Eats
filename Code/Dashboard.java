import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Eco-Eats Dashboard - Complete Fixed Version
 * Version: 2.2 - PNG Icons
 */
public class Dashboard extends JFrame {
    private DatabaseHelper.UserData currentUser;
    private DatabaseHelper.NutritionGoals userGoals;
    private DatabaseHelper.DailyLog todayLog;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private MealLogPanel mealLogPanel;
    private ProgressPanel progressPanel;

    public Dashboard(DatabaseHelper.UserData user) {
        this.currentUser = user;
        loadUserData();

        setTitle("Eco-Eats - Dashboard");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top Navigation Bar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Main content area with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(245, 245, 245));

        // Add different panels
        mainContentPanel.add(createDashboardPanel(), "dashboard");
        mainContentPanel.add(createNutritionPanel(), "nutrition");

        // Use new panels
        mealLogPanel = new MealLogPanel(currentUser.id);
        mainContentPanel.add(mealLogPanel, "meals");

        progressPanel = new ProgressPanel(currentUser.id);
        mainContentPanel.add(progressPanel, "progress");

        mainContentPanel.add(createGoalsPanel(), "goals");
        mainContentPanel.add(new EditableProfilePanel(currentUser), "profile");

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void loadUserData() {
        userGoals = DatabaseHelper.getUserGoals(currentUser.id);
        if (userGoals == null) {
            userGoals = new DatabaseHelper.NutritionGoals();
            userGoals.userId = currentUser.id;
        }
        todayLog = DatabaseHelper.getTodayLog(currentUser.id);
    }

    // ==================== ICON LOADING METHOD ====================
    private ImageIcon loadIcon(String iconName, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(iconName);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + iconName);
            return null;
        }
    }

    // ==================== TOP BAR ====================
    private JPanel createTopBar() {
        JPanel topBar = new JPanel();
        topBar.setLayout(new BorderLayout());
        topBar.setBackground(new Color(123, 141, 74));
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Logo and title
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftSection.setOpaque(false);

        JLabel logoLabel = new JLabel(loadIcon("leaf.png", 32, 32));
        leftSection.add(logoLabel);

        JLabel titleLabel = new JLabel("ECO-EATS");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        leftSection.add(titleLabel);

        // Date
        JLabel dateLabel = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        leftSection.add(dateLabel);

        topBar.add(leftSection, BorderLayout.WEST);

        // Right section - User info
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightSection.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.fullName + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        rightSection.add(welcomeLabel);

        // Export button
        JButton exportButton = createStyledButton("Export Report", new Color(76, 175, 80), "src/IMG/export.png");
        exportButton.addActionListener(e -> showExportDialog());
        rightSection.add(exportButton);

        JButton logoutButton = createStyledButton("Logout", new Color(180, 60, 60), "src/IMG/logout.png");
        logoutButton.addActionListener(e -> logout());
        rightSection.add(logoutButton);

        topBar.add(rightSection, BorderLayout.EAST);

        return topBar;
    }

    // ==================== SIDEBAR ====================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 60, 35));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        String[] menuItems = {" Dashboard", " Nutrition", " Meal Log", " Progress", " Goals", " Profile"};
        String[] menuIcons = {"src/IMG/dashboard.png", "src/IMG/nutrition.png", "src/IMG/meallog.png", "src/IMG/progress.png", "src/IMG/goals.png", "src/IMG/profile.png"};
        String[] menuKeys = {"dashboard", "nutrition", "meals", "progress", "goals", "profile"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], menuIcons[i], menuKeys[i]);
            sidebar.add(menuButton);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createMenuButton(String text, String iconPath, String panelKey) {
        JButton button = new JButton(text);
        button.setIcon(loadIcon(iconPath, 24, 24));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 60, 35));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 30, 10, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(123, 141, 74));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(45, 60, 35));
            }
        });

        button.addActionListener(e -> {
            if ("nutrition".equals(panelKey)) {
                refreshNutritionData();
            } else if ("meals".equals(panelKey)) {
                if (mealLogPanel != null) {
                    mealLogPanel.refresh();
                }
            }
            cardLayout.show(mainContentPanel, panelKey);
        });

        return button;
    }

    // ==================== DASHBOARD PANEL ====================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Today's Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(45, 60, 35));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setOpaque(false);

        // Calorie Card
        contentPanel.add(createCalorieCard());

        // Macros Card
        contentPanel.add(createMacrosCard());

        // Water & Minerals Card
        contentPanel.add(createWaterMineralsCard());

        // Vitamins Card
        contentPanel.add(createVitaminsCard());

        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCalorieCard() {
        JPanel card = createCard("Calories", "src/IMG/calories.png");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        int consumed = todayLog.calories;
        int goal = userGoals.calories;
        int percentage = goal > 0 ? (consumed * 100) / goal : 0;

        JLabel consumedLabel = new JLabel(consumed + " / " + goal + " kcal");
        consumedLabel.setFont(new Font("Arial", Font.BOLD, 28));
        consumedLabel.setForeground(new Color(123, 141, 74));
        consumedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(consumedLabel);

        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Progress bar
        JProgressBar progressBar = new JProgressBar(0, goal);
        progressBar.setValue(consumed);
        progressBar.setStringPainted(true);
        progressBar.setString(percentage + "%");
        progressBar.setPreferredSize(new Dimension(0, 30));
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        progressBar.setForeground(new Color(123, 141, 74));
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(progressBar);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel remainingLabel = new JLabel((goal - consumed) + " kcal remaining");
        remainingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        remainingLabel.setForeground(Color.GRAY);
        remainingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(remainingLabel);

        return card;
    }

    private JPanel createMacrosCard() {
        JPanel card = createCard("Macronutrients", "src/IMG/nutrition.png");
        card.setLayout(new GridLayout(3, 1, 10, 10));

        card.add(createMacroRow("Protein", todayLog.protein_g, userGoals.protein_g, "g", new Color(76, 175, 80)));
        card.add(createMacroRow("Carbs", todayLog.carbs_g, userGoals.carbs_g, "g", new Color(255, 193, 7)));
        card.add(createMacroRow("Fats", todayLog.fats_g, userGoals.fats_g, "g", new Color(244, 67, 54)));

        return card;
    }

    private JPanel createWaterMineralsCard() {
        JPanel card = createCard("Water & Minerals", "src/IMG/water.png");
        card.setLayout(new GridLayout(4, 1, 8, 8));

        card.add(createMineralRow("Water", todayLog.water_ml, userGoals.water_ml, "ml", new Color(33, 150, 243)));
        card.add(createMineralRow("Calcium", todayLog.calcium_mg, userGoals.calcium_mg, "mg", new Color(156, 39, 176)));
        card.add(createMineralRow("Potassium", todayLog.potassium_mg, userGoals.potassium_mg, "mg", new Color(255, 152, 0)));
        card.add(createMineralRow("Sodium", todayLog.sodium_mg, userGoals.sodium_mg, "mg", new Color(121, 85, 72)));

        return card;
    }

    private JPanel createVitaminsCard() {
        JPanel card = createCard("Vitamins", "src/IMG/vitamins.png");
        card.setLayout(new GridLayout(4, 1, 8, 8));

        card.add(createMineralRow("Vitamin A", todayLog.vitamin_a_iu, userGoals.vitamin_a_iu, "IU", new Color(233, 30, 99)));
        card.add(createMineralRow("Vitamin D", todayLog.vitamin_d_iu, userGoals.vitamin_d_iu, "IU", new Color(103, 58, 183)));
        card.add(createMineralRow("Vitamin E", todayLog.vitamin_e_iu, userGoals.vitamin_e_iu, "IU", new Color(0, 188, 212)));
        card.add(createMineralRow("Vitamin K", todayLog.vitamin_k_mcg, userGoals.vitamin_k_mcg, "mcg", new Color(205, 220, 57)));

        return card;
    }

    // ==================== NUTRITION PANEL ====================
    private JPanel createNutritionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title and buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Detailed Nutrition Tracking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 60, 35));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton addWaterButton = createStyledButton("Add Water", new Color(33, 150, 243), "src/IMG/addwater.png");
        addWaterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddWaterDialog();
            }
        });
        buttonPanel.add(addWaterButton);

        JButton addFoodButton = createStyledButton("Add Food", new Color(123, 141, 74), "src/IMG/addfood.png");
        addFoodButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddFoodDialog();
            }
        });
        buttonPanel.add(addFoodButton);

        JButton resetButton = createStyledButton("Reset Day", new Color(244, 67, 54), "src/IMG/reset.png");
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetDailyLog();
            }
        });
        buttonPanel.add(resetButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Nutrition cards
        JPanel nutritionPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        nutritionPanel.setOpaque(false);

        nutritionPanel.add(createDetailedMacroCard());
        nutritionPanel.add(createDetailedMineralsCard());
        nutritionPanel.add(createDetailedVitaminsCard());
        nutritionPanel.add(createWaterCard());

        JScrollPane scrollPane = new JScrollPane(nutritionPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailedMacroCard() {
        JPanel card = createCard("Macronutrients", "src/IMG/nutrition.png");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        String[][] macros = {
                {"Protein", todayLog.protein_g + "g", userGoals.protein_g + "g", String.valueOf(todayLog.protein_g), String.valueOf(userGoals.protein_g)},
                {"Carbohydrates", todayLog.carbs_g + "g", userGoals.carbs_g + "g", String.valueOf(todayLog.carbs_g), String.valueOf(userGoals.carbs_g)},
                {"Fats", todayLog.fats_g + "g", userGoals.fats_g + "g", String.valueOf(todayLog.fats_g), String.valueOf(userGoals.fats_g)}
        };

        Color[] colors = {new Color(76, 175, 80), new Color(255, 193, 7), new Color(244, 67, 54)};

        for (int i = 0; i < macros.length; i++) {
            card.add(createDetailedNutrientRow(macros[i][0], macros[i][1], macros[i][2],
                    Integer.parseInt(macros[i][3]), Integer.parseInt(macros[i][4]), colors[i]));
            if (i < macros.length - 1) {
                card.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        return card;
    }

    private JPanel createDetailedMineralsCard() {
        JPanel card = createCard("Minerals", "src/IMG/minerals.png");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        String[][] minerals = {
                {"Calcium", todayLog.calcium_mg + "mg", userGoals.calcium_mg + "mg", String.valueOf(todayLog.calcium_mg), String.valueOf(userGoals.calcium_mg)},
                {"Potassium", todayLog.potassium_mg + "mg", userGoals.potassium_mg + "mg", String.valueOf(todayLog.potassium_mg), String.valueOf(userGoals.potassium_mg)},
                {"Sodium", todayLog.sodium_mg + "mg", userGoals.sodium_mg + "mg", String.valueOf(todayLog.sodium_mg), String.valueOf(userGoals.sodium_mg)},
                {"Magnesium", todayLog.magnesium_mg + "mg", userGoals.magnesium_mg + "mg", String.valueOf(todayLog.magnesium_mg), String.valueOf(userGoals.magnesium_mg)},
                {"Iron", todayLog.iron_mg + "mg", userGoals.iron_mg + "mg", String.valueOf(todayLog.iron_mg), String.valueOf(userGoals.iron_mg)},
                {"Zinc", todayLog.zinc_mg + "mg", userGoals.zinc_mg + "mg", String.valueOf(todayLog.zinc_mg), String.valueOf(userGoals.zinc_mg)}
        };

        Color[] colors = {
                new Color(156, 39, 176), new Color(255, 152, 0), new Color(121, 85, 72),
                new Color(0, 150, 136), new Color(183, 28, 28), new Color(57, 73, 171)
        };

        for (int i = 0; i < minerals.length; i++) {
            card.add(createDetailedNutrientRow(minerals[i][0], minerals[i][1], minerals[i][2],
                    Integer.parseInt(minerals[i][3]), Integer.parseInt(minerals[i][4]), colors[i]));
            if (i < minerals.length - 1) {
                card.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        return card;
    }

    private JPanel createDetailedVitaminsCard() {
        JPanel card = createCard("Vitamins", "src/IMG/vitamins.png");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        String[][] vitamins = {
                {"Vitamin A", todayLog.vitamin_a_iu + "IU", userGoals.vitamin_a_iu + "IU", String.valueOf(todayLog.vitamin_a_iu), String.valueOf(userGoals.vitamin_a_iu)},
                {"Vitamin D", todayLog.vitamin_d_iu + "IU", userGoals.vitamin_d_iu + "IU", String.valueOf(todayLog.vitamin_d_iu), String.valueOf(userGoals.vitamin_d_iu)},
                {"Vitamin E", todayLog.vitamin_e_iu + "IU", userGoals.vitamin_e_iu + "IU", String.valueOf(todayLog.vitamin_e_iu), String.valueOf(userGoals.vitamin_e_iu)},
                {"Vitamin K", todayLog.vitamin_k_mcg + "mcg", userGoals.vitamin_k_mcg + "mcg", String.valueOf(todayLog.vitamin_k_mcg), String.valueOf(userGoals.vitamin_k_mcg)}
        };

        Color[] colors = {
                new Color(233, 30, 99), new Color(103, 58, 183),
                new Color(0, 188, 212), new Color(205, 220, 57)
        };

        for (int i = 0; i < vitamins.length; i++) {
            card.add(createDetailedNutrientRow(vitamins[i][0], vitamins[i][1], vitamins[i][2],
                    Integer.parseInt(vitamins[i][3]), Integer.parseInt(vitamins[i][4]), colors[i]));
            if (i < vitamins.length - 1) {
                card.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        return card;
    }

    private JPanel createWaterCard() {
        JPanel card = createCard("Water Intake", "src/IMG/water.png");
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        int consumed = todayLog.water_ml;
        int goal = userGoals.water_ml;
        int percentage = goal > 0 ? (consumed * 100) / goal : 0;

        JLabel amountLabel = new JLabel(consumed + "ml / " + goal + "ml");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 24));
        amountLabel.setForeground(new Color(33, 150, 243));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(amountLabel);

        card.add(Box.createRigidArea(new Dimension(0, 20)));

        JProgressBar bar = new JProgressBar(0, goal);
        bar.setValue(consumed);
        bar.setStringPainted(true);
        bar.setString(percentage + "%");
        bar.setPreferredSize(new Dimension(0, 35));
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        bar.setForeground(new Color(33, 150, 243));
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(bar);

        return card;
    }

    // ==================== GOALS PANEL ====================
    private JPanel createGoalsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Nutrition Goals");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(45, 60, 35));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JButton editButton = createStyledButton("Edit Goals", new Color(123, 141, 74), "src/IMG/edit.png");
        editButton.addActionListener(e -> {
            new SetGoalsDialog(this, currentUser.id, userGoals);
            refreshNutritionData();
        });
        titlePanel.add(editButton, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Display current goals
        JPanel goalsContent = new JPanel();
        goalsContent.setLayout(new BoxLayout(goalsContent, BoxLayout.Y_AXIS));
        goalsContent.setBackground(Color.WHITE);
        goalsContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[][] goalsData = {
                {"Calories", userGoals.calories + " kcal"},
                {"Protein", userGoals.protein_g + " g"},
                {"Carbohydrates", userGoals.carbs_g + " g"},
                {"Fats", userGoals.fats_g + " g"},
                {"Water", userGoals.water_ml + " ml"},
                {"Calcium", userGoals.calcium_mg + " mg"},
                {"Potassium", userGoals.potassium_mg + " mg"},
                {"Sodium", userGoals.sodium_mg + " mg"},
                {"Magnesium", userGoals.magnesium_mg + " mg"},
                {"Iron", userGoals.iron_mg + " mg"},
                {"Zinc", userGoals.zinc_mg + " mg"},
                {"Vitamin A", userGoals.vitamin_a_iu + " IU"},
                {"Vitamin D", userGoals.vitamin_d_iu + " IU"},
                {"Vitamin E", userGoals.vitamin_e_iu + " IU"},
                {"Vitamin K", userGoals.vitamin_k_mcg + " mcg"}
        };

        for (String[] goal : goalsData) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(Color.WHITE);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            row.setBorder(new EmptyBorder(5, 10, 5, 10));

            JLabel nameLabel = new JLabel(goal[0]);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            row.add(nameLabel, BorderLayout.WEST);

            JLabel valueLabel = new JLabel(goal[1]);
            valueLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            valueLabel.setForeground(new Color(123, 141, 74));
            row.add(valueLabel, BorderLayout.EAST);

            goalsContent.add(row);
        }

        JScrollPane scrollPane = new JScrollPane(goalsContent);
        scrollPane.setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ==================== UTILITY METHODS ====================

    private JPanel createCard(String title, String iconPath) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setIcon(loadIcon(iconPath, 20, 20));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(45, 60, 35));

        return card;
    }

    private JButton createStyledButton(String text, Color bgColor, String iconPath) {
        JButton button = new JButton(text);
        if (iconPath != null) {
            button.setIcon(loadIcon(iconPath, 16, 16));
        }
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createMacroRow(String name, int consumed, int goal, String unit, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setPreferredSize(new Dimension(80, 20));
        panel.add(nameLabel, BorderLayout.WEST);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);

        JProgressBar bar = new JProgressBar(0, goal);
        bar.setValue(Math.min(consumed, goal));
        bar.setStringPainted(true);
        bar.setString(consumed + unit + " / " + goal + unit);
        bar.setPreferredSize(new Dimension(0, 25));
        bar.setForeground(color);
        bar.setBackground(new Color(220, 220, 220));
        progressPanel.add(bar, BorderLayout.CENTER);

        panel.add(progressPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMineralRow(String name, int consumed, int goal, String unit, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setPreferredSize(new Dimension(100, 20));
        panel.add(nameLabel, BorderLayout.WEST);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);

        JProgressBar bar = new JProgressBar(0, goal);
        bar.setValue(Math.min(consumed, goal));
        bar.setStringPainted(true);
        bar.setString(consumed + unit + " / " + goal + unit);
        bar.setPreferredSize(new Dimension(0, 20));
        bar.setForeground(color);
        bar.setBackground(new Color(220, 220, 220));
        progressPanel.add(bar, BorderLayout.CENTER);

        panel.add(progressPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailedNutrientRow(String name, String consumed, String goal,
                                             int consumedInt, int goalInt, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setPreferredSize(new Dimension(120, 25));
        panel.add(nameLabel, BorderLayout.WEST);

        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false);

        JProgressBar bar = new JProgressBar(0, goalInt);
        bar.setValue(Math.min(consumedInt, goalInt));
        bar.setStringPainted(true);
        bar.setString(consumed + " / " + goal);
        bar.setPreferredSize(new Dimension(0, 25));
        bar.setForeground(color);
        bar.setBackground(new Color(220, 220, 220));
        progressPanel.add(bar, BorderLayout.CENTER);

        panel.add(progressPanel, BorderLayout.CENTER);

        return panel;
    }

    // ==================== ACTION METHODS ====================

    private void showAddWaterDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Water Amount (ml):"));
        JTextField waterField = new JTextField("250");
        panel.add(waterField);

        panel.add(new JLabel("Common amounts:"));
        JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] amounts = {"250ml (1 glass)", "500ml (1 bottle)", "1000ml (1 liter)"};
        for (String amount : amounts) {
            JButton quickBtn = new JButton(amount);
            quickBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    waterField.setText(amount.split("ml")[0]);
                }
            });
            quickPanel.add(quickBtn);
        }
        panel.add(quickPanel);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Water Intake", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int waterAmount = Integer.parseInt(waterField.getText().trim());
                if (waterAmount <= 0 || waterAmount > 5000) {
                    JOptionPane.showMessageDialog(this,
                            "Please enter a valid amount between 1 and 5000 ml",
                            "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DatabaseHelper.NutritionData nutrition = new DatabaseHelper.NutritionData();
                nutrition.water_ml = waterAmount;

                boolean success = DatabaseHelper.addNutritionData(currentUser.id, nutrition);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            waterAmount + "ml of water added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshNutritionData();
                    if (mealLogPanel != null) {
                        mealLogPanel.refresh();
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to add water. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid number",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddFoodDialog() {
        AddFoodDialog dialog = new AddFoodDialog(this, currentUser.id);
        // Refresh data after dialog closes
        refreshNutritionData();
        if (mealLogPanel != null) {
            mealLogPanel.refresh();
        }
    }

    private void resetDailyLog() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset today's nutrition log?\nThis will delete all food entries for today.",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            boolean success = DatabaseHelper.resetDailyLog(currentUser.id);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Today's log has been reset successfully!",
                        "Reset Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshNutritionData();
                if (mealLogPanel != null) {
                    mealLogPanel.refresh();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to reset log. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshNutritionData() {
        loadUserData();

        // Recreate panels
        mainContentPanel.removeAll();
        mainContentPanel.add(createDashboardPanel(), "dashboard");
        mainContentPanel.add(createNutritionPanel(), "nutrition");

        mealLogPanel = new MealLogPanel(currentUser.id);
        mainContentPanel.add(mealLogPanel, "meals");

        if (progressPanel != null) {
            progressPanel = new ProgressPanel(currentUser.id);
        }
        mainContentPanel.add(progressPanel, "progress");

        mainContentPanel.add(createGoalsPanel(), "goals");
        mainContentPanel.add(new EditableProfilePanel(currentUser), "profile");

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginPage();
        }
    }

    private void showExportDialog() {
        String[] options = {"7 Days", "30 Days", "90 Days"};
        String selection = (String) JOptionPane.showInputDialog(this,
                "Select report period:",
                "Export Nutrition Report",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (selection != null) {
            int days = 7;
            if (selection.equals("30 Days")) {
                days = 30;
            } else if (selection.equals("90 Days")) {
                days = 90;
            }

            boolean success = PDFExportService.exportNutritionReport(this, currentUser.id, days);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Report exported successfully!",
                        "Export Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to export report. Please try again.",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}