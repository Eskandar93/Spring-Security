package com.spring.springSecurity.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // used to indicate that a class declares one or more @Bean methods. T
// hese @Bean methods are then processed by the Spring container to generate bean definitions
// and service requests for those beans at runtime.

@EnableWebSecurity   // Enables Spring Security in the application, a
// allows customization of security settings through a WebSecurityConfigurerAdapter or SecurityFilterChain

public class SecurityConfiguration {

    @Autowired
    private UserPrincipleDetailsService userPrincipleDetailsService;

//    Authorization with Default Security Rules

//    By default, all application endpoints require authentication. This is enforced through a SecurityFilterChain bean created by
//    Spring Boot’s auto-configuration. The filter chain includes a default HttpSecurity setup,
//    which specifies basic security rules such as requiring authentication for all requests

//    HttpSecurity configuration is used to define access rules for different endpoints,
//    ensuring that requests are authorized based on the user's roles,
//    with specific URLs being restricted to users with certain roles,
//    and others being open to all users

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        //  authorization rules are defined using .authorizeRequests()
        // permitAll() -> access all users
        // authenticated() -> require authenticated

        return http
                .authorizeHttpRequests(request->
                        request
                                .requestMatchers("/api/main").permitAll()
                                .requestMatchers("/api/profile").authenticated()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/manage").hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers("/api/basic/mybasic").hasAuthority("ACCESS_BASIC_ADMIN")
                                .requestMatchers("/api/basic/allbasic").hasAuthority("ACCESS_BASIC_MANAGER")
                                .anyRequest().authenticated())
                .formLogin(form ->
                        form
                                .loginPage("/api/login")
                                .loginProcessingUrl("/signin") // redirect page when you success login operation
                                .usernameParameter("user")
                                .passwordParameter("pass")
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/api/main") // redirect page when you success log-out operation
                )

                .httpBasic(Customizer.withDefaults())
                .build();
    }

    // Encrypt the password by default algorithm  (BCrypt)
    // there are many encryption algorithms of password

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

//    DaoAuthenticationProvider retrieves the user's credentials, such as username and password, from the database and compares them to the credentials provided by the user during login. If the credentials match, the provider creates an Authentication object representing the authenticated user.
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipleDetailsService);

        return daoAuthenticationProvider;
    }

//    To use the DaoAuthenticationProvider, you need to configure it in your Spring Security configuration file.

    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authenticationProvider());
    }
}
