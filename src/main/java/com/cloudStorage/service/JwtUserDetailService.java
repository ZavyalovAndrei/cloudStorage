package com.cloudStorage.service;

import com.cloudStorage.model.EntityUser;
import com.cloudStorage.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        EntityUser user = userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getEntityRole().getRole()));
        return new User(user.getLogin(), user.getPassword(),
                true, true, true, true, authorities);
    }
}
