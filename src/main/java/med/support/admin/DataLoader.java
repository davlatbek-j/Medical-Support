package med.support.admin;

import lombok.RequiredArgsConstructor;
import med.support.entity.Role;
import med.support.entity.User;
import med.support.entity.enums.RoleName;
import med.support.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        User user1 = new User("admin-bot",passwordEncoder.encode("2+2=2-2"), new Role(RoleName.ROLE_ADMIN));
//        userRepository.save(user1);
    }

}
