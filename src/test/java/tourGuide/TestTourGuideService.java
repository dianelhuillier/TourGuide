package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import rewardCentral.RewardCentral;
import tourGuide.DTO.*;
import tourGuide.config.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.domain.User;
import tourGuide.webClient.GpsUtilWebClient;
import tourGuide.webClient.RewardsWebClient;
import tourGuide.webClient.TripPricerWebClient;

public class TestTourGuideService {

	@Test
	public void getUserLocation() {
		InternalTestHelper.setInternalUserNumber(0);
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);


		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));

	}




	@Test
	public void trackUser() {

		InternalTestHelper.setInternalUserNumber(0);
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
		RewardsWebClient rewardsWebClient = new RewardsWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
		TourGuideService tourGuideService = new TourGuideService(rewardsService,
				gpsUtilWebClient, tripPricerWebClient);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}
	@Test
	public void addUser() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();
		
		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}
	
	@Test
	public void getAllUsers() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
		
		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();
		
		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}
/*	@Ignore
	@Test
	public void trackUser() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}*/

/*	@Test
	public void trackUser()  {

		InternalTestHelper.setInternalUserNumber(0);GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
		RewardsWebClient rewardsWebClient = new RewardsWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
		TourGuideService tourGuideService = new TourGuideService(rewardsService,
				gpsUtilWebClient, tripPricerWebClient);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}*/
	
	 // Not yet implemented
	@Test
	public void getNearbyAttractions() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		
		List<AttractionDTO> attractions = tourGuideService.getNearByAttractions(visitedLocation,user);
		
		tourGuideService.tracker.stopTracking();
		
		assertEquals(5, attractions.size());
	}

/*	@Test
	public void getTripDeals() {
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);
		
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);
		
		tourGuideService.tracker.stopTracking();
		// changed 10 for 5
		assertEquals(5, providers.size());

		InternalTestHelper.setInternalUserNumber(0);
		GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
		TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
		RewardsWebClient rewardsWebClient = new RewardsWebClient();
		RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
		TourGuideService tourGuideService = new TourGuideService(rewardsService,
				gpsUtilWebClient, tripPricerWebClient);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(5, providers.size());
	}*/
/*    @Test
public void getTripDeals() {
    GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
    RewardsService rewardsService = new RewardsService(gpsUtilWebClient, new RewardsWebClient());
    InternalTestHelper.setInternalUserNumber(0);
    TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsUtilWebClient, null);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

    List<Provider> providers = tourGuideService.getTripDeals(user);

    tourGuideService.tracker.stopTracking();

    assertEquals(10, providers.size());
}*/
@Test
public void getTripDeals() {
	InternalTestHelper.setInternalUserNumber(0);
	GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
	TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
	RewardsWebClient rewardsWebClient = new RewardsWebClient();
	RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
	TourGuideService tourGuideService = new TourGuideService(rewardsService,
			gpsUtilWebClient, tripPricerWebClient);

	User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

	List<Provider> providers = tourGuideService.getTripDeals(user);

	tourGuideService.tracker.stopTracking();

	assertEquals(5, providers.size());
}
}
