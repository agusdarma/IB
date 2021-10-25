<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - System Setting</title>
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="SystemSetting!processInput"/>
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
        	<s:hidden name="systemSetting.id" id="settingId"/>
            <s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <s:a cssClass="btn bg-green text-white" action="SystemSetting" >
                      <i class="fas fa-users"></i> List System Setting
                    </s:a>
                  </div>
                </div>
                
                <div class="card-body">
                  <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                  <div class="row mx-2">
                    <div class="col-lg-4">
                      
                      	<div class="form-group">
                          <label class="text-black"><b>Nama Setting</b></label>
                          <s:textfield type="text" id="settingName" name="systemSetting.settingName" disabled="true" cssClass="form-control form-control-lg" />
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Keterangan Setting</b></label>
                          <s:textarea type="text" id="settingDescription" name="systemSetting.settingDesc" disabled="true" cssClass="form-control form-control-lg" />
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Tipe Setting</b></label>
                          <s:textfield type="text" id="valueType" name="systemSetting.valueType" disabled="true" cssClass="form-control form-control-lg" />
                        </div>
                    <s:if test="settingInJson">
					<s:iterator value="settingMap">
						<div class="form-group">
                          <label class="text-black"><b><s:property value="key" /></b></label>
                          <s:textfield name="settingMap['%{key}']" value="%{value}" cssClass="form-control form-control-lg"/>
                        </div>
					</s:iterator>
					</s:if>
					<s:else>
						<div class="form-group">
                          <label class="text-black"><b>Data Setting</b></label>
                          <s:textarea type="text" id="settingValue" name="systemSetting.settingValue"
										rows="3" cssClass="form-control form-control-lg" required="true" maxlength="255"/>
                        </div>
					</s:else>
                        
                        
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