package com.watchit.api.services;

import com.watchit.api.entity.User;
import com.watchit.api.repository.UserRepository;
import com.watchit.api.security.UserAuthDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsAuthService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s Not Found", username)));
    return userToUserDetails(user);
  }

  public UserDetails userToUserDetails(User user) {
    UserAuthDetails userAuthDetails = new UserAuthDetails();
    userAuthDetails.setUsername(user.getUsername());
    userAuthDetails.setPassword(user.getPassword());
    return userAuthDetails;
  }

}
