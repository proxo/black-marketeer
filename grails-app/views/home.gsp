<html>
<head>
<meta name="layout" content="main"/>
</head>
<body>
	<formset>
	<legend>Active stocks</legend>
	<g:if test="${papers}">
		<table width="100%">
		
		<g:each in="${papers}" var ="p">
			<tr><td>${p?.paperCode}</td><td>${p?.dateCreated}</td></tr>
		</g:each> 
		</table>
	</g:if>
	</formset>
</body>
</html>