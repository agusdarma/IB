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
		<title>Digimob</title>

		<!-- FAVICON -->
		<link href="<s:url value='/img/favicon.ico'/>" rel="shortcut icon" type="image/png"/>

        <!-- CSS -->
		<link id="cssfont" href="<s:url value='/Style/CSS/mmbsfont.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssinput" href="<s:url value='/Style/CSS/mmbsinput.css'/>" rel="stylesheet" type="text/css" />
		<link id="csslink" href="<s:url value='/Style/CSS/mmbslink.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssfontsize" href="<s:url value='/Style/CSS/mmbsfontsize-medium.css'/>" rel="stylesheet" type="text/css" />
		<link id="cssfontfamily" href="<s:url value='/Style/CSS/mmbsfontfamily-utsaahregular.css'/>" rel="stylesheet" type="text/css" />
        <!--[CSS Library Bootstrap 4.1]-->
		<link href="<s:url value='/Style/CSS/bootstrap/bootstrap.min.css'/>" rel="stylesheet">
		<!--[Iconset Font Awesome 5.1]-->
		<link href="<s:url value='/Style/CSS/fontawesome/css/fontawesome-all.min.css'/>" rel="stylesheet">
		<!--personalized css for this theme-->
		<link href="<s:url value='/Style/CSS/mmbstheme-telkomsel.css'/>" rel="stylesheet">
        <!--<link id="cssresponsive" href="emobileindonesiaresponsive.css" rel="stylesheet" type="text/css"/>-->

        <!-- JAVA SCRIPT -->
        <script type="text/javascript" src="<s:url value='/Java Script/jquery-1.11.1.min.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/jquery-migrate-1.2.1.min.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/jquery-scrollto.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/jquery.mousewheel.min.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/emobile.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/jquery.simplr.smoothscroll.min.js'/>"></script>

        <script type="text/javascript" src="<s:url value='/Java Script/jquery3/jquery-3.3.1.min.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/popper/popper.min.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/bootstrap/bootstrap.min.js'/>"></script>
        <script type="text/javascript" src="<s:url value='/Java Script/gijgo.min.js'/>"></script>
  		<link href="<s:url value='/Style/CSS/gijgo.css'/>" rel="stylesheet" type="text/css">

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
				NavigationNeutralizer("SubGroupNavigation");
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

				/* $(function()
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

					$("input:radio[name='" + 'SubGroupNavigation' + "']").change(function()
					{
						for (var i = 0; i < NavigationCounter; i++)
						{
							NavigationEffect("#Navigation" + i + "1", "#NavigationItem" + i + "1");
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

					$("input:radio[name='"+'SubGroupNavigation'+"']").each(function(index, element)
					{
						$(this).prop("checked", false);
					});
				}
			});
		</script>

		<!-- POPPER FOR SIDEBAR -->
	    <script type="text/javascript">
	        $(document).ready(function () {
	            $('#sidebarCollapse').on('click', function () {
	                $('#sidebar').toggleClass('active');
	                if ($("i", this).hasClass("fas fa-chevron-left")) {
	                	$("i", this).removeClass("fas fa-chevron-left");
	                	$("i", this).toggleClass("fas fa-chevron-right");
	                } else {
	                	$("i", this).removeClass("fas fa-chevron-right");
	                	$("i", this).toggleClass("fas fa-chevron-left");
	                }
	            });
	            
	            var headerMenu = '<s:property value="menuId" />';
	            if(headerMenu=='307'||headerMenu=='308')
	            {
		            document.getElementById('306').click();
	            }
	            document.getElementById(headerMenu.substring(0,1)).click();
	            $("#li"+headerMenu).toggleClass("red");
	            
	        });
	    </script>

		<decorator:head />
	</head>

	<body>
		<!-- CONDITION FOR IE 8 AND LOWER
		<%@ include file="/WEB-INF/includes/include_progress_ie8.jsp"%>

		<!-- PLEASE WAIT WHILE WE'RE SETTING THINGS UP FOR YOU
		<%@ include file="/WEB-INF/includes/include_progress_page.jsp"%>

		<!-- PLEASE WAIT WHILE WE PROCESS YOUR REQUEST
		<%@ include file="/WEB-INF/includes/include_progress_request.jsp"%>

		<!-- ANCHOR LINK TO TOP
		<%@ include file="/WEB-INF/includes/include_button_totop.jsp"%>

		<!-- HEADER
		<%@ include file="/WEB-INF/includes/include_header_general.jsp"%> -->

	<div class="wrapper">
        <!-- Sidebar  -->
        <nav id="sidebar">
            <div class="sidebar-header">
                <s:a href="%{home}" cssClass="ClearHyperlink"><img src="<s:url value='/Resource/Logo/mbanking-red.png'/>" alt="Telkomsel Samba" id="Logo"/></s:a>
            </div>
            <s:set name="levelVO" value="%{#session.LOGIN_KEY.levelVO}" />
            <ul class="list-unstyled components">
               	<s:iterator value="#levelVO.listMenu" status="Counter">
					<s:if test="hasChild()">
		                <li>
		                    <a href='#<s:property value="menuText.replaceAll(' ', '')" />' id='<s:property value="menuId" />' data-toggle="collapse" aria-expanded="false" class="dropdown-toggle"><s:property value="menuText" /></a>
		                    <ul class="collapse list-unstyled" id='<s:property value="menuText.replaceAll(' ', '')" />'>
		                    	<s:iterator value="childs">
		                    		<s:if test="hasChild()">
					                    <a href='#<s:property value="menuText.replaceAll(' ', '')" />' id='<s:property value="menuId" />' data-toggle="collapse" aria-expanded="false" class="dropdown-toggle"><s:property value="menuText" /></a>
					                    <ul class="collapse list-unstyled" id='<s:property value="menuText.replaceAll(' ', '')" />'>
					                    	<s:iterator value="childs">
						                        <li id='li<s:property value="menuId" />'>
													<s:url id="aUrl" action="%{menuUrl}" includeParams="none" />
													<s:a href="%{aUrl}" cssClass="ClearHyperlink dropdown-item" ><s:property value="menuText" /></s:a>
						                        </li>
					                       	</s:iterator>
					                    </ul>
		                    		</s:if>
		                    		<s:else>
				                        <li id='li<s:property value="menuId" />'>
											<s:url id="aUrl" action="%{menuUrl}" includeParams="none" />
											<s:a href="%{aUrl}" cssClass="ClearHyperlink dropdown-item"><s:property value="menuText" /></s:a>
				                        </li>
				                    </s:else>
			                    </s:iterator>
		                    </ul>
		                </li>
					</s:if>
					<s:else>
						<li class="nav-item">
							<s:property value="menuText" />
						</li>
					</s:else>
				</s:iterator>
            </ul>
        </nav>

        <!-- Page Content  -->
        <div id="content">

            <nav class="navbar navbar-expand-lg">
                <div class="container-fluid">

                    <button type="button" id="sidebarCollapse" class="btn btn-secondary">
                        <i class="fas fa-chevron-left"></i>
                    </button>
                    <button class="btn btn-dark d-inline-block d-lg-none ml-auto" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <i class="fas fa-align-justify"></i>
                    </button>

                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="nav navbar-nav ml-auto">
					      	<li class="nav-item dropdown">
							  	<button class="btn btn-outline-primary dropdown-toggle dropdown-root" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fas fa-user-circle" style="vertical-align: middle;"></i>&nbsp;<s:property value="%{#session.LOGIN_KEY.userCode}" />&nbsp;<i class="fas fa-caret-down"></i>
								</button>
						        </a>
						        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
						          <div class="dropdown-item" href="#">User Level: <mark><s:property value="%{#session.LOGIN_KEY.userLevelDisplay}"/></mark></div>
						          <div class="dropdown-divider"></div>
						          <div class="dropdown-item" href="#"><s:text name="w.lastLogin"/> :
                                <s:if test="%{#session.LOGIN_KEY.neverLogin}">
                                    <s:text name="w.NA" />
                                </s:if>
                                <s:else>
                                    <mark><s:date name="%{#session.LOGIN_KEY.lastLoginOn}" format="dd.MM.yyyy HH:mm:ss"/></mark>
                                </s:else></div>
						        </div>
					      	</li>
                            <li class="nav-item">
				                <s:url id="ButtonLogout" action="Logout" includeParams="none"/>
								<s:a href="%{ButtonLogout}" cssClass="ClearHyperlink">
									<button type="button" id="ButtonLogout" class="btn btn-danger ml-auto">
									  <i class="fas fa-power-off" style="vertical-align: middle;"></i>&nbsp;Logout
									</button>
					            </s:a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <decorator:body />

        </div>
    </div>
	</body>

	<!-- FOOTER
    <%@ include file="/WEB-INF/includes/include_footer_general.jsp"%> -->
</html>
