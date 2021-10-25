<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Input IB - NabungDividen</title>
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="UserData!processInput"/>
		<script type="text/javascript">
			$(document).ready(function() 
			{
				ButtonRequest("#form1", "#btnSave", "#ProgressRequest", "#MessageResult", '<s:property value="%{remoteurl}"/>');
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
        	<s:hidden name="userData.id" id="userDataId"/>
            <s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row my-4">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="card-body">
			        <s:a cssClass="btn bg-green text-white" action="UserData" >
			            <i class="fas fa-users"></i> List User
			        </s:a>
			        <hr>                  		            
                  <div class="card shadow bg-blue text-white mb-4">
                    <div class="card-body">
                    	<div class="row">                   	
                    		<div class="col-lg-12">
                    			<b>
                    				Input User Baru
                    			</b>              
                    		</div>
                    	</div>
                    </div>
                  </div>
                  <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                  <div class="row mx-2">
                    <div class="col-lg-6">
                    	<div class="card">
                    		<div class="card-body bg-gray">
			                    <s:if test="userData.id==0">
			                    	<div class="form-group">
			                          <label class="text-black"><b>User Code</b></label>
			                          <s:textfield maxlength="16" type="text" id="userID" name="userData.userCode" cssClass="form-control" required="required"/>
			                        </div>
			                        <div class="form-group">
			                          <label class="text-black"><b>Password</b></label>
			                          <s:password maxlength="64" type="password" id="password" name="userData.userPassword" cssClass="form-control"  required="required"/>
			                        </div>
			                        <div class="form-group">
			                          <label class="text-black"><b>Konfirmasi Password</b></label>
			                          <s:password maxlength="64" type="password" id="confirmPassword" name="confirmPassword" cssClass="form-control" required="required"/>
			                        </div>
			                    </s:if>
			                    <s:else>
			                        <div class="form-group">
			                          <label class="text-black"><b>User Code</b></label>
			                          <s:textfield maxlength="16" type="text" id="userID" name="userData.userCode" cssClass="form-control" readonly="true"/>
			                        </div>
			                    </s:else>
			                      	<div class="form-group">
			                          <label class="text-black"><b>User Name</b></label>
			                          <s:textfield maxlength="32" type="text" id="userName" name="userData.userName" cssClass="form-control" required="true" />
			                        </div>                    		
                    		</div>
                    	</div>
                    </div>
                    <div class="col-lg-6">
                    	<div class="card">
                    		<div class="card-body bg-gray">
		                        <div class="form-group">
		                          <label class="text-black"><b>Phone Number</b></label>
		                          <s:textfield maxlength="32" type="text" id="phoneNo" name="userData.phoneNo" cssClass="form-control" required="true" />
		                        </div>
		                        <div class="form-group">
		                          <label class="text-black"><b>Email</b></label>
		                          <s:textfield maxlength="100" type="text" id="email" name="userData.email" cssClass="form-control" required="true" />
		                        </div>
		                        <div class="form-group">
		                          <label class="text-black"><b>User Level</b></label>
		                          <s:select id="userlevel" name="userData.levelId" list="listUserLevel" listKey="levelId" listValue="levelName" cssClass="form-control"/>
		                        </div>
		                        <div class="form-group">
		                          <label class="text-black"><b>User Group</b></label>
		                          <s:select id="userGroup" name="userData.groupId" list="listUserGroup" listKey="id" listValue="groupName" cssClass="form-control"/>
		                        </div>
		                        <div class="form-group">
		                          <label class="text-black"><b>User Branch</b></label>
		                          <s:select id="userBranch" name="userData.branchId" list="listUserBranch" listKey="id" listValue="branchName" cssClass="form-control"/>
		                        </div>
		                        <div class="form-group">
		                          <label class="text-black"><b>User Status</b></label>
		                          <s:select id="userStatus" name="userData.userStatus" cssClass="form-control" list="listUserStatus" listKey="lookupValue" listValue="lookupDesc" />
		                        </div>                    		
                    		</div>
                    	</div>
                    </div>
                  </div>
                  <br>
                  <div class="row my-4">
                    <div class="col-lg-12 text-right">
                    	<div class="mx-4">
	                      <s:submit id="btnSave" type="button" cssClass="btn btn-lg bg-blue-gradient text-white" style="text-align: left;">
	                        <i class="fas fa-save mr-2"></i> Simpan Data
	                      </s:submit>                    	
                    	</div>
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