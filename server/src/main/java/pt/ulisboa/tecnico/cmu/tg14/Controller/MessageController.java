package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.LocationMover;
import pt.ulisboa.tecnico.cmu.tg14.DTO.MessageMover;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.MessageImpl;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.MessageKeysImpl;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.ProfileImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.Model.MessageKeys;
import pt.ulisboa.tecnico.cmu.tg14.Model.Profile;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    MessageKeysImpl messageKeysImpl =
            (MessageKeysImpl)context.getBean("messageKeysImpl");

    MessageImpl messageImpl =
            (MessageImpl)context.getBean("messageImpl");

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    public OperationStatus create(@RequestBody MessageMover messageMover){
        //@RequestParam(value="startTime") Long startTime,@RequestParam(value="endTime") Long endTime,@RequestParam(value="creationTime") Long creationTime,@RequestParam(value="content") String content,@RequestParam(value="publisher") String publisher,@RequestParam(value="location") String location){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        Timestamp endTime  = messageMover.getEndTime();

        System.out.println("create message endtime ->" + endTime);

        messageImpl.create(messageMover.getId(),messageMover.getStartTime(),endTime,
                messageMover.getCreationTime(),messageMover.getContent(),
                messageMover.getPublisher(),messageMover.getLocation());

        for(Profile p: messageMover.getBlackList()){
            messageKeysImpl.create(messageMover.getId(),p.getKey(),p.getValue(),false);
        }

        for(Profile p: messageMover.getWhiteList()){
            messageKeysImpl.create(messageMover.getId(),p.getKey(),p.getValue(),true);
        }

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

        return isAllowed(messageList);
    }


    private List<Message> isAllowed(List<Message> messages) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        ArrayList<Message> result = new ArrayList<>();

        ProfileImpl profileImpl =
                (ProfileImpl)context.getBean("profileImpl");

        MessageKeysImpl messageKeysImpl =
                (MessageKeysImpl)context.getBean("messageKeysImpl");

        Timestamp now = new Timestamp(System.currentTimeMillis());

        for(Message m : messages){
            System.out.println("isAllowed a verificar mensagem -> " + m.getContent());

            //check timeframe
            //FIXME -> entime = 0 , mensagem disponivel para sempre
            if(m.getStartTime().before(now) && (m.getEndTime()!=null? m.getEndTime().after(now):true)){
                System.out.println("isAllowed mensagem na timeframe");

                List<Profile> userKeys = profileImpl.list(username);
                List<MessageKeys> messageKeys = messageKeysImpl.getMessageKeys(m.getId());
                List<Profile> blackList = new ArrayList<>();
                List<Profile> whiteList = new ArrayList<>();

                if(messageKeys.isEmpty()){
                    System.out.println("isAllowed mensagekeys empty, aceite");
                    result.add(m);
                    continue;
                }

                //Fill whitelist and blacklist
                for (MessageKeys mKey:messageKeys){
                    if(mKey.getWhite()){
                        Profile p = new Profile(username,mKey.getKey(),mKey.getValue());
                        //System.out.println("isAllowed mensagekeys white -> " + p.getKey() + p.getValue() + p.getUsername());
                        whiteList.add(p);
                    }

                    else{
                        Profile p = new Profile(username,mKey.getKey(),mKey.getValue());
                        //System.out.println("isAllowed mensagekeys black -> " + p.getKey() + p.getValue() + p.getUsername());
                        blackList.add(p);
                    }
                }


                //Check whitelist and blacklist & pray
                boolean passou = true;
                if (!containsAllMinado(userKeys, whiteList)){
                    //System.out.println("isAllowed mensagem nao passou whitelist");
                    passou=false;
                }

                for (Profile b : blackList){
                    System.out.println("isAllowed blacklist da mensagem -> " +b.getKey());
                    if(containsMinado(userKeys,b)){
                        //System.out.println("isAllowed mensagem nao passou blacklist");
                        passou = false;
                        break;
                    }
                }

                if(passou){
                    System.out.println("isAllowed mensagem aceite");
                    result.add(m);
                }

            }
        }

        return result;
    }

    private boolean containsMinado(List<Profile> a,Profile pb){
        //a.contains(b)
        for(Profile p: a){
            if(p.getKey().equals(pb.getKey())&&p.getValue().equals(pb.getValue())){
             return true;
            }
        }
        return false;
    }

    private boolean containsAllMinado(List<Profile> a, List<Profile> b){
        //a.containsAll(b)
        for(Profile pb : b){
            if(!containsMinado(a,pb)){
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "/myMessages")
    public List<Message> getMyMessages(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        return messageImpl.getMessagesByUsername(username);
    }

}
