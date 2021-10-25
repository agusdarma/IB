<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.security" /> | <s:text name="t.resetPassword" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="ResetPassword!processInput"/>
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
            	<h1><s:text name="t.security" /> | <s:text name="t.resetPassword" /></h1>
                <h2><s:text name="t.resetPassword.description" /></h2>
            </hgroup>
            
              <div class="nav nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <s:url id="search" action="ResetPassword!gotoSearch" includeParams="none"/>
               	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
               	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.update"/></h5></a>
              </div>
            <br>
            
            <!-- CONTENT -->
             <div class="col-6 offset-md-3">
                <s:form id="form1" action="ResetPassword!processInput" method="post">
                	<!-- MESSAGE RESULT -->
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>      		
            		<!-- FORM -->
                	<fieldset>                    	
                        <s:actionerror/><s:actionmessage/>                       
                        	<s:hidden name="userData.id" id="resetPasswordId"/>
                        	<s:hidden name="userData.userCode" />
                        		<div class="form-group row">
									<label for="userID" class="col-sm-4 col-form-label"><s:text name="l.userCode" /></label>		
									<div class="col-sm-8">
										<s:property  value="userData.userCode" />
									</div>
								</div>
								
								<br>
								<div class="form-group row">
									<label for="userName" class="col-sm-4 col-form-label"><s:text name="l.userName" /></label>
									<div class="col-sm-8">
										<s:property value="userData.userName" />							
									</div>
								</div>
		
								<br>
								<div class="form-group row">
									<label for="newPassword" class="col-sm-4 col-form-label">
										<s:text name="l.newPassword"/>
										<font color="red" size="1"><i><b>*)</b></i></font>
									</label>
									<div class="col-sm-8">
										<s:password maxlength="64" id="newPassword" name="newPassword" cssClass="form-control"  required="required"/>							
									</div>										
								</div>

								<br>
								<div class="form-group row">								
									<label for="confirmPassword" class="col-sm-4 col-form-label">
										<s:text name="l.confirmPassword" />
										<font color="red" size="1"><i><b>*)</b></i></font>
									</label>
									<div class="col-sm-8">
										<s:password maxlength="64" id="confirmPassword" name="confirmPassword" cssClass="form-control" required="required"/>
									</div>
								</div>
                    </fieldset>
                    
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