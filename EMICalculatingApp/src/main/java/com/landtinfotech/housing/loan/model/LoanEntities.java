package com.landtinfotech.housing.loan.model;

/*
 * The business model class of this application
 */

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class LoanEntities {

	//For simplicity, lets assume bank gives minimum loan amount of 1rs and maximum of 10lakhs
	@Min(1)
	@Max(1000000)
	private int loanAmount;
	
	//Assuming minimum loan term as 1year and maximum 10years
	@Min(1)
	@Max(10)
	private int termInYears;
	
	//Different types of loan given by bank
	private String loanType;
	
	//Loan interest for different loan types
	private double interestRate;
	
	//EMI per month
	private double emiInMonths;
	
	//Final loan amount which will be paid to the bank
	private double finalLoanAmount;

	//Getters and setters...
	public int getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(int loanAmount) {
		this.loanAmount = loanAmount;
	}

	public int getTermInYears() {
		return termInYears;
	}

	public void setTermInYears(int termInYears) {
		this.termInYears = termInYears;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getEmiInMonths() {
		return emiInMonths;
	}

	public void setEmiInMonths(double emiInMonths) {
		this.emiInMonths = emiInMonths;
	}

	public double getFinalLoanAmount() {
		return finalLoanAmount;
	}

	public void setFinalLoanAmount(double finalLoanAmount) {
		this.finalLoanAmount = finalLoanAmount;
	}
	
}
