# Lab Report 01: Project Proposal for Eco-Eats

## University of Frontier Technology, Bangladesh
### Department of Software Engineering
### Faculty of Software and Machine Intelligence Engineering

---

**LAB REPORT 01**  
**Title: Project Proposal for Eco-Eats**

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
**Submission Date:** 30-08-2025

---

## Eco-Eats: Personal Nutrition Tracker and Food Expert

### 1. Problem Analysis and Motivation

In today's fast-paced world, many individuals struggle to maintain a balanced diet due to busy lifestyles, lack of nutritional knowledge, or reliance on fast food. While people may track calories informally or through fragmented tools, most existing solutions fail to provide a comprehensive yet simple approach that combines calorie tracking, nutrient breakdown, BMI analysis, and long-term reporting.

Eco-Eats aims to empower individuals to make informed dietary decisions by providing them with an easy-to-use platform for nutrition tracking and analysis. By integrating calorie tracking, nutrient breakdown, BMI calculation, and exportable reports into a single system.

### 2. Literature Review

Digital nutrition tracking has advanced from traditional self-report food diaries to mobile applications and sensor-driven systems. Commercial platforms such as **MyFitnessPal** provide large food databases, barcode scanning, and meal-scan technology to assist users with monitoring dietary intake [1].

Research consistently shows that adherence to dietary logging predicts greater weight loss, with participants who tracked consistently (>66% of days) achieving significantly more outcomes than inconsistent users [2].

More recently, image-based methods using deep learning have improved food recognition and portion estimation, though challenges remain in handling mixed dishes and cultural variability [3]. Emerging sensor-based approaches, such as the *MealMeter* system, integrate physiological and motion data to automate macronutrient estimation with promising accuracy [4].

Despite these innovations, long-term adherence and generalizability across diverse populations remain key challenges. Future systems may need to integrate hybrid approaches, combining manual logging, image recognition, and multimodal sensing for more robust and user-friendly solutions.

### 3. Methodology

The Eco-Eats methodology follows a structured workflow that includes requirement analysis, system design, development, testing, and deployment. The system enables users to log meals, calculate calories and nutrients, analyze BMI, and export reports.

