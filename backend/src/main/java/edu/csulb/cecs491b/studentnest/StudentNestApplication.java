package edu.csulb.cecs491b.studentnest;

import edu.csulb.cecs491b.studentnest.entity.SystemAdmin;
import edu.csulb.cecs491b.studentnest.entity.UserStatus;
import edu.csulb.cecs491b.studentnest.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StudentNestApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentNestApplication.class, args);
    }
    
    @Bean
    CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder enc) {
        return args -> {
            users.findByEmail("sysadmin@nest.edu").ifPresentOrElse(
                u -> System.out.println("✔ System admin already exists"),
                () -> {
                    var sa = new SystemAdmin();
                    sa.setFirstName("System");
                    sa.setLastName("Admin");
                    sa.setEmail("sysadmin@nest.edu");
                    sa.setPassword(enc.encode("StrongPass!23"));
                    sa.setStatus(UserStatus.ACTIVE);
                    users.save(sa);
                    System.out.println("✅ Created default system admin: sysadmin@nest.edu");
                }
            );
        };
    }
}

