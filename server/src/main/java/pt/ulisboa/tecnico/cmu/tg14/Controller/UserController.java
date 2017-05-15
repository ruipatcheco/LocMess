package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.SessionImpl;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.UserImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;
import pt.ulisboa.tecnico.cmu.tg14.Security.SessionVerifier;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

    SessionImpl sessionImpl = (SessionImpl) context.getBean("sessionImpl");

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

    //FIXME change to request body
   @RequestMapping("/updatePassword")
   public ResponseEntity<OperationStatus> updatePassword(String sessionID, @RequestParam(value="password") String password){
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName(); //get logged in username

       if(!SessionVerifier.isValid(sessionID)){
           return new ResponseEntity(HttpStatus.UNAUTHORIZED);
       }


       userImpl.update(username,password);
       OperationStatus status = new OperationStatus();
       status.setOK();
       return new ResponseEntity<OperationStatus>(status,HttpStatus.OK);

   }

   public PasswordEncoder passwordEncoder(){
       PasswordEncoder encoder = new BCryptPasswordEncoder();
       return encoder;
   }

   @RequestMapping("/login")
   public String login(){

       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName(); //get logged in username

       sessionImpl.disableAllSessions(username);

       BigInteger randomID = new BigInteger(64, new SecureRandom());
       MessageDigest md = null;
       try {
           md = MessageDigest.getInstance("SHA-256");
           byte[] result = md.digest(randomID.toByteArray());
           String sessionID = new String(Base64.getEncoder().encode(result));
           sessionImpl.create(username,sessionID,true);
           return sessionID;
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return "error";
   }

   @RequestMapping("/logout")
    public ResponseEntity<OperationStatus> logout(String sessionID){
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String username = auth.getName(); //get logged in username
       if(!SessionVerifier.isValid(sessionID)){
           return new ResponseEntity(HttpStatus.UNAUTHORIZED);
       }
       sessionImpl.disableSession(username,sessionID);
       OperationStatus operationStatus = new OperationStatus();
       operationStatus.setOK();
       return new ResponseEntity(operationStatus,HttpStatus.OK);
   }

}
