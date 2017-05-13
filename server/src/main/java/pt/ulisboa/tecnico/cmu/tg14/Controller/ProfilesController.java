package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.ProfileImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;

import java.util.List;

/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/api/profile")
public class ProfilesController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    ProfileImpl profileImpl =
            (ProfileImpl)context.getBean("profileImpl");


    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public OperationStatus create(@RequestBody Profile keyValueData){

        System.out.println("Creating profile -> " + keyValueData.getKey());
            //@RequestParam(value="username") String username, @RequestParam(value="key") String key,@RequestParam(value="value") String value){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        profileImpl.create(username, keyValueData.getKey(),keyValueData.getValue());

        OperationStatus status = new OperationStatus();
        status.setOK();
        return status;
    }



    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public OperationStatus delete(@RequestBody Profile keyValueData){
        System.out.println("Deleting key -> " + keyValueData.getKey());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        profileImpl.delete(username, keyValueData.getKey());
        OperationStatus status = new OperationStatus();
        status.setOK();
        return status;
    }


    @RequestMapping("/listAll")
    public List<Profile>  listAll(){
        List<Profile> profileList = profileImpl.list();

        for(Profile p : profileList)
            p.setUsername(null);
        return profileList;
    }


    @RequestMapping("/myList")
    public List<Profile> listMyPKeys(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        List<Profile> userProfileKeys = profileImpl.list(name);
        System.out.println("/myList -> user profile keys from username = " +name);
        System.out.println("/myList -> user profile keys " +userProfileKeys.size());

        return userProfileKeys;
    }

    }
