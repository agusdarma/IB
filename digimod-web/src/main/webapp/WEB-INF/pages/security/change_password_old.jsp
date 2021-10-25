<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="j"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<meta name="decorator" content="content">
		<title><s:text name="t.changePassword" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="ChangePassword!process"/>
		<script type="text/javascript">
		$(document).ready(function() 
		{
			MessageResult("<s:property value='message' />", "#MessageResult");	
			ButtonRequest("#form1", "#btnSave", "#ProgressRequest", "#MessageResult", '<s:property value="%{remoteurl}"/>');
		});
		</script>
	</head>

	<body>
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.security"/> | <s:text name="t.changePassword"/></h1>
                <h2><s:text name="t.changePassword.description"/></h2>
            </hgroup>
        
              <div class="nav nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"
                		><h5><s:text name="f.update"/></h5>
                	</a>
              </div>
            
            <br>
            <!-- CONTENT -->
            <div class="col-6 offset-md-3">
                
	           <!-- PAPER MAIN -->
	           <s:form id="form1">
	           	<!-- MESSAGE RESULT -->
	       		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>	           		           
	       				<div class="form-group row">
							<label for="changepassword_username" class="col-sm-4 col-form-label"><s:text name="l.userName"/></label>
							<div class="col-sm-8"><s:property value="%{#session.LOGIN_KEY.userCode}" /></div>
						</div>
						<div class="form-group row">
							<label for="oldPassword" class="col-sm-4 col-form-label">
								<s:text name="l.oldPassword"/>
								<font color="red" size="1"><i><b>*)</b></i></font>	
							</label>
							<div class="col-sm-8">
								<s:password maxlength="64" type="password" id="oldPassword" name="oldPassword" cssClass="form-control" required="true"/>
							</div>
						</div>
						<div class="form-group row">
							<label for="newPassword" class="col-sm-4 col-form-label">
								<s:text name="l.newPassword"/>
								<font color="red" size="1"><i><b>*)</b></i></font>
							</label>
							<div class="col-sm-8">
								<s:password maxlength="64" type="password" id="newPassword" name="newPassword" cssClass="form-control" required="true"/>
							</div>
						</div>
						<div class="form-group row">
							<label for="confirmPassword" class="col-sm-4 col-form-label">
								<s:text name="l.confirmPassword"/>
								<font color="red" size="1"><i><b>*)</b></i></font>
							</label>
							<div class="col-sm-8">
								<s:password maxlength="64" type="password" id="confirmPassword" name="confirmPassword" cssClass="form-control" required="true"/>
							</div>
						</div>
               </div>
	               <!-- BUTTON -->
	    			<div class="col-12 offset-md-4">
	               		<s:submit type="submit" id="btnSave" cssClass="ButtonPrimary btn btn-danger" value="%{getText('b.change')}"/>
                        <button type="reset" class="ButtonReset btn btn-primary" value="Reset"><i class="fas fa-sync" style="vertical-align: middle;"></i>&nbsp;Reset</button>	    
	               </div>
	           </s:form>
        </main>
	</body>
</html>