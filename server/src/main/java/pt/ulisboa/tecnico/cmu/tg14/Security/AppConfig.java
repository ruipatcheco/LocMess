package pt.ulisboa.tecnico.cmu.tg14.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.*;

import javax.validation.Valid;

/**
 * Created by tiago on 11/05/2017.
 */
@Configuration
public class AppConfig {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.data-username}")
    String username;
    @Value("${spring.datasource.data-password}")
    String password;

    @Bean
    public DriverManagerDataSource dataSource(){
        return new DriverManagerDataSource(url,username,password);
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

    @Bean
    public MessageKeysImpl messageKeysImpl(){
        return new MessageKeysImpl();
    }

    @Bean
    public SessionImpl sessionImpl() {
        return new SessionImpl();
    }

}
