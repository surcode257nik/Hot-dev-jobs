package com.luv2code.jobportal.config;

import com.luv2code.jobportal.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private final String[] publicUrl = {"/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"};

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
                             CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        try{
            httpSecurity.authenticationProvider(authenticationProvider());
            httpSecurity.authorizeHttpRequests(auth->{
                auth.requestMatchers(publicUrl).permitAll();
                auth.anyRequest().authenticated();
            });
            httpSecurity.formLogin(form-> form.loginPage("/login").permitAll()
                            .successHandler(customAuthenticationSuccessHandler))
                    .logout(logout->{
                        logout.logoutUrl("/logout");
                        logout.logoutSuccessUrl("/");
                    }).cors(Customizer.withDefaults())
                    .csrf(csrf->csrf.disable());

        }catch (Exception e){
            System.out.println("Exception: "+e.getMessage()+"\n\n"+e);
        }
        return httpSecurity.build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        System.out.println("Hi");
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        System.out.println("Hello");
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }
}
