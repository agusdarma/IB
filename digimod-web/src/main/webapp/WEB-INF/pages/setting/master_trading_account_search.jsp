<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>List Master Trading Account - NabungDividen</title>
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="MstTradeAccount!processInput"/>
		<script type="text/javascript">
			$(document).ready(function() 
			{
				ButtonRequest("#form1", "#btnSave", "#ProgressRequest", "#MessageResult", '<s:property value="%{remoteurl}"/>');
				$('#dataTable').DataTable();
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
       
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <s:a cssClass="btn bg-green text-white" action="MstTradeAccount!gotoInput" >
                      <i class="fas fa-user-plus"></i> Tambah Master Trading Account
                    </s:a>
                  </div>
                </div>
                
                <div class="card shadow mx-4 my-4">
                  <div class="card-header bg-blue border-blue">
                    <h6 class="m-0 font-weight-bold text-white">List Master Trading Account</h6>
                  </div>
                  <div class="card-body border-blue">
                      <div class="table-responsive">
                        <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                          <thead>
                            <tr>
                              <th scope="col">No</th>
                              <th scope="col">Nama Account</th>
                              <th scope="col">Trading Account No</th>
                              <th scope="col">IB User Code</th>
                              <th scope="col">IB User Name</th>
                              <th scope="col">Status</th>
                              <th scope="col"></th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="listMasterTradingAccountAll">
                            <tr>
                              <td><s:property value="rowNum" /></td>
                              <td><s:property value="name" /></td>
                              <td><s:property value="tradingAccountNo" /></td>
                              <td><s:property value="ibUserCode" /></td>
                              <td><s:property value="ibUserName" /></td>
                              <td><s:property value="userStatusDisplay" /></td>   
                                                        
                              <td>
                              	<s:url id="urlDetail" action="MstTradeAccount!detail">
                              		<s:param name="masterTradingAccountId" value="%{id}" />
                              	</s:url>
                                <s:a href="%{urlDetail}"><i class="fas fa-edit mr-4"></i> Edit 
                                </s:a>
                              </td>
                            </tr>
                          </s:iterator>
                          
                          </tbody>
                        </table>
                      </div>                    
                  </div>
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