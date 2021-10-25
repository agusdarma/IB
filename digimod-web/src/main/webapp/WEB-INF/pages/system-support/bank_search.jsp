<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.bank" /></title>
		
		<s:url var="remoteurl" action="Bank!processSearch"/>
		<script type="text/javascript">
			$(document).ready(function()
			{
				MessageResult("<s:property value='message' />", "#MessageResult");
				DirectSubmit("#formSearch", "#btnSearch", "#ProgressRequest", "divSearch", '<s:property value="%{remoteurl}"/>');
				GenerateTableNoProcessTime("#formSearch", "#ProgressRequest", "divSearch", '<s:property value="%{remoteurl}"/>');
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
                        
            <div class="nav navbar-expand nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.search"/></h5></a>
                <s:url id="input" action="Bank!gotoInput" includeParams="none"/>
                <s:a href="%{input}" cssClass="ClearHyperlink nav-item nav-link" id="nav-profile-tab"><h5><s:text name="f.insert"/></h5></s:a>
            </div>
            <br>
            
            <!-- CONTENT -->
            <div class="col-6 offset-md-3">
            	
                <!-- CONTENT MAIN -->
                <s:form id="formSearch">
                <!-- MESSAGE RESULT -->
             	<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>                
             		<!-- FORM -->
                        <s:actionerror /><s:actionmessage />
						<div class="form-group row">
	                       	<label for="bankCode" class="col-sm-4 col-form-label"><s:text name="l.bankCode" /></label>
							<div class="col-sm-8">
								<s:textfield id="bankCode" name="paramVO.bankCode" cssClass="form-control"/>
							</div>
						</div>
                       	<div class="form-group row">
	                       	<label for="bankName" class="col-sm-4 col-form-label"><s:text name="l.bankName" /></label>
							<div class="col-sm-8">
								<s:textfield id="bankName" name="paramVO.bankName" cssClass="form-control"/>
							</div>
						</div>
                       	<div class="form-group row">
	                       	<label for="bankAlias" class="col-sm-4 col-form-label"><s:text name="l.bankAlias" /></label>
							<div class="col-sm-8">
								<s:textfield id="bankInitial" name="paramVO.bankInitial" cssClass="form-control"/>
							</div>
						</div>
                    
                    <!-- BUTTON -->
                    <div class="col-12 offset-md-3">
                        <button type="submit" id="btnSearch" class="ButtonPrimary btn btn-danger" value="search"><i class="fas fa-search" style="vertical-align: middle;"></i>&nbsp;Search</button>
                        <button type="reset" class="ButtonReset btn btn-primary" value="Reset"><i class="fas fa-sync" style="vertical-align: middle;"></i>&nbsp;Reset</button>
                    </div>
                </s:form>
            </div>
            <br>
         	<div id="divSearch" ></div>
        </main>
	</body>
</html>