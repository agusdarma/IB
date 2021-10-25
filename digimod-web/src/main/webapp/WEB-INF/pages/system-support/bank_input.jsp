<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.bank" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="Bank!processInput"/>
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
					formData.append('fileName', $('#imageLogo').val());
					formData.append('bank.bankCode', $("#bankCode").val());
					formData.append('bank.bankName', $("#bankName").val());
					formData.append('bank.id', $("#bankId").val());
					formData.append('bank.bankInitial', $("#bankInitial").val());
					formData.append('bank.imageLogo', file);
					formData.append('bank.showOn', $("#showOn").val());
					formData.append('bank.showDefAccount', $("#showDefAccount").val());
					formData.append('bank.ussdCode', $("#ussdCode").val());
					formData.append('bank.tpda', $("#tpda").val());
					formData.append('bank.bankUrl', $("#bankUrl").val());
					formData.append('bank.showOrder', $("#showOrder").val());
					formData.append('bank.supportLongCall', $("#supportLongCall").val());
					// PROCESS
					
					$.ajax
					({
						type 		: 'POST', // define the type of HTTP verb we want to use (POST for our form)
						url 		: /* '<s:property value="%{remoteurl}" />' */ '<s:property value="%{remoteurl}"/>', // the url where we want to POST
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
							window.location.hash = '#form1';

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
            	<h1><s:text name="t.setting" /> | <s:text name="t.bank" /></h1>
                <h2><s:text name="t.bank.description" /></h2>
            </hgroup>
            
              <div class="nav nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <s:url id="search" action="Bank!gotoSearch" includeParams="none"/>
                <s:url id="input" action="Bank!gotoInput" includeParams="none"/>
                <s:if test="bank.id == 0">
                	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
                	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.insert"/></h5></a>
                </s:if>
                <s:else>
                	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
                	<s:a href="%{input}" cssClass="ClearHyperlink nav-item nav-link" id="nav-profile-tab"><h5><s:text name="f.insert"/></h5></s:a>                	
                	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.update"/></h5></a>
				</s:else>                                     
              </div>
           	<br>
           	
           	<div class="col-6 offset-md-3">
                <!-- CONTENT MAIN -->
                <s:form id="form1">                	
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
            		
                	<fieldset>
                        <s:actionerror /><s:actionmessage />
                       		<s:hidden name="bank.id" id="bankId"/>
                        	<br>
                        	<s:if test="bank.id==0">
	                        	<div class="form-group row">
	                        		<label for="bankCode" class="col-sm-4 col-form-label"><s:text name="l.bankCode" /><font color="red" size="1"><i><b>*)</b></i></font></label>
									<div class="col-sm-8">
										<s:textfield id="bankCode" name="bank.bankCode" cssClass="form-control" required="true"
											onkeypress="return fileNameForbiddenChar(event)" maxlength="10" />
									</div>
								</div>
							</s:if>
							<s:else>
	                        	<div class="form-group row">
	                        		<label for="bankCode" class="col-sm-4 col-form-label"><s:text name="l.bankCode" /><font color="red" size="1"><i><b>*)</b></i></font></label>
									<div class="col-sm-8">
										<s:textfield id="bankCode" name="bank.bankCode" cssClass="form-control" readOnly="true"
											onkeypress="return fileNameForbiddenChar(event)" maxlength="10"/>
									</div>
								</div>							
							</s:else>
                        	<br>
                        	<div class="form-group row">
	                        	<label for="bankName" class="col-sm-4 col-form-label"><s:text name="l.bankName" /><font color="red" size="1"><i><b>*)</b></i></font></label>
								<div class="col-sm-8">
									<s:textfield id="bankName" name="bank.bankName" maxlength="50" cssClass="form-control" required="true"/>
								</div>
							</div>
                        	<div class="form-group row">
	                        	<label for="bankAlias" class="col-sm-4 col-form-label"><s:text name="l.bankInitial" /><font color="red" size="1"><i><b>*)</b></i></font></label>
								<div class="col-sm-8">
									<s:textfield id="bankInitial" name="bank.bankInitial" maxlength="30" cssClass="form-control" required="true"/>
								</div>
                        	</div>

                        	<s:if test="bank.id>0">
								<br>
								<div class="form-group row">
									<label for="'photo'" class="col-sm-4 col-form-label"><s:text name="l.existingLogo" /></label>
									<div class="col-sm-8">
										<img id='photo' src="<s:url value="%{photoLink}" />" width='200' height='200' style="z-index: 2;"/>
									</div>
								</div>
							</s:if>
                        	<div class="form-group row">
	                        	<label for="bankLogo" class="col-sm-4 col-form-label"><s:text name="l.bankLogo" /><font color="red" size="1"><i><b>*)</b></i></font></label>
	                        	<div class="col-sm-8">
	                        		<input id="imageLogo" type="file" name="bank.imageLogo" cssClass="form-control" />
	                        	</div>
	                        	<div class="col-sm-4">
	                        	</div>
	                        	<div class="col-sm-8">
	                        		<font size="1" color="red"> *Recommended image size 128x128 px</font>
	                        	</div>
							</div>
							
                        	<div class="form-group row">
	                        	<label for="showOn" class="col-sm-4 col-form-label"><s:text name="l.showOn" /></label>
	                        	<div class="col-sm-8">
									<s:select id="showOn" name="bank.showOn" cssClass="form-control"
										list="listShowOn" listKey="lookupValue" listValue="lookupDesc" />
								</div>
							</div>
                        	<div class="form-group row">
	                        	<label for="showDefAccount" class="col-sm-4 col-form-label"><s:text name="l.showDefAccount" /></label>
	                        	<div class="col-sm-8">
									<s:select id="showDefAccount" name="bank.showDefAccount" cssClass="form-control"
										list="listShowDefAcc" listKey="lookupValue" listValue="lookupDesc" />
								</div>
                        	</div>
                        	<div class="form-group row">
	                        	<label for="ussdCode" class="col-sm-4 col-form-label"><s:text name="l.ussdCode" /></label>
	                        	<div class="col-sm-8">
									<s:textfield id="ussdCode" maxlength="50" name="bank.ussdCode" cssClass="form-control"/>
								</div>
                        	</div>
                        	<div class="form-group row">
	                        	<label for="tpda" class="col-sm-4 col-form-label"><s:text name="l.tpda" /></label>
	                        	<div class="col-sm-8">
									<s:textfield id="tpda" maxlength="100" name="bank.tpda" cssClass="form-control" />
								</div>
							</div>							
                        	<div class="form-group row">
	                        	<label for="bankUrl" class="col-sm-4 col-form-label"><s:text name="l.bankUrl" /><font color="red" size="1"><i><b>*)</b></i></font></label>
	                        	<div class="col-sm-8">
									<s:textfield id="bankUrl" maxlength="100" name="bank.bankUrl" cssClass="form-control" required="true"/>
								</div>
							</div>
                        	<div class="form-group row">
                        		<label for="showOrder" class="col-sm-4 col-form-label"><s:text name="l.showOrder" /><font color="red" size="1"><i><b>*)</b></i></font></label>
								<div class="col-sm-8">
									<s:textfield id="showOrder" name="bank.showOrder" onkeypress="return isNumberOnly(event)"
										cssClass="form-control" required="true"/>	
								</div>
							</div>
                        	<div class="form-group row">
	                        	<label for="supportLongCall" class="col-sm-4 col-form-label"><s:text name="l.supportLongCall" /></label>
	                        	<div class="col-sm-8">
									<s:select id="supportLongCall" name="bank.supportLongCall" cssClass="form-control" list="#{0:'No', 1:'Yes'}" />
								</div>
                        	</div>
                        	
						</fieldset>
					<br>
					
					<s:if test="checkPrevileges(menuId)">
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