![Workflow of Eco-Eats](https://tinyurl.com/Eco-eates-photo1)  
*Figure 1: Workflow of Eco-Eats*

### 4. Feasibility Study

#### 4.1 Technical Feasibility
- Cross-platform frameworks (e.g., **Flutter**, **React Native**) can be used for faster development
- Backend development can be done using **Node.js** or **Django**
- Nutrition data can be integrated via publicly available APIs (USDA, Edamam, etc.)
- Cloud hosting platforms (**AWS**, **Firebase**) ensure scalability and high availability
- No reliance on new or experimental technologies

**Conclusion:** The project is technically feasible

#### 4.2 Economic Feasibility
- The use of **open source tools** significantly reduces development costs
- **Cloud hosting costs** can scale according to usage, ensuring flexibility
- A **free initial version** can be launched, with premium features generating revenue later
- Overall development cost is **moderate** and manageable

**Conclusion:** The project is economically viable

#### 4.3 Operational Feasibility
- Target users (students, professionals, health-conscious individuals) are already familiar with **mobile applications**
- A **simple, user-friendly UI** will ensure ease of adoption
- **Health and nutrition tracking** aligns well with existing user habits
- The app fits naturally into users' **daily routines**

**Conclusion:** The project is operationally feasible

### 5. Main Phases

The development of the **Eco-Eats** follows a structured approach to ensure systematic planning, design, implementation, and testing. The main phases of the project are as follows:

- Project proposal and planning
- Requirement specification of the project
- Selection of an appropriate SDLC model (Waterfall/Agile)
- Developing Data Flow Diagram (DFD) for the system
- Developing UML Use Case Diagram to capture interactions between users and system
- Developing UML Sequence Diagram for report generation, BMI calculation, and notifications
- Developing UML Class Diagram for data entities such as users, food items, nutrition logs, reports, and administrators
- System development (coding) of daily input, calorie breakdown, and excel export
- Software testing (unit testing, integration testing, and user acceptance testing)
- Deployment and delivery preparation with documentation

| SL | Task | Week | Responsible Person | Phase |
|----|------|------|-------------------|-------|
| 1 | Requirement Specification and Data Collection | 1,2 | Project Manager and Team Members | Research and Planning |
| 2 | Requirement Finalization | 3 | Project Manager and Team Members | Analysis |
| 3 | System Design and Modeling (DFD, UML Use Case) | 3,4 | Project Manager and Team Members | Design |
| 4 | UML Sequence, Communication, and Class Diagram Development | 5 | Project Manager and Team Members | Design |
| 5 | System Development (System Features) | 6,7,8 | Project Manager and Team Members | Implementation |
| 6 | Testing and Feedback Sharing | 7 | Project Manager and Team Members | Testing |
| 7 | Beta Version Delivery for Feedback | 8 | Project Manager and Team Members | Testing |
| 8 | Feedback Incorporation and Requirement Change | 9 | Project Manager and Team Members | Testing |
| 9 | Delivery Preparation with Documentation | 10 | Project Manager and Team Members | Deployment |

*Table 1: Project Task Schedule*

### 6. Workplan of Eco-Eats

![Workplan of Eco-Eats](workplan.png)  
*Figure 2: Workplan of Eco-Eats*

### 7. Budget Details of Eco-Eats

| Criteria | Existing System (BDT) | New System (BDT) |
|----------|----------------------|------------------|
| Team meeting | 15,000 | 10,000 |
| Project meeting | 10,000 | 8,000 |
| First aid | 1,000 | 1,000 |
| Website maintenance | 4,000 | 3,000 |
| Server and Hosting | 35,000 | 25,000 |
| Database Setup | 20,000 | 15,000 |
| Computer | 200,000 | 150,000 |
| Laptop | 120,000 | 100,000 |
| Software Licenses | 45,000 | 29,000 |
| Team Leader | 210,000 | 200,000 |
| System Designer | 100,000 | 90,000 |
| Software Engineer | 110,000 | 100,000 |
| Developer | 75,000 | 65,000 |
| Tester | 45,000 | 35,000 |
| Officer | 40,000 | 30,000 |
| **Total Cost** | **1,130,000** | **961,000** |

*Table 2: Budget Details of Eco-Eats*

### 8. Conclusion

The Eco-Eats project is designed to provide users with an effective and user-friendly solution for tracking their daily nutritional intake. By enabling food logging, calorie tracking, BMI calculation, nutrient breakdown, and report generation in Excel, the system addresses a growing demand for health and nutrition awareness.

The feasibility analysis shows that the project is technically achievable, economically viable, operationally practical, and can be developed within a realistic schedule. With its structured plan, defined requirements, and clear scope, Eco-Eats has the potential to help individuals adopt healthier dietary habits while offering a reliable platform for nutrition monitoring.

Overall, Eco-Eats is a feasible and impactful project that can be successfully implemented to meet its objectives and deliver long-term value to its users.

### References

1. MyFitnessPal, "MyFitnessPal Platform Features," 2023. [Online]. Available: https://www.myfitnesspal.com

2. G. M. Turner-McGrievy, E. H. Dunn, et al., "Adherence to Dietary Logging and Its Effect on Weight Loss," *Journal of Nutrition Education and Behavior*, 2017.

3. F. Zhu, M. Bosch, I. Woo, et al., "The Use of Mobile Devices in Aiding Dietary Assessment and Evaluation," *Journal of the American Dietetic Association*, vol. 110, no. 8, pp. 1231-1235, 2010.

4. Y. Zhang, K. Lee, and T. Nakamura, "MealMeter: A Sensor-Based Nutrition Estimation System," *IEEE Transactions on Biomedical Engineering*, 2025.

### Contributors

1. **Mst. Rafia Jannat Rafi (ID: 2303001)**
   - Literature Review
   - Workplan
   - References

2. **Abrar Mohammed Dayan (ID: 2303012)**
   - Problem Analysis and Motivation
   - Methodology
   - Main Phases

3. **Tanzim Ahmed Towker (ID: 2303038)**
   - Feasibility Study
   - Budget Details
   - Conclusion
