package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.HashResult;
import pt.ulisboa.tecnico.cmu.tg14.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.DTO.LocationMover;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.CoordinatesImpl;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.LocationImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;
import sun.rmi.runtime.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by trosado on 20/03/17.
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {

    ApplicationContext context =
            new ClassPathXmlApplicationContext("Beans.xml");

    LocationImpl locationImpl =
            (LocationImpl) context.getBean("locationImpl");
    CoordinatesImpl coordinatesImpl =
            (CoordinatesImpl) context.getBean("coordinatesImpl");

    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    @ResponseBody
    public OperationStatus createLocation(@RequestBody LocationMover locationMover){
            //@RequestParam(value="name") String name,@RequestParam(value="ssid",required = false) String ssid,@RequestParam(value="ble",required = false) String ble,@RequestParam(value="lat",required = false) Float lat,@RequestParam(value="lon",required = false) Float lon,@RequestParam(value="radius",required = false) Integer radius){
        Double lat = locationMover.getLatitude();
        Double lon = locationMover.getLongitude();
        Integer radius = locationMover.getRadius();

        locationImpl.create(locationMover.getName(),locationMover.getSsid(),locationMover.getBle());

        if(lat!=0&&lon!=0&&radius!=0){
            CoordinatesImpl coord = (CoordinatesImpl) context.getBean("coordinatesImpl");
            coord.create(locationMover.getName(),lat,lon,radius);
        }
        OperationStatus status = new OperationStatus();
        status.setOK();
        return status;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public OperationStatus deleteLocation(@RequestParam String name){
        locationImpl.delete(name);
        OperationStatus status = new OperationStatus();
        status.setOK();
        return status;
    }
/*
    @RequestMapping("/listByCoord")
    public Location getLocation(@RequestParam(value="lat") Float lat,@RequestParam(value="lon") Float lon){
        return locationImpl.getLocationByCoord(lat,lon);
    }
*/

    @RequestMapping(value = "/nearbyLocations", method = RequestMethod.POST)
    public List<LocationMover> getNearByLocations(@RequestBody LocationQuery queryString){
        List<Location> locations = new ArrayList<>();
//        locations.addAll(locationImpl.getLocationByBle(queryString.getBleList()));
        locations.addAll(locationImpl.getLocationBySSID(queryString.getSsidList()));
        locations.removeAll(Collections.singleton(null)); // remove null results

        Float lat = queryString.getLatitude();
        Float lon = queryString.getLongitude();

        System.out.println("/nearbylocations: lat -> " + lat);
        System.out.println("/nearbylocations: lon -> " + lat);

        if(lat != 0 && lon!=0) {
            List<Location> loc = locationImpl.getLocationByCoord(lat, lon);
            locations.addAll(loc);

        }
        List<LocationMover> result = locations.stream().map(this::convertToLocResult).collect(Collectors.toList());


        return result;
    }

    private LocationMover convertToLocResult(Location location){

        UUID coordID = location.getCoordinates();
        Coordinates coord = null;
        if(coordID != null){
            coord = coordinatesImpl.getCoordinates(location.getName());
            return new LocationMover(location.getName(),location.getSsid()
                    ,location.getBle(),coord.getLatitude()
                    ,coord.getLongitude(),coord.getRadius());
        }
        return new LocationMover(location.getName(),location.getSsid()
                ,location.getBle(),0
                ,0,0);
    }


    @RequestMapping("/list")
    public List<LocationMover> getLocationList(){
        List<LocationMover> result  = locationImpl.getLocationList().stream().map(this::convertToLocResult).collect(Collectors.toList());
        return result;
    }

    @RequestMapping("/list/hash")
    public HashResult getLocationListHash(){
        List<Location> result  = locationImpl.getLocationList();


        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for(Location location : result){
                byte[] hash = digest.digest(location.getName().getBytes("UTF-8"));
                out.write(hash);
               // System.out.println("Hash loc name -> " + location.getName());
            }
            byte[] listHash  = digest.digest(out.toByteArray());

            return new HashResult(listHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashResult(new byte[0]);
    }

}
