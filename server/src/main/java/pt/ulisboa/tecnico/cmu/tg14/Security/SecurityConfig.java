package pt.ulisboa.tecnico.cmu.tg14.Security;

/**
 * Created by trosado on 5/10/17.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
import pt.ulisboa.tecnico.cmu.tg14.Service.UserLoginService;

import java.util.List;

/**
 * Security configuration.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserLoginService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService( userDetailsService);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().fullyAuthenticated();
        http.httpBasic();
        http.csrf().disable();
    }
/*
    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    UserImpl userImpl =
            (UserImpl)context.getBean("userImpl");

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password("secret1").roles("USER");
        auth.inMemoryAuthentication().withUser("user2").password("secret2").roles("USER");

        List<User> users = userImpl.listUser();
        for(User user:users){
            auth.inMemoryAuthentication().withUser(user.getUsername()).password(user.getPassword()).roles("USER");
        }
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().fullyAuthenticated();
        http.httpBasic();
        http.csrf().disable();
    }*/
}