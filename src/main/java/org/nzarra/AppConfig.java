package org.nzarra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    private static String[] ignoreResources = {
            "/assets/**",
            "/css/**",
            "/images/**",
            "/js/**",
            "/scss/**",
            "/",
            "/main.css"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().
                authorizeHttpRequests(
                (requests) -> requests.antMatchers(ignoreResources).permitAll().
                        antMatchers("/admin").hasAnyAuthority(User.ROLE_ADMINISTRATOR, User.ROLE_COMPETITION_ADMINISTRATOR).
                        anyRequest().authenticated()).
                formLogin((form) -> form.loginPage("/login").successHandler(authenticationSuccessHandler()).permitAll()).
                logout((logout) -> logout.logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true).permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new AuthenticationSuccessHandler() {

            private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String targetUrl = determineTargetUrl(authentication);

                if (response.isCommitted()) {
                    logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
                    return;
                }

                redirectStrategy.sendRedirect(request, response, targetUrl);
                clearAuthenticationAttributes(request);
            }
        };
    }

    protected String determineTargetUrl(final Authentication authentication) {
        Map<String, String> roleTargetUrlMap = new HashMap<>();
        roleTargetUrlMap.put(User.ROLE_ADMINISTRATOR, "/admin/home");
        roleTargetUrlMap.put(User.ROLE_COMPETITION_ADMINISTRATOR, "/admin/home");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if(roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null)
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
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
