package com.jm.task.services;


import com.jm.task.dao.UserDao;
import com.jm.task.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class UsersServiceImpl implements UsersService {

    private final UserDao userDao;

    public UsersServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public User save(User user) throws IllegalStateException {
        return userDao.save(user);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Transactional
    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDao.getUserByLogin(email);
    }

    @Transactional
    @Override
    public void update(Long idToUpdate, User modifiedUser) throws IllegalStateException {
        userDao.update(idToUpdate, modifiedUser);
    }

}