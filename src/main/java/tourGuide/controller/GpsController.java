package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.DTO.Attraction;
import tourGuide.DTO.AttractionDTO;
import tourGuide.DTO.Location;
import tourGuide.DTO.VisitedLocation;
import tourGuide.domain.User;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.webClient.GpsUtilWebClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GpsController {

    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    @Autowired
    TourGuideService tourGuideService;
    @Autowired
    RewardsService rewardsService;
    @Autowired
    GpsUtilWebClient gpsUtilWebClient;



    /** HTML GET request that returns a random location of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of a user location in longitude and latitude
     */
    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));

        return JsonStream.serialize(visitedLocation.location);
    }

    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }

    /** HTML GET request that returns the 5 closest attractions of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of nearby attractions.
     */
    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        VisitedLocation userLocation = tourGuideService.getUserLocation(getUser(userName));

        List<Attraction> attractionList = gpsUtilWebClient.getAttractions();

        List<AttractionDTO> attractionDTOList = new ArrayList<>();

        for (Attraction attraction : attractionList) {
            Double distance = rewardsService.getDistance(userLocation.location, attraction);


            //     rewardsService.calculateRewards(getUser(userName));  // VOID

            Location location = new Location(attraction.latitude, attraction.longitude);
            AttractionDTO attractionDTO = new AttractionDTO(userLocation.location, attraction.attractionName, location, distance);
            attractionDTOList.add(attractionDTO);
        }

        attractionDTOList.sort(Comparator.comparing(AttractionDTO::getDistance));
        attractionDTOList = attractionDTOList.stream().limit(5).collect(Collectors.toList());

        return JsonStream.serialize(attractionDTOList);
    }

    /** HTML GET request that returns the current location of all users
     *
     * @return a Json string of current location of all users.
     */
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations(@RequestParam String userName) {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }


        VisitedLocation userLocation = getUser(userName).getLastVisitedLocation();
        AttractionDTO attractionDTO = new AttractionDTO(userLocation.userId, userLocation.location);
        List<AttractionDTO> attractionDTOList = new ArrayList<>();
        attractionDTOList.add(attractionDTO);

        List<AttractionDTO> attractionDTOListStream = attractionDTOList
                .stream()
                .map(p -> new AttractionDTO(
                        p.getUserId(),
                        p.getUserLocation()))
                .collect(Collectors.toList());

        return JsonStream.serialize(attractionDTOListStream);
    }
}