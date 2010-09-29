<%@ page import="pl.empit.spiv.model.TransactionSide" %>
<html>
<head>
	<title>Adding a new paper transaction for ${paper?.paperName}</title>
	<meta name="layout" content="main"/>
	<gui:resources components="['tooltip','datePicker']"/>
	<g:javascript>
		function setProvision(e) {
			if (e.responseJSON.result) {
				var p = e.responseJSON.result.value;
				$("provision").value = p; 
			}
		}
	</g:javascript>
</head>
<body>
	<p style="font-size: 120%;"><b>Add Company paper</b></p>
	<div>
		<g:hasErrors>
			<div class="errors">
				<g:renderErrors bean="${pt}" as="list" />
			</div>
		</g:hasErrors>
		<g:form action="save">
			<dl style="margin-top: 10px; margin-bottom: 50px;">
				<dt>Trade date:</dt>
				<dd style="margin-bottom: 5px;">
					<g:datePicker name="name" value="${pt?.tradeDate}" precision="minute"/>
				</dd>
				
				<dt>Comment:</dt>
				<dd style="margin-bottom: 5px;">
					<g:textArea name="comment" value="${pt?.comment}" rows="5" cols="60"/>
				</dd>
				
				<dt>Provision:</dt>
				<dd style="margin-bottom: 5px;">
					<g:textField name="provision" value="${pt?.provision}" />  <g:submitToRemote action="calculateProvision" onSuccess="setProvision(e)" value="Calculate"/>
				</dd>
				
				<dt>Price:</dt>
				<dd style="margin-bottom: 5px;">
					<g:textField name="price" value="${pt?.price}">
						<g:formatNumber number="${pt?.price}" type="currency" currencyCode="PLN" />
					</g:textField>
				</dd>	
				
				<dt>Number of papers:</dt>
				<dd style="margin-bottom: 5px;">
					<g:textField name="numOfPapers" value="${pt?.numOfPapers}" />
				</dd>
				
				<dt>Number of papers:</dt>
				<dd style="margin: 7px;">
					<span style="padding: 10px;">Buy&nbsp;<g:radio name="side" value="${TransactionSide.BUY}" checked="${pt?.side == TransactionSide.BUY}" /></span>
					<span style="padding: 10px;">Sell&nbsp;<g:radio name="side" value="${TransactionSide.SELL}" checked="${pt?.side == TransactionSide.SELL}" >Sell</g:radio></span>
				</dd>
				<g:hiddenField name="paperId" value="${paperId}"/>
				<dt>&nbsp;</dt><dd>&nbsp;</dd>
				<dt>
					<g:submitButton 
						name="save" value="Save"/>
					&nbsp;
				
				</dt>
			</dl>
		</g:form>
	</div>
</body>
</html>