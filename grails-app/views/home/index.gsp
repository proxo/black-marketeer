<html>
<head>
	<meta name="layout" content="main"/>
	<gui:resources components="['tooltip']"/>
	<export:resource />
</head>

<body>
	<h1 style="font-size: 120%; margin-bottom: 10px;"><b>Active stocks</b></h1>
	<g:if test="${flash.message}">
	<div class="message">${flash.message}</div>
	<div style="margin-bottom: 10px;"></div>
	</g:if>

	<div id="toolbar">
		<span>
			<g:link controller="paper" action="add"><img src="<g:resource dir="images" file="list-add.png" />"/></g:link>
		</span>
	</div>
	<div style="margin-bottom: 30px;">
	
	<table width="100%">
		<tr>
			<th>Code</th>
			<th>Num of papers</th>
			<th>Avg. buy price</th>
			<th>Last price</th>
			<th>BEP</th>
			<th>Profit</th>
			<th>Sum. Provision</th>
			<th>&nbsp;</th>
		</tr>	
		<g:each in="${papers}" var="p">
			<tr>
				<td>
					<gui:toolTip text="${p?.paperCode}">${p?.paperCode}</gui:toolTip>
				</td>
				<td>${p?.summary?.numOfPapers}</td>
				<td>
					<g:formatNumber number="${p?.summary?.averagePrice}" type="currency" currencyCode="PLN" />
				</td>
				<td>
					<g:formatNumber number="${p?.summary?.lastPrice}" type="currency" currencyCode="PLN" />
				</td>
				<td>
					<g:formatNumber number="${p?.summary?.breakEvenPrice}" type="currency" currencyCode="PLN" />
				</td>
				<td>
					<g:formatNumber number="${p?.summary?.profit}" type="currency" currencyCode="PLN" />
				</td>
				<td>
					<g:formatNumber number="${p?.summary?.provision}" type="currency" currencyCode="PLN" />
				</td>
				<td>
					<span style="margin-right:5px;">
						<g:link controller="paperTransaction" action="add" id="${p.id}">
							<img src="<g:resource dir="images" file="document-new.png" />"/>
						</g:link>
					</span>
					
					<span>
						<g:link controller="paper" action="prepareUpdate" id="${p.id}">
							<img src="<g:resource dir="images" file="view-refresh.png" />"/>
						</g:link>
					</span>
				</td>
			</tr>
		</g:each> 
	</table>
	<g:if test="${papers}">
		<span style="float:right;"><export:formats formats="['csv', 'excel', 'xml']" controller="paper" action="exportData"/></span>
	</g:if>
	</div>
</body>
</html>