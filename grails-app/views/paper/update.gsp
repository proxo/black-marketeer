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
	<p style="font-size: 120%;"><b>Update company paper</b></p>
	<div>
		<g:hasErrors>
			<div class="errors">
				<g:renderErrors bean="${paper}" as="list" />
			</div>
		</g:hasErrors>
		
		<g:form action="update">
			<dl style="margin-top: 10px; margin-bottom: 50px;">
				<dt>Name: ${paper?.company?.name}</dt>
				<dt>&nbsp;</dt>
				<dt>Last price: <g:textField name="lastPrice" value="${paper?.summary?.lastPrice}"/>&nbsp;
					<g:submitToRemote controller="paper" action="fetchPrice" onSuccess="setFetchedPrice(e)" value="Fetch price"/>
				</dt>
				<dt>&nbsp;</dt><dd>&nbsp;</dd>
				<dt>
					<g:hiddenField name="id" value="${paper?.id}" />
					<g:submitButton 
						name="update" value="Save"/>
					
				</dt>
			</dl>
		</g:form>
		
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
				<td><g:formatNumber number="${t.price}" type="currency" currencyCode="PLN" /></td>
				<td>${t.numOfPapers}</td>
				<td><g:formatNumber number="${t.provision}" type="currency" currencyCode="PLN" /></td>

			</tr>
			</g:each> 
		</table>
		</div>
	</div>
</body>
</html>