package pt.ulisboa.tecnico.cmu.tg14.Security;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.SessionImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Session;

/**
 * Created by trosado on 5/15/17.
 */
public class SessionVerifier {

    static ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    static  SessionImpl sessionImpl =
            (SessionImpl) context.getBean("sessionImpl");


    public static boolean isValid(String sessionID){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        if(sessionID == null){
            return false;
        }

        Session session = sessionImpl.getSessionIDByUsername(username);
        return session.getSessionId().equals(sessionID);
    }
}