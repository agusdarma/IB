<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.sourceAccount" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="SourceAccount!processInput"/>
		<script type="text/javascript">
			$(document).ready(function() 
			{
				ButtonRequest("#form1", "#btnSave", "#ProgressRequest", "#MessageResult", '<s:property value="%{remoteurl}"/>');
			});
		</script>
	</head>

	<body>
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.setting" /> | <s:text name="t.sourceAccount" /></h1>
                <h2><s:text name="t.sourceAccount.description" /></h2>
            </hgroup>
            
              <div class="nav nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <s:url id="search" action="SourceAccount!gotoSearch" includeParams="none"/>
                <s:url id="input" action="SourceAccount!gotoInput" includeParams="none"/>
                <s:if test="sourceAccount.id == 0">
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
            <!-- CONTENT -->
            <div class="col-6 offset-md-3">
                <!-- PAPER CONTENT -->
                <s:form id="form1" action="SourceAccount!processInput" method="post">
                    <!-- MESSAGE RESULT -->
                    <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                    
                    <!-- FORM -->
                    <fieldset>
                        <s:actionerror /><s:actionmessage />
                            <s:hidden name="sourceAccount.id" id="sracId"/>
                            <s:hidden name="moduleState" />
                            <s:if test="sourceAccount.id==0">
                            	<div class="form-group row">
	                                <label for="phoneNo" class="col-sm-4 col-form-label">
	                                	<s:text name="l.phoneNo" />
	                                	<font color="red" size="1"><i><b>*)</b></i></font>
	                                </label> 
	                                <div class="col-sm-8">
	                                	<s:textfield maxlength="16" type="text" id="phoneNo" name="sourceAccount.phoneNo" cssClass="form-control" required="required"/>
	                            	</div>
                            	</div>
                            	
                            </s:if>
                            <s:else>
                           		<div class="form-group row">
	                                <label for="phoneNo" class="col-sm-4 col-form-label">
	                                	<s:text name="l.phoneNo" />
	                                	<font color="red" size="1"><i><b>*)</b></i></font>	
	                                </label> 
	                                <div class="col-sm-8">       
	                                	<s:textfield maxlength="16" type="text" id="phoneNo" name="sourceAccount.phoneNo" readOnly="true" cssClass="form-control" />
	                            	</div>
                            	</div>
                            </s:else>                            
                            <div class="form-group row">
	                            <label for="sracName" class="col-sm-4 col-form-label">
	                            	<s:text name="l.sracName" />
	                            	<font color="red" size="1"><i><b>*)</b></i></font>	
	                            </label>
	                            <div class="col-sm-8">
	                            	<s:textfield maxlength="50" type="text" id="sracName" name="sourceAccount.sracName" cssClass="form-control" required="true" />
	                        	</div>
	                        </div>
	                        <div class="form-group row">
	                            <label for="sracNumber" class="col-sm-4 col-form-label">
	                            	<s:text name="l.sracNumber" />
	                            	<font color="red" size="1"><i><b>*)</b></i></font>	
	                            </label>
	                            <div class="col-sm-8">
	                            	<s:textfield maxlength="32" type="text" id="sracNumber" name="sourceAccount.sracNumber" cssClass="form-control" required="true" />
	                        	</div>
	                        </div>
                            <div class="form-group row">
                                <label for="sracPin" class="col-sm-4 col-form-label">
                                	<s:text name="l.sracPin"/>
                                	<font color="red" size="1"><i><b>*)</b></i></font>
                                </label>
                                <div class="col-sm-8">
                                	<s:password maxlength="8" type="password" id="sracPin" name="sourceAccount.sracPin" cssClass="form-control"  required="required"/>
                                </div>
                           	</div>
                               <div class="form-group row">
                                <label for="sracConfirmPin" class="col-sm-4 col-form-label">
                                	<s:text name="l.sracConfirmPin" />
                                	<font color="red" size="1"><i><b>*)</b></i></font>	
                                </label>
                                <div class="col-sm-8">
                                	<s:password maxlength="8" type="password" id="sracConfirmPin" name="sracConfirmPin" cssClass="form-control" required="required"/>
                                </div>
                            </div>
                            <div class="form-group row">
                            	<label for="sracStatus" class="col-sm-4 col-form-label"><s:text name="l.status"/></label>
                            	<div class="col-sm-8">
                            		<s:select id="sracStatus" name="sourceAccount.sracStatus" cssClass="form-control" list="listStatus" listKey="lookupValue" listValue="lookupDesc" />
                            	</div>
                            </div>
                    </fieldset>
                    <br>
                    
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