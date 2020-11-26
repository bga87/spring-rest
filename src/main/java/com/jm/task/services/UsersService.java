package com.jm.task.services;


import com.jm.task.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UsersService extends UserDetailsService {

    User save(User user) throws IllegalStateException;
    void delete(Long id);
    List<User> listUsers();
    void update(Long idToUpdate, User modifiedUser) throws IllegalStateException;

}