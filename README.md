#  StudentNest 491B
## Description

**StudentNest**, a  mock modern redesign of CSULB's Course Management System (CMS).

## Table of Contents
1. [Tech Stack](#tech-stack)
2. [API Endpoints](#api-endpoints)
   - [Login](#login)
   - [Register](#register)

##  Tech Stack
- **Java 21**  
- **Spring Boot 3.2.x**  
- **Spring Security** (with Password Encoding)  
- **Spring Data JPA / Hibernate**  
- **MySQL 8**  
- **Maven** (build tool)
- **React**

[//]: # (## Project Structure)

[//]: # (src/main/java/edu/csulb/cecs491b/studentnest  )

[//]: # (├── config/ # Security and application config  )

[//]: # (├── controller/ # REST controllers &#40;Auth, Hello, etc.&#41;  )

[//]: # (├── controller.dto/ # Data Transfer Objects &#40;Login, Register requests&#41;  )

[//]: # (├── entity/ # JPA entities &#40;User, Student, Teacher, etc.&#41;  )

[//]: # (├── repository/ # Spring Data JPA repositories  )

[//]: # (└── StudentNestApplication.java # Main entry point  )

##  API Endpoints

### Login
#### /auth/login/{student | professor}
| type   | description |
|--------|-------------|
| string | email       |
| string | password    |

### Register
#### /auth/register/{student | professor}
| type   | description |
|--------|-------------|
| string | first name  |
| string | last name   |
| string | email       |
| string | password    |
