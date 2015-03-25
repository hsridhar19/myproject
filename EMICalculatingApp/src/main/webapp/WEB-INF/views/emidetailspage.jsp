<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EMI Details</title>
</head>
<body>
	<div align="center">
		<table border="0">
			<tr>
				<td colspan="2" align="center">
					<h3>Your EMI details are as follows</h3>
				</td>
			</tr>
			<tr>
				<td>Loan Type:</td>
				<td>${loanEntities.loanType}</td>
			</tr>
			<tr>
				<td>Loan Amount:</td>
				<td>${loanEntities.loanAmount}</td>
			</tr>
			<tr>
				<td>Terms in years:</td>
				<td>${loanEntities.termInYears}</td>
			</tr>
			<tr>
				<td>Interest Rate:</td>
				<td>${loanEntities.interestRate}</td>
			</tr>
			<tr>
				<td>EMI in months:</td>
				<td>${loanEntities.emiInMonths}</td>
			</tr>
			<tr>
				<td>Final Loan Amount:</td>
				<td>${loanEntities.finalLoanAmount}</td>
			</tr>
		</table>
	</div>
</body>
</html>