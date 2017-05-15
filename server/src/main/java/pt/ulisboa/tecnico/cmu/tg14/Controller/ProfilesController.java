package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.ProfileImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;
import pt.ulisboa.tecnico.cmu.tg14.Security.SessionVerifier;

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
    public ResponseEntity<OperationStatus> create(String sessionID, @RequestBody Profile keyValueData){

        System.out.println("Creating profile -> " + keyValueData.getKey());
            //@RequestParam(value="username") String username, @RequestParam(value="key") String key,@RequestParam(value="value") String value){
        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        profileImpl.create(username, keyValueData.getKey(),keyValueData.getValue());

        OperationStatus status = new OperationStatus();
        status.setOK();
        return new ResponseEntity<OperationStatus>(status,HttpStatus.OK);
    }



    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public ResponseEntity<OperationStatus> delete(String sessionID,@RequestBody Profile keyValueData){
        System.out.println("Deleting key -> " + keyValueData.getKey());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username

        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        profileImpl.delete(username, keyValueData.getKey());
        OperationStatus status = new OperationStatus();
        status.setOK();
        return new ResponseEntity<OperationStatus>(status,HttpStatus.OK);

    }


    @RequestMapping("/listAll")
    public ResponseEntity<List<Profile>>  listAll(String sessionID){
        List<Profile> profileList = profileImpl.list();

        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }


        for(Profile p : profileList)
            p.setUsername(null);
        return new ResponseEntity<List<Profile>>(profileList,HttpStatus.OK);
    }


    @RequestMapping("/myList")
    public ResponseEntity<List<Profile>> listMyPKeys(String sessionID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username

        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }


        List<Profile> userProfileKeys = profileImpl.list(name);
        System.out.println("/myList -> user profile keys from username = " +name);
        System.out.println("/myList -> user profile keys " +userProfileKeys.size());

        return new ResponseEntity<List<Profile>>(userProfileKeys,HttpStatus.OK);
    }

    }
