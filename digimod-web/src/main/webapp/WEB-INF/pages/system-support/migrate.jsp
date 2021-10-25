<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.migrateAppsData" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="Migrate!processInput"/>
		<s:url var="findVersioning" action="Migrate!findVersioning"/>		
		<s:url var="exportBank" action="Migrate!exportBank" />
		<s:url var="exportMerchant" action="Migrate!exportMerchant" />
		<s:url var="exportMerchantBank" action="Migrate!exportMerchantBank" />
		<s:url var="exportAppsWording" action="Migrate!exportAppsWording" />
		<s:url var="exportAppsIcon" action="Migrate!exportAppsIcon" />
		<s:url var="exportAppsTrxType" action="Migrate!exportAppsTrxType" />
		<s:url var="exportAppsMenu" action="Migrate!exportAppsMenu" />
		<s:url var="exportAppsContent" action="Migrate!exportAppsContent" />
		<s:url var="exportCategory" action="Migrate!exportCategory" />
		<s:url var="exportListValue" action="Migrate!exportListValue" />
		<s:url var="exportCategoryBiller" action="Migrate!exportCategoryBiller" />
		<s:url var="exportListBiller" action="Migrate!exportListBiller" />
		<s:url var="exportAppsHomeScreen" action="Migrate!exportAppsHomeScreen" />
		
		<%@ include file="/WEB-INF/includes/include_script_table_generator.jsp"%>
		<script type="text/javascript">
			function additionalFunction()
			{
				jQuery.getJSON('<s:property value="%{findVersioning}" escape="false" />', function(data) 
				{
					var no = "No Update";
					var yes = "<font color='red'><b>Need to migrate</b></font>";
					if(data.bank==0)
					{
						$("#bank").text(no);
						$("#bankP").hide();
						$("#bankC").hide();
					}
					else
					{
						$("#bank").html(yes);
						$("#bankP").show();
						$("#bankC").show();						
					}
					
					if(data.merchant==0)
					{
						$("#merchant").text(no);	
						$("#merchantP").hide();
						$("#merchantC").hide();						
					}
					else
					{
						$("#merchant").html(yes);
						$("#merchantP").show();
						$("#merchantC").show();						
					}
					
					if(data.merchantBank==0)
					{
						$("#merchantBank").text(no);	
						$("#merchantBankP").hide();
						$("#merchantBankC").hide();						
					}
					else
					{
						$("#merchantBank").html(yes);
						$("#merchantBankP").show();
						$("#merchantBankC").show();						
					}
					
					if(data.appsWording==0)
					{
						$("#appsWording").text(no);
						$("#appsWordingP").hide();
						$("#appsWordingC").hide();	
					}
					else
					{
						$("#appsWording").html(yes);
						$("#appsWordingP").show();
						$("#appsWordingC").show();	
					}
					
					if(data.appsMenuLogo==0)
					{
						$("#appsMenuLogo").text(no);	
						$("#appsMenuLogoP").hide();
						$("#appsMenuLogoC").hide();	
					}
					else
					{
						$("#appsMenuLogo").html(yes);
						$("#appsMenuLogoP").show();
						$("#appsMenuLogoC").show();	
					}
					
					if(data.appsMenuTrxType==0)
					{
						$("#appsMenuTrxType").text(no);	
						$("#appsMenuTrxTypeP").hide();
						$("#appsMenuTrxTypeC").hide();	
					}
					else
					{
						$("#appsMenuTrxType").html(yes);
						$("#appsMenuTrxTypeP").show();
						$("#appsMenuTrxTypeC").show();	
					}
					
					if(data.appsMenu==0)
					{
						$("#appsMenu").text(no);
						$("#appsMenuP").hide();
						$("#appsMenuC").hide();	
					}
					else
					{
						$("#appsMenu").html(yes);
						$("#appsMenuP").show();
						$("#appsMenuC").show();	
					}
					
					if(data.appsContent==0)
					{
						$("#appsContent").text(no);	
						$("#appsContentP").hide();
						$("#appsContentC").hide();	
					}
					else
					{
						$("#appsContent").html(yes);
						$("#appsContentP").show();
						$("#appsContentC").show();	
					}
					
					if(data.category==0)
					{
						$("#category").text(no);	
						$("#categoryP").hide();
						$("#categoryC").hide();	
					}
					else
					{
						$("#category").html(yes);
						$("#categoryP").show();
						$("#categoryC").show();	
					}
					
					if(data.listValue==0)
					{
						$("#listValue").text(no);	
						$("#listValueP").hide();
						$("#listValueC").hide();	
					}
					else
					{
						$("#listValue").html(yes);
						$("#listValueP").show();
						$("#listValueC").show();	
					}
					
/* 					if(data.categoryBiller==0)
					{
						$("#categoryBiller").text(no);
						$("#categoryBillerP").hide();
						$("#categoryBillerC").hide();	
					}
					else
					{
						$("#categoryBiller").html(yes);
						$("#categoryBillerP").show();
						$("#categoryBillerC").show();	
					}
					
					if(data.listValueBiller==0)
					{
						$("#listValueBiller").text(no);	
					}
					else
					{
						$("#listValueBiller").html(yes);
					} */
					
					if(data.appsHomeScreen==0)
					{
						$("#appsHomeScreen").text(no);
						$("#appsHomeScreenP").hide();
						$("#appsHomeScreenC").hide();
					}
					else
					{
						$("#appsHomeScreen").html(yes);
						$("#appsHomeScreenP").hide();
						$("#appsHomeScreenC").hide();
					}
				});// end jquery json			
			}
		
			$(document).ready(function() 
			{
				additionalFunction();
				ButtonRequestAdditionalFunction("#form1", "#btnSave", "#ProgressRequest", "#MessageResult", '<s:property value="%{remoteurl}"/>');
			});
		</script>
	</head>

	<body>
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.setting" /> | <s:text name="t.migrateAppsData" /></h1>
                <h2><s:text name="t.migrateAppsData.description" /></h2>
            </hgroup>
            
            <div class="nav navbar-expand nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5>Migrate</h5></a>
            </div>
            <br>
            <!-- CONTENT -->
            <div class="col-6 offset-md-3">
                
                <!-- CONTENT MAIN -->
                <s:form id="form1">
                	
                	<!-- MESSAGE RESULT -->
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
            		<%@ include file="/WEB-INF/includes/param_export.jsp" %>
                        <s:actionerror /><s:actionmessage />
	                       	<div class="form-group row">
		                       	<label for="bank" class="col-sm-4 col-form-label">Bank</label>
		                       	<div class="col-sm-8">
			 						<b class="Property" id="bank"></b>
				                   	<img id="bankP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportBank}"/>','PDF')" 
			    	               		style="cursor:pointer;">
			        	           	<img id="bankC" src='./Resource/JQuery/JMesa/csv.gif' 
			            	       		onclick="exportLink('#form1', '<s:property value="%{exportBank}"/>','CSV')" 
			                	   		style="cursor:pointer;">
		                	   	</div>
	                       	</div>
	 						<div class="form-group row">
		 						<label for="merchant" class="col-sm-4 col-form-label">Merchant</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="merchant"></b>
			 						<img id="merchantP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportMerchant}"/>','PDF')" 
				                   		style="cursor:pointer;">
				                   	<img id="merchantC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportMerchant}"/>','CSV')" 
				                   		style="cursor:pointer;">
			                   	</div>
							</div>	
	 						<div class="form-group row">
		 						<label for="merchantBank" class="col-sm-4 col-form-label">Merchant Bank</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="merchantBank"></b>
									<img id="merchantBankP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportMerchantBank}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="merchantBankC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportMerchantBank}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
		                   	</div>
		                   	<div class="form-group row">
		 						<label for="appsMenu" class="col-sm-4 col-form-label">Apps Menu</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="appsMenu"></b>
									<img id="appsMenuP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsMenu}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="appsMenuC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsMenu}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
							</div>
							<div class="form-group row">
		 						<label for="appsContent" class="col-sm-4 col-form-label">Apps Content</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="appsContent"></b>
									<img id="appsContentP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsContent}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="appsContentC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsContent}"/>','CSV')"
				                   		style="cursor:pointer;">
				                 </div>
							</div>
	 						<div class="form-group row">
		 						<label for="appsWording" class="col-sm-4 col-form-label">Apps Wording</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="appsWording"></b>
									<img id="appsWordingP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsWording}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="appsWordingC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsWording}"/>','CSV')"
				                   		style="cursor:pointer;">
				                 </div>
							</div>
	 						<div class="form-group row">
		 						<label for="appsMenuLogo" class="col-sm-4 col-form-label">Apps Menu Logo</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="appsMenuLogo"></b>
									<img id="appsMenuLogoP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsIcon}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="appsMenuLogoC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsIcon}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
							</div>
	 						<div class="form-group row">
		 						<label for="appsMenuTrxType" class="col-sm-4 col-form-label">Apps Menu Trx Type</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="appsMenuTrxType"></b>
									<img id="appsMenuTrxTypeP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsTrxType}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="appsMenuTrxTypeC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsTrxType}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
							</div>
	 						<div class="form-group row">
		 						<label for="category" class="col-sm-4 col-form-label">Category</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="category"></b>
									<img id="categoryP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportCategory}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="categoryC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportCategory}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
							</div>
	 						<div class="form-group row">
		 						<label for="listValue" class="col-sm-4 col-form-label">List Value</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="listValue"></b>
									<img id="listValueP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportListValue}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="listValueC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportListValue}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
							</div>
	 						<%-- <div class="form-group row">
		 						<label for="categoryBiller" class="col-sm-4 col-form-label">Category Biller</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="categoryBiller"></b>
									<img src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportCategoryBiller}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportCategoryBiller}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
	 						</div>
	 						<div class="form-group row">
		 						<label for="listValueBiller" class="col-sm-4 col-form-label">List Value Biller</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="listValueBiller"></b> 						
									<img src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportListBiller}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportListBiller}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
		                   	</div> --%>		                   	
	 						<div class="form-group row">
		 						<label for="appsHomeScreen" class="col-sm-4 col-form-label">Home Screen Logo</label>
		 						<div class="col-sm-8">
			 						<b class="Property" id="appsHomeScreen"></b> 						
									<img id="appsHomeScreenP" src='./Resource/JQuery/JMesa/pdf.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsHomeScreen}"/>','PDF')"
				                   		style="cursor:pointer;">
				                   	<img id="appsHomeScreenC" src='./Resource/JQuery/JMesa/csv.gif' 
				                   		onclick="exportLink('#form1', '<s:property value="%{exportAppsHomeScreen}"/>','CSV')"
				                   		style="cursor:pointer;">
			                   	</div>
		                   	</div>
						</div>
						
					</fieldset>

					<s:if test="checkPrevileges(menuId)">
	          			<div class="PositionerCenter">
	                        <button type="submit" id="btnSave" class="ButtonPrimary btn btn-danger" value="%{getText('b.save')}"><i class="fas fa-database" style="vertical-align: middle;"></i>&nbsp;<s:text name="Migrate"/></button>
	                        <!-- <button type="reset" class="ButtonReset btn btn-primary" value="Reset"><i class="fas fa-sync" style="vertical-align: middle;"></i>&nbsp;Reset</button> -->
	                    </div>
                    </s:if>

				</s:form>
            </div>
        </main>
	</body>
</html>