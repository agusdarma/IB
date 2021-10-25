<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.merchant" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="Merchant!processInput"/>
		<script type="text/javascript">
			$(document).ready(function() 
			{
				$("#btnSave").click(function()
				{
					$("#MessageResult").removeClass(MessageState);					
					ProcessStart = Date.now();
					$("#ProgressRequest").show();
					var file = $('#imageLogo').get(0).files[0];
					var formData = new FormData();
					formData.append('merchant.id', $("#merchantId").val());
					formData.append('merchant.merchantCode', $("#merchantCode").val());
					if($("#merchantId").val()==0)
					{
						formData.append('merchant.merchantName', $("#merchantCode option:selected").text());
					}
					else
					{
						formData.append('merchant.merchantName', $("#merchantName").val());
					}
					formData.append('merchant.merchantStatus', $("#merchantStatus").val());
					formData.append('merchant.showOrder', $("#showOrder").val());
					formData.append('merchant.imageLogo', file);
					formData.append('merchant.merchantLogo', $("#imageLogo").val());
					// PROCESS
					$.ajax
					({
						type 		: 'POST', // define the type of HTTP verb we want to use (POST for our form)
						url 		: '<s:property value="%{remoteurl}"/>',
						data 		: formData, // our data object
						cache: false,
					    contentType: false,
					    processData: false,
					}).done(function(resultJson) 
					{
						if(resultJson.substr(2,7)=="DOCTYPE")
						{
							window.location.href='<s:property value="@id.co.emobile.samba.web.data.WebConstant@LOGIN_PATH"/>';
						}
						$("#ProgressRequest").hide();
						resultObject = JSON.parse(resultJson);
						
						if(resultObject.rc == 0)
						{
							if(resultObject.type > 0)
							{
								window.location.href=resultObject.path;	
							}
							else
							{
								$("#form1").trigger("reset");
								ProcessEnd = Date.now();
								ProcessTime = (ProcessEnd - ProcessStart) / 1000;
								
								$("#MessageResult").addClass(ResultSuccess);
								$("#MessageResult").empty();
								$("#MessageResult").append(resultObject.message + ", process took " + ProcessTime + " seconds");
							}
						}
						else
						{
							$("#MessageResult").addClass(ResultFailure);
							$("#MessageResult").empty();
							$("#MessageResult").append(resultObject.message);
						}
					});
					
					// TIMEOUT
					
					setTimeout(function()
					{
						$("#ProgressRequest").hide();
					}, 
					15000);
					
					setTimeout(function()
					{
						$(LoadingSquareClass).append("Process took longer than usual, press ESC to cancel.");
						
						$(document).keyup(function(e) 
						{
							if (e.keyCode == 27)
							{
								$("#ProgressRequest").hide();
							}
						});
					}, 
					5000);
					
					return false;
				});
			});
		</script>
	</head>

	<body>
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.setting" /> | <s:text name="t.merchant" /></h1>
                <h2><s:text name="t.merchant.description" /></h2>
            </hgroup>
            
            <!-- CONTENT -->
            <nav>
              <div class="nav nav-tabs col-4 offset-md-4" id="nav-tab" role="tablist">
                <s:url id="search" action="Merchant!gotoSearch" includeParams="none"/>
                <s:url id="input" action="Merchant!gotoInput" includeParams="none"/>
                <s:if test="merchant.id == 0">
                	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
                	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.insert"/></h5></a>
                </s:if>
                <s:else>
                	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
                	<s:a href="%{input}" cssClass="ClearHyperlink nav-item nav-link" id="nav-profile-tab"><h5><s:text name="f.insert"/></h5></s:a>                	
                	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.update"/></h5></a>
				</s:else>                                     
              </div>
            </nav>
           	<br>
            
            <div class="col-6 offset-md-3">
                <!-- CONTENT MAIN -->
                <s:form id="form1">
                	
                	<!-- MESSAGE RESULT -->
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                        <s:actionerror /><s:actionmessage />
                       		<s:hidden name="merchant.id" id="merchantId"/>		
							<div class="form-group row">
                        		<label for="merchant" class="col-sm-4 col-form-label"><s:text name="l.merchant" /></label>
	                        	<s:if test="merchant.id == 0">	                        	
									<div class="col-sm-8">
										<s:select id="merchantCode" name="merchant.merchantCode" cssClass="form-control" 
											list="listBiller" listKey="displayName" listValue="displayName"
											headerKey="none" headerValue="Choose Merchant"/>
									</div>
									<s:hidden name="merchant.merchantName" id="merchantName"/>									
								</s:if>
								<s:else>
									<s:hidden name="merchant.merchantCode" id="merchantCode"/>
									<s:hidden name="merchant.merchantName" id="merchantName"/>									
									<div class="col-sm-8">
										<span class="Property"><s:property value="merchant.merchantName" /></span>	
									</div>
								</s:else>
							</div>
                        	<div class="form-group row">
	                        	<label for="merchantStatus" class="col-sm-4 col-form-label"><s:text name="l.merchantStatus" /></label>
								<div class="col-sm-8">
									<s:select id="merchantStatus" name="merchant.merchantStatus" cssClass="form-control" 
										list="listUserStatus" listKey="lookupValue" listValue="lookupDesc" />
								</div>
                        	</div>
                        	<div class="form-group row">
	                        	<label for="showOrder" class="col-sm-4 col-form-label"><s:text name="l.showOrder" /></label>
	                        	<div class="col-sm-8">
									<s:textfield id="showOrder" name="merchant.showOrder" onkeypress="return isNumberOnly(event)"
										cssClass="form-control" required="true"/>
								</div>
                        	</div>

                        	<s:if test="merchant.id>0">
								<br>
								<div class="form-group row">
									<label for="'photo'" class="col-sm-4 col-form-label"><s:text name="l.existingLogo" /></label>
									<div class="col-sm-8">
										<img id='photo' src="<s:url value="%{photoLink}" />" width='200' height='200' style="z-index: 2;"/>
									</div>
								</div>
							</s:if>
                        	<div class="form-group row">
	                        	<label for="merchantLogo" class="col-sm-4 col-form-label"><s:text name="l.merchantLogo" /></label>
	                        	<div class="col-sm-8">
		                        	<input id="imageLogo" type="file" name="merchant.imageLogo" cssClass="form-control" />
	                        	</div>
	                        	<div class="col-sm-4">
	                        	</div>
	                        	<div class="col-sm-8">
	                        		<font size="1" color="red"> *Recommended image size 128x128 px</font>
	                        	</div>
                        	</div>

					<s:if test="checkPrevileges(menuId)">
	                    <!-- BUTTON -->
	                    <div class="col-12 offset-md-3">
	                        <button type="submit" id="btnSave" class="ButtonPrimary btn btn-danger" value="%{getText('b.save')}"><i class="fas fa-save" style="vertical-align: middle;"></i>&nbsp;<s:text name="b.save"/></button>
	                        <button type="reset" class="ButtonReset btn btn-primary" value="Reset"><i class="fas fa-sync" style="vertical-align: middle;"></i>&nbsp;Reset</button>
	                    </div>
                    </s:if>
				</s:form>
            </div>
        </main>
	</body>
</html>