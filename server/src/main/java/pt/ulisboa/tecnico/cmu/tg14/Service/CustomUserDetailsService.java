package pt.ulisboa.tecnico.cmu.tg14.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import java.util.Collection;

import static java.util.Arrays.asList;

/**
 * Created by tiago on 11/05/2017.
 */
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserImpl userimpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usr = userimpl.getUser(username);
        if(usr == null){
            throw new UsernameNotFoundException("User "+username+" not found.");
        }

        return new org.springframework.security.core.userdetails.User(username,usr.getPassword(),getGrantedAuthorities(username));

    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(String username) {
        Collection< ? extends GrantedAuthority> authorities = asList(() -> "ROLE_USER");
        return authorities;
    }
}
