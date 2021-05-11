package uk.tw.energy.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.tw.energy.exception.JOIEnergyException;
import uk.tw.energy.service.AccountService;

@RestController
@RequestMapping("/usage")
public class UserUsageController {
	
	private final AccountService acccountService;
	
	public UserUsageController(AccountService acccountService) {
		this.acccountService=acccountService;
	}
	
	@GetMapping("/week/{smartId}")
	public ResponseEntity getUsageCostWeek(@PathVariable("smartId") String smartId ) {
		
		
		Map<String, BigDecimal> weekUsageCostMap;
		try {
			weekUsageCostMap= new HashMap<String, BigDecimal>();
			weekUsageCostMap = acccountService.getCostOfWeekUsage(smartId);
			return ResponseEntity.ok(weekUsageCostMap);
		} catch (JOIEnergyException e) {
			
			ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
		
		
		 
	}

}
