package com.landtinfotech.housing.loan.controller;

/*
 * The main spring controller which receives the user inputs and computes the emi and displays the details back to user 
 */

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.landtinfotech.housing.loan.model.LoanEntities;
import com.landtinfotech.simple.loan.calculatorinterface.MonthlyPaymentCalculator;
import com.landtinfotech.simple.loan.calculatorinterface.impl.MonthlyPaymentCalculatorImpl;

@Controller
public class LoanController {

	// Get method to display the page where user enters the loan details.
	@RequestMapping(method = RequestMethod.GET, value="/loandetailspage")
	public String viewLoanOptions(Map<String, Object> model ,RedirectAttributes redirectAttributes) {

		Map<String,String> loanLists = new LinkedHashMap<String,String>();

		/*Can be Extended by populating List of Loans from DB and Using Jquery in UI for Select 
		  loanLists.put("Vehicle Loan", "Vehicle Loan");
		  loanLists.put("Gold Loan", "Gold Loan");
		*/

		if(!model.containsKey("loanEntities")){
			LoanEntities loanEntities = new LoanEntities();
			loanEntities.setInterestRate(3.5);//hardcoded because only housing loan is implemented
			loanLists.put("Housing Loan", "Housing Loan");
			model.put("Loanlist", loanLists);
			model.put("loanEntities", loanEntities);
		}
		return "loandetailspage";
	}

	// Post method which receives the loan details from user, computes the emi and displays the details back to user
	@RequestMapping(method = RequestMethod.POST, value="calculate")
	public String processloanDetails(@Valid LoanEntities loanEntities, BindingResult binding,
			RedirectAttributes redirectAttributes, HttpSession session) {

		//Check for basic validations, if errors found then return the same loan details page back to user
		if(binding.hasErrors())
		{
			redirectAttributes.addFlashAttribute("errorMessage", "RECTIFY INPUT FOR AMOUNT/TERM");
			return "redirect:loandetailspage";
		}
		//No errors found so perform the emi calculation
		//Based on loan type, get the interest from DB and pass to below method
		//Currently Home loan is added with fixed interest of 3.5 so passing the values directly
		
		MonthlyPaymentCalculator mpc = new MonthlyPaymentCalculatorImpl();
		double emi = mpc.calculateMonthlyPayment(loanEntities.getLoanAmount(), loanEntities.getTermInYears(), 
				loanEntities.getInterestRate());
		double finalLoanAmount = mpc.calculateFinalAmount(emi, loanEntities.getTermInYears());
		
		loanEntities.setEmiInMonths(emi);
		loanEntities.setFinalLoanAmount(finalLoanAmount);
		return "emidetailspage";
	}
}
