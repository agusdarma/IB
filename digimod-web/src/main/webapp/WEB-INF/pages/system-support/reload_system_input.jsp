<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.systemSetting" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="listUrl" action="SystemSetting!processList" />
		<s:url var="reloadUssdMenu" action="SystemSetting!reloadUssdMenu" />
		<s:url var="reloadSystemSetting" action="SystemSetting!reloadSystemSetting" />
		<s:url var="reloadWhitelist" action="SystemSetting!reloadWhitelist" />
		<script type="text/javascript">
			function appendMessage(data)
			{
				$(MessageResultID).removeClass(MessageState);
				$(MessageResultID).addClass(ResultSuccess);
				$(MessageResultID).empty();
				$(MessageResultID).append(data);	
			}
			
			$(document).ready(function()
			{			
				$("#rldSystemSetting").click(function(){
					jQuery.getJSON('<s:property value="%{reloadSystemSetting}" escape="false" />', function(data) 
					{
						appendMessage(data);						
					});// end jquery json
				});
				
				$("#rldUssdMenu").click(function(){
					jQuery.getJSON('<s:property value="%{reloadUssdMenu}" escape="false" />', function(data) 
					{
						appendMessage(data);
					});// end jquery json
				});
				
				$("#rldUssdParamData").click(function(){
					jQuery.getJSON('<s:property value="%{reloadUssdParamData}" escape="false" />', function(data) 
					{
						appendMessage(data);
					});// end jquery json
				});
				
				$("#rldWhitelist").click(function(){
					jQuery.getJSON('<s:property value="%{reloadWhitelist}" escape="false" />', function(data) 
					{
						appendMessage(data);
					});// end jquery json
				});
				
				$("#rldSmsPrefix").click(function(){
					jQuery.getJSON('<s:property value="%{reloadSmsPrefix}" escape="false" />', function(data) 
					{
						appendMessage(data);
					});// end jquery json
				});	
				
				if("<s:property value='message' />".length > 0)
				{
					MessageResult("<s:property value='message' />", "#MessageResult");
				}
			});
		</script>
	</head>

	<body>
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.setting" /> | <s:text name="t.reloadSystem" /></h1>
                <h2><s:text name="t.reloadSystem.description" /></h2>
            </hgroup>
            
            <!-- SHORT PROFILE -->
            <%@ include file="/WEB-INF/includes/include_aside_shortprofile.jsp"%>
            
            <!-- CONTENT -->
            <section class="ContainerBlock">
            	<!-- CONTENT HEADER -->
            	<div class="PaperHeaderGroup">
	                <div class="PaperHeader PaperSelected">
	                    <div class="ShapeFolderHornBorder"></div>
	                    <div class="ShapeFolderHornBody"></div>
	                    <h2><s:text name="f.update"/></h2>
	                </div>
                </div>
            	
                 <!-- CONTENT MAIN -->
                <s:form id="form1" method="post">
                	
                	<!-- MESSAGE RESULT -->
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
            		
                	<fieldset>
                    	<legend>Reload To engine</legend>
                        <div class="ShapeTailBorder"></div>
                        <div class="ShapeTailBody"></div>
                        <s:actionerror /><s:actionmessage />
                       	<div class="FieldSetContainer">
	                       	<label for="bankCode">Bank</label>
							<s:select name="bankCode" id="bankCode" list="listBank" listKey="bankCode" listValue="bankName" cssClass="DropDown"/>                       				             	
			             	<input type="button" value="Ussd Menu" id="rldUssdMenu" />
		             	</div>
					</fieldset>
					<div class="PositionerCenter">
		             	<input type="button" value="Reload Whitelist" id="rldWhitelist" class="ButtonPrimary"/>
		             	<input type="button" value="Reload System Setting" id="rldSystemSetting" class="ButtonPrimary"/>
		            </div>
				</s:form>
            </section>
        </main>
	</body>
</html>