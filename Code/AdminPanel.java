import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

/**
 * Admin Panel - Complete CRUD Operations
 * Manage Users and Food Database
 * Version: 1.0
 */
public class AdminPanel extends JFrame {
    private DatabaseHelper.UserData currentAdmin;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JLabel statsUsersLabel, statsFoodsLabel, statsLogsLabel, statsActionsLabel;

    public AdminPanel(DatabaseHelper.UserData admin) {
        this.currentAdmin = admin;

        setTitle("Eco-Eats - Admin Panel");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top Bar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Main content area with CardLayout
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(new Color(245, 245, 245));

        // Add panels
        mainContentPanel.add(createDashboardPanel(), "dashboard");
        mainContentPanel.add(createUserManagementPanel(), "users");
        mainContentPanel.add(createFoodManagementPanel(), "foods");

        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
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
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(180, 60, 60));
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        leftSection.setOpaque(false);

        JLabel logoLabel = new JLabel(loadIcon("src/IMG/admin.png", 32, 32));
        leftSection.add(logoLabel);

        JLabel titleLabel = new JLabel("ADMIN PANEL");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        leftSection.add(titleLabel);

        topBar.add(leftSection, BorderLayout.WEST);

        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightSection.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Admin: " + currentAdmin.fullName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        rightSection.add(welcomeLabel);

        JButton logoutButton = createStyledButton("Logout", new Color(220, 53, 69), null);
        logoutButton.addActionListener(e -> logout());
        rightSection.add(logoutButton);

        topBar.add(rightSection, BorderLayout.EAST);

