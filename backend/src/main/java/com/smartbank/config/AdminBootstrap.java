package com.smartbank.config;
import com.smartbank.entity.User; import com.smartbank.repository.UserRepository; import org.springframework.beans.factory.annotation.Value; import org.springframework.boot.ApplicationArguments; import org.springframework.boot.ApplicationRunner; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Component;
@Component public class AdminBootstrap implements ApplicationRunner {
 private final UserRepository users; private final PasswordEncoder passwords; private final String email,password;
 public AdminBootstrap(UserRepository users,PasswordEncoder passwords,@Value("${BOOTSTRAP_ADMIN_EMAIL:}") String email,@Value("${BOOTSTRAP_ADMIN_PASSWORD:}") String password){this.users=users;this.passwords=passwords;this.email=email;this.password=password;}
 @Override public void run(ApplicationArguments args){if(email.isBlank()&&password.isBlank())return;if(email.isBlank()||password.length()<16)throw new IllegalStateException("Set both bootstrap admin variables and use a password of at least 16 characters.");if(users.existsByEmailIgnoreCase(email))return;User admin=new User();admin.fullName="SmartBank Administrator";admin.email=email.trim().toLowerCase();admin.phone="Not provided";admin.passwordHash=passwords.encode(password);admin.role="ADMIN";users.save(admin);}
}
