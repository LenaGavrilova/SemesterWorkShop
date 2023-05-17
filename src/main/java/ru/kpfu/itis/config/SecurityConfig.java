package ru.kpfu.itis.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kpfu.itis.services.UserDetailService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailService userDetailService;

    @Autowired
    public SecurityConfig(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()

                .authorizeRequests()

                .antMatchers("/admin").hasAnyRole("ADMIN")

                .antMatchers("/auth/login", "/error", "/auth/registration", "/book", "/book/info/{id}", "/img/**", "/book/search", "/swagger-ui/**", "/api/books/**").permitAll()
                .antMatchers("/admin", "/admin/orderList", "/admin/editStatus/{id}").hasRole("ADMIN")

                .anyRequest().hasAnyRole("USER", "ADMIN")

                .and()
                .formLogin().loginPage("/auth/login")

                .loginProcessingUrl("/process_login")
                .usernameParameter("username")
                .passwordParameter("password")

                .defaultSuccessUrl("/index", true)

                .and()

                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/css/**",
                "/js/**",
                "/img/**");
    }

}
