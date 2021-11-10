<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Withdraw Account - NabungDividen</title>
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="WithdrawAction!processInput"/>
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
            <s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <div class="card bg-blue text-white shadow mt-2">
                      <div class="card-body">
                        <div class="row text-white">
                          <div class="ml-4">
                            <i class="fas fa-dollar-sign" style="font-size: 1.5em"></i>
                          </div>
                          <div class="ml-4">
                            <b>Total Commission :</b> <b>$</b><b><s:property value="%{#session.LOGIN_KEY.totalClientCommission}" /></b>
                          </div>                         
                        </div>
                      </div>
                    </div> 
                  </div>
                  <div class="col">
                    <div class="card bg-green text-white shadow mt-2">
                      <div class="card-body">
                        <div class="row text-white">
                          <div class="ml-4">
                            <i class="fas fa-dollar-sign" style="font-size: 1.5em"></i>
                          </div>
                          <div class="ml-4">
                            <b>Total Have Been Withdrawn :</b> <b>$</b><b><s:property value="%{#session.LOGIN_KEY.clientCommissionWithdrawn}" /></b>
                          </div>                         
                        </div>
                      </div>
                    </div>  
                  </div>
                  <div class="col">
                    <div class="card bg-orange text-white shadow mt-2">
                      <div class="card-body">
                        <div class="row text-white">
                          <div class="ml-4">
                            <i class="fas fa-dollar-sign" style="font-size: 1.5em"></i>
                          </div>
                          <div class="ml-4">
                            <b>Total Commission Available :</b> <b>$</b><b><s:property value="%{#session.LOGIN_KEY.clientCommissionAvailable}" /></b>
                          </div>                         
                        </div>
                      </div>
                    </div>  
                  </div>                  
                </div>       
                    
                      
              
                <div class="card-body">
                  <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                  <div class="row mx-2">
                    <div class="col-lg-4">
                      
                      	<div class="form-group">
                          <label class="text-black"><b>Amount</b></label>
                          <s:textfield maxlength="16" type="text" id="amount" name="amount" cssClass="form-control form-control-lg" required="true" />
                        </div>                        
                    </div>
                  </div>
                  <div class="row" style="margin-top: 5vh;">
                    <div class="col-lg-12">
                      <s:submit id="btnSave" type="button" cssClass="btn bg-blue btn-lg text-white" style="text-align: left;">
                        <i class="fas fa-sync-alt mr-2"></i> Withdraw
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