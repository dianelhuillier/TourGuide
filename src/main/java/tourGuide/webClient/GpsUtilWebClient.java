package tourGuide.webClient;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tourGuide.DTO.Attraction;
import tourGuide.DTO.*;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GpsUtilWebClient {

    // Declare the base url (for docker deployment)
    private final String BASE_URL = "http://localhost:8081";
    // Declare the base url (for localhost)
    private final String BASE_URL_LOCALHOST = "http://localhost:8081";
    // Declare the path to UserLocation
    private final String PATH_USER_LOCATION = "/getUserLocation";
    // Declare the path to AllAttractions
    private final String PATH_ALL_ATTRACTIONS = "/getAllAttractions";
    //Declare the AttractionId name to use in the request of the Rest Template Web Client
    private final String USER_ID = "?userId=";


    //Define the User Location URI
    private final String getUserLocationGpsUtilUri() {
        return BASE_URL + PATH_USER_LOCATION;
    }

    //Define the All attractions URI
    private final String getAllAttractionsGpsUtilUri() {
        return BASE_URL + PATH_ALL_ATTRACTIONS;
    }


    private static final RateLimiter rateLimiter = RateLimiter.create(1000.0D);



    public VisitedLocation getUserLocation(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        VisitedLocation visitedLocation;

        ResponseEntity<VisitedLocation> result  =
                restTemplate.getForEntity(getUserLocationGpsUtilUri() +
                                USER_ID +
                                userId
                        ,VisitedLocation.class);
        visitedLocation = result.getBody();
        return visitedLocation;





    }

    public List<Attraction> getAllAttractions() {
        RestTemplate restTemplate = new RestTemplate();
        List<Attraction> attractionList;

        ResponseEntity<List<Attraction>> result =
                restTemplate.exchange(getAllAttractionsGpsUtilUri(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                        });
        attractionList= result.getBody();
        return attractionList;

 /*       List<Attraction> attractions = new ArrayList();
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
    }*/
}


}
/*

    WebClient client = WebClient.builder()
            .baseUrl("http://localhost:8080")
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
            .build();



            UriSpec<RequestBodySpec> uriSpec = client.post();
*/
