<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>L and T Infotech Bank App</title>
</head>
<body>
	<div align="center">


		<form:form action="calculate" method="post" commandName="loanEntities">

			<table border="0">
				<tr>
					<td colspan="2" align="center"><h2>L and T Infotech Bank App</h2></td>
				</tr>
				<tr>
					<font color="red"> ${errorMessage}</font>
				</tr>
				<tr>
					<td>Loan Type:</td>
					<td><form:select path="loanType">
							<form:options items="${Loanlist}" />
						</form:select></td>
				</tr>
				<tr>
					<td>Interest:</td>
					<td><form:input path="interestRate" readonly="true" /></td>
				</tr>

				<tr>
					<td>Loan Amount:</td>
					<td><form:input path="loanAmount" /> <font color="red">
							<form:errors path="loanAmount"></form:errors>
					</font></td>
				</tr>
				<tr>
					<td>Terms in Years:</td>
					<td><form:input path="termInYears" /> <font color="red">
							<form:errors path="termInYears"></form:errors>
					</font></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="Calculate" /></td>
				</tr>
			</table>
		</form:form>
	</div>
</body>
</html>