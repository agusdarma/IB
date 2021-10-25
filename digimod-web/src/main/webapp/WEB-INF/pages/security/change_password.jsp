<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kasda Non Tunai - Ganti Password</title>
</head>

<body>
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content" class="bg-gray">

        <!-- Topbar -->
        <%@ include file="/WEB-INF/includes/include_topbar.jsp"%>
        <!-- End of Topbar -->

        <!-- Begin Page Content -->
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="card-body">
                <%-- 
                  <nav aria-label="breadcrumb">
                  <ol class="breadcrumb bg-blue">
                    <li class="breadcrumb-item"><a class="text-golden" href="dashboard.html">Dashboard</a></li>
                    <li class="breadcrumb-item active text-white" aria-current="page">Ubah Password</li>
                  </ol>
                  </nav>
                --%>
                  <s:form id="formChangePassword" class="user" action="ChangePassword!process" >
                  <div class="row mx-2">
                    <div class="col-lg-4">
                      
                      	<div class="form-group">
                          <label class="text-black"><b>User Code</b></label>
                          <s:property value="%{#session.LOGIN_KEY.userCode}" />
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Password lama</b></label>
                          <s:password maxlength="64" type="password" id="oldPassword" name="oldPassword" cssClass="form-control form-control-lg" required="true"/>
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
                      <s:submit type="button" cssClass="btn bg-blue btn-lg text-white" style="text-align: left;">
                        <i class="fas fa-sync-alt mr-2"></i></i> Ubah Password
                      </s:submit>
                    </div>
                  </div>
                  </s:form>
                  
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.container-fluid -->

      </div>
      <!-- End of Main Content -->

    </div>
   	
</body>
</html>