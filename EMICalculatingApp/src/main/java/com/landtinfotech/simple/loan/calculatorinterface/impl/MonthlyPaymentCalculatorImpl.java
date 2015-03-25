package com.landtinfotech.simple.loan.calculatorinterface.impl;

/*
 * Implementation class of the interface
 */
import com.landtinfotech.simple.loan.calculatorinterface.MonthlyPaymentCalculator;

public class MonthlyPaymentCalculatorImpl implements MonthlyPaymentCalculator {

	public double calculateMonthlyPayment(int loanAmount, int termInYears, double interest) {

		// Convert interest rate into a decimal  eg. 6.5% = 0.065	       
		interest /= 100.0;

		//Monthly interest rate is the yearly rate divided by 12	 
		double monthlyRate = interest / 12.0;

		//The length of the term in months is the number of years times 12
		int termInMonths = termInYears * 12;

		// Calculate the monthly payment
		// Typically this formula is provided so 
		// lets not go into the details
		double monthlyPayment = 
				(loanAmount*monthlyRate) / 
				(1-Math.pow(1+monthlyRate, -termInMonths));

		return monthlyPayment;
	}

	@Override
	public double calculateFinalAmount(double monthlyPayment, int termInYears) {
		
		// Calculate the monthly payment
		// Typically this formula is provided so 
		// lets not go into the details
		return monthlyPayment * (termInYears * 12);
	}
}
