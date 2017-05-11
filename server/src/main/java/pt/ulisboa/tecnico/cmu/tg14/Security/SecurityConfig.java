package pt.ulisboa.tecnico.cmu.tg14.Security;

/**
 * Created by trosado on 5/10/17.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import pt.ulisboa.tecnico.cmu.tg14.Service.UserLoginService;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*TODO
    *  - Make createUser public
    *  - Use pwd cyphering on checking
    *  - Get User loggedIn
    * */


    @Autowired
    private UserLoginService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService( userDetailsService);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/api/**").fullyAuthenticated().and()
                .csrf().disable();
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"/api/user/create").anonymous();

        http.httpBasic();
        http.csrf().disable();
    }
}