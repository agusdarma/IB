<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content-new">
		<title>Kas Non Tunai - Group Approval</title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="processInput" action="GroupApproval!processInput" />
		<script type="text/javascript">
		$(document).ready(function() 
		{
			ButtonRequest("#form1", "#btnSave", "#ProgressRequest", "#MessageResult", '<s:property value="%{processInput}"/>');
		});
		</script>
	</head>

<body>
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content" class="bg-gray">

        <!-- Topbar -->
        <%@ include file="/WEB-INF/includes/include_topbar.jsp"%>
        <!-- End of Topbar -->

        <!-- Begin Page Content -->
        <s:form id="form1">
       	<s:hidden name="userApproval.id" />
		<s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <s:a cssClass="btn bg-green text-white" action="GroupApproval" >
                      <i class="fas fa-users"></i> List User Approval
                    </s:a>
                  </div>
                </div>
                
                <div class="card-body">
                  <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                  <div class="row mx-2">
                    <div class="col-lg-4">
                    	<div class="form-group">
                          <label class="text-black"><b>User Code</b></label>
                          <s:textfield maxlength="16" type="text" id="userID" name="userApproval.userCode" cssClass="form-control" readonly="true"/>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>User Name</b></label>
                          <s:textfield maxlength="16" type="text" id="userName" name="userApproval.userName" cssClass="form-control" readonly="true"/>
                        </div>
                         <div class="form-group">
                          <label class="text-black"><b>Phone Number</b></label>
                          <s:textfield maxlength="16" type="text" id="phoneNumber" name="userApproval.phoneNo" cssClass="form-control" readonly="true"/>
                        </div>
                      	
                    </div>
                  </div>
                  
                  <%-- 
                  <div class="card shadow mx-4 my-4">
	                  <div class="card-header bg-blue border-blue">
	                    <h6 class="m-0 font-weight-bold text-white">List User Group</h6>
	                  </div>
                  <div class="card-body border-blue">
                      --%>
                        <table class="card shadow" id="dataTable">
                          <thead class="card-header bg-blue border-blue">
                            <tr class="font-weight-bold text-white">
								<th>List User Group</th>
                            </tr>
                          </thead>
                          <tbody>
	                          <s:iterator value="listUserGroup" status="rowStatus">
									<tr>
										<td> &nbsp; </td>
										<td>
											<div class="CheckboxSlide CheckboxSlideForOthers">
											<s:if test="isApprovalInGroup(id)">
												<input type="checkbox" id="TriggerAccess<s:property value="id"/>" name="selectedGroup[<s:property value="%{#rowStatus.index}"/>]" checked="checked" value="<s:property value="id"/>"/>
											</s:if>	
											<s:else>
												<input type="checkbox" id="TriggerAccess<s:property value="id"/>" name="selectedGroup[<s:property value="%{#rowStatus.index}"/>]" value="<s:property value="id"/>"/>
											</s:else>
												<label for="TriggerAccess<s:property value="id"/>"></label>
											</div>
										</td>
										<td>
											&nbsp; <label for="TriggerAccess<s:property value="id"/>"><s:property value="groupName"/></label>
										</td>												
										
									</tr>
								</s:iterator>
                          </tbody>
                        </table>
                        <%-- 
                  </div>
                </div> 
                  --%>
                  <div class="row" style="margin-top: 20vh;">
                    <div class="col-lg-12">
                      <s:submit id="btnSave" type="button" cssClass="btn bg-blue btn-lg text-white" style="text-align: left;">
                        <i class="fas fa-sync-alt mr-2"></i> Simpan Data
                      </s:submit>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.container-fluid -->
		</s:form>
		
      </div>
      <!-- End of Main Content -->

    </div>
   	
</body>
</html>