package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import tourGuide.DTO.*;
import tourGuide.config.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.webClient.GpsUtilWebClient;
import tourGuide.webClient.RewardsWebClient;

public class TestRewardsService {

	@Test
	public void userGetRewards() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());

		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilWebClient.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();

		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		Attraction attraction = gpsUtilWebClient.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}
	
	@Ignore // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();

		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtilWebClient.getAttractions().size(), userRewards.size());
	}
	
}
