package uk.tw.energy.service;

import org.springframework.stereotype.Service;

import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.exception.JOIEnergyException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AccountService {

    private final Map<String, String> smartMeterToPricePlanAccounts;
    
    private final MeterReadingService meterReadingService;
    
    private final PricePlanService pricePlanService;

    public AccountService(Map<String, String> smartMeterToPricePlanAccounts,
    		                 MeterReadingService meterReadingService, PricePlanService pricePlanService) {
    	this.meterReadingService=meterReadingService;
        this.smartMeterToPricePlanAccounts = smartMeterToPricePlanAccounts;
        this.pricePlanService = pricePlanService;
    }

    public String getPricePlanIdForSmartMeterId(String smartMeterId) {
        return smartMeterToPricePlanAccounts.get(smartMeterId);
    }
    
        //get readings using smart id -getReadings()
    
    public Map<String, BigDecimal> getCostOfWeekUsage(String smartMeterId) throws JOIEnergyException {
    	Optional<List<ElectricityReading>> initialReadings = meterReadingService.getReadings(smartMeterId);
    	if(initialReadings.isPresent()) {
    		
    		
    	List<ElectricityReading> lastWeeekReadings = 	initialReadings.get()
    			                                                     .stream()
    			                                                      .filter(reading->isReadingOfLastWeek(reading.getTime()))
    			                                                      .collect(Collectors.toList());
    	
    	
    	
    	Map<String, BigDecimal> weeklyCostUsage = pricePlanService.getCostForPlanId(lastWeeekReadings, 
    			                                               getPricePlanIdForSmartMeterId(smartMeterId));
    	
    	
    	return weeklyCostUsage;
    		
    	}
    	else {
    		throw new JOIEnergyException("No Readings found for"+smartMeterId);
    	}
		
    	
    	
    }
    
    private boolean isReadingOfLastWeek(Instant currentReading) {
    	
    	return Instant.now().minus(Duration.ofDays(7)).isBefore(currentReading);
    }
		//get the readings of last week
  		//calculateCost 
    
    
}
