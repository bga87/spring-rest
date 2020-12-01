package com.jm.task.controllers;


import com.jm.task.domain.User;
import com.jm.task.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(usersService.listUsers(), HttpStatus.OK);
    }

    @PostMapping(path = "/admin/users", consumes = "application/json")
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(usersService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(usersService.getUser(id), HttpStatus.OK);
    }

    @PatchMapping(path = "/admin/users/{id}", consumes = "application/json")
    public ResponseEntity<Void> updateUser(@PathVariable("id") Long id, @RequestBody @Valid User user) {
        usersService.update(id, user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/users/current")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        return new ResponseEntity<>(
                usersService.getUser(((User) authentication.getPrincipal()).getId()),
                HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        usersService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String[]>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String[]> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String[] errorMessages = new String[] { err.getDefaultMessage() };
            errors.merge(fieldName, errorMessages,
                    (strArr1, strArr2) -> Stream.of(strArr1, strArr2).flatMap(Arrays::stream).toArray(String[]::new));
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}