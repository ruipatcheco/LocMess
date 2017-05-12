package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.LocationMover;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.MessageImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    MessageImpl messageImpl =
            (MessageImpl)context.getBean("messageImpl");

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public OperationStatus create(@RequestBody Message message){
        //@RequestParam(value="startTime") Long startTime,@RequestParam(value="endTime") Long endTime,@RequestParam(value="creationTime") Long creationTime,@RequestParam(value="content") String content,@RequestParam(value="publisher") String publisher,@RequestParam(value="location") String location){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        Timestamp endTime  = message.getEndTime();

        if(endTime.getTime() <= 0)
            messageImpl.create(message.getId(),message.getStartTime(),
                    message.getCreationTime(),message.getContent(),
                    username,message.getLocation());
        else
            messageImpl.create(message.getId(),message.getStartTime(),endTime,
                    message.getCreationTime(),message.getContent(),
                    message.getPublisher(),message.getLocation());

        OperationStatus status = new OperationStatus();
        status.setOK();
        return status;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public ResponseEntity<OperationStatus> delete(@RequestParam(value = "id") String id){
        UUID msgId = UUID.fromString(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        Message msg = messageImpl.getMessage(msgId);
        OperationStatus status = new OperationStatus();

        if(msg.getPublisher().equals(username)){
            messageImpl.delete(msgId);
            status.setOK();
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(status,HttpStatus.OK);

    }

    @RequestMapping("/get")
    public Message get(@RequestParam(value = "id") String id){
        Message m = messageImpl.getMessage(UUID.fromString(id));
        return m;
    }

    @RequestMapping(value = "/getMessagesByLocation", method = RequestMethod.POST)
    public List<Message> getMessagesByLocation(@RequestBody LocationMover locationMover){
        List<Message> messageList = messageImpl.getMessagesByLocation(locationMover.getName());
        return messageList;
    }

    @RequestMapping(value = "/myMessages")
    public List<Message> getMyMessages(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        return messageImpl.getMessagesByUsername(username);
    }

}
