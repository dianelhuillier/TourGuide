package tourGuide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.GetMapping;
import tourGuide.DTO.*;
import tourGuide.controller.RewardsController;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.webClient.GpsUtilWebClient;
import tourGuide.webClient.RewardsWebClient;

@Service
public class RewardsService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
	private final int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private final int attractionProximityRange = 200;
	private final GpsUtilWebClient gpsUtilWebClient;
	private final RewardsWebClient rewardsWebClient;
	public TourGuideService tourGuideService;

	public RewardsService(GpsUtilWebClient gpsUtilWebClient, RewardsWebClient rewardsWebClient) {
		this.gpsUtilWebClient = gpsUtilWebClient;
		this.rewardsWebClient = rewardsWebClient;
	}

	private Logger logger = LoggerFactory.getLogger(RewardsController.class);

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/*public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtilWebClient.getAllAttractions();
		System.out.println(attractions);
		userLocations.addAll(user.getVisitedLocations());
		attractions.addAll(gpsUtilWebClient.getAllAttractions());
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}*/

/*	public void calculateRewards(User user) {
		logger.debug("Access to calculateRewards rewardsService ");

*//*		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtilWebClient.getAllAttractions();*//*
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
		List<Attraction> attractions = new CopyOnWriteArrayList<>();

		userLocations.addAll(user.getVisitedLocations());
		attractions.addAll(gpsUtilWebClient.getAllAttractions());
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					System.out.println("ifservice" + nearAttraction(visitedLocation, attraction));
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
						System.out.println("testservice");
						System.out.println("getuserrewards" + user.getUserRewards());
					}
				}
			}
		}
	}*/
public void calculateRewards(User user) {
	CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
	List<Attraction> attractions = new CopyOnWriteArrayList<>();

	userLocations.addAll(user.getVisitedLocations());
	attractions.addAll(gpsUtilWebClient.getAllAttractions());
	System.out.println("calculate reward : list attractions : " + attractions); //ok
	userLocations.forEach(v -> {
		attractions.forEach(a -> {
			if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(a.attractionName)).count() == 0) {
				System.out.println("calculate 1erif :" + a.attractionName);
				if (nearAttraction(v, a)) {
					System.out.println("calculate 2emeif : " + nearAttraction(v, a));
					user.addUserReward(new UserReward(v, a, getRewardPoints(a, user)));
					System.out.println("calculate 3emeif : " + user.getUserRewards());
				}
			}
		});
	});
}
/*	public List<UserReward> calculateRewards(User user) {
		List<VisitedLocation> userLocations = user.getVisitedLocations();
		List<Attraction> attractions = gpsUtilWebClient.getAllAttractions();
		userLocations.addAll(user.getVisitedLocations());
		attractions.addAll(gpsUtilWebClient.getAllAttractions());
		userLocations.forEach(v -> {
			attractions.forEach(a -> {
//				Attraction attraction = new Attraction();
//				getRewardPoints(attraction, user);
				user.addUserReward(new UserReward(v, a, getRewardPoints(a, user)));
				user.getUserRewards();
			});

		});
		return user.getUserRewards();

	}*/
/*		userLocations.forEach(v -> {
			attractions.forEach(a -> {
				if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(a.attractionName)).count() == 0) {
					if (nearAttraction(v, a)) {
						user.addUserReward(new UserReward(v, a, getRewardPoints(a, user)));
					}
				}
			});
		});*/
/*		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
						System.out.println(user);
					}
				}
			}
		}*/

/*		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
		List<Attraction> attractions = new CopyOnWriteArrayList<>();

		userLocations.addAll(user.getVisitedLocations());
		attractions.addAll(gpsUtilWebClient.getAllAttractions());

		userLocations.forEach(v -> {
			attractions.forEach(a -> {
				if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(a.attractionName)).count() == 0) {
					if (nearAttraction(v, a)) {
						user.addUserReward(new UserReward(v, a, getRewardPoints(a, user)));
					}
				}
			});
		});
	}*/

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return !(getDistance(attraction, location) > attractionProximityRange);
	}

// TODO

public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
	logger.debug("Access to nearAttraction rewardsService ");
	System.out.println("near attraction : " + getDistance(attraction, visitedLocation.location));
	if (getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true) return true;
	else return false;
}
/*	private int getRewardPoints(Attraction attraction, User user) {
		logger.debug("Access to getRewardPoints rewardsService ");

		return getAttractionRewardPoints(attraction.attractionId, user.getUserId());
		//		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}*/
/*	public int getRewardPoints(Attraction attraction, User user) {
		logger.debug("Access to getRewardPoints rewardsService ");
		UUID attractionId = attraction.attractionId;
		UUID userId = user.getUserId();
		return rewardsWebClient.getRewardPointsWebClient(attractionId, userId);
	}*/
public int getRewardPoints(Attraction attraction, User user) {
	logger.debug("Access to getRewardPoints rewardsService ");
	UUID attractionId = attraction.attractionId;
	UUID userId = user.getUserId();
//	System.out.println("getAttractionRewardPoints" + rewardsWebClient.getRewardPointsWebClient(attractionId, userId));
//	return getAttractionRewardPoints(attractionId, userId);
	try {
		TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1, 1000));
	} catch (InterruptedException var4) {
	}

	int randomInt = ThreadLocalRandom.current().nextInt(1, 1000);
//	return rewardsWebClient.getRewardPointsWebClient(attractionId, userId);
	System.out.println("random int rewardpoints : " + randomInt);
return randomInt;
}
	//	@GetMapping("/getDistance")
	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

	public List<Integer> getRewards(User user) {
	VisitedLocation visitedLocation = user.getLastVisitedLocation();
		System.out.println(user.getLastVisitedLocation());
		calculateRewards(user);
		List<Attraction> attractionList = gpsUtilWebClient.getAllAttractions();
		List<AttractionDTO> attractionDTOList = new ArrayList<>();

		for (Attraction attraction : attractionList) {
			UserReward userReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
//			tourGuideService.getNearByAttractions(visitedLocation, user);
			user.addUserReward(userReward);
//			System.out.println("getrewardsservice" + tourGuideService.getNearByAttractions(visitedLocation, user));
			AttractionDTO attractionDTO = new AttractionDTO(getRewardPoints(attraction, user));
			attractionDTOList.add(attractionDTO);

			attractionDTOList.stream().map(AttractionDTO::getRewardPoints).collect(Collectors.toList());
			System.out.println(attractionDTOList);
		}

		return attractionDTOList.stream().map(AttractionDTO::getRewardPoints).collect(Collectors.toList());
	}

/*	public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
		try {
			TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(1, 1000));
		} catch (InterruptedException var4) {
		}

		int randomInt = ThreadLocalRandom.current().nextInt(1, 1000);
		return randomInt;
	}*/
}
