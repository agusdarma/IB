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
		<%@ include file="/WEB-INF/includes/include_script_table_generator.jsp"%>
		<script type="text/javascript">			
			$(document).ready(function()
			{			
				
				if("<s:property value='message' />".length > 0)
				{
					MessageResult("<s:property value='message' />", "#MessageResult");
				}
				GenerateTableNoProcessTime("", "#ProgressRequest", "divSearch", '<s:property value="%{listUrl}"/>');
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
            
            <div class="nav navbar-expand nav-tabs col-4 offset-md-4" id="nav-tab" role="tablist">
				<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.search"/></h5></a>
			</div>
            <br>
            
            <!-- CONTENT -->
            <div class="col-6 offset-md-3">
            	
                <!-- CONTENT MAIN -->
                <s:form>
                <!-- MESSAGE RESULT -->
             	<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>                
             	</s:form>
            </div> 
          	 <div id="divSearch" ></div>
        </main>
	</body>
</html>