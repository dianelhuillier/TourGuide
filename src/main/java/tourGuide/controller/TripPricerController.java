package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.DTO.Provider;

import tourGuide.service.TourGuideService;

import java.util.List;

@RestController
public class TripPricerController {

    private Logger logger = LoggerFactory.getLogger(TripPricerController.class);

	@Autowired
	TourGuideService tourGuideService;


    /** HTML GET request that returns 5 random trip deals of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a string of a list of Provider in a random way
     */
    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        logger.debug("Access to /getTripDeals endpoint with username : " + userName);

    	List<Provider> providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
        System.out.println(providers);
    	return JsonStream.serialize(providers);
    }
}