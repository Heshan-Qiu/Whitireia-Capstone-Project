package org.nzarra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DatabaseLoader(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("Preloading " + repository.save(new User("admin", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Administrator", List.of(User.ROLE_ADMINISTRATOR))));
        logger.debug("Preloading " + repository.save(new User("heshan", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Heshan Qiu", List.of(User.ROLE_COMPETITION_ADMINISTRATOR))));
        logger.debug("Preloading " + repository.save(new User("donald", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Donald Heimuli", List.of(User.ROLE_JUDGE))));
    }
}
