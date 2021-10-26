package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController
public class RewardsController {

    private Logger logger = LoggerFactory.getLogger(RewardsController.class);

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    RewardsService rewardsService;

    /**
     * HTML GET request that returns the rewards of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of all UserRewards
     */
/*    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        logger.debug("Access to /getRewards endpoint with username : " + userName);
        System.out.println(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
        return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
    }*/
    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        logger.debug("Access to /getRewards endpoint with username : " + userName);
        System.out.println(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
        return JsonStream.serialize(rewardsService.getRewards(tourGuideService.getUser(userName)));
    }

}