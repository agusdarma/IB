<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.systemSetting" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="SystemSetting!processInput"/>
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
            	<h1><s:text name="t.setting" /> | <s:text name="t.systemSetting" /></h1>
                <h2><s:text name="t.systemSetting.description" /></h2>
            </hgroup>            
            
           <nav>
              <div class="nav nav-tabs col-4 offset-md-4" id="nav-tab" role="tablist">
                <s:url id="search" action="ResetPassword!gotoSearch" includeParams="none"/>
               	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
               	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.update"/></h5></a>
              </div>
            </nav>
            <br>
                
               <!-- CONTENT -->
               <div class="col-6 offset-md-3">
               <s:form id="form1" method="post">                	
                	<!-- MESSAGE RESULT -->
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
            		<!-- FORM -->
                	<fieldset>
                        <s:actionerror /><s:actionmessage />
                       	<div class="FieldSetContainer">
                        	<s:hidden name="systemSetting.id" id="settingId"/>
                        	
                        	<div class="form-group row">
								<label for="settingName" class="col-sm-4 col-form-label"><s:text name="l.settingName" /></label>
								<div class="col-sm-8">
									<s:textfield type="text" id="settingName" name="systemSetting.settingName" disabled="true" cssClass="form-control" />
								</div>
							</div>
							
							<br>
							<div class="form-group row">
								<label for="settingDescription" class="col-sm-4 col-form-label"><s:text name="l.settingDesc" /></label>
								<div class="col-sm-8">
									<s:textarea type="text" id="settingDescription" name="systemSetting.settingDesc" disabled="true" cssClass="form-control" />
								</div>
							</div>
							
							<br>
							<div class="form-group row">
								<label for="valueType" class="col-sm-4 col-form-label"><s:text name="l.valueType" /></label>
								<div class="col-sm-8">
									<s:textfield type="text" id="valueType" name="systemSetting.valueType" disabled="true" cssClass="form-control" />
								</div>
							</div>
							<br>
						<s:if test="settingInJson">
						<s:iterator value="settingMap">
							<div class="form-group row">
								<label for="settingValue" class="col-sm-4 col-form-label"><s:property value="key" /></label>
								<div class="col-sm-8">
									<s:textfield name="settingMap['%{key}']" value="%{value}" cssClass="form-control"/>
								</div>
							</div>
							<br />
						</s:iterator>
						</s:if>
						<s:else>
							<div class="form-group row">
								<label for="settingValue" class="col-sm-4 col-form-label"><s:text name="l.settingValue" /><font color="red" size="1"><i><b>*)</b></i></font></label>
								<div class="col-sm-8">
									<s:textarea type="text" id="settingValue" name="systemSetting.settingValue"
										rows="3" cssClass="form-control" required="true" maxlength="255"/>
								</div>
							</div>
							<br>
						</s:else>
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