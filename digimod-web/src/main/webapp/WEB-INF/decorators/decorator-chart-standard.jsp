<!DOCTYPE html>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<!-- META DATA -->
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="application-name" content="PT Emobile Indonesia - SAMBA (Single Apps Multi Bank)" />
		<meta name="description" content="PT eMobile Indonesia provides mobile banking, internet banking and electronic banking solutions. Multiplatform Mobile Banking System (MMBS), Mobile Payment (mATM), Mobile Wallet (mWallet), Broadcast Management System (BMS) and Mobile Approval (MA)." />
		<meta name="keywords" content="PT Emobile Indonesia, Mobile Banking, Internet Banking, Electronic Banking, BII Mobile Banking, Bank Mega Mobile, Bank Mayapada Mobile Banking, Multiplatform Mobile Banking System, Mobile Payment (mATM), Mobile Wallet (mWallet), Broadcast Management System (BMS), Mobile Approval (MA)" />
		<meta name="author" content="PT Emobile Indonesia" />
		<meta name="designer" content="PT Emobile Indonesia" />
		<meta name="copyright" content="Copyright 2014 PT Emobile Indonesia" />
		<meta name="dcterms.description" content="PT eMobile Indonesia provides mobile banking, internet banking and electronic banking solutions. Multiplatform Mobile Banking System (MMBS), Mobile Payment (mATM), Mobile Wallet (mWallet), Broadcast Management System (BMS) and Mobile Approval (MA)." />
		<meta name="dcterms.creator" content="PT Emobile Indonesia" />
		<meta name="dcterms.rightsHolder" content="PT Emobile Indonesia" />
		<meta name="dcterms.rights" content="All rights reserved. No part of this document may be reproduced or transmitted in any form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without prior written permission from the Author, PT Emobile Indonesia." />
		<meta name="dcterms.dateCopyrighted" content="2014" />
		<meta content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport" />
		<title>PT Emobile Indonesia - Samba, <decorator:title default="Welcome" /></title>
		
		<!-- FAVICON -->
		<link href="<s:url value='/img/favicon.ico'/>" rel="shortcut icon" type="image/png"/>
		
		<!-- CSS -->
		<link id="csslayout" href="<s:url value='/Style/CSS/mmbslayout.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssfont" href="<s:url value='/Style/CSS/mmbsfont.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssinput" href="<s:url value='/Style/CSS/mmbsinput.css'/>" rel="stylesheet" type="text/css" />
		<link id="csslink" href="<s:url value='/Style/CSS/mmbslink.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssfontsize" href="<s:url value='/Style/CSS/mmbsfontsize-medium.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssfontfamily" href="<s:url value='/Style/CSS/mmbsfontfamily-utsaahregular.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssdatetime" href="<s:url value='/Style/CSS/jquery.datetimepicker.css'/>" rel="stylesheet" type="text/css" />
		<link id="csstheme" href="<s:url value='/Style/CSS/mmbstheme-telkomsel-svg.css'/>" rel="stylesheet" type="text/css" />
		<!--<link id="cssresponsive" href="emobileindonesiaresponsive.css" rel="stylesheet" type="text/css"/>-->
		
		<!-- JAVA SCRIPT -->
		<script type="text/javascript" src="<s:url value='/Java Script/jquery-1.11.1.min.js'/>"></script>
		<script type="text/javascript" src="<s:url value='/Java Script/jquery-migrate-1.2.1.min.js'/>"></script>
		<script type="text/javascript" src="<s:url value='/Java Script/jquery-scrollto.js'/>"></script>
		<script type="text/javascript" src="<s:url value='/Java Script/jquery.mousewheel.min.js'/>"></script>
		<script type="text/javascript" src="<s:url value='/Java Script/emobile.js'/>"></script>
		<script type="text/javascript" src="<s:url value='/Java Script/jquery.simplr.smoothscroll.min.js'/>"></script>
		<script type="text/javascript" src="<s:url value='/Java Script/jquery.datetimepicker.js'/>"></script>
		<script type="text/javascript" src="<s:url value='/Java Script/Chart.min.js'/>"></script>
		
		<!-- IE 8 COMPATIBILITY -->
		<!--[if lt IE 9]>
	        <script type="text/javascript">
	        	// HTML5 SEMANTIC ELEMENTS
	        	var HTML5SemanticElements = 
	            (
	                "abbr,article,aside,audio,canvas,datalist,details," +
	                "figure,footer,header,hgroup,main,mark,menu,meter,nav,output," +
	                "progress,section,summary,time,video"
	            ).split(',');
	             
	            for (var i = 0; i < HTML5SemanticElements.length; i++) 
	            {
	            	document.createElement(HTML5SemanticElements[i]);
	            }
	        </script>
	    <![endif]-->
		
		<!-- INITIALIZATION -->
		<%@ include file="/WEB-INF/includes/include_script_initialization.jsp"%>
		<%@ include file="/WEB-INF/includes/include_script_table_generator.jsp"%>
		<s:url var="loginUrl" action="UserData!processInput" />
		
		<!-- ON DOCUMENT LOAD -->
		<script type="text/javascript">
			$(window).load(function()
			{
				// PROGRESS PAGE
		
				ProgressPage("#ProgressPage", 1);

				
				// NAVIGATION
				
				NavigationNeutralizer("GroupNavigation");
			});
		</script>
		
		<!-- ON DOCUMENT READY -->
		<script type="text/javascript">
			$(document).ready(function() 
			{
				// COMPATIBILITY CHECK

				BrowserCheck("csslayout", "mmbslayout_ie11", "mmbslayout_ielte10", "mmbslayout_moz", "mmbslayout");
				BrowserCheck("cssinput", "mmbsinput_ie", "mmbsinput_ie", "mmbsinput", "mmbsinput");
				
				
				// DISPLAY SETTING
				
					// THEME
					
					ThemeArray = [ "Persian Blue", "Snowy Owl", "Vintage Afternoon", "Clear River", "Hermonville Vineyards", "Tomato Fever", "Telkomsel" ];
					ThemeCSSArray = CSSArrayInitialization(ThemeArray);
					ThemeSelector(CurrentTheme, "csstheme", ThemeArray, ThemeCSSArray);
					ImageSelector(CurrentTheme, "#LogoMMBS");
					ImageSelector(CurrentTheme, "#LogoEmobile");
					ImageSelector(CurrentTheme, "#IconHome");
	
					// FONT SIZE
	
					FontSizeArray = [ "Small", "Medium", "Large" ];
					FontSizeCSSArray = CSSArrayInitialization(FontSizeArray);
					FontSizeSelector(CurrentFontSize, "cssfontsize", FontSizeArray, FontSizeCSSArray);
	
					// FONT FAMILY
	
					FontFamilyArray = [ "Utsaah Regular", "AgencyFB Regular", "CourierNew Regular", "Futura CondensedMedium" ];
					FontFamilyCSSArray = CSSArrayInitialization(FontFamilyArray);
					FontFamilySelector(CurrentFontFamily, "cssfontfamily", FontFamilyArray, FontFamilyCSSArray);
	
					// LANGUAGE
	
					LanguageArray = [ "Bahasa Indonesia", "English" ];

				ProgressInitialization("#ProgressRequest");
				RefreshGreeting("#ContainerGreeting");

				// SRSMOOTHSCROLL

			/*	$(function() 
				{
					$.srSmoothscroll
					({
						step : 150,
						speed : 500,
						ease : 'swing'
					});
				}); */

				// INPUT

				DateTimeInitialization(".Date", ".Time", ".DateTime");

				// BUTTON

				ButtonToTop("#ButtonToTop");
				ModeFullScreen(document.documentElement, "#ButtonFullScreen");
			});
		</script>
		
		<!-- IE 8 COMPATIBILITY -->
		<!--[if lt IE 9]>
	        <script type="text/javascript">
	        	// ON DOCUMENT READY
	        	$(document).ready(function()
				{
	            	// SVG COMPATIBILITY
	                SVGCompatibility();
	                
	                // RADIO AND CHECKBOX COMPATIBILITY
	                RadioAndCheckboxForIE8();
	                
	                // LABEL COMPATIBILITY
	                for(var i = 0; i < NavigationCounter; i++)
	                {
	                	LabelForIE8("#Trigger" + NavigationCounter, "#Navigation" + NavigationCounter);
	                }
	            });
        <![endif]-->
		
		<!-- ON DOCUMENT RESIZE -->
		<script type="text/javascript">
			$(document).resize(function() 
			{
		
			});
		</script>
		
		<!-- ON MOUSE SCROLL -->
		<script type="text/javascript">
			$(window).scroll(function() 
			{
				// NAVIGATION
				
				NavigationFixed("nav");
				ButtonToTopState("#ButtonToTop");
			});
		</script>
		
		<!-- ON DOCUMENT CLICK -->
		<script type="text/javascript">
			$(document).click(function(e) 
			{
				// NAVIGATION
				
				if($(e.target).parents().is($("nav")) == true)
				{
					$("input:radio[name='"+'GroupNavigation'+"']").change(function() 
					{
						for(var i = 0; i < NavigationCounter; i++)
						{
							NavigationEffect("#Navigation" + i, "#NavigationItem" + i);
						}
					});
				}
				else
				{
					$("nav ul .SubMenu").each(function(index, element) 
					{
						if($(this).css("display").toLowerCase() == "block")
						{
							$(this).css("display", "none");
						}
						else
						{
							
						};
                    });
					
					$("input:radio[name='"+'GroupNavigation'+"']").each(function(index, element) 
					{
						$(this).prop("checked", false);
					});
				}
			});
		</script>
		
		<decorator:head />
	</head>
	
	<body>
		<!-- CONDITION FOR IE 8 AND LOWER -->
		<%@ include file="/WEB-INF/includes/include_progress_ie8.jsp"%>
	
		<!-- PLEASE WAIT WHILE WE'RE SETTING THINGS UP FOR YOU -->
		<%@ include file="/WEB-INF/includes/include_progress_page.jsp"%>
	
		<!-- PLEASE WAIT WHILE WE PROCESS YOUR REQUEST -->
		<%@ include file="/WEB-INF/includes/include_progress_request.jsp"%>
	
		<!-- ANCHOR LINK TO TOP -->
		<%@ include file="/WEB-INF/includes/include_button_totop.jsp"%>
	
		<!-- HEADER -->
		<%@ include file="/WEB-INF/includes/include_header_general.jsp"%>
	
		<!-- NAVIGATION -->
		<nav>
			<a href="./MainMenu.web" class="ClearHyperlink"><img src="<s:url value='/Resource/Icon/Persian Blue/icon_home.svg'/>" alt="PT Emobile Indonesia - MMBS, Home Icon" id="IconHome" style="display: none;"/></a>
			
			<img src="<s:url value='/Resource/Logo/logo_bankmega.svg'/>" alt="PT Emobile Indonesia - MMBS, Bank Mega Logo" id="LogoClient" style="opacity : 0;"/>
			
			<s:set name="levelVO" value="%{#session.LOGIN_KEY.levelVO}" />
			<ul>
				<s:iterator value="#levelVO.listMenu" status="Counter">
					<label for="Navigation<s:property value='%{#Counter.index}'/>" id="Trigger<s:property value='%{#Counter.index}'/>">
						<li>
							<s:property value="menuText" />
							
						</li>
					</label>
					<input type="radio" id="Navigation<s:property value='%{#Counter.index}'/>" name="GroupNavigation" /> 
					
					<s:if test="hasChild()">
						<ul id="NavigationItem<s:property value='%{#Counter.index}'/>" class="SubMenu">
							<s:iterator value="childs">
		                        <s:url id="aUrl" action="%{menuUrl}" includeParams="none" /> 
								<s:a href="%{aUrl}" cssClass="ClearHyperlink">
									<li>
										<s:property value="menuText" />
									</li>
								</s:a>
							</s:iterator>
						</ul>
					</s:if>
					
					<script type="text/javascript">
						NavigationCounter++;
					</script>
				</s:iterator>
			</ul>
		</nav>
	
		<decorator:body />
	</body>
	
	<!-- FOOTER -->
	<%@ include file="/WEB-INF/includes/include_footer_general.jsp"%>
</html>