package com.spring.springSecurity.configStartApp;

import com.spring.springSecurity.model.Authorities;
import com.spring.springSecurity.model.Role;
import com.spring.springSecurity.model.User;
import com.spring.springSecurity.repo.AuthoritiesRepo;
import com.spring.springSecurity.repo.RoleRepo;
import com.spring.springSecurity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class StartupProject implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AuthoritiesRepo authoritiesRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // authority (access users, access api, access basic)
        // role (admin, manager, user)

        // Add roles

        if(roleRepo.findAll().isEmpty()) {

            Role role1 = new Role();
            Role role2 = new Role();
            Role role3 = new Role();
            role1.setRoleName("ADMIN");
            role2.setRoleName("MANAGER");
            role3.setRoleName("USER");
            roleRepo.saveAll(Arrays.asList(role1, role2, role3));
        }

        // Add Authorities

        if(authoritiesRepo.findAll().isEmpty()) {
            Authorities auth1 = new Authorities("ACCESS_USERS");
            Authorities auth2 = new Authorities("ACCESS_API");
            Authorities auth3 = new Authorities("ACCESS_BASIC");
            authoritiesRepo.saveAll(Arrays.asList(auth1, auth2, auth3));
        }

        // Add  (admin, manager, user)

        if(userRepo.findAll().isEmpty()) {

            // Add Admin

            User admin = new User("Jon", passwordEncoder.encode("123"), "25", "Alex", true);
            Set<Role> roles = new HashSet<>();
            Set<Authorities> authorities = new HashSet<>();

            for (Role role : roleRepo.findAll()) {
                roles.add(role);
            }

            // for loop equal this stream
//            List<Role> roles1 = roleRepo.findAll().stream()
//                    .toList();

            for (Authorities auth : authoritiesRepo.findAll()) {
                authorities.add(auth);
            }

            // for loop equal this addAll method

            // authorities.addAll(authoritiesRepo.findAll());

            // for loop equal this Method reference
            // authoritiesRepo.findAll().forEach(authorities::add);

            admin.setRoles(roles);
            admin.setAuthorities(authorities);
            userRepo.save(admin);

            // Add Manager

            User manager = new User("Mary", passwordEncoder.encode("456"), "30", "Cairo", true);

            Role managerRole1 = roleRepo.findById(2L).get();
            Role managerRole2 = roleRepo.findById(3L).get();

            Authorities managerAuthorities1 = authoritiesRepo.findById(2L).get();
            Authorities managerAuthorities2 = authoritiesRepo.findById(3L).get();

            manager.getRoles().add(managerRole1);
            manager.getRoles().add(managerRole2);

            manager.getAuthorities().add(managerAuthorities1);
            manager.getAuthorities().add(managerAuthorities2);

            userRepo.save(manager);

            // Add User

            User user = new User("Mark", passwordEncoder.encode("789"), "20", "Aswan", true);

            Role userRole = roleRepo.findById(3L).get();
            Authorities userAuthorities = authoritiesRepo.findById(3L).get();

            user.getRoles().add(userRole);
            user.getAuthorities().add(userAuthorities);

            userRepo.save(user);
        }
    }
}
