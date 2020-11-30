package com.jm.task.controllers;


import com.jm.task.domain.User;
import com.jm.task.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


@RestController
@RequestMapping(produces = "application/json")
public class UsersController {

    private final UsersService usersService;


    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/admin/users")
    public List<User> getUsers() {
        return usersService.listUsers();
    }

    @PostMapping(path = "/admin/users", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody @Valid User user) {
        return usersService.save(user);
    }

    @GetMapping("/admin/users/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return usersService.getUser(id);
    }

    @PatchMapping(path = "/admin/users/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateUser(@PathVariable("id") Long id, @RequestBody @Valid User user) {
        usersService.update(id, user);
    }

    @DeleteMapping("/admin/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        usersService.delete(id);
    }

    @RequestMapping(path = "/accessDenied")
    public void accessDenied() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String[]> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String[]> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String[] errorMessages = new String[] { err.getDefaultMessage() };
            errors.merge(fieldName, errorMessages,
                    (strArr1, strArr2) -> Stream.of(strArr1, strArr2).flatMap(Arrays::stream).toArray(String[]::new));
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public String handleGeneralException(Exception ex) {
        return ex.getMessage();
    }

}