package tourGuide.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.jsoniter.output.JsonStream;
import org.apache.catalina.realm.AuthenticatedUserRealm;
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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
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
        System.out.println("location");
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
   //     User user = tourGuideService.getUser(userName);

        List<Attraction> attractionList = getAllAttractions();

        List<AttractionDTO> attractionDTOList = new ArrayList<>();
        System.out.println(attractionList.size());
        for (Attraction attraction : attractionList) {
            Double distance = rewardsService.getDistance(userLocation.location, attraction);


                 rewardsService.calculateRewards(getUser(userName));
            System.out.println("test /getnearbyattractions gpscontroller 83" );  //on y arrive

            Location location = new Location(attraction.latitude, attraction.longitude);
            AttractionDTO attractionDTO = new AttractionDTO(location, attraction.attractionName, distance, rewardsService.getRewardPoints(attraction, getUser(userName)));
            attractionDTOList.add(attractionDTO);
            System.out.println("attractiondtolist : " + attractionDTOList); //list ok
        }

        attractionDTOList.sort(Comparator.comparing(AttractionDTO::getDistance));
        attractionDTOList = attractionDTOList.stream().limit(5).collect(Collectors.toList());
        return JsonStream.serialize(attractionDTOList);
  //      return JsonStream.serialize(tourGuideService.getNearByAttractions(userLocation, user));

    }



    /*
    [{"attractionName":"San Diego Zoo","userLocation":{"longitude":167.85394,"latitude":-61.636956},"distance":7723.816952,"userId":null,"rewardPoints":0,"location":{"longitude":-117.149048,"latitude":32.735317}},
    {"attractionName":"Disneyland","userLocation":{"longitude":167.85394,"latitude":-61.636956},"distance":7766.695924,"userId":null,"rewardPoints":0,"location":{"longitude":-117.922008,"latitude":33.817595}},
    {"attractionName":"Joshua Tree National Park","userLocation":{"longitude":167.85394,"latitude":-61.636956},"distance":7828.35495,"userId":null,"rewardPoints":0,"location":{"longitude":-115.90065,"latitude":33.881866}},
    {"attractionName":"Kartchner Caverns State Park","userLocation":{"longitude":167.85394,"latitude":-61.636956},"distance":7870.807619,"userId":null,"rewardPoints":0,"location":{"longitude":-110.347382,"latitude":31.837551}},
    {"attractionName":"Mojave National Preserve","userLocation":{"longitude":167.85394,"latitude":-61.636956},"distance":7914.631366,"userId":null,"rewardPoints":0,"location":{"longitude":-115.510399,"latitude":35.141689}}]*/

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



    private static final RateLimiter rateLimiter = RateLimiter.create(1000.0D);

    @RequestMapping("/getAllAttractions")
    public List<Attraction> getAllAttractions() {
        rateLimiter.acquire();
        List<Attraction> attractions = new ArrayList();
        attractions.add(new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D));
        attractions.add(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767D, -110.821999D));
        attractions.add(new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689D, -115.510399D));
        attractions.add(new Attraction("Joshua Tree National Park", "Joshua Tree National Park", "CA", 33.881866D, -115.90065D));
        attractions.add(new Attraction("Buffalo National River", "St Joe", "AR", 35.985512D, -92.757652D));
        attractions.add(new Attraction("Hot Springs National Park", "Hot Springs", "AR", 34.52153D, -93.042267D));
        attractions.add(new Attraction("Kartchner Caverns State Park", "Benson", "AZ", 31.837551D, -110.347382D));
        attractions.add(new Attraction("Legend Valley", "Thornville", "OH", 39.937778D, -82.40667D));
        attractions.add(new Attraction("Flowers Bakery of London", "Flowers Bakery of London", "KY", 37.131527D, -84.07486D));
        attractions.add(new Attraction("McKinley Tower", "Anchorage", "AK", 61.218887D, -149.877502D));
        attractions.add(new Attraction("Flatiron Building", "New York City", "NY", 40.741112D, -73.989723D));
        attractions.add(new Attraction("Fallingwater", "Mill Run", "PA", 39.906113D, -79.468056D));
        attractions.add(new Attraction("Union Station", "Washington D.C.", "CA", 38.897095D, -77.006332D));
        attractions.add(new Attraction("Roger Dean Stadium", "Jupiter", "FL", 26.890959D, -80.116577D));
        attractions.add(new Attraction("Texas Memorial Stadium", "Austin", "TX", 30.283682D, -97.732536D));
        attractions.add(new Attraction("Bryant-Denny Stadium", "Tuscaloosa", "AL", 33.208973D, -87.550438D));
        attractions.add(new Attraction("Tiger Stadium", "Baton Rouge", "LA", 30.412035D, -91.183815D));
        attractions.add(new Attraction("Neyland Stadium", "Knoxville", "TN", 35.955013D, -83.925011D));
        attractions.add(new Attraction("Kyle Field", "College Station", "TX", 30.61025D, -96.339844D));
        attractions.add(new Attraction("San Diego Zoo", "San Diego", "CA", 32.735317D, -117.149048D));
        attractions.add(new Attraction("Zoo Tampa at Lowry Park", "Tampa", "FL", 28.012804D, -82.469269D));
        attractions.add(new Attraction("Franklin Park Zoo", "Boston", "MA", 42.302601D, -71.086731D));
        attractions.add(new Attraction("El Paso Zoo", "El Paso", "TX", 31.769125D, -106.44487D));
        attractions.add(new Attraction("Kansas City Zoo", "Kansas City", "MO", 39.007504D, -94.529625D));
        attractions.add(new Attraction("Bronx Zoo", "Bronx", "NY", 40.852905D, -73.872971D));
        attractions.add(new Attraction("Cinderella Castle", "Orlando", "FL", 28.419411D, -81.5812D));
        return attractions;
    }

    public gpsUtil.location.VisitedLocation getUserLocation(UUID userId) {
        rateLimiter.acquire();
        this.sleep();
        double longitude = ThreadLocalRandom.current().nextDouble(-180.0D, 180.0D);
        longitude = Double.parseDouble(String.format("%.6f", longitude));
        double latitude = ThreadLocalRandom.current().nextDouble(-85.05112878D, 85.05112878D);
        latitude = Double.parseDouble(String.format("%.6f", latitude));
        gpsUtil.location.VisitedLocation visitedLocation = new gpsUtil.location.VisitedLocation(userId, new gpsUtil.location.Location(latitude, longitude), new Date());
        return visitedLocation;
    }
    private void sleep() {
        int random = ThreadLocalRandom.current().nextInt(30, 100);

        try {
            TimeUnit.MILLISECONDS.sleep((long)random);
        } catch (InterruptedException var3) {
        }

    }

    private void sleepLighter() {
        try {
            TimeUnit.MILLISECONDS.sleep(10L);
        } catch (InterruptedException var2) {
        }

    }
}