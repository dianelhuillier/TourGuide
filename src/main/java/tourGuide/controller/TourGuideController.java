package tourGuide.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.DTO.AttractionDTO;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;
	@Autowired
    GpsUtil gpsUtil;


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
    public String testLocation( @RequestParam String userName)  {
        User user = tourGuideService.getUser(userName);
        System.out.println(userName);
        double longitude = ThreadLocalRandom.current().nextDouble(-180.0D, 180.0D);

        double latitude = ThreadLocalRandom.current().nextDouble(-85.05112878D, 85.05112878D);
        VisitedLocation visitedLocation = new VisitedLocation(null, new Location(latitude, longitude), new Date());        System.out.println(visitedLocation);
        return JsonStream.serialize(visitedLocation.location);
    }

//DTO?
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
    VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
    return JsonStream.serialize(getUser(userName).getLastVisitedLocation());
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


        VisitedLocation visitedLocation = getUser(userName).getLastVisitedLocation();
        AttractionDTO attractionDTO = new AttractionDTO(visitedLocation.location, visitedLocation.userId);

        return JsonStream.serialize(attractionDTO);
    }





    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
    	return JsonStream.serialize(providers);
    }
    
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}