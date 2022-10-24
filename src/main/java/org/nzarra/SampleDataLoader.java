package org.nzarra;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SampleDataLoader.class);

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SampleDataLoader(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
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
                Boolean.TRUE, "Donald Heimuli", List.of(User.ROLE_JUDGE, User.ROLE_MARSHALL, User.ROLE_SCRUTINEER))));

        Faker faker = new Faker();
        List<User> judges = IntStream.rangeClosed(1, 10).mapToObj(i -> new User(faker.name().username(),
                passwordEncoder.encode("123456"), Boolean.TRUE, faker.name().fullName(),
                List.of(User.ROLE_JUDGE))).toList();
        List<User> marshall = IntStream.rangeClosed(1, 10).mapToObj(i -> new User(faker.name().username(),
                passwordEncoder.encode("123456"), Boolean.TRUE, faker.name().fullName(),
                List.of(User.ROLE_MARSHALL))).toList();
        List<User> scrutineers = IntStream.rangeClosed(1, 10).mapToObj(i -> new User(faker.name().username(),
                passwordEncoder.encode("123456"), Boolean.TRUE, faker.name().fullName(),
                List.of(User.ROLE_SCRUTINEER))).toList();
        repository.saveAll(judges);
        repository.saveAll(marshall);
        repository.saveAll(scrutineers);
    }
}
