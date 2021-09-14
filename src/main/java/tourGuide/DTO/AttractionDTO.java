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
    public  Location location;
//    public List<AttractionDTO> attractionDTOS = new ArrayList<>();
//    public List<Attraction> attractionsList;





    public AttractionDTO(Location location, UUID userId) {
        this.userId = userId;
        this.location = location;
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