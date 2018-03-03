package com.juniormiqueletti.moneyapp.service.security;

import com.juniormiqueletti.moneyapp.model.User;
import com.juniormiqueletti.moneyapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOptional = repository.findByEmail(email);
        User user = userOptional.orElseThrow(()-> new UsernameNotFoundException("User and/or password wrong."));

        return new AppUser(user, getUserPermissions(user));
    }

    private Set<SimpleGrantedAuthority> getUserPermissions(User user) {
        Set<SimpleGrantedAuthority> authority = new HashSet<>();
        user.getPermissionList().forEach( p -> authority.add(new SimpleGrantedAuthority(p.getDescription().toUpperCase())));
        return authority;
    }
}
