package pt.ulisboa.tecnico.cmu.tg14.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.*;

/**
 * Created by tiago on 11/05/2017.
 */
@Configuration
public class AppConfig {

    @Bean
    public DriverManagerDataSource dataSource(){
        return new DriverManagerDataSource("jdbc:mysql://localhost:3306/LocMess","root","toor");
    }

    @Bean
    public UserImpl userImpl(){
        return new UserImpl();
    }

    @Bean
    public LocationImpl locationImpl(){
        return new LocationImpl();
    }

    @Bean
    public CoordinatesImpl coordinatesImpl(){
        return new CoordinatesImpl();
    }

    @Bean
    public MessageImpl messageImpl(){
        return new MessageImpl();
    }

    @Bean
    public ProfileImpl profileImpl(){
        return new ProfileImpl();
    }


}
