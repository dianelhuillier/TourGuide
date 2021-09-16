/*
package tourGuide.proxy;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
*/
/*import tourGuide.beans.AttractionBean;
import tourGuide.beans.VisitedLocationBean;*//*


import java.util.List;
import java.util.UUID;



@FeignClient(name = "GpsUtil-microservice", url = "${gpsutil.url}") //localhost:8081")
public interface GpsUtilProxy {

    @GetMapping(value = "/gpsUtil/getUserLocation/{userId}")
    VisitedLocation getUserLocation(@PathVariable ("userId") UUID userId);

    @GetMapping(value = "/gpsUtil/getAttractions")
    List<Attraction> getAttractions();

}
*/
