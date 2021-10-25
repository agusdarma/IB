<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="j"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<meta name="decorator" content="content-new">
		<title>Kas Non Tunai - Ubah Password</title>
		
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
                          <label class="text-black"><b>Password lama</b></label>
                          <s:password maxlength="64" type="password" id="oldPassword" name="oldPassword" class="form-control form-control-lg" required="true"/>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Password baru</b></label>
						  <s:password maxlength="64" type="password" id="newPassword" name="newPassword" cssClass="form-control form-control-lg" required="true"/>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Konfirmasi password baru</b></label>
						  <s:password maxlength="64" type="password" id="confirmPassword" name="confirmPassword" cssClass="form-control form-control-lg" required="true"/>
                        </div>
                      
                    </div>
                  </div>
                  <div class="row" style="margin-top: 20vh;">
                    <div class="col-lg-12">
                      <s:submit id="btnSave" type="button" cssClass="btn bg-blue btn-lg text-white" style="text-align: left;">
                        <i class="fas fa-sync-alt mr-2"></i> Ubah Password
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