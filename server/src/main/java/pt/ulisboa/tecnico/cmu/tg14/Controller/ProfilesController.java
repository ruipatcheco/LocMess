package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.ProfileImpl;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import java.util.List;

/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/profile")
public class ProfilesController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    ProfileImpl profileImpl =
            (ProfileImpl)context.getBean("profileImpl");


    @RequestMapping("/create")
    public String create(@RequestParam(value="username") String username, @RequestParam(value="key") String key,@RequestParam(value="value") String value){
        profileImpl.create(username, key,value);
        return "OK";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam(value="username") String username, @RequestParam(value="key") String key){
        profileImpl.delete(username, key);
        return "OK";
    }


    @RequestMapping("/listAll")
    public List<Profile>  listAll(){
        List<Profile> profileList = profileImpl.listAll();
        return profileList;
    }

    }
