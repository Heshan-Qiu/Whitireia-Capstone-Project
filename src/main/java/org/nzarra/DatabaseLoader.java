package org.nzarra;

import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);

    private final AccountRepository repository;

    @Autowired
    public DatabaseLoader(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Preloading " + repository.save(new Account("admin", MD5Encoder.encode("123456".getBytes()),
                Account.ROLE_ADMINISTRATOR, Boolean.TRUE, "Administrator")));
        log.info("Preloading " + repository.save(new Account("heshan", MD5Encoder.encode("123456".getBytes()),
                Account.ROLE_COMPETITION_ADMINISTRATOR, Boolean.TRUE, "Heshan Qiu")));
        log.info("Preloading " + repository.save(new Account("donald", MD5Encoder.encode("123456".getBytes()),
                Account.ROLE_JUDGE, Boolean.TRUE, "Donald Heimuli")));
    }
}
