package pt.ulisboa.tecnico.cmu.tg14.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
import pt.ulisboa.tecnico.cmu.tg14.PasswordHasher;
import pt.ulisboa.tecnico.cmu.tg14.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 22/04/2017.
 */
@Service
@Transactional
public class UserLoginService implements UserDetailsService {
    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    UserImpl userimpl =
            (UserImpl)context.getBean("userImpl");


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userimpl.getUser(username);
        if(user ==null){
            throw new UsernameNotFoundException(username);
        }
        List<String> roles = new ArrayList<>();
        roles.add("USER"); // Hardcoded given there is no more roles
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),true,true,true,true,getAuthorities(roles));

    }

    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
