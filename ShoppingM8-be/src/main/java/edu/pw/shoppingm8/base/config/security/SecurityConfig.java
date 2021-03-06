package edu.pw.shoppingm8.base.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_ANT_PATTERNS = {
            "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**",
    };

    private static final String[] PUBLIC_POST_ANT_PATTERNS = {
            "/auth/login", "/auth/register", "/auth/refresh", "/auth/login/**"
    };

    private final UserDetailsService userDetailsService;
    private final String secretKey;

    public SecurityConfig(UserDetailsService userServiceImpl,
                          @Value("${shoppingM8.security.secretKey}") String secretKey) {
        this.userDetailsService = userServiceImpl;
        this.secretKey = secretKey;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests().antMatchers("/h2/**").permitAll().and()
                .authorizeRequests().antMatchers(SWAGGER_ANT_PATTERNS).permitAll().and()
                .authorizeRequests().antMatchers(HttpMethod.POST, PUBLIC_POST_ANT_PATTERNS).permitAll().and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), secretKey, userDetailsService))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity
                .csrf().disable();
        httpSecurity
                .headers().frameOptions().disable();
        httpSecurity.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
