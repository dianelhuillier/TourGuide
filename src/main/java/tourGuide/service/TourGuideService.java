package tourGuide.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tourGuide.DTO.*;
import tourGuide.config.InternalTestHelper;
import tourGuide.config.Tracker;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.webClient.GpsUtilWebClient;
import tourGuide.webClient.RewardsWebClient;
import tourGuide.webClient.TripPricerWebClient;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	public GpsUtilWebClient gpsUtilWebClient;
	public RewardsWebClient rewardsWebClient;
	private RewardsService rewardsService;

	public TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
	public final Tracker tracker;
	boolean testMode = true;

	public TourGuideService(RewardsService rewardsService, GpsUtilWebClient gpsUtilWebClient, TripPricerWebClient tripPricerWebClient) {
		Locale.setDefault(Locale.US);
		this.rewardsService = rewardsService;
		this.gpsUtilWebClient=gpsUtilWebClient;
		this.tripPricerWebClient=tripPricerWebClient;

		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}



	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation;
		if (user.getVisitedLocations().size() > 0) visitedLocation = user.getLastVisitedLocation();
		else visitedLocation = trackUserLocation(user);
		return visitedLocation;
	}
/*	public VisitedLocation trackUserLocation(User user) {
	//	Locale.setDefault(Locale.US);
		VisitedLocation visitedLocation = gpsUtilWebClient.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsWebClient.calculateRewards(user);
		return visitedLocation;
	}*/
public VisitedLocation trackUserLocation(User user) {
	UUID userId = user.getUserId();
	VisitedLocation visitedLocation = gpsUtilWebClient.getUserLocation(userId);
	user.addToVisitedLocations(visitedLocation);
	rewardsService.calculateRewards(user);

	return visitedLocation;
}
	public VisitedLocation trackUser (User user){
		return user.getLastVisitedLocation();
	}


/*	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for(Attraction attraction : gpsUtilWebClient.getAttractions()) {
			if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
//
//						.stream()
//						.limit(5)
//						.collect(Collectors.toList());
			}
		}
		return nearbyAttractions;
	}*/
	public List<AttractionDTO> getNearByAttractions(VisitedLocation visitedLocation, User user) {
		List<AttractionDTO> nearbyAttractions = new ArrayList<>();
		for(Attraction attraction : gpsUtilWebClient.getAllAttractions()) {
			System.out.println("tourservice getnearbyattractions : " + gpsUtilWebClient.getAllAttractions());

/*AttractionDTO attractionDTO = new AttractionDTO(attraction.attractionName, attraction.longitude, attraction.latitude,
		visitedLocation.location, rewardsService.getDistance(attraction, visitedLocation.location),
		rewardsService.getRewardPoints(attraction, user));*/
			AttractionDTO attractionDTO = new AttractionDTO(visitedLocation.location,
					attraction.attractionName, rewardsService.getDistance(attraction, visitedLocation.location),
								rewardsService.getRewardPoints(attraction, user));
		//	try {


			if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attractionDTO);
				System.out.println("tourguideservice nearbyattractions=  " + nearbyAttractions);

	//			nearbyAttractions.sort(Comparator.comparing(AttractionDTO::getDistance));
	//			nearbyAttractions = nearbyAttractions.stream().limit(5).collect(Collectors.toList());
				System.out.println("tourguideservice nearbyattractions sorted : " + nearbyAttractions);
			}
	//		}finally {

	//		}
		}

		return nearbyAttractions;
	}


/*	List<UserNearestAttractionsModel> listAttractionsSorted = nearestAttractions
			.stream()
			.sorted(Comparator.comparing(UserNearestAttractionsModel::getAttractionProximityRangeMiles))
			.limit(nbNearestAttractions)
			.collect(Collectors.toList());*/





	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}









	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		System.out.println("cumulative reward points tourservice");
		List<Provider> providers = tripPricerWebClient.getPriceWebClient(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}
	public List<UserReward> getUserRewards(User user) {
		System.out.println("user.getUserRewards( tourguideservice" + user.getUserRewards());
		return user.getUserRewards();
	}



//	public VisitedLocation getUserLocation(User user) {
//		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
//				user.getLastVisitedLocation() :
//				trackUserLocation(user);
//		return visitedLocation;}







/*	public List<AttractionDTO> getLastUserLocation(User user) {
List<AttractionDTO> visitedLocations = (List<AttractionDTO>) user.getLastVisitedLocation();
List<AttractionDTO> listLastVisited = visitedLocations.stream().map(AttractionDTO::getLocation).collect(Collectors.toList());

return listLastVisited;

	}*/




	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}


}