        return topBar;
    }

    // ==================== SIDEBAR ====================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 58, 64));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        String[] menuItems = { " Dashboard", " User Management", " Food Management" };
        String[] menuIcons = { "src/IMG/dashboard.png", "src/IMG/users.png", "src/IMG/food.png" };
        String[] menuKeys = { "dashboard", "users", "foods" };

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
        button.setBackground(new Color(52, 58, 64));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 30, 10, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(73, 80, 87));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 58, 64));
            }
        });

        button.addActionListener(e -> {
            if ("users".equals(panelKey) || "foods".equals(panelKey) || "dashboard".equals(panelKey)) {
                refreshCurrentPanel(panelKey);
            }
            cardLayout.show(mainContentPanel, panelKey);
        });

        return button;
    }

    // ==================== DASHBOARD PANEL ====================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("System Overview");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(52, 58, 64));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshButton = createStyledButton("Refresh", new Color(33, 150, 243), "src/IMG/refresh.png");
        refreshButton.addActionListener(e -> refreshDashboardStats());
        titlePanel.add(refreshButton, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.NORTH);

        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        statsUsersLabel = new JLabel("0");
        statsFoodsLabel = new JLabel("0");
        statsLogsLabel = new JLabel("0");
        statsActionsLabel = new JLabel("0");

        statsPanel.add(createStatsCard("Total Users", statsUsersLabel, new Color(76, 175, 80), "src/IMG/users.png"));
        statsPanel.add(createStatsCard("Food Items", statsFoodsLabel, new Color(255, 152, 0), "src/IMG/food.png"));
        statsPanel.add(createStatsCard("Today's Logs", statsLogsLabel, new Color(33, 150, 243), "src/IMG/logs.png"));
        statsPanel.add(createStatsCard("Admin Actions Today", statsActionsLabel, new Color(156, 39, 176), "src/IMG/action.png"));

        panel.add(statsPanel, BorderLayout.CENTER);

        refreshDashboardStats();
        return panel;
    }

    private JPanel createStatsCard(String title, JLabel valueLabel, Color color, String iconPath) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 30, 30, 30)));

        JLabel iconLabel = new JLabel(loadIcon(iconPath, 48, 48));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel, BorderLayout.NORTH);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 42));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(valueLabel, BorderLayout.CENTER);

        JLabel titleLabelComponent = new JLabel(title);
        titleLabelComponent.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabelComponent.setForeground(Color.GRAY);
        titleLabelComponent.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabelComponent, BorderLayout.SOUTH);

        return card;
    }

    private void refreshDashboardStats() {
        Map<String, Integer> stats = DatabaseHelper.getSystemStats();
        statsUsersLabel.setText(String.valueOf(stats.getOrDefault("totalUsers", 0)));
        statsFoodsLabel.setText(String.valueOf(stats.getOrDefault("totalFoods", 0)));
        statsLogsLabel.setText(String.valueOf(stats.getOrDefault("todayLogs", 0)));
        statsActionsLabel.setText(String.valueOf(stats.getOrDefault("todayActions", 0)));
    }

    // ==================== USER MANAGEMENT PANEL ====================
    private JTable userTable;
    private DefaultTableModel userTableModel;

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(52, 58, 64));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton refreshButton = createStyledButton("Refresh", new Color(33, 150, 243), "src/IMG/refresh.png");
        refreshButton.addActionListener(e -> loadUsers());
        buttonPanel.add(refreshButton);

        JButton editButton = createStyledButton("Edit Selected", new Color(255, 193, 7), "src/IMG/edit.png");
        editButton.addActionListener(e -> editSelectedUser());
        buttonPanel.add(editButton);

        JButton deleteButton = createStyledButton("Delete Selected", new Color(244, 67, 54), "src/IMG/delete.png");
        deleteButton.addActionListener(e -> deleteSelectedUser());
        buttonPanel.add(deleteButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = { "ID", "Full Name", "Email", "Age", "Gender", "Activity Level", "Role", "Created At" };
        userTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(userTableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 13));
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(new Color(52, 58, 64));
        userTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

        loadUsers();
        return panel;
    }

    private void loadUsers() {
        userTableModel.setRowCount(0);
        List<DatabaseHelper.UserData> users = DatabaseHelper.getAllUsers();
        for (DatabaseHelper.UserData user : users) {
            userTableModel.addRow(new Object[] {
                    user.id,
                    user.fullName,
                    user.email,
                    user.age,
                    user.gender,
                    user.activityLevel,
                    user.role,
                    user.createdAt.toString().substring(0, 19)
            });
        }
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a user to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
        String role = (String) userTableModel.getValueAt(selectedRow, 6);

        if ("admin".equals(role)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot edit admin users",
                    "Edit Restricted",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fullName = (String) userTableModel.getValueAt(selectedRow, 1);
        int age = (int) userTableModel.getValueAt(selectedRow, 3);
        String gender = (String) userTableModel.getValueAt(selectedRow, 4);
        String activity = (String) userTableModel.getValueAt(selectedRow, 5);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField nameField = new JTextField(fullName);
        JTextField ageField = new JTextField(String.valueOf(age));
        JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        genderCombo.setSelectedItem(gender);
        JComboBox<String> activityCombo = new JComboBox<>(new String[] {
                "Sedentary", "Lightly Active", "Moderately Active", "Very Active", "Extremely Active"
        });
        activityCombo.setSelectedItem(activity);

        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo);
        panel.add(new JLabel("Activity Level:"));
        panel.add(activityCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = nameField.getText().trim();
                int newAge = Integer.parseInt(ageField.getText().trim());
                String newGender = (String) genderCombo.getSelectedItem();
                String newActivity = (String) activityCombo.getSelectedItem();

                if (DatabaseHelper.updateUser(userId, newName, newAge, newGender, newActivity, currentAdmin.id)) {
                    JOptionPane.showMessageDialog(this,
                            "User updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to update user",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid age",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a user to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
        String role = (String) userTableModel.getValueAt(selectedRow, 6);
        String fullName = (String) userTableModel.getValueAt(selectedRow, 1);

        if ("admin".equals(role)) {
            JOptionPane.showMessageDialog(this,
                    "Cannot delete admin users",
                    "Delete Restricted",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete user: " + fullName + "?\nThis will also delete all their data.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.deleteUser(userId, currentAdmin.id)) {
                JOptionPane.showMessageDialog(this,
                        "User deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
                refreshDashboardStats();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete user",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== FOOD MANAGEMENT PANEL ====================
    private JTable foodTable;
    private DefaultTableModel foodTableModel;

    private JPanel createFoodManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Food Database Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(52, 58, 64));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton addButton = createStyledButton("Add Food", new Color(76, 175, 80), "src/IMG/addfood.png");
        addButton.addActionListener(e -> addNewFood());
        buttonPanel.add(addButton);

        JButton refreshButton = createStyledButton("Refresh", new Color(33, 150, 243), "src/IMG/refresh.png");
        refreshButton.addActionListener(e -> loadFoods());
        buttonPanel.add(refreshButton);

        JButton editButton = createStyledButton("Edit Selected", new Color(255, 193, 7), "src/IMG/edit.png");
        editButton.addActionListener(e -> editSelectedFood());
        buttonPanel.add(editButton);

        JButton deleteButton = createStyledButton("Delete Selected", new Color(244, 67, 54), "src/IMG/delete.png");
        deleteButton.addActionListener(e -> deleteSelectedFood());
        buttonPanel.add(deleteButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = { "ID", "Name", "Group", "Base(g)", "Cal", "Protein", "Carbs", "Fats", "Calcium",
                "Iron" };
        foodTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        foodTable = new JTable(foodTableModel);
        foodTable.setFont(new Font("Arial", Font.PLAIN, 12));
        foodTable.setRowHeight(25);
        foodTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        foodTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        foodTable.getTableHeader().setBackground(new Color(52, 58, 64));
        foodTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(foodTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

        loadFoods();
        return panel;
    }

    private void loadFoods() {
        foodTableModel.setRowCount(0);
        List<DatabaseHelper.FoodItem> foods = DatabaseHelper.getAllFoods();
        for (DatabaseHelper.FoodItem food : foods) {
            foodTableModel.addRow(new Object[] {
                    food.id,
                    food.name,
                    food.foodGroup,
                    food.baseQuantity,
                    food.calories,
                    String.format("%.1fg", food.protein_g),
                    String.format("%.1fg", food.carbs_g),
                    String.format("%.1fg", food.fats_g),
                    food.calcium_mg + "mg",
                    String.format("%.1fmg", food.iron_mg)
            });
        }
    }

    private void addNewFood() {
        FoodEditorDialog dialog = new FoodEditorDialog(this, null, currentAdmin.id);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadFoods();
            refreshDashboardStats();
        }
    }

    private void editSelectedFood() {
        int selectedRow = foodTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a food item to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int foodId = (int) foodTableModel.getValueAt(selectedRow, 0);
        DatabaseHelper.FoodItem food = DatabaseHelper.getFoodItemById(foodId);

        if (food != null) {
            FoodEditorDialog dialog = new FoodEditorDialog(this, food, currentAdmin.id);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadFoods();
            }
        }
    }

    private void deleteSelectedFood() {
        int selectedRow = foodTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a food item to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int foodId = (int) foodTableModel.getValueAt(selectedRow, 0);
        String foodName = (String) foodTableModel.getValueAt(selectedRow, 1);

        int choice = JOptionPane.showConfirmDialog(this,
                "Delete food: " + foodName + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.deleteFoodItem(foodId, currentAdmin.id)) {
                JOptionPane.showMessageDialog(this,
                        "Food item deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadFoods();
                refreshDashboardStats();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete food item",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== UTILITY METHODS ====================

    private void refreshCurrentPanel(String panelKey) {
        switch (panelKey) {
            case "dashboard":
                refreshDashboardStats();
                break;
            case "users":
                loadUsers();
                break;
            case "foods":
                loadFoods();
                break;
        }
    }

    private JButton createStyledButton(String text, Color bgColor, String iconPath) {
        JButton button = new JButton(text);
        if (iconPath != null) {
            button.setIcon(loadIcon(iconPath, 16, 16));
        }
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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
}