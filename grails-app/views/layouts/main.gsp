<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
 "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
   <title>Spiv &raquo; <g:layoutTitle default="Welcome"/></title>
   <link rel="stylesheet" href="${resource(dir:'css',file:'reset-fonts-grids.css')}" type="text/css">
   <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
    <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
   <g:layoutHead />
   <g:javascript library="application" />
   <g:javascript library="prototype"/>
</head>
<body>
<div id="doc2" class="yui-t1">
   <div id="hd" role="banner"><h1>Stock market profiteer</h1>
  		
   <div id="spinner" style="display:none;"><img src="${resource(dir:'images',file:'spinner.gif')}" alt="Spinner" /></div>
   </div>
   <div id="bd" role="main">
	<div id="yui-main">
	<div class="yui-b"><div class="yui-g">
	<g:layoutBody />
	</div>
</div>
	</div>
	<div class="yui-b"></div>
	
	</div>
   <div id="ft" role="contentinfo">
   	<p>${grailsApplication.config.grails.projectName} - stock market investment follower. <span>Version: <g:meta name="app.version"/> on Grails: <g:meta name="app.grails.version"/></span>
   	</p>
   </div>
</div>
</body>
</html>
