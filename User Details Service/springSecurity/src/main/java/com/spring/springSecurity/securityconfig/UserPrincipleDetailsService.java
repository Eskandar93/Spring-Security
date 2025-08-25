package com.spring.springSecurity.securityconfig;

import com.spring.springSecurity.model.User;
import com.spring.springSecurity.model.UserPrinciple;
import com.spring.springSecurity.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// to fetch user details
// UserPrincipleDetailsService is custom of UserDetailsService

@Service
public class UserPrincipleDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        return new UserPrinciple(user);
    }
}
