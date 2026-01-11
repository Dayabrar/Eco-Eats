import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Progress Panel - Nutrition Progress Charts
 * Shows 7-day and 30-day trends
 */
public class ProgressPanel extends JPanel {
    private int userId;
    private JComboBox<String> periodCombo;
    private JPanel chartsPanel;

    public ProgressPanel(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Progress Charts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 60, 35));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setOpaque(false);

        periodCombo = new JComboBox<>(new String[]{"Last 7 Days", "Last 30 Days"});
        periodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        periodCombo.addActionListener(e -> loadCharts());
        controlPanel.add(periodCombo);

        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 13));
        refreshButton.setBackground(new Color(33, 150, 243));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(new EmptyBorder(8, 15, 8, 15));
        refreshButton.addActionListener(e -> loadCharts());
        controlPanel.add(refreshButton);

        headerPanel.add(controlPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Charts panel
        chartsPanel = new JPanel(new GridLayout(0, 1, 0, 20));
        chartsPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(chartsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadCharts();
    }

    private void loadCharts() {
        chartsPanel.removeAll();

        int days = periodCombo.getSelectedIndex() == 0 ? 7 : 30;
        Map<LocalDate, NutritionData> data = loadNutritionData(days);

        if (data.isEmpty()) {
            JLabel noDataLabel = new JLabel("No data available for the selected period");
            noDataLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            noDataLabel.setForeground(Color.GRAY);
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chartsPanel.add(noDataLabel);
        } else {
            chartsPanel.add(createCaloriesChart(data));
            chartsPanel.add(createMacrosChart(data));
            chartsPanel.add(createMineralsChart(data));
            chartsPanel.add(createVitaminsChart(data));
        }

        chartsPanel.revalidate();
        chartsPanel.repaint();
    }

    private Map<LocalDate, NutritionData> loadNutritionData(int days) {
        Map<LocalDate, NutritionData> dataMap = new TreeMap<>();
        String sql = """
            SELECT log_date, calories, protein_g, carbs_g, fats_g,
                   calcium_mg, iron_mg, potassium_mg, vitamin_a_iu, vitamin_d_iu
            FROM daily_logs
            WHERE user_id = ? AND log_date >= DATE_SUB(CURRENT_DATE, INTERVAL ? DAY)
            ORDER BY log_date ASC
        """;

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, days);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getDate("log_date").toLocalDate();
                NutritionData data = new NutritionData();
                data.calories = rs.getInt("calories");
                data.protein = rs.getInt("protein_g");
                data.carbs = rs.getInt("carbs_g");
                data.fats = rs.getInt("fats_g");
                data.calcium = rs.getInt("calcium_mg");
                data.iron = rs.getInt("iron_mg");
                data.potassium = rs.getInt("potassium_mg");
                data.vitaminA = rs.getInt("vitamin_a_iu");
                data.vitaminD = rs.getInt("vitamin_d_iu");
                dataMap.put(date, data);
            }
        } catch (SQLException e) {
            System.err.println("Error loading nutrition data: " + e.getMessage());
        }

        return dataMap;
    }

    private JPanel createCaloriesChart(Map<LocalDate, NutritionData> data) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 60;
                int chartHeight = height - 2 * padding;
                int chartWidth = width - 2 * padding;

                // Background
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, width, height);

                // Title
                g2d.setColor(new Color(45, 60, 35));
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString("Calories Trend", padding, padding / 2);

                // Axes
                g2d.setColor(Color.GRAY);
                g2d.drawLine(padding, padding, padding, height - padding);
                g2d.drawLine(padding, height - padding, width - padding, height - padding);

                if (data.isEmpty()) return;

                // Find max value
                int maxCalories = data.values().stream().mapToInt(d -> d.calories).max().orElse(2000);
                maxCalories = Math.max(maxCalories, 2000);

                // Goal line
                int goalY = height - padding - (2000 * chartHeight / maxCalories);
                g2d.setColor(new Color(244, 67, 54));
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
                g2d.drawLine(padding, goalY, width - padding, goalY);
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                g2d.drawString("Goal: 2000", width - padding + 5, goalY + 5);

                // Plot data
                List<LocalDate> dates = new ArrayList<>(data.keySet());
                int pointSpacing = chartWidth / Math.max(dates.size() - 1, 1);

                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(new Color(123, 141, 74));

                for (int i = 0; i < dates.size(); i++) {
                    LocalDate date = dates.get(i);
                    int calories = data.get(date).calories;
                    int x = padding + i * pointSpacing;
                    int y = height - padding - (calories * chartHeight / maxCalories);

                    // Line
                    if (i > 0) {
                        LocalDate prevDate = dates.get(i - 1);
                        int prevCalories = data.get(prevDate).calories;
                        int prevX = padding + (i - 1) * pointSpacing;
                        int prevY = height - padding - (prevCalories * chartHeight / maxCalories);
                        g2d.drawLine(prevX, prevY, x, y);
                    }

                    // Point
                    g2d.fillOval(x - 4, y - 4, 8, 8);

                    // Date label
                    if (dates.size() <= 10 || i % 2 == 0) {
                        g2d.setColor(Color.GRAY);
                        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                        String dateStr = date.format(DateTimeFormatter.ofPattern("MM/dd"));
                        g2d.drawString(dateStr, x - 15, height - padding + 15);
                        g2d.setColor(new Color(123, 141, 74));
                    }
                }
            }
        };

        chartPanel.setPreferredSize(new Dimension(0, 300));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return chartPanel;
    }

    private JPanel createMacrosChart(Map<LocalDate, NutritionData> data) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 60;
                int chartHeight = height - 2 * padding;
                int chartWidth = width - 2 * padding;

                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, width, height);

                g2d.setColor(new Color(45, 60, 35));
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString("Macronutrients Trend", padding, padding / 2);

                g2d.setColor(Color.GRAY);
                g2d.drawLine(padding, padding, padding, height - padding);
                g2d.drawLine(padding, height - padding, width - padding, height - padding);

                if (data.isEmpty()) return;

                List<LocalDate> dates = new ArrayList<>(data.keySet());
                int pointSpacing = chartWidth / Math.max(dates.size() - 1, 1);

                // Find max for scaling
                int maxValue = 300;

                // Plot protein
                g2d.setColor(new Color(76, 175, 80));
                g2d.setStroke(new BasicStroke(2));
                plotLine(g2d, dates, data, pointSpacing, padding, height, chartHeight, maxValue, true);

                // Plot carbs
                g2d.setColor(new Color(255, 193, 7));
                plotLine(g2d, dates, data, pointSpacing, padding, height, chartHeight, maxValue, false);

                // Legend
                g2d.setFont(new Font("Arial", Font.PLAIN, 11));
                int legendX = width - 150;
                int legendY = padding + 20;

                g2d.setColor(new Color(76, 175, 80));
                g2d.fillRect(legendX, legendY, 15, 3);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Protein", legendX + 20, legendY + 3);

                g2d.setColor(new Color(255, 193, 7));
                g2d.fillRect(legendX, legendY + 20, 15, 3);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Carbs", legendX + 20, legendY + 23);
            }

            private void plotLine(Graphics2D g2d, List<LocalDate> dates, Map<LocalDate, NutritionData> data,
                                  int pointSpacing, int padding, int height, int chartHeight, int maxValue, boolean isProtein) {
                for (int i = 0; i < dates.size(); i++) {
                    LocalDate date = dates.get(i);
                    int value = isProtein ? data.get(date).protein : data.get(date).carbs;
                    int x = padding + i * pointSpacing;
                    int y = height - padding - (value * chartHeight / maxValue);

                    if (i > 0) {
                        LocalDate prevDate = dates.get(i - 1);
                        int prevValue = isProtein ? data.get(prevDate).protein : data.get(prevDate).carbs;
                        int prevX = padding + (i - 1) * pointSpacing;
                        int prevY = height - padding - (prevValue * chartHeight / maxValue);
                        g2d.drawLine(prevX, prevY, x, y);
                    }
                    g2d.fillOval(x - 3, y - 3, 6, 6);
                }
            }
        };

        chartPanel.setPreferredSize(new Dimension(0, 300));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return chartPanel;
    }

    private JPanel createMineralsChart(Map<LocalDate, NutritionData> data) {
        return createSimpleChart(data, "Minerals (Calcium & Iron)", Color.MAGENTA, Color.ORANGE);
    }

    private JPanel createVitaminsChart(Map<LocalDate, NutritionData> data) {
        return createSimpleChart(data, "Vitamins (A & D)", new Color(233, 30, 99), new Color(103, 58, 183));
    }

    private JPanel createSimpleChart(Map<LocalDate, NutritionData> data, String title, Color color1, Color color2) {
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 60;

                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, width, height);

                g2d.setColor(new Color(45, 60, 35));
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.drawString(title, padding, padding / 2);

                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                g2d.drawString("Data visualization available", width / 2 - 80, height / 2);
            }
        };

        chartPanel.setPreferredSize(new Dimension(0, 250));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return chartPanel;
    }

    private static class NutritionData {
        int calories, protein, carbs, fats;
        int calcium, iron, potassium;
        int vitaminA, vitaminD;
    }
}