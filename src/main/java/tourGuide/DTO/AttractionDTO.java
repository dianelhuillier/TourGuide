package tourGuide.DTO;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tourGuide.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@AllArgsConstructor
public class AttractionDTO {


    public  UUID userId;
    public Location userLocation;

    public String attractionName;
    public  Location location;
    public Double distance;

    public int rewardPoints;

//REWARDS POINTS


    public AttractionDTO(Location userLocation, String attractionName, Location location, Double distance) {
        this.userLocation = userLocation;
        this.attractionName = attractionName;
        this.location = location;
        this.distance = distance;
    }

    public AttractionDTO(UUID userId, Location userLocation) {
        this.userId = userId;
        this.userLocation = userLocation;
    }












/*    public AttractionDTO(String userName) {
        this.userName = userName;
        this.userId = getUserId();
        this.location = getLocation();
    }*/



//    public AttractionDTO getLastVisitedLocationDTO() {
//        return attractionDTOS.get(attractionDTOS.size() -1 );
//    }






}