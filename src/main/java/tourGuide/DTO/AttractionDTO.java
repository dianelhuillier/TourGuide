package tourGuide.DTO;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.ArrayList;
import java.util.List;

public class AttractionDTO {
    private List<VisitedLocation> visitedLocations = new ArrayList<>();
    public VisitedLocation getLastVisitedLocation() {
        return visitedLocations.get(visitedLocations.size() - 1);
    }
    private List<Attraction> attractionsList;
}
