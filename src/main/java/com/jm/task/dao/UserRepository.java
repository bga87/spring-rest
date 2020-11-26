package com.jm.task.dao;


import com.jm.task.domain.Job;
import com.jm.task.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends Repository<User, Long> {

    User save(User user);
    void delete(User user);
    List<User> findAllByEmail(String email);
    List<User> findAllByJob(Job job);

    @EntityGraph(attributePaths = {"job", "roles"})
    List<User> findAll();

    @EntityGraph(attributePaths = "job")
    List<User> findAllWithJobsByNameAndSurnameAndAgeAllIgnoreCase(String name, String surname, Byte age);

    @EntityGraph(attributePaths = "job")
    List<User> findAllWithJobsByNameAndSurnameAndAgeAndIdNotAllIgnoreCase(String name, String surname, Byte age, Long id);

    @EntityGraph(attributePaths = {"job", "roles"})
    Optional<User> findWithFetchedJobAndRolesByEmail(String email);

    @EntityGraph(attributePaths = "job")
    Optional<User> findWithFetchedJobById(Long id);

    @EntityGraph(attributePaths = "job")
    Optional<User> findById(Long id);

}