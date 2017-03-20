package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import java.util.List;

/**
 * Created by trosado on 20/03/17.
 */
@RestController
public class UserController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    UserImpl userImpl =
            (UserImpl)context.getBean("userImpl");

    @RequestMapping("/create")
    public String createUser(@RequestParam(value="username") String username, @RequestParam(value="password") String password){
        userImpl.create(username,password);
        return "OK";
    }

    @RequestMapping("/list")
    public List<User> listUser(){
        return userImpl.listUser();
    }

    @RequestMapping("/updatePassword")
    public void updatePassword(@RequestParam(value="username") String username, @RequestParam(value="password") String password){
        userImpl.update(username,password);
    }

    }
