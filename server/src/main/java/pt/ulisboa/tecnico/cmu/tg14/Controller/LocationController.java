package pt.ulisboa.tecnico.cmu.tg14.Controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.cmu.tg14.DTO.HashResult;
import pt.ulisboa.tecnico.cmu.tg14.DTO.LocationQuery;
import pt.ulisboa.tecnico.cmu.tg14.DTO.LocationMover;
import pt.ulisboa.tecnico.cmu.tg14.DTO.OperationStatus;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.CoordinatesImpl;
import pt.ulisboa.tecnico.cmu.tg14.Implementation.LocationImpl;
import pt.ulisboa.tecnico.cmu.tg14.Model.Coordinates;
import pt.ulisboa.tecnico.cmu.tg14.Model.Location;
import pt.ulisboa.tecnico.cmu.tg14.Model.Message;
import pt.ulisboa.tecnico.cmu.tg14.Security.SessionVerifier;
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
    public ResponseEntity<OperationStatus> createLocation(String sessionID,@RequestBody LocationMover locationMover){
            //@RequestParam(value="name") String name,@RequestParam(value="ssid",required = false) String ssid,@RequestParam(value="ble",required = false) String ble,@RequestParam(value="lat",required = false) Float lat,@RequestParam(value="lon",required = false) Float lon,@RequestParam(value="radius",required = false) Integer radius){
        Double lat = locationMover.getLatitude();
        Double lon = locationMover.getLongitude();
        Integer radius = locationMover.getRadius();

        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        locationImpl.create(locationMover.getName(),locationMover.getSsid(),locationMover.getBle());

        if(lat!=0&&lon!=0&&radius!=0){
            CoordinatesImpl coord = (CoordinatesImpl) context.getBean("coordinatesImpl");
            coord.create(locationMover.getName(),lat,lon,radius);
        }
        OperationStatus status = new OperationStatus();
        status.setOK();
        return new ResponseEntity<OperationStatus>(status,HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<OperationStatus> deleteLocation(String sessionID,@RequestParam String name){

        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        locationImpl.delete(name);
        OperationStatus status = new OperationStatus();
        status.setOK();
        return new ResponseEntity<OperationStatus>(status,HttpStatus.OK);
    }
/*
    @RequestMapping("/listByCoord")
    public Location getLocation(@RequestParam(value="lat") Float lat,@RequestParam(value="lon") Float lon){
        return locationImpl.getLocationByCoord(lat,lon);
    }
*/

    @RequestMapping(value = "/nearbyLocations", method = RequestMethod.POST)
    public ResponseEntity<List<Location>> getNearByLocations(String sessionID,@RequestBody LocationQuery queryString){
        List<Location> locations = new ArrayList<>();
        locations.addAll(locationImpl.getLocationByBle(queryString.getBleList()));
        locations.addAll(locationImpl.getLocationBySSID(queryString.getSsidList()));
        locations.removeAll(Collections.singleton(null)); // remove null results

        Float lat = queryString.getLatitude();
        Float lon = queryString.getLongitude();

        System.out.println("/nearbylocations: lat -> " + lat);
        System.out.println("/nearbylocations: lon -> " + lat);


        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }


        if(lat != 0 && lon!=0) {
            List<Location> loc = locationImpl.getLocationByCoord(lat, lon);
            locations.addAll(loc);
        }

        List<LocationMover> result = locations.stream().map(this::convertToLocResult).collect(Collectors.toList());



        return new ResponseEntity(result,HttpStatus.OK);
    }



    private LocationMover convertToLocResult(Location location){

        Coordinates coord = coordinatesImpl.getCoordinates(location.getName());
        if(coord != null && coord.getLatitude()!=0.0 && coord.getLongitude()!=0.0){
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
    public ResponseEntity<List<LocationMover>> getLocationList(String sessionID){
        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<LocationMover> result  = locationImpl.getLocationList().stream().map(this::convertToLocResult).collect(Collectors.toList());
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @RequestMapping("/list/hash")
    public ResponseEntity<HashResult> getLocationListHash(String sessionID){
        List<Location> result  = locationImpl.getLocationList();

        if(!SessionVerifier.isValid(sessionID)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for(Location location : result){
                byte[] hash = digest.digest(location.getName().getBytes("UTF-8"));
                out.write(hash);
               // System.out.println("Hash loc name -> " + location.getName());
            }
            byte[] listHash  = digest.digest(out.toByteArray());

            return new ResponseEntity(new HashResult(listHash),HttpStatus.OK);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new ResponseEntity(new HashResult(new byte[0]),HttpStatus.OK);
    }

}
