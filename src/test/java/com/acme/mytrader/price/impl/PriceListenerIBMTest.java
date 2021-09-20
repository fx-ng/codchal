package com.acme.mytrader.price.impl;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;

public class PriceListenerIBMTest {

	PriceListener priceListener;

	ExecutionService executionService;

	@Before
	public void setUp() {
		executionService = mock(ExecutionService.class);
		priceListener = new PriceListenerIBM(executionService);
	}

	@Test
	public void priceUpdate_shouldNotExecuteBuyOrderMultipleTimes_whenPriceBelowTreshold() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD - 5.0D;

		//when
		priceListener.priceUpdate(securityToken, price);
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, times(1)).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}
		
	@Test
	public void priceUpdate_shouldNotExecuteBuyOrderMultipleTimes_whenPriceAboveTreshold() {
		// given
		String securityToken = "topSecret123";

		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD + 5.0D;

		//when
		priceListener.priceUpdate(securityToken, price);
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, never()).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}
	
	@Test
	public void priceUpdate_shouldNotExecuteBuyOrder_whenPriceEqualsTreshold() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD;

		//when
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, never()).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}
	
	@Test
	public void priceUpdate_shouldNotExecuteBuyOrder_whenPriceBelowTresholdAndEdgeOfTolerance() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD - PriceListenerIBM.DEFAULT_PRICE_TOLERANCE;

		//when
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, never()).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}
	
	@Test
	public void priceUpdate_shouldExecuteBuyOrder_whenPriceBelowTresholdAndOutOfTolerance() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD - 10* PriceListenerIBM.DEFAULT_PRICE_TOLERANCE;

		//when
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, times(1)).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}
	
	@Test
	public void priceUpdate_shouldNotExecuteBuyOrder_whenPriceBelowTresholdAndInsideOfTolerance() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD - 0.1 * PriceListenerIBM.DEFAULT_PRICE_TOLERANCE;

		//when
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, never()).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}

	@Test
	public void priceUpdate_shouldNotExecuteBuyOrder_whenPriceAboveTresholdAndEdgeOfTolerance() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD + PriceListenerIBM.DEFAULT_PRICE_TOLERANCE;

		//when
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, never()).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}
	
	@Test
	public void priceUpdate_shouldNotExecuteBuyOrder_whenPriceAboveTresholdAndOutOfTolerance() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD + 10* PriceListenerIBM.DEFAULT_PRICE_TOLERANCE;

		//when
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, never()).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}
	
	@Test
	public void priceUpdate_shouldNotExecuteBuyOrder_whenPriceAboveTresholdAndInsideOfTolerance() {
		// given
		String securityToken = "topSecret123";
		double price = PriceListenerIBM.DEFAULT_PRICE_TRESHOLD + 0.1 * PriceListenerIBM.DEFAULT_PRICE_TOLERANCE;

		//when
		priceListener.priceUpdate(securityToken, price);

		//then
		Mockito.verify(executionService, never()).buy(anyString(), anyDouble(), anyInt());
		Mockito.verify(executionService, never()).sell(anyString(), anyDouble(), anyInt());
	}


}
