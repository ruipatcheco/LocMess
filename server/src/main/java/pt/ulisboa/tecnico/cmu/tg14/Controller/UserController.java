package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
import pt.ulisboa.tecnico.cmu.tg14.PasswordHasher;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;
import java.util.List;
/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/user")
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
            userImpl.create(user.getUsername(),PasswordHasher.hashToString(user.getPassword()));
            status.setOK();
        }
        return status;
    }

    @RequestMapping("/list")
    public List<User> listUser(){
        return userImpl.listUser();
    }

   /* @RequestMapping("/updatePassword")
    public void updatePassword(@RequestParam(value="username") String username, @RequestParam(value="password") String password){
        userImpl.update(username,password);
    }

*/
   /* public boolean checkPassword(@RequestParam(value="username") String username, @RequestParam(value="password") String password){
        User u = userImpl.getUser(username);
        return PasswordHasher.isExpectedPassword(password.toCharArray(),u.getPassword());
    }*/


    }
