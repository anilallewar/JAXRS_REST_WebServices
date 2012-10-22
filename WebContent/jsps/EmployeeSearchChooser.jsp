<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Choose the search criteria for employee search</title>

<style type="text/css">
	body{
		line-height: 1.5em;
		font-family: Arial, Helvetica, "Helvetica Neue", Geneva, sans-serif;
		margin: 0;
		padding: 10px;
	}
	table, th, td {
			border: 1px solid black;
			line-height: 1.5em;
			font-family: Arial, Helvetica, "Helvetica Neue", Geneva, sans-serif;
			margin: 0;
			padding: 5px;
	}
	th	{
			background-color:green;
			color:white;
	}
	.padded {
			padding: 15x;
	}
		
</style>
</head>
<body>
	<h2 style="color:red;"> Employee Search </h2>
	<br><br>
	<form name="employeeForm" action="/JAXRS_REST_WebServices/rest/employee" method="post">
		<table border=1 width="90%">
			
			<tr>
				<td>
					Please enter the NAME of the employee(optional)
					<br>
				</td>
				<td align="center">
					<input type="text" name="name"/>
				</td>
			</tr>
			
			<tr>
				<td>
					Please enter the DESIGNATION of the employee(optional)
					<br>
				</td>
				<td align="center">
					<input type="text" name="designation"/>
				</td>
			</tr>
			
			<tr>
				<td>
					Please enter the DEPARTMENT of the employee(optional)
					<br>
				</td>
				<td align="center">
					<input type="text" name="department"/>
				</td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<input type=submit value="Submit"/>
				</td>
			</tr>
			
		</table>
	</form>
</body>
</html>