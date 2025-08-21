package com.spring.springSecurity.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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

//    Authorization with Default Security Rules

//    By default, all application endpoints require authentication. This is enforced through a SecurityFilterChain bean created by
//    Spring Boot’s auto-configuration. The filter chain includes a default HttpSecurity setup,
//    which specifies basic security rules such as requiring authentication for all requests

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

//    In-Memory Authentication
//
//    Spring Security’s InMemoryUserDetailsManager implements UserDetailsService to provide support for username/password based authentication that is stored in memory.
//    InMemoryUserDetailsManager provides management of UserDetails by implementing the UserDetailsManager interface. UserDetails-based authentication is used
//    by Spring Security when it is configured to accept a username and password for authentication.
//

    @Bean
    public UserDetailsService userDetailsService()
    {
        UserDetails user = User
                .withUsername("Mark")
                .password(bCryptPasswordEncoder().encode("789"))
                .roles("USER")
                .build();

        UserDetails admin = User
                .withUsername("Jon")
                .password(bCryptPasswordEncoder().encode("123"))
                //.roles("ADMIN")              //  is equal ROLE_ADMIN in authorities .authorities("ROLE_ADMIN")
                .authorities("ACCESS_BASIC_ADMIN", "ROLE_ADMIN")
                .build();

        UserDetails manager = User
                .withUsername("Mary")
                .password(bCryptPasswordEncoder().encode("456"))
                //.roles("MANAGER")
                .authorities("ACCESS_BASIC_MANAGER", "ROLE_MANAGER")
                .build();

        return new InMemoryUserDetailsManager(user, admin, manager);
    }
}
