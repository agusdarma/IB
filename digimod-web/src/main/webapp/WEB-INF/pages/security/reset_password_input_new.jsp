<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content-new">
		<title>Kas Non Tunai - Reset Password</title>
		
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
<!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content" class="bg-gray">

        <!-- Topbar -->
        <%@ include file="/WEB-INF/includes/include_topbar.jsp"%>
        <!-- End of Topbar -->

		<s:form id="form1">
        <!-- Begin Page Content -->
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="card-body">
                  <nav aria-label="breadcrumb">
                  <ol class="breadcrumb bg-blue">
                    <li class="breadcrumb-item active text-white" aria-current="page">Ubah Password</li>
                  </ol>
                  </nav>
                  <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>	           	
                  <div class="row mx-2">
                    <div class="col-lg-4">
                    	<div class="form-group">
                          <label class="text-black"><b><s:text name="l.userCode" /></b></label>
                          <label class="form-control form-control-lg" ><b ><s:property  value="userData.userCode" /></b></label>
                        </div>
                    	<div class="form-group">
                          <label class="text-black"><b><s:text name="l.userName" /></b></label>
                          <label class="form-control form-control-lg" ><b><s:property  value="userData.userName" /></b></label>
                        </div>                      
                        <div class="form-group">
                          <label class="text-black"><b><s:text name="l.newPassword" /></b></label>
                          <s:password maxlength="64" type="password" id="newPassword" name="newPassword" class="form-control form-control-lg" required="true"/>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b><s:text name="l.confirmPassword" /></b></label>
                          <s:password maxlength="64" type="password" id="confirmPassword" name="confirmPassword" class="form-control form-control-lg" required="true"/>
                        </div>                      
                    </div>
                  </div>
                  <div class="row" style="margin-top: 20vh;">
                    <div class="col-lg-12">
                      <s:submit id="btnSave" type="button" cssClass="btn bg-blue btn-lg text-white" style="text-align: left;">
                        <i class="fas fa-sync-alt mr-2"></i> Reset Password
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