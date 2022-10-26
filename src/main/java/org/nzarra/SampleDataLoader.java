package org.nzarra;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class SampleDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SampleDataLoader.class);

    private final UserRepository userRepository;

    private final CompetitionRepository competitionRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SampleDataLoader(UserRepository userRepository, CompetitionRepository competitionRepository,
                            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.competitionRepository = competitionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("Preloading " + userRepository.save(new User("admin", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Administrator", List.of(User.ROLE_ADMINISTRATOR))));
        logger.debug("Preloading " + userRepository.save(new User("heshan", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Heshan Qiu", List.of(User.ROLE_COMPETITION_ADMINISTRATOR))));
        logger.debug("Preloading " + userRepository.save(new User("donald", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Donald Heimuli", List.of(User.ROLE_COMPETITION_ADMINISTRATOR, User.ROLE_MARSHALL, User.ROLE_SCRUTINEER))));
        logger.debug("Preloading " + userRepository.save(new User("hamid", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Hamid Mahroeian", List.of(User.ROLE_COMPETITION_ADMINISTRATOR))));
        logger.debug("Preloading " + userRepository.save(new User("glen", passwordEncoder.encode("123456"),
                Boolean.TRUE, "Glen Houlihan", List.of(User.ROLE_COMPETITION_ADMINISTRATOR, User.ROLE_JUDGE))));

        Faker faker = new Faker();
        List<User> judges = IntStream.rangeClosed(1, 5).mapToObj(i -> new User(faker.name().username(),
                passwordEncoder.encode("123456"), Boolean.TRUE, faker.name().fullName(),
                List.of(User.ROLE_JUDGE))).toList();
        List<User> marshall = IntStream.rangeClosed(1, 3).mapToObj(i -> new User(faker.name().username(),
                passwordEncoder.encode("123456"), Boolean.TRUE, faker.name().fullName(),
                List.of(User.ROLE_MARSHALL))).toList();
        List<User> scrutineers = IntStream.rangeClosed(1, 5).mapToObj(i -> new User(faker.name().username(),
                passwordEncoder.encode("123456"), Boolean.FALSE, faker.name().fullName(),
                List.of(User.ROLE_SCRUTINEER))).toList();
        userRepository.saveAll(judges);
        userRepository.saveAll(marshall);
        userRepository.saveAll(scrutineers);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date from = format.parse("01-01-2022");
        Date to = new Date();
        List<Competition> competitions = IntStream.rangeClosed(1, 20).mapToObj(
                i -> new Competition(faker.funnyName().name(), faker.date().between(from, to),
                        faker.address().fullAddress(), faker.number().numberBetween(1, 5), faker.bool().bool())).toList();
        competitionRepository.saveAll(competitions);
    }
}
