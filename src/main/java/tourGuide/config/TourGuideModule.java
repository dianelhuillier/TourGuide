package tourGuide.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
//import tourGuide.proxy.GpsUtilProxy;
import tourGuide.service.RewardsService;
import tourGuide.webClient.GpsUtilWebClient;
import tourGuide.DTO.*;
import tourGuide.webClient.RewardsWebClient;

@Configuration
public class TourGuideModule {


	@Autowired
	GpsUtilWebClient gpsUtilWebClient;

	@Bean
	public GpsUtilWebClient getGpsUtilWebClient() {
		return new GpsUtilWebClient();
	}

	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtilWebClient(), getRewardsCentralUri());
	}
	
	@Bean
	public RewardsWebClient getRewardsCentralUri() {
		return new RewardsWebClient();
	}
	
}
