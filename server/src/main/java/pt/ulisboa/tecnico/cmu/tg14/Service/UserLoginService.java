package pt.ulisboa.tecnico.cmu.tg14.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
public class UserLoginService {
   /* @Autowired
    private UserImpl userimpl;

    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userimpl.getUser(username);
        if(user ==null){
            throw new UserNotFoundException(username);
        }
        List<String> roles = new ArrayList<>();
        roles.add("USER"); // Hardcoded given there is no more roles
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),true,true,true,true,getAuthorities(roles));

    }*/

    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
