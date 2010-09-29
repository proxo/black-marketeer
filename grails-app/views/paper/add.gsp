<html>
<head>
	<title>Adding a new company paper</title>
	<meta name="layout" content="main"/>
	<gui:resources components="['tooltip','datePicker']"/>
</head>
<body>
	<p style="font-size: 120%;"><b>Add Company paper</b></p>
	<div>
		<g:hasErrors>
			<div class="errors">
				<g:renderErrors bean="${paper}" as="list" />
			</div>
		</g:hasErrors>
		<g:form>
			<dl style="margin-top: 10px; margin-bottom: 50px;">
				<dt>Name:</dt>
				<dd style="margin-bottom: 5px;">
					<g:textField name="name" value="${paper?.company?.name}" />
				</dd>
				
				<dt>CEO:</dt>
				<dd style="margin-bottom: 5px;">
					<g:textField name="CEO" value="${paper?.company?.CEO}" />
				</dd>
				
				<dt>Code:</dt>
				<dd style="margin-bottom: 5px;">
					<g:textField name="paperCode" value="${paper?.paperCode}" />
				</dd>
				
				<dt>IPO date:</dt>
				<dd style="margin-bottom: 5px;">
					<g:datePicker name="ipoDate" value="${paper?.ipoDate}" precision="day"/>
				</dd>	
				
				<dt>&nbsp;</dt><dd>&nbsp;</dd>
				<dt><g:actionSubmit name="save" value="Save" action="save"/>&nbsp;<g:actionSubmit name="cancel" value="Cancel" action="cancel"/></dt>
			</dl>
		</g:form>
	</div>
</body>
</html>