# Lab Report 02: Requirement Specification for Eco-Eats

## University of Frontier Technology, Bangladesh
### Department of Software Engineering
### Faculty of Software and Machine Intelligence Engineering

---

**LAB REPORT 02**  
**Title: Requirement Specification for Eco-Eats**

---

### Submitted by
**Mst Rafia Jannat Rafi**  
Std.ID: 2303001

**Abrar Mohammed Dayan**  
Std.ID: 2303012

**Tanzim Ahmed Towker**  
Std.ID: 2303038

### Submitted to
**Shifat Ara Rafiq**  
Lecturer,  
Department of Software Engineering,  
University of Frontier Technology, Bangladesh

---

**Lab Date:** 19-08-2025  
**Submission Date:** 25-08-2025

---

## Eco-Eats App

### Step 1: Overview

#### 1.1 Introduction
This document specifies the Software Requirements Specification (SRS) for the Eco-Eats application. It defines the functional and non-functional requirements of the system, serving as a reference for all stakeholders involved in its design, development, testing, and deployment. The document ensures a shared understanding of the capabilities, limitations, and objectives of the system, minimizing ambiguity during the development process.

The purpose of the Eco-Eats application is to provide users with an effective way to monitor and analyze their daily nutritional intake. The system enables users to register, manage their profiles, and log the food they consume. Based on these data, the application calculates the total calorie intake, provides a breakdown of nutrients, and tracks progress over time. Additionally, it generates personalized insights, such as body mass index (BMI) calculations, and exports nutrition reports in Excel format for long-term analysis. The application aims to encourage healthier eating habits by giving users a clear picture of their dietary patterns.

#### 1.2 Project Scope
The **Eco-Eats** is a mobile and web application designed to track daily food intake, calculate Body Mass Index (BMI), and provide simple nutrition analysis for healthier lifestyle management.

### Step 2: Overall Description

#### 2.1 Product Perspective
The Eco-Eats app is a new, self-contained system designed to help users track daily calorie intake, calculate BMI, and generate nutrition reports.

#### 2.2 Product Features
**Eco-Eats** comes with a set of simple but powerful features that make it easy for anyone to track their nutrition and health progress.

- **Sign Up & Login:** Each user gets their own account so their data stays private and personalized.
- **Profile Setup:** Users can add details like age, height, weight, and activity level, which helps the app give more accurate results.
- **Daily Food Logging:** A straightforward way to record what you eat every day — just add the food and calories.
- **Calorie Tracking:** The app keeps a running total of your daily and monthly calories, so you always know where you stand.
- **BMI Check:** With just your height and weight, the app calculates your BMI and tells you whether you're underweight, healthy, or overweight.
- **Nutrient Breakdown:** Easy-to-read charts show how much protein, carbs, and fat you're getting from your diet.
- **Monthly Reports:** At the end of the month, the app creates an Excel file with all your data, including summaries and progress over time.

#### 2.3 User Classes and Characteristics
The Eco-Eats application is designed for multiple types of users, each with distinct roles, access levels, and needs. The primary user classes are described below:

- **Users:**
  - **Description:** Health-conscious individuals who want to monitor their dietary intake and improve nutrition.
  - **Characteristics:**
    - Basic computer or mobile device literacy
    - Interested in tracking calories, nutrients, and health metrics
    - May range in age from teenagers to older adults
  - **Responsibilities:**
    - Register and maintain a personal account
    - Input daily food consumption
    - Review nutrient breakdown, BMI, and reports

- **System Administrators** (future integration):
  - **Description:** Personnel responsible for maintaining the system, managing databases, and monitoring user activity.
  - **Characteristics:**
    - Technical knowledge of system administration
    - Ability to troubleshoot, update, and secure the application
  - **Responsibilities:**
    - Ensure system security and data integrity
    - Manage food and nutrient databases

#### 2.4 Operating Environment

**Hardware Platform**
- Any personal computer or laptop (Windows/Linux/Mac) with at least **4 GB RAM** and **200 MB** free disk space.

**Operating System**
- Windows 10/11, Linux (Ubuntu/CentOS), or macOS.

