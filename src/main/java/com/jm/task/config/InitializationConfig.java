package com.jm.task.config;


import com.jm.task.dao.RoleRepository;
import com.jm.task.domain.Job;
import com.jm.task.domain.Role;
import com.jm.task.domain.User;
import com.jm.task.services.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
public class InitializationConfig {

    @Transactional
    @Bean("availableRoles")
    public Set<Role> availableRoles(ServletContext context, RoleRepository roleRepo) {
       /*
        * Создаем и сохраняем  в базе доступные роли, помещаем в контекст сервлета
        * множество созданных ролей для использования при формировании страниц из шаблонов thymeleaf;
        * регистрируем роли в кач-ве бина в контексте спринга
        */
        Role admin = roleRepo.save(new Role("ADMIN"));
        Role user = roleRepo.save(new Role("USER"));
        Set<Role> roles = new LinkedHashSet<>(Arrays.asList(admin, user));
        context.setAttribute("availableRoles", roles);
        return roles;
    }

    @Bean
    public CommandLineRunner initialDataLoader(UsersService usersService, Set<Role> availableRoles) {
        return new CommandLineRunner() {
            // Создаем в базе учетную запись администратора со всеми доступными ролями
            @Transactional
            @Override
            public void run(String... args) throws Exception {
                usersService.save(new User("admin", "admin", (byte) 0, null,
                                "admin@mail.ru", "admin", availableRoles));
                usersService.save(new User("Gleb", "Bespalov", (byte) 33,
                        new Job("Programmer", 90000), "ga.bespalov@gmail.com",
                        "12345",
                        availableRoles.stream()
                                .filter(role -> role.getRoleName().contains("USER"))
                                .collect(Collectors.toSet())));
            }
        };
    }

}