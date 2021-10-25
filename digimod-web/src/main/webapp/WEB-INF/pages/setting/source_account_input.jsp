<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - Data Rekening Sumber</title>
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="SourceAccount!processInput"/>
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
        	<s:hidden name="sourceAccount.id" id="sracId"/>
            <s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <s:a cssClass="btn bg-green text-white" action="SourceAccount" >
                      <i class="fas fa-users"></i> List Rekening Sumber
                    </s:a>
                  </div>
                </div>
                
                <div class="card-body">
                  <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                  <div class="row mx-2">
                    <div class="col-lg-4">
                    <s:if test="sourceAccount.id==0">
                    	<div class="form-group">
                          <label class="text-black"><b>Nomor Handphone</b></label>
                          <s:textfield maxlength="16" type="text" id="phoneNo" name="sourceAccount.phoneNo" cssClass="form-control form-control-lg" required="required"/>
                        </div>
                    </s:if>
                    <s:else>
                        <div class="form-group">
                          <label class="text-black"><b>Nomor Handphone</b></label>
                          <s:textfield maxlength="16" type="text" id="phoneNo" name="sourceAccount.phoneNo" readOnly="true" cssClass="form-control form-control-lg" />
                        </div>
                    </s:else>
                    	
                      
                      	<div class="form-group">
                          <label class="text-black"><b>Nama Rekening</b></label>
                          <s:textfield maxlength="50" type="text" id="sracName" name="sourceAccount.sracName" cssClass="form-control form-control-lg" required="true" />
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Nomor Rekening</b></label>
                          <s:textfield maxlength="32" type="text" id="sracNumber" name="sourceAccount.sracNumber" cssClass="form-control form-control-lg" required="true" />
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>PIN Rekening</b></label>
                          <s:password maxlength="8" type="password" id="sracPin" name="sourceAccount.sracPin" cssClass="form-control orm-control-lg"  required="required"/>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Konfirmasi PIN</b></label>
                          <s:password maxlength="8" type="password" id="sracConfirmPin" name="sracConfirmPin" cssClass="form-control orm-control-lg" required="required"/>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Group Rekening</b></label>
                          <s:select id="userGroup" name="sourceAccount.groupId" list="listUserGroup" listKey="id" listValue="groupName" cssClass="form-control form-control-lg"/>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Status</b></label>
                          <s:select id="sracStatus" name="sourceAccount.sracStatus" cssClass="form-control orm-control-lg" list="listStatus" listKey="lookupValue" listValue="lookupDesc" />
                        </div>

                    </div>
                  </div>
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