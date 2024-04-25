package com.envn8.app.security;

import com.envn8.app.security.jwt.AuthEntryPointJwt;
import com.envn8.app.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private AuthEntryPointJwt authEntryPoint;
    private UserDetailsService userDetailsService;
    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, AuthEntryPointJwt authEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // System.out.println("SecurityConfig.filterChain");
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/dc/**").permitAll()
                // .requestMatchers("/")
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  AuthTokenFilter jwtAuthenticationFilter() {
        return new AuthTokenFilter();
    }
}


// @Configuration
// // public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
//     public class SecurityConfig extends WebSecurityConfiguration{

     

//     @Autowired
//     private AuthenticationConfiguration authenticationConfiguration;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     @Bean
//     public AuthenticationManager authenticationManager() throws Exception {
//         return authenticationConfiguration.getAuthenticationManager();
//     }
    

//         @Override
//         protected void configure(HttpSecurity httpSecurity) throws Exception {
//             httpSecurity
//                     .csrf().disable()
//                     .authorizeRequests()
//                     .requestMatchers("/api/auth/**").permitAll()
//                     .anyRequest().authenticated()
//                     .and()
//                     .httpBasic()
//                     .and()
//                     .sessionManagement()
//                     .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//         }
//     }

