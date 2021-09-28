/*
package tourGuide.controller;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import tourGuide.DTO.*;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;
import tourGuide.webClient.GpsUtilWebClient;

@RestController
public class TourGuideController {

    @Autowired
    TourGuideService tourGuideService;
    @Autowired
    RewardsService rewardsService;
    @Autowired
    GpsUtilWebClient gpsUtilWebClient;


    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }


    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));

        return JsonStream.serialize(visitedLocation.location);
    }

    @GetMapping("/testLocation")
    public String testLocation(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
        System.out.println(userName);
        double longitude = ThreadLocalRandom.current().nextDouble(-180.0D, 180.0D);

        double latitude = ThreadLocalRandom.current().nextDouble(-85.05112878D, 85.05112878D);
        VisitedLocation visitedLocation = new VisitedLocation(null, new Location(latitude, longitude), new Date());
        System.out.println(visitedLocation);
        return JsonStream.serialize(visitedLocation.location);
    }

//DTO?
*/
/*    //  TODO: Change this method to no longer return a List of Attractions.
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
    VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
    return JsonStream.serialize(getUser(userName).getLastVisitedLocation());
}*//*


 */
/*   //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public List<AttractionDTO> getNearbyAttractions(@RequestParam String userName) {
        List<AttractionDTO> nearestAttractions = new ArrayList<>();
        List<AttractionDTO> attractionDTOList = new ArrayList<>();

        List<Attraction> attractionList = gpsUtil.getAttractions();

        for (Attraction attraction : attractionList) {
            Location location = new Location(attraction.latitude, attraction.longitude);

            VisitedLocation visitedLocation = getUser(userName).getLastVisitedLocation();
            Location userLocation = new Location(visitedLocation.location.latitude, visitedLocation.location.longitude);


            AttractionDTO attractionDTO = new AttractionDTO(rewardsService.getDistance(location, userLocation));

            attractionDTOList.add(attractionDTO);

           List<String>listAttractionsSorted= attractionDTOList.stream().map().collect(Collectors.toList());

            Collections.sort(listAttractionsSorted);

            return listAttractionsSorted;
}
        }*//*



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

        List<tourGuide.DTO.Attraction> attractionList = gpsUtilWebClient.getAttractions();

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


    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(tourGuideService.getUserRewards(getUser(userName)));
    }


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


    private User getUser(String userName) {
        return tourGuideService.getUser(userName);
    }


    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
        return JsonStream.serialize(providers);
    }

}*/
