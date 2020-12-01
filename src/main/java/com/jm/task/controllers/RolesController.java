package com.jm.task.controllers;


import com.jm.task.dao.RoleRepository;
import com.jm.task.domain.Role;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(produces = "application/json")
public class RolesController {

    private final RoleRepository roleRepo;

    public RolesController(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @GetMapping("/admin/roles")
    public List<Role> getRoles() {
        return roleRepo.findAll(Sort.by("id"));
    }

}