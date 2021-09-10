package tourGuide.DTO;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
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


    public  String userName;
    public  UUID userId;
    public  Location location;

    public AttractionDTO() {
    }


    public AttractionDTO(String userName) {
        this.userName = userName;
        this.userId = userId;
        this.location = getLocation();
    }



    private List<AttractionDTO> attractionDTOS = new ArrayList<>();





    public AttractionDTO getLastVisitedLocationDTO() {
        return attractionDTOS.get(attractionDTOS.size() -1 );
    }

    public List<Attraction> attractionsList;





}