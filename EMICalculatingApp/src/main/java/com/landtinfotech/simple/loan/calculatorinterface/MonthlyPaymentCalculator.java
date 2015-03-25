package com.landtinfotech.simple.loan.calculatorinterface;

//Interface to calculate monthly and yearly loan amount
public interface MonthlyPaymentCalculator {

	// Method to calculate EMI with interest
	public double calculateMonthlyPayment(int loanAmount, int termInYears, double interest);
	
	// Method to calculate the Total loan amount with interest to be paid after specified loan duration
	public double calculateFinalAmount(double monthlyPayment, int termInYears);
	
}
