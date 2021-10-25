<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.generateSummaryData" /></title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="generateSummaryData" action="GenerateSummaryData!generateSummaryData" />
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
				$("#generateSumData").click(function(){						
					appendMessage("Please Wait while we make the summary daily trx for trx date : " + $('#startDate').val() + " - " + $('#endDate').val());
					jQuery.getJSON('<s:property value="%{generateSummaryData}" escape="false" />?startDate='+ $('#startDate').val() + '&endDate='+ $('#endDate').val() +'&summary='+$('#summary').val(), function(data) 
					{						
						var body=document.getElementById("bodyId");
						var head=document.getElementById("headId");
						$("#bodyId").empty();
						$("#headId").empty();
						var rowLine="";
						var headLine="";
						if($("#summary").val()=="daily")
						{
							headLine +="<tr>"
								+"<th>"+'<s:text name="l.recordNo" />'+"</th>"
								+"<th>"+'<s:text name="l.trxDate" />'+"</th>"
								+"<th>"+'<s:text name="l.trxCode" />'+"</th>"
								+"<th>"+'<s:text name="l.trxGroup" />'+"</th>"
								+"<th>"+'<s:text name="l.bankCode" />'+"</th>"
								+"<th>"+'<s:text name="l.amount" />'+"</th>"
								+"</tr>";
							if(data.listSummary.length > 0)
							{
								for (var i=0; i < data.listSummary.length; i++)
								{
									rowLine +="<tr id=\"row"+i+"\">"
										+"<td>"+data.listSummary[i].rowNum+"</td>"
										+"<td>"+data.listSummary[i].trxDate+"</td>"
										+"<td>"+data.listSummary[i].trxCode+"</td>"
										+"<td>"+data.listSummary[i].trxGroup+"</td>"
										+"<td>"+data.listSummary[i].bankCode+"</td>"
										+"<td>"+data.listSummary[i].amount+"</td>"
										+"</tr>";
								}								
							}
							else
							{
								rowLine +="<tr>"
								+"<td>No Data Found</td>"	
								+"</tr>";							
							}
						}																								
						head.innerHTML=headLine;
						body.innerHTML=rowLine;
						appendMessage(data.message);						
					});// end jquery jsonv
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
            	<h1><s:text name="t.setting" /> | <s:text name="t.generateSummaryData" /></h1>
                <h2><s:text name="t.generateSummaryData.description" /></h2>
            </hgroup>
            
            <!-- CONTENT -->
           	<div class="nav navbar-expand nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true">
                	<h5>Generate</h5>
                </a>
            </div>
            <br>

			<!-- CONTENT MAIN -->
			<div class="col-6 offset-md-3">
				<s:form id="form1" method="post">
                	
					<!-- MESSAGE RESULT -->
					<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                        <s:actionerror /><s:actionmessage />
	                       	<div class="form-group row">
                        		<label for="startDate" class="col-sm-4 col-form-label">Start Date</label>
                        		<div class="col-sm-8">
									<s:textfield id="startDate" name="startDate" cssClass="form-control" required="true" />
								</div>
							</div>
	                       	<div class="form-group row">
                        		<label for="endDate" class="col-sm-4 col-form-label">End Date</label>
                        		<div class="col-sm-8">
									<s:textfield id="endDate" name="endDate" cssClass="form-control" required="true" />
								</div>
							</div>
	                       	<div class="form-group row">
                        		<label for="endDate" class="col-sm-4 col-form-label"><s:text name="l.summaryType" /></label>
                        		<div class="col-sm-8">
								<s:select list="#{'daily':'Summary Daily Trx'}" name="summary" id="summary" cssClass="form-control"/>
								</div>
							</div>
					
					<s:if test="checkPrevileges(menuId)">
						<div class="col-12 offset-md-3">
							<button type="button" id="generateSumData" class="ButtonPrimary btn btn-danger" value="%{getText('b.save')}"><i class="fas fa-file" style="vertical-align: middle;"></i>&nbsp;<s:text name="Generate Summary Data"/></button>                    
						</div> 
					</s:if>
				</s:form>
            </div>
              
            <br>          
            <div id="divSearch" class="PositionerCenter" style="overflow: auto; width: 80%;">
				<table>
					<thead id="headId">
					</thead>
					<tbody id="bodyId">								
					</tbody>
					<tfoot id="footId">
					</tfoot>
				</table>
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