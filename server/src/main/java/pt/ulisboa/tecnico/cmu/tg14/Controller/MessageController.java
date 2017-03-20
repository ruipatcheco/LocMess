package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.MessageImpl;
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

    @RequestMapping("/create")
    public String create(@RequestParam(value="startTime") Timestamp startTime,@RequestParam(value="endTime") Timestamp endTime,@RequestParam(value="creationTime") Timestamp creationTime,@RequestParam(value="content") String content,@RequestParam(value="publisher") String publisher,@RequestParam(value="coordinatesID") UUID coordinatesID){
        messageImpl.create(startTime,endTime,creationTime,content,publisher,coordinatesID);
        return "OK";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "id") String id){
        messageImpl.delete(UUID.fromString(id));
        return "OK";
    }

    @RequestMapping("/get")
    public Message get(@RequestParam(value = "id") String id){
        Message m = messageImpl.getMessage(UUID.fromString(id));
        return m;
    }

    @RequestMapping("/getMessagesByLocation")
    public List<Message> getMessagesByLocation(@RequestParam(value = "location") String location){
        List<Message> messageList = messageImpl.getMessagesByLocation(location);
        return messageList;
    }

    }
