package com.acme.mytrader.price.impl;

import java.util.ArrayList;
import java.util.List;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;

/**
 * This class automatically executes buy orders using a simplified buy strategy
 * (fixed price, fixed volume) for a given stock. (assumption here: IBM stock)
 * 
 * @author Kai Barkschat barkschat@thenextgen.de
 *
 */
public class PriceListenerIBM implements PriceListener {

	public static final double DEFAULT_PRICE_TRESHOLD = 55.0D;

	public static final double DEFAULT_PRICE_TOLERANCE = 1.0E-6;

	private double priceTreshold = DEFAULT_PRICE_TRESHOLD;

	private double priceTolerance = DEFAULT_PRICE_TOLERANCE;
	
	private List<Double> priceHistory = new ArrayList<>();

	private int volumeInLots = 100;

	private ExecutionService executionService;

	/**
	 * Using Constructor Injection, as no framework given by the challenge
	 * 
	 * @param executionService external dependency
	 * @param priceTreshold The target price or the limit, under which stocks are being bought
	 * @param priceTolerance acceptable tolerance-range used when working with floating point inexactness
	 */
	public PriceListenerIBM(ExecutionService executionService, double priceTreshold, int priceTolerance, int volumeInLots) {
		this.executionService = executionService;
		this.priceTreshold = priceTreshold;
		this.priceTolerance = priceTolerance;
		this.volumeInLots = volumeInLots;
	}

	/**
	 * Using Constructor Injection, as no framework given by the challenge
	 * 
	 * @param executionService external dependency
	 */
	public PriceListenerIBM(ExecutionService executionService) {
		this.executionService = executionService;
	}

	@Override
	public void priceUpdate(String security, double price) {

		// the following precondition-checks should be discussed/considered:
		// - security == null
		// - price < 0.0 D
		
		// assumption: buy only once, do not rebuy as long as price remains under
		// treshold
		if (priceLastUpdate() > priceTreshold) {

			if (price < priceTreshold && Math.abs(price - priceTreshold) > priceTolerance) {
				executionService.buy(security, price, volumeInLots);
			}
		}

		priceHistory.add(price);

	}

	/**
	 * Return the last known price from the history. 
	 * 
	 * The history is kept in RAM here and not persisted.
	 * Also there might be required some cleanup routines, as Memory is limited.
	 * 
	 * @return
	 */
	private double priceLastUpdate() {
		if (priceHistory.isEmpty()) {
			return Double.MAX_VALUE;
		} else {
			return priceHistory.get(priceHistory.size() -1);			
		}
	}

}
