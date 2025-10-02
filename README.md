StudentNest 491B
# 🎓 StudentNest Backend

Backend service for **StudentNest**, a Student Management System built with **Spring Boot**, **MySQL**, and **JPA**.  
This project provides APIs for student/teacher registration, course management, tuition processing, and student record tracking.

## 🚀 Tech Stack
- **Java 21**  
- **Spring Boot 3.2.x**  
- **Spring Security** (with Password Encoding)  
- **Spring Data JPA / Hibernate**  
- **MySQL 8**  
- **Maven** (build tool)
## 📂 Project Structure

src/main/java/edu/csulb/cecs491b/studentnest
├── config/ # Security and application config
├── controller/ # REST controllers (Auth, Hello, etc.)
├── controller.dto/ # Data Transfer Objects (Login, Register requests)
├── entity/ # JPA entities (User, Student, Teacher, etc.)
├── repository/ # Spring Data JPA repositories
└── StudentNestApplication.java # Main entry point

Run the code(application)
right click on your StudentNestApplication.java
Run as --> Spring Boot App
After sucessfully run the code next step:
 
Inside a Postman
1.Method:  Post: http://localhost:8080/auth/register
    got to the Body --> raw -->JSON : and paste  : 
     {
    "email": "testuser@example.com",
    "username": "tester",
    "password": "Password123!"
  }
    click "SEND"
    after send you will get response if there issue please check again and do it
2. click on "+" New Method : Post: http://localhost:8080/api/auth/login
    same step Body --> raw --> --> JSON
    {
  "email": "testuser@example.com",
  "password": "Password123!"
    }
    Click : "SEND"
  You will get respons after sending 
  then open your browser and verify 









