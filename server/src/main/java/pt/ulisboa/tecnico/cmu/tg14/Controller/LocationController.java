package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.CoordinatesImpl;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.LocationImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;

import java.util.List;
import java.util.UUID;

/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/location")
public class LocationController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    LocationImpl locationImpl =
            (LocationImpl) context.getBean("locationImpl");


    @RequestMapping("/create")
    public String createLocation(@RequestParam(value="name") String name,@RequestParam(value="ssid",required = false) String ssid,@RequestParam(value="ble",required = false) String ble,@RequestParam(value="lat",required = false) Float lat,@RequestParam(value="lon",required = false) Float lon,@RequestParam(value="radius",required = false) Integer radius){
        UUID cid = null;
        if(lat!=null&&lon!=null&&radius!=null){
            CoordinatesImpl coord = (CoordinatesImpl) context.getBean("coordinatesImpl");
            cid = coord.create(lat,lon,radius);
        }
        locationImpl.create(name,ssid,ble,cid);

        return name;
    }

    @RequestMapping("/listByCoord")
    public Location getLocation(@RequestParam(value="lat") float lat,@RequestParam(value="lon") float lon){
        return locationImpl.getLocationByCoord(lat,lon);
    }

    @RequestMapping("/list") //FIXME to remove
    public List<Location> getLocationList(){
        return locationImpl.getLocationList();
    }
}
