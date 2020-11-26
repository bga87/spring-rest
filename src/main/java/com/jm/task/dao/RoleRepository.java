package com.jm.task.dao;


import com.jm.task.domain.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;


public interface RoleRepository extends Repository<Role, Long> {

    Optional<Role> findById(Long id);
    List<Role> findAll(Sort sort);
    Role save(Role role);

}