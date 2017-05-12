package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;
import java.util.List;
/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    UserImpl userImpl =
            (UserImpl)context.getBean("userImpl");

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public OperationStatus createUser(@RequestBody User user){
        OperationStatus status = new OperationStatus();
        if(userImpl.getUser(user.getUsername()) != null)
            status.setError();
        else{
            userImpl.create(user.getUsername(),passwordEncoder().encode(user.getPassword()));
            status.setOK();
        }
        return status;
    }

    //FIXME to remove
    @RequestMapping("/list")
    public List<User> listUser(){
        return userImpl.listUser();
    }

    //FIXME change to request body
   @RequestMapping("/updatePassword")
   public void updatePassword(@RequestParam(value="password") String password){
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName(); //get logged in username
        userImpl.update(username,password);
   }

   public PasswordEncoder passwordEncoder(){
       PasswordEncoder encoder = new BCryptPasswordEncoder();
       return encoder;
   }

    }
