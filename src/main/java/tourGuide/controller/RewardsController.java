package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourGuide.service.TourGuideService;

@RestController
public class RewardsController {

    private Logger logger = LoggerFactory.getLogger(RewardsController.class);

	@Autowired
	TourGuideService tourGuideService;



    /** HTML GET request that returns the rewards of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of all UserRewards
     */
    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        logger.debug("Access to /getRewards endpoint with username : " + userName);

    	return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
    }
}