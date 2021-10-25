<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.report" /> | <s:text name="t.userActivityCms" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="CmsUserActivityReport!processSearch"/>
		<script type="text/javascript">	 
			$(document).ready(function()
			{
				$("#btnSearch").click(function()
				{
					$('#exportType').val("");
					GenerateTable("#formSearch", "#ProgressRequest", "divSearch", '<s:property value="%{remoteurl}"/>');
					GenerateTableNoProcessTime("#formSearch", "#ProgressRequest", "divSearch", '<s:property value="%{remoteurl}"/>');
				});
			});
		</script>
	</head>

	<body>
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.report" /> | <s:text name="t.userActivityCms" /></h1>
                <h2><s:text name="t.UserActivityCms.description" /></h2>
            </hgroup>
            
            <div class="nav navbar-expand nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.search"/></h5></a>
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
                        <%@ include file="/WEB-INF/includes/param_export.jsp" %>
	                        <div class="form-group row">
	                        	<label for="startDate" class="col-sm-4 col-form-label"><s:text name="l.period"/></label>
                        	<div class="col-sm-4">
                        		<s:textfield cssClass="form-control" id="startDate" name="paramVO.startDate"/>                  
                        	</div>
                        	<div class="text-center" style="margin-left: -1%; margin-right: -1%;">-</div>
                        	<div class="col-sm-4">
                        		<s:textfield cssClass="form-control" id="endDate" name="paramVO.endDate"/>
                        	</div>  
	                        </div>
                            <div class="form-group row">
	                            <label for="userDataId" class="col-sm-4 col-form-label"><s:text name="l.userCode"/></label>
	                            <div class="col-sm-8">
		                            <s:textfield id="userCode" name="paramVO.userCode" cssClass="form-control" />
	                            </div>
                            </div>
                            <div class="form-group row">
	                            <label for="activity" class="col-sm-4 col-form-label"><s:text name="l.activity"/></label>
	                            <div class="col-sm-8">
		                            <s:select id="activity" list="listAction" headerKey="-1" headerValue="All"
		                            	name="paramVO.action" cssClass="form-control"/>
	                            </div>
                            </div>
                            <div class="form-group row">
                            	<label for="moduleName" class="col-sm-4 col-form-label"><s:text name="l.moduleName"/></label>
                            	<div class="col-sm-8">
	                            	<s:select list="listModuleName" id="moduleName" headerKey="-1" headerValue="All"
	                            	name="paramVO.moduleName" cssClass="form-control"/>
                            	</div>
                            </div>
                    
                    <!-- BUTTON -->
                    <div class="col-12 offset-md-3">
                        <button type="button" id="btnSearch" class="ButtonPrimary btn btn-danger" value="search"><i class="fas fa-search" style="vertical-align: middle;"></i>&nbsp;Search</button>
                        <button type="reset" class="ButtonReset btn btn-primary" value="Reset"><i class="fas fa-sync" style="vertical-align: middle;"></i>&nbsp;Reset</button>
                    </div>
                    			
                </s:form>
                <br><br>
            </div>
            <div id="divSearch">
            </div>
            <script>
			    $('#startDate').datepicker({
			    	format: 'dd/mm/yyyy',
			    	uiLibrary: 'bootstrap4'
			    });
			    $('#endDate').datepicker({
			    	format: 'dd/mm/yyyy',
			        uiLibrary: 'bootstrap4'
			    });
	    	</script>
        </main>
	</body>
</html>