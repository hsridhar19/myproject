package com.landtinfotech.simple.loan.calculatorinterface.impl.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.landtinfotech.simple.loan.calculatorinterface.impl.MonthlyPaymentCalculatorImpl;

public class MonthlyPaymentCalculatorImplTest {

	MonthlyPaymentCalculatorImpl monthlyPay;

	@SuppressWarnings("deprecation")
	@Test
	public void monthlyPayPositive() {
		monthlyPay = new MonthlyPaymentCalculatorImpl();

		double result = monthlyPay.calculateFinalAmount(1000, 1);
		double delta = 1.0;
		assertEquals(12000.0,result,delta);

		result= 1 + 2;
		assertTrue(result == 3);
	}

	@Test
	public void monthlyPayNegative() {
		monthlyPay = new MonthlyPaymentCalculatorImpl();
		double result= monthlyPay.calculateFinalAmount(1000, 1);
		assertTrue(result != 6);
	}
}
