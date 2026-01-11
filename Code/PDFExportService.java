import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * PDF Export Service - Fixed Date Formatting
 * Version: 4.1 - Fixed LocalDate formatting issue
 * Features: Previous 7/30/90 day reports, Averages, Over-limit warnings, Recommendations
 */
public class PDFExportService {

    private static final Map<String, Integer> STANDARD_VALUES;
    private static final Map<String, Integer> MAXIMUM_SAFE_VALUES;

    static {
        STANDARD_VALUES = new HashMap<>();
        STANDARD_VALUES.put("calories", 2000);
        STANDARD_VALUES.put("protein_g", 50);
        STANDARD_VALUES.put("carbs_g", 300);
        STANDARD_VALUES.put("fats_g", 70);
        STANDARD_VALUES.put("water_ml", 2000);
        STANDARD_VALUES.put("calcium_mg", 1000);
        STANDARD_VALUES.put("potassium_mg", 3500);
        STANDARD_VALUES.put("sodium_mg", 2300);
        STANDARD_VALUES.put("magnesium_mg", 400);
        STANDARD_VALUES.put("iron_mg", 18);
        STANDARD_VALUES.put("zinc_mg", 11);
        STANDARD_VALUES.put("vitamin_a_iu", 5000);
        STANDARD_VALUES.put("vitamin_d_iu", 600);
        STANDARD_VALUES.put("vitamin_e_iu", 15);
        STANDARD_VALUES.put("vitamin_k_mcg", 120);

        MAXIMUM_SAFE_VALUES = new HashMap<>();
        MAXIMUM_SAFE_VALUES.put("calories", 3500);
        MAXIMUM_SAFE_VALUES.put("protein_g", 200);
        MAXIMUM_SAFE_VALUES.put("carbs_g", 500);
        MAXIMUM_SAFE_VALUES.put("fats_g", 150);
        MAXIMUM_SAFE_VALUES.put("water_ml", 5000);
        MAXIMUM_SAFE_VALUES.put("calcium_mg", 2500);
        MAXIMUM_SAFE_VALUES.put("potassium_mg", 6000);
        MAXIMUM_SAFE_VALUES.put("sodium_mg", 5000);
        MAXIMUM_SAFE_VALUES.put("magnesium_mg", 700);
        MAXIMUM_SAFE_VALUES.put("iron_mg", 45);
        MAXIMUM_SAFE_VALUES.put("zinc_mg", 40);
        MAXIMUM_SAFE_VALUES.put("vitamin_a_iu", 10000);
        MAXIMUM_SAFE_VALUES.put("vitamin_d_iu", 4000);
        MAXIMUM_SAFE_VALUES.put("vitamin_e_iu", 1000);
        MAXIMUM_SAFE_VALUES.put("vitamin_k_mcg", 1000);
    }

    public static boolean exportNutritionReport(JFrame parent, int userId, int days) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Nutrition Report");

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        String filename = String.format("EcoEats_Report_%s_to_%s.pdf",
                startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        fileChooser.setSelectedFile(new File(filename));

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File file = fileChooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            addHeader(document, days);
            addPeriodInfo(document, days);
            addSummaryStats(document, userId, days);
            addNutrientAnalysis(document, userId, days);
            addOverLimitWarnings(document, userId, days);
            addRecommendations(document, userId, days);

            document.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void addHeader(Document document, int days) {
        Paragraph title = new Paragraph("Eco-Eats Nutrition Report")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                        new com.itextpdf.kernel.colors.DeviceRgb(123, 141, 74)));
        document.add(title);

