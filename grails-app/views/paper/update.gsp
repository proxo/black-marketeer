<html>
<head>
	<title>Updating paper</title>
	<meta name="layout" content="main"/>
	<gui:resources components="['tooltip','datePicker']"/>
	<g:javascript>
		function setFetchedPrice(e) {
			if (e.responseJSON.result) {
				var p = e.responseJSON.result.value;
				$("lastPrice").value = p; 
			}
		}
	</g:javascript>
</head>
<body>
	<p style="font-size: 120%; margin-bottom: 10px;"><b>Update company paper</b></p>
	<div>
		<table width="100%" border="0">
		<tr>
			<td>
			<g:hasErrors>
				<div class="errors">
					<g:renderErrors bean="${paper}" as="list" />
				</div>
			</g:hasErrors>
			
			<g:form action="update">
				<dl style="margin-top: 10px; margin-bottom: 50px;">
					<dt>Name: <g:textField name="paperCode" value="${paper?.company?.name}" /> </dt>
					<dt>&nbsp;</dt>
					
					<dt>Paper code: <g:textField name="paperCode" value="${paper?.paperCode}" /> </dt>
					<dt>&nbsp;</dt>
					
					<dt>Last price: <g:textField name="lastPrice" value="${formatNumber(number:paper?.summary?.lastPrice,type:'currency',currencyCode:'PLN')}" disabled="true"/></dt>
					<dt>&nbsp;</dt><dd>&nbsp;</dd>
					<dt>
						<g:hiddenField name="id" value="${paper?.id}" />
					</dt>
				</dl>
							
				<g:actionSubmit action="update" value="Update" />
				<g:actionSubmit action="cancel" value="Cancel" />
			</g:form>
			</td>
		
			<td width="60%">
				<dl style="margin-top: 10px; margin-bottom: 50px;">
					<dt>Number of papers: ${paper?.summary.numOfPapers} sztuk</dt>
					<dt>&nbsp;</dt>
					<dt>Total provision: <g:formatNumber number="${paper?.summary.totalProvision}" type="currency" currencyCode="PLN" /></dt>
					<dt>&nbsp;</dt>
					<dt>Average price: <g:formatNumber number="${paper?.summary.averagePrice}" type="currency" currencyCode="PLN" /></dt>
					<dt>&nbsp;</dt>
					<dt>Stop loss price: <g:formatNumber number="${paper?.summary.stopLossPrice}" type="currency" currencyCode="PLN" /></dt>
					<dt>&nbsp;</dt>
					<dt>Break even price: <g:formatNumber number="${paper?.summary.breakEvenPrice}" type="currency" currencyCode="PLN" /></dt>
					<dt>&nbsp;</dt>
					<dt>Total invested: <g:formatNumber number="${paper?.summary.totalInvested}" type="currency" currencyCode="PLN" /></dt>
					<dt>&nbsp;</dt>
					<dt>Profit: <g:formatNumber number="${paper?.summary.profit}" type="currency" currencyCode="PLN" /></dt>
					<dt>&nbsp;</dt>
					<dt>Profit after tax: <g:formatNumber number="${paper?.summary.profitAfterTax}" type="currency" currencyCode="PLN" /></dt>
					<dt>&nbsp;</dt>
				</dl>
			</td>
		
		</tr>
		</table>
		
		<div style="margin-top: 30px; margin-bottom: 30px;">
		<table width="100%">
			<tr>
				<th>Type</th>
				<th>Date</th>
				<th>Price</th>
				<th>Num of papers</th>
				<th>Provision</th>
			</tr>
			<g:each in="${paper.transactions}" var="t">
			<tr>
				<td>
					<g:if test="${t.side.name() == 'BUY'}">
						<img src="<g:resource dir="images" file="go-up.png" />" alt="buy" />
					</g:if>
					<g:else>
						<img src="<g:resource dir="images" file="go-down.png" />" alt="sell" />
					</g:else>
				</td>
				<td>
					<g:formatDate format="yyyy-MM-dd" date="${t.tradeDate}" />
				</td>
				
				<td>
					<g:formatNumber number="${t.price}" type="currency" currencyCode="PLN" />
				</td>
				<td>${t.numOfPapers}</td>
				<td>
					<g:formatNumber number="${t.provision}" type="currency" currencyCode="PLN" />
				</td>

			</tr>
			</g:each> 
		</table>
		</div>
	</div>
</body>
</html>