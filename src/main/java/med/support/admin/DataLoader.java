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

        User user1 = new User("a1",passwordEncoder.encode("a1"), new Role(RoleName.ROLE_ADMIN));
        User user2 = new User("a2",passwordEncoder.encode("a2"), new Role(RoleName.ROLE_ADMIN));
        User user3 = new User("a3",passwordEncoder.encode("a3"), new Role(RoleName.ROLE_ADMIN));
        User user4 = new User("a4",passwordEncoder.encode("a4"), new Role(RoleName.ROLE_ADMIN));
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//        userRepository.save(user4);
    }

}
