package org.nzarra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class AppConfig {

    private static String[] ignoreResources = {
            "/assets/**",
            "/css/**",
            "/images/**",
            "/js/**",
            "/scss/**",
            "/**",
            "/main.css"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeHttpRequests(
                (requests) -> requests.antMatchers(ignoreResources).permitAll().
                        antMatchers("/admin").hasAnyAuthority(User.ROLE_ADMINISTRATOR, User.ROLE_COMPETITION_ADMINISTRATOR).
                        anyRequest().authenticated()).
                formLogin((form) -> form.loginPage("/login").defaultSuccessUrl("/home").permitAll()).
                logout((logout) -> logout.logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true).permitAll());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall configureFirewall() {
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
        strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
        return strictHttpFirewall;
    }
}
