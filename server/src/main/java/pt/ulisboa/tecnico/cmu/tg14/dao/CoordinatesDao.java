package pt.ulisboa.tecnico.cmu.tg14.dao;

import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
public interface CoordinatesDao {
    /**
     * This is the method to be used to initialize
     * database resources ie. connection.
     */
    void setDataSource(DataSource ds);

    void create(String name ,double latitude,double longitude,int radius);
    Coordinates getCoordinates(String name);
    void updateCoordinates(String name,double latitude,double longitude,int radius);
    void delete(String name);

}