        Paragraph subtitle = new Paragraph("Previous " + days + " Days Analysis (Including Today)")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY);
        document.add(subtitle);

        document.add(new Paragraph("\n"));
    }

    private static void addPeriodInfo(Document document, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        String periodText = String.format("Report Period: %s to %s (%d days)",
                startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                days);

        Paragraph period = new Paragraph(periodText)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT)
                .setFontColor(ColorConstants.GRAY);
        document.add(period);

        // FIXED: Use LocalDateTime for time formatting instead of LocalDate
        String generatedText = "Generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));

        Paragraph generated = new Paragraph(generatedText)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.LEFT)
                .setFontColor(ColorConstants.LIGHT_GRAY);
        document.add(generated);

        document.add(new Paragraph("\n"));
    }

    private static void addSummaryStats(Document document, int userId, int days) {
        Paragraph sectionTitle = new Paragraph("Summary Statistics")
                .setFontSize(16)
                .setBold()
                .setFontColor(ColorConstants.BLACK);
        document.add(sectionTitle);

        Map<String, Double> averages = DatabaseHelper.getAverageNutrition(userId, days);

        Table table = new Table(UnitValue.createPercentArray(new float[]{40, 30, 30}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(createHeaderCell("Nutrient"));
        table.addHeaderCell(createHeaderCell("Daily Average"));
        table.addHeaderCell(createHeaderCell("Status"));

        addSummaryRow(table, "Calories", averages.getOrDefault("calories", 0.0), "kcal", "calories");
        addSummaryRow(table, "Protein", averages.getOrDefault("protein_g", 0.0), "g", "protein_g");
        addSummaryRow(table, "Carbohydrates", averages.getOrDefault("carbs_g", 0.0), "g", "carbs_g");
        addSummaryRow(table, "Fats", averages.getOrDefault("fats_g", 0.0), "g", "fats_g");
        addSummaryRow(table, "Water", averages.getOrDefault("water_ml", 0.0), "ml", "water_ml");

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private static void addNutrientAnalysis(Document document, int userId, int days) {
        Paragraph sectionTitle = new Paragraph("Detailed Nutrient Analysis")
                .setFontSize(16)
                .setBold()
                .setFontColor(ColorConstants.BLACK);
        document.add(sectionTitle);

        Map<String, Double> averages = DatabaseHelper.getAverageNutrition(userId, days);

        Table table = new Table(UnitValue.createPercentArray(new float[]{25, 20, 20, 35}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(createHeaderCell("Nutrient"));
        table.addHeaderCell(createHeaderCell("Your Average"));
        table.addHeaderCell(createHeaderCell("Recommended"));
        table.addHeaderCell(createHeaderCell("Status"));

        for (String nutrient : STANDARD_VALUES.keySet()) {
            double userValue = averages.getOrDefault(nutrient, 0.0);
            Integer standardValueObj = STANDARD_VALUES.get(nutrient);
            double standardValue = standardValueObj != null ? standardValueObj : 0;
            String status = getNutrientStatus(userValue, standardValue, nutrient);

            table.addCell(createCell(getNutrientDisplayName(nutrient)));
            table.addCell(createCell(String.format("%.1f %s", userValue, getNutrientUnit(nutrient))));
            table.addCell(createCell(String.format("%.0f %s", standardValue, getNutrientUnit(nutrient))));
            table.addCell(createStatusCell(status));
        }

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private static void addOverLimitWarnings(Document document, int userId, int days) {
        Map<String, Double> averages = DatabaseHelper.getAverageNutrition(userId, days);
        ArrayList<OverLimitInfo> overLimitNutrients = new ArrayList<OverLimitInfo>();

        for (String nutrient : STANDARD_VALUES.keySet()) {
            double userValue = averages.getOrDefault(nutrient, 0.0);
            Integer recommendedObj = STANDARD_VALUES.get(nutrient);
            Integer maxSafeObj = MAXIMUM_SAFE_VALUES.get(nutrient);

            double recommended = recommendedObj != null ? recommendedObj : 0;
            double maxSafe = maxSafeObj != null ? maxSafeObj : 0;

            if (userValue > recommended) {
                double percentageOver = ((userValue - recommended) / recommended) * 100;
                boolean isDangerous = userValue > maxSafe;

                overLimitNutrients.add(new OverLimitInfo(
                        nutrient,
                        userValue,
                        recommended,
                        maxSafe,
                        percentageOver,
                        isDangerous
                ));
            }
        }

        if (overLimitNutrients.size() > 0) {
            Paragraph sectionTitle = new Paragraph("⚠ Over-Limit Warnings")
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                            new com.itextpdf.kernel.colors.DeviceRgb(244, 67, 54)));
            document.add(sectionTitle);

            Paragraph warning = new Paragraph("The following nutrients exceed recommended daily values:")
                    .setFontSize(11)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setMarginBottom(10);
            document.add(warning);

            Table table = new Table(UnitValue.createPercentArray(new float[]{20, 15, 15, 15, 35}));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(createWarningHeaderCell("Nutrient"));
            table.addHeaderCell(createWarningHeaderCell("Your Avg"));
            table.addHeaderCell(createWarningHeaderCell("Recommended"));
            table.addHeaderCell(createWarningHeaderCell("% Over"));
            table.addHeaderCell(createWarningHeaderCell("Health Impact"));

            for (OverLimitInfo info : overLimitNutrients) {
                table.addCell(createCell(getNutrientDisplayName(info.nutrient)));
                table.addCell(createCell(String.format("%.1f %s", info.userValue, getNutrientUnit(info.nutrient))));
                table.addCell(createCell(String.format("%.0f %s", info.recommended, getNutrientUnit(info.nutrient))));

                Cell percentCell = createCell(String.format("+%.0f%%", info.percentageOver));
                if (info.isDangerous) {
                    percentCell.setBackgroundColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                            new com.itextpdf.kernel.colors.DeviceRgb(255, 200, 200)));
                    percentCell.setBold();
                }
                table.addCell(percentCell);

                table.addCell(createCell(getHealthImpact(info)));
            }

            document.add(table);

            boolean hasDangerousLevels = false;
            for (OverLimitInfo info : overLimitNutrients) {
                if (info.isDangerous) {
                    hasDangerousLevels = true;
                    break;
                }
            }

            if (hasDangerousLevels) {
                Paragraph criticalWarning = new Paragraph("🚨 CRITICAL: Some nutrients are at potentially harmful levels. Please consult a healthcare professional.")
                        .setFontSize(12)
                        .setBold()
                        .setFontColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                                new com.itextpdf.kernel.colors.DeviceRgb(200, 0, 0)))
                        .setBackgroundColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                                new com.itextpdf.kernel.colors.DeviceRgb(255, 240, 240)))
                        .setPadding(10)
                        .setMarginTop(10);
                document.add(criticalWarning);
            }

            document.add(new Paragraph("\n"));
        } else {
            Paragraph goodNews = new Paragraph("✓ All nutrients are within recommended limits")
                    .setFontSize(14)
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                            new com.itextpdf.kernel.colors.DeviceRgb(76, 175, 80)))
                    .setBackgroundColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                            new com.itextpdf.kernel.colors.DeviceRgb(240, 255, 240)))
                    .setPadding(10);
            document.add(goodNews);
            document.add(new Paragraph("\n"));
        }
    }

    private static String getHealthImpact(OverLimitInfo info) {
        String nutrientName = info.nutrient;

        if (info.isDangerous) {
            switch (nutrientName) {
                case "calories":
                    return "Risk of weight gain, obesity";
                case "sodium_mg":
                    return "High blood pressure risk";
                case "fats_g":
                    return "Cardiovascular risk";
                case "protein_g":
                    return "Kidney strain, dehydration";
                case "vitamin_a_iu":
                    return "Liver damage, bone issues";
                case "iron_mg":
                    return "Organ damage risk";
                case "calcium_mg":
                    return "Kidney stones, constipation";
                default:
                    return "Potential toxicity";
            }
        } else {
            if (info.percentageOver < 20) {
                return "Slightly elevated, monitor";
            } else if (info.percentageOver < 50) {
                return "Moderately high, adjust intake";
            } else {
                return "Significantly high, reduce intake";
            }
        }
    }

    private static void addRecommendations(Document document, int userId, int days) {
        Paragraph sectionTitle = new Paragraph("Recommendations")
                .setFontSize(16)
                .setBold()
                .setFontColor(ColorConstants.BLACK);
        document.add(sectionTitle);

        Map<String, Double> averages = DatabaseHelper.getAverageNutrition(userId, days);
        ArrayList<String> recommendations = generateRecommendations(averages);

        for (String recommendation : recommendations) {
            Paragraph rec = new Paragraph("• " + recommendation)
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontColor(ColorConstants.DARK_GRAY);
            document.add(rec);
        }
    }

    private static Cell createHeaderCell(String text) {
        return new Cell().add(new Paragraph(text)
                        .setBold()
                        .setFontSize(10)
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                        new com.itextpdf.kernel.colors.DeviceRgb(123, 141, 74)))
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Cell createWarningHeaderCell(String text) {
        return new Cell().add(new Paragraph(text)
                        .setBold()
                        .setFontSize(10)
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                        new com.itextpdf.kernel.colors.DeviceRgb(244, 67, 54)))
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Cell createCell(String text) {
        return new Cell().add(new Paragraph(text)
                        .setFontSize(9))
                .setPadding(5);
    }

    private static Cell createStatusCell(String status) {
        com.itextpdf.kernel.colors.Color color;
        if (status.contains("Excellent")) {
            color = com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                    new com.itextpdf.kernel.colors.DeviceRgb(76, 175, 80));
        } else if (status.contains("Good")) {
            color = com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                    new com.itextpdf.kernel.colors.DeviceRgb(33, 150, 243));
        } else if (status.contains("Adequate") || status.contains("Above Target")) {
            color = com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                    new com.itextpdf.kernel.colors.DeviceRgb(255, 193, 7));
        } else {
            color = com.itextpdf.kernel.colors.Color.convertRgbToCmyk(
                    new com.itextpdf.kernel.colors.DeviceRgb(244, 67, 54));
        }

        return new Cell().add(new Paragraph(status)
                        .setFontSize(9)
                        .setBold())
                .setBackgroundColor(color)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static void addSummaryRow(Table table, String nutrient, double value, String unit, String nutrientKey) {
        Integer standardObj = STANDARD_VALUES.get(nutrientKey);
        double standard = standardObj != null ? standardObj : 1;
        double percentage = (value / standard) * 100;

        String status;
        if (percentage > 130) {
            status = String.format("%.0f%% (Over limit!)", percentage);
        } else if (percentage > 100) {
            status = String.format("%.0f%% (Above target)", percentage);
        } else {
            status = String.format("%.0f%% of target", percentage);
        }

        table.addCell(createCell(nutrient));
        table.addCell(createCell(String.format("%.1f %s", value, unit)));
        table.addCell(createCell(status));
    }

    private static String getNutrientStatus(double userValue, double standardValue, String nutrient) {
        if (standardValue == 0) return "No Standard";
        double percentage = (userValue / standardValue) * 100;

        Integer maxSafeObj = MAXIMUM_SAFE_VALUES.get(nutrient);
        double maxSafe = maxSafeObj != null ? maxSafeObj : standardValue * 2;

        if (userValue > maxSafe) {
            return "Dangerously High!";
        } else if (percentage > 150) {
            return "Significantly High";
        } else if (percentage > 120) {
            return "Above Target";
        } else if (percentage >= 90) {
            return "Excellent";
        } else if (percentage >= 70) {
            return "Good";
        } else if (percentage >= 50) {
            return "Adequate";
        } else {
            return "Needs Improvement";
        }
    }

    private static String getNutrientDisplayName(String nutrient) {
        Map<String, String> displayNames = new HashMap<>();
        displayNames.put("calories", "Calories");
        displayNames.put("protein_g", "Protein");
        displayNames.put("carbs_g", "Carbohydrates");
        displayNames.put("fats_g", "Fats");
        displayNames.put("water_ml", "Water");
        displayNames.put("calcium_mg", "Calcium");
        displayNames.put("potassium_mg", "Potassium");
        displayNames.put("sodium_mg", "Sodium");
        displayNames.put("magnesium_mg", "Magnesium");
        displayNames.put("iron_mg", "Iron");
        displayNames.put("zinc_mg", "Zinc");
        displayNames.put("vitamin_a_iu", "Vitamin A");
        displayNames.put("vitamin_d_iu", "Vitamin D");
        displayNames.put("vitamin_e_iu", "Vitamin E");
        displayNames.put("vitamin_k_mcg", "Vitamin K");

        return displayNames.getOrDefault(nutrient, nutrient);
    }

    private static String getNutrientUnit(String nutrient) {
        if (nutrient.endsWith("_g")) return "g";
        if (nutrient.endsWith("_ml")) return "ml";
        if (nutrient.endsWith("_mg")) return "mg";
        if (nutrient.endsWith("_iu")) return "IU";
        if (nutrient.endsWith("_mcg")) return "mcg";
        if (nutrient.equals("calories")) return "kcal";
        return "";
    }

    private static ArrayList<String> generateRecommendations(Map<String, Double> averages) {
        ArrayList<String> recommendations = new ArrayList<>();
        boolean hasOverLimit = false;
        boolean hasUnderLimit = false;

        for (String nutrient : STANDARD_VALUES.keySet()) {
            double userValue = averages.getOrDefault(nutrient, 0.0);
            Integer standardValueObj = STANDARD_VALUES.get(nutrient);
            Integer maxSafeObj = MAXIMUM_SAFE_VALUES.get(nutrient);

            double standardValue = standardValueObj != null ? standardValueObj : 1;
            double maxSafe = maxSafeObj != null ? maxSafeObj : standardValue * 2;

            if (standardValue == 0) continue;

            double percentage = (userValue / standardValue) * 100;

            if (userValue > maxSafe) {
                String nutrientName = getNutrientDisplayName(nutrient);
                recommendations.add(0, String.format("🚨 URGENT: Reduce %s intake immediately (%.0f%% above safe limit)",
                        nutrientName, ((userValue - maxSafe) / maxSafe) * 100));
                hasOverLimit = true;
            } else if (percentage > 130) {
                String nutrientName = getNutrientDisplayName(nutrient);
                recommendations.add(String.format("⚠ Significantly reduce %s intake (currently at %.0f%% of recommended)",
                        nutrientName, percentage));
                hasOverLimit = true;
            } else if (percentage < 70) {
                String nutrientName = getNutrientDisplayName(nutrient);
                recommendations.add(String.format("Increase %s intake (currently at %.0f%% of recommended)",
                        nutrientName, percentage));
                hasUnderLimit = true;
            }
        }

        if (!hasOverLimit && !hasUnderLimit) {
            recommendations.add("✓ Excellent! Your nutrition intake is well-balanced.");
            recommendations.add("Continue maintaining your current eating habits.");
        } else if (!hasOverLimit) {
            recommendations.add("✓ Good job keeping nutrients within safe limits!");
        }

        return recommendations;
    }

    private static class OverLimitInfo {
        String nutrient;
        double userValue;
        double recommended;
        double maxSafe;
        double percentageOver;
        boolean isDangerous;

        OverLimitInfo(String nutrient, double userValue, double recommended, double maxSafe,
                      double percentageOver, boolean isDangerous) {
            this.nutrient = nutrient;
            this.userValue = userValue;
            this.recommended = recommended;
            this.maxSafe = maxSafe;
            this.percentageOver = percentageOver;
            this.isDangerous = isDangerous;
        }
    }
}