**Software Dependencies**
- Java Development Kit (**JDK 17** or later)
- Integrated Development Environment (IDE) such as *Eclipse*, *IntelliJ*, or *NetBeans*
- Data Storage: The system will use Excel (.xlsx) files to store and update user logs and nutrition data
- External Libraries (optional): *JDBC* for database connectivity, *Apache POI* for Excel export

**Other Requirements**
- Console-based or simple GUI using *Java Swing*/*JavaFX*
- Requires internet connection only if using external nutrition APIs (optional)

#### 2.5 Design and Implementation Constraints
When building **Eco-Eats**, the developers won't have complete freedom to do whatever they want. There are some rules and limitations they'll need to follow so the app stays practical, secure, and easy to maintain.

- **Platform Choice:** At first, Eco-Eats will mainly focus on mobile devices (Android first). To save time and effort, a cross-platform framework like Flutter or React Native should be used instead of building separate apps from scratch.
- **Food Data Sources:** The nutrition details will come from third-party APIs (like USDA or Edamam). That means the app depends on these services — if they limit requests or change their system, the developers have to adapt.
- **Storage:** All user data (like food logs and reports) will be stored in the cloud. The database needs to be lightweight and efficient so the app doesn't slow down when storing months of data.
- **Privacy & Security:** Since the app collects personal health info (age, weight, BMI, etc.), it must be kept safe with proper authentication and encryption. User privacy should always be protected.
- **Reports:** For now, reports will only be generated in Excel format. Other formats like PDF might come later, but the initial focus is Excel.
- **Performance:** The app should run smoothly even on average smartphones. Features like generating reports must be optimized so they don't take forever to load.
- **Technology Stack:** Developers should stick to consistent languages and frameworks (like Flutter for front-end and maybe Node.js or Django for the back-end). Using standard coding practices is important so others can maintain the app later.
- **Future Integration:** The design should keep the door open for adding future features, like connecting with fitness wearables or using AI to recognize food from photos.

### Step 3: System Features

#### System Feature 1: Input Daily Calories
**Description and Priority:**  
Allows users to manually input the food items they have consumed along with calorie values. This helps track daily caloric intake and store it in the system's database for further analysis.  
**Priority: High**

**Priority Components (1–9 scale):**
- Benefit: 8 (essential for nutrition tracking)
- Penalty: 7 (inaccurate nutrition data if missing)
- Cost: 3 (moderate development effort)
- Risk: 4 (low; mostly data entry errors)

**Response Sequences:**
1. User logs into the app.
2. User navigates to the "Daily Log" section.
3. User types the food item name and calorie count (optional: portion size, macronutrients).
4. System validates input and stores it in the user's daily record.
5. System updates the total daily calorie count and shows progress toward the recommended intake.

**Functional Requirements:**
- FR1: The system shall allow users to add food items with calorie values.
- FR2: The system shall timestamp and store each entry for future reference.
- FR3: The system shall calculate and display the total daily calories.
- FR4: The system shall allow users to edit or delete entries.
- FR5: The system shall validate numeric inputs and handle invalid or missing data gracefully.

#### System Feature 2: BMI Calculation
**Description and Priority:**  
Calculates Body Mass Index (BMI) using the user's profile data (height and weight). Provides an indicator of health status.  
**Priority: High**

**Priority Components (1–9 scale):**
- Benefit: 7
- Penalty: 5
- Cost: 2
- Risk: 3

**Response Sequences:**
1. User registers or updates profile with height and weight.
2. System retrieves height and weight from the profile.
3. System calculates BMI using the formula:  
   BMI = weight (kg) / height (m)²
4. System displays BMI value along with category (Underweight, Normal, Overweight, Obese).
5. If data is missing, system prompts user to enter required information.

**Functional Requirements:**
- FR1: The system shall store and update user height and weight in the profile.
- FR2: The system shall compute BMI whenever the profile is updated or accessed.
- FR3: The system shall display BMI value and health category.
- FR4: The system shall handle invalid or missing inputs (e.g., height cannot be zero).
- FR5: The system shall secure user data to maintain privacy.

#### System Feature 3: Food Suggestion
**Description & Priority:**  
Suggests food items to cover nutritional deficiencies by analyzing user's intake vs. recommended standards.  
**Priority: High**

**Priority Components (1–9):**
- Benefit: 9
- Penalty: 8
- Cost: 5
- Risk: 6

**Response Sequences:**
1. User logs food intake.
2. System analyzes intake.
3. System detects deficiencies.
4. System suggests suitable foods.

**Functional Requirements:**
- FR1: Identify nutrient deficiencies.
- FR2: Suggest foods to cover gaps.
- FR3: Retrieve food data from nutrition database.
- FR4: Display multiple options with basic details.

#### System Feature 4: Track Total Calories
**Description & Priority:**  
Calculates and displays total calories consumed daily/weekly/monthly based on user input.  
**Priority: High**

**Priority Components (1–9):**
- Benefit: 8
- Penalty: 7
- Cost: 3
- Risk: 3

**Response Sequences:**
1. User logs food items.
2. System adds calorie values.
3. System updates daily total and shows progress vs. recommended intake.

**Functional Requirements:**
- FR1: Sum all logged calories for a given day.
- FR2: Display daily/weekly/monthly totals.
- FR3: Show progress against recommended intake.
- FR4: Update totals dynamically when entries are added/edited/deleted.

#### System Feature 5: Nutrient Breakdown
**Description and Priority:**  
Users to manually log their daily food consumption by entering food items along with calorie values. The system stores the data, updates the user's daily caloric intake, and provides progress tracking against recommended intake levels.  
**Priority: High**

**Priority Components (1–9 scale):**
- Benefit: 8 (essential for accurate nutrition tracking)
- Penalty: 7 (loss of key tracking functionality if absent)
- Cost: 3 (moderate development effort required)
- Risk: 4 (low risk; primarily user input errors)

**Response Sequences:**
1. User logs into the app.
2. User navigates to the Daily Log section.
3. User enters the food item name and calorie count (optional: portion size, macronutrients).
4. The system validates the input and stores it in the user's daily record.
5. The system updates the total daily calorie count and displays progress toward the recommended intake.

**Functional Requirements:**
- FR1: The system shall allow users to add food items with calorie values.
- FR2: The system shall timestamp and store each entry for historical reference.
- FR3: The system shall calculate and display the total daily calorie intake.
- FR4: The system shall allow users to edit or delete existing entries.
- FR5: The system shall validate numeric inputs and handle invalid or missing data gracefully.

#### System Feature 6: Export to Excel
**Description and Priority:**  
This feature allows users to export their nutrition and calorie intake data into an Excel spreadsheet. The exported file will include daily logs, total calorie consumption, nutrient breakdown, and comparisons with recommended standards. This enables users to analyze their progress over time and share reports with healthcare professionals if needed.  
**Priority: Medium–High**

**Priority Components (1–9 scale):**
- **Benefit:** 7 (useful for long-term tracking, professional consultation, and reporting)
- **Penalty:** 6 (lack of export limits user analysis outside the app)
- **Cost:** 4 (moderate implementation effort with Excel libraries)
- **Risk:** 3 (low; mostly formatting and file compatibility issues)

**Response Sequences:**
1. User logs into the app.
2. User navigates to the *Reports/Export* section.
3. User selects the desired timeframe (daily, weekly, monthly).
4. System generates a structured Excel file containing nutrition data.
5. User downloads or shares the Excel file via email/cloud storage.

**Functional Requirements:**
- FR1: The system shall allow users to export their nutrition data into an Excel file.
- FR2: The system shall include timestamps, food items, calorie values, and nutrient breakdown in the export.
- FR3: The system shall provide options to select a specific timeframe (e.g., weekly, monthly).
- FR4: The system shall ensure the exported file is compatible with standard Excel software.
- FR5: The system shall allow users to download the file locally or share it through email/cloud.

### Step 4: External Interface & Nonfunctional Requirements

#### (a) User Interface
- Mobile App UI: Dashboard showing daily calorie total, nutrient breakdown, and BMI status.
- GUI Standards: Responsive design, intuitive navigation for quick food logging and report viewing.
- Error Messages: Clear alerts (e.g., "Calories must be a number," "Height cannot be zero.").
- Standard Features: Home, Daily Log, Profile, Reports, Settings, Help buttons on every screen.
- Accessibility: Supports multiple units (kg/lbs, cm/ft), multiple languages, and large-text mode.

#### (b) Hardware Interfaces
**Eco-Eats** mainly works on smartphones, so it doesn't need any special sensors or equipment. Here's how it interacts with devices:

- **Smartphone or Tablet:** This is where users interact with the app. They type in what they eat, check their calories, see charts, and download reports.
- **Cloud Server / Database:** All user data — like food logs and nutrition info — is saved on the cloud. The app talks to the cloud securely over the internet to store and fetch data.
- **Food Database APIs:** When a user adds a food item, the app connects to external nutrition databases to get accurate values. This happens over the internet too.
- **Local Device Storage:** If the user is offline, the app can save data temporarily on the phone and then sync it with the cloud later.
- **Excel Reports:** Users can download monthly reports as Excel files. They can open these on a PC, tablet, or even back on their phone.
- **Future Devices (Optional):** Later on, the app could connect to smartwatches or fitness bands to track activity automatically, or use the phone camera to recognize food.

#### (c) Software Interfaces
- Backend Framework: Node.js/Django/Flask for API and business logic.
- Database: PostgreSQL or MongoDB for storing user profiles, food logs, and nutrition data.
- Mobile/Web Framework: Flutter SDK or React Native (Android/iOS) and responsive web app.
- APIs: REST APIs integrating USDA FoodData Central or Edamam.
- Data Exchange: JSON messages (e.g., { "food": "apple", "calories": 95, "protein": 0.5 }).

#### (d) Communications Interfaces (Future integration)
- **Protocols:** HTTPS for app–server communication, REST API for data exchange.
- **Network:** Wi-Fi / 4G / 5G for mobile devices, standard broadband for web access.
- **Security:** JWT-based authentication with encrypted data transfer.
- **Performance:** Daily food entry updates processed in real-time (less than 2s), monthly Excel report generation completed within 10s.

#### (e) Nonfunctional Requirements
**Performance:**
- The system should log a food entry or calculate BMI in less than 2 seconds.
- Must support at least 200–300 concurrent users without major delay.

**Reliability:**
- Server uptime target: 99%.
- Data must not be lost; use secure database backups.

**Security:**
- User data (profile, health details) must be encrypted.
- Authentication via secure login (e.g., JWT or Firebase).

**Usability:**
- Simple, intuitive design for mobile and web users.
- Minimal training needed; clear navigation and error messages.

**Portability:**
- Cross-platform: Android, iOS, and web browser support.

**Maintainability:**
- Modular design for easy updates (e.g., adding new nutrition APIs).
- Well-documented backend and API structure.

#### (f) Other Requirements
- **Regulatory Compliance:**
  - The system must comply with applicable data protection laws (e.g., GDPR, local privacy regulations).
  - Health-related recommendations shall include a disclaimer that the app is not a substitute for professional medical advice.

- **Data Backup & Recovery:**
  - The system shall automatically back up user data daily.
  - Recovery procedures must ensure that no more than 24 hours of data are lost in case of failure.

- **Scalability:**
  - The system shall support at least 10,000 concurrent users without performance degradation.

- **Interoperability:**
  - Exported Excel files shall be compatible with Microsoft Excel, Google Sheets, and LibreOffice Calc.
  - APIs shall follow REST/HTTPS standards to allow future integration with external applications.

- **Legal and Ethical Requirements:**
  - All food and nutrition data sources must be verified and cited.
  - The application shall not provide misleading or harmful dietary advice.

![Requirement Diagram](Requirement Diagram.drawio.png)

### Contributors

1. **Mst. Rafia Jannat Rafi (ID: 2303001)**
   - a. Product Perspective
   - b. Operating Environment
   - c. System Features
     - i. Input Daily Calories
     - ii. BMI Calculation
   - d. User Interface
   - e. Software Interface
   - f. Non-functional Requirements

2. **Abrar Mohammed Dayan (ID: 2303012)**
   - a. Introduction
   - b. User Classes and Characteristics
   - c. System Features
     - i. Nutrient Breakdown
     - ii. Export to Excel
   - d. Communications Interfaces
   - e. Other Requirements

3. **Tanzim Ahmed Towker (ID: 2303038)**
   - a. Project Scope
   - b. Product Features
   - c. System Features
     - i. Food Suggestion
     - ii. Track Total Calories
   - d. Design and Implementation Constraints
   - e. Hardware Interface
