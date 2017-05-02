package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.LocationMover;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.MessageImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.Model.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    MessageImpl messageImpl =
            (MessageImpl)context.getBean("messageImpl");

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public OperationStatus create(@RequestBody Message message){
            //@RequestParam(value="startTime") Long startTime,@RequestParam(value="endTime") Long endTime,@RequestParam(value="creationTime") Long creationTime,@RequestParam(value="content") String content,@RequestParam(value="publisher") String publisher,@RequestParam(value="location") String location){

        messageImpl.create(message.getStartTime(),message.getEndTime(),
                message.getCreationTime(),message.getContent(),
                message.getPublisher(),message.getLocation());

        OperationStatus status = new OperationStatus();
        status.setOK();
        return status;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public OperationStatus delete(@RequestParam(value = "id") String id){
        OperationStatus status = new OperationStatus();
        messageImpl.delete(UUID.fromString(id));
        status.setOK();
        return status;
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



    }
