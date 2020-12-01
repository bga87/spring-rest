package com.jm.task.config.security;


import com.jm.task.config.security.handlers.LoginSuccessHandler;
import com.jm.task.services.UsersService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebRequestSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UsersService usersService;

    public WebRequestSecurityConfig(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .regexMatchers("\\/", "\\/\\?.*", "\\/bootstrap.*", "\\/jquery.*", "\\/js.*", "\\/favicon.*").permitAll()
                .antMatchers("/users/current").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/user/**").access("hasAuthority('ROLE_USER') and !hasAuthority('ROLE_ADMIN')")
                .regexMatchers(HttpMethod.GET, "\\/admin\\/users\\/1002\\/*").hasAuthority("ROLE_ADMIN")
                .regexMatchers("\\/admin\\/users\\/1002\\/*").denyAll()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/**").denyAll()
                .and()
                .formLogin().loginPage("/").successHandler(new LoginSuccessHandler())
                .and()
                .csrf().disable();
    }

}