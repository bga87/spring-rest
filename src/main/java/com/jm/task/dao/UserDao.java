package com.jm.task.dao;


import com.jm.task.domain.User;

import java.util.List;


public interface UserDao {

    User save(User user) throws IllegalStateException;
    void delete(long id);
    List<User> listUsers();
    User getUserByLogin(String email);
    void update(Long idToUpdate, User modifiedUser) throws IllegalStateException;

}