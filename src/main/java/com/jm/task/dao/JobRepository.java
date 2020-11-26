package com.jm.task.dao;


import com.jm.task.domain.Job;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface JobRepository extends Repository<Job, Long> {

    Job save(Job job);
    void delete(Job job);
    Optional<Job> findByNameAndSalary(String name, Integer salary);

}