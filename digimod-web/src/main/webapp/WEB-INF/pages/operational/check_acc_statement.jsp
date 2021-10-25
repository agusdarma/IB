<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - Check Acc Statement</title>
	<s:url var="exportReport" action="CheckAccStatement!exportReport" />
	
	<script type="text/javascript">
			$(document).ready(function() 
			{
				$('#startDate').datepicker({
			    	format: 'dd/mm/yyyy',
			    	uiLibrary: 'bootstrap4'
			    });
			    $('#endDate').datepicker({
			    	format: 'dd/mm/yyyy',
			        uiLibrary: 'bootstrap4'
			    });
			    
				$('#dataTable').DataTable({
					"searching": false,
				});
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
              <%-- 
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <button type="button" class="btn bg-orange text-white">
                      <i class="fas fa-search"></i> Filter
                    </button>
                  </div>
                </div>
              --%>
            <s:if test="message != null">
			<div class="alert alert-danger" role="alert">
              <s:property value="message" />
            </div>
			</s:if>
                <div class="card shadow mx-4 my-4">
                  <div class="card-header bg-blue border-blue">
                    <h6 class="m-0 font-weight-bold text-white">Check Account Statement</h6>
                  </div>

                  <s:form id="form1" action="CheckAccStatement!processSearch">
                  	<s:hidden id="exportType" name="exportType" />     
                  
                  <%-- Report Param --%>            	
                  <div class="row d-flex justify-content-center my-4">
	                   <div class="card mx-4 my-4 col-8 border-blue">                
	                  	<div class="my-4 mx-4">
		 					<div class="form-group row">
							  <label for="example-date-input" class="col-2 col-form-label">Tanggal</label>
							  <div class="col-4">
							  	<s:textfield id="startDate" name="startDate" cssClass="form-control" />
							  </div>
							  <label class="col-1 col-form-label text-center"> - </label>					
							  <div class="col-4">
							  	<s:textfield id="endDate" name="endDate" cssClass="form-control" />
							  </div>					  
							</div>
							<div class="form-group row">
								<label for="example-date-input" class="col-2 col-form-label">Account</label>
								<div class="col-9">
									<s:select id="accountId" name="paramVO.accountId" list="listSourceAcc" cssClass="custom-select"/>
								</div>					
							</div>
							<div class="form-group row d-flex justify-content-center">
								<s:submit id="btnSearch" type="button" cssClass="btn bg-orange text-white"><i class="fa fa-search"></i> Search</s:submit>
							</div>                	
	                  	</div>                
	                  </div>                 
                  </div>
                  <%-- End Report Param --%>   
                           	
                  <s:if test='"0".equals(accStatementVO.statementRc)'>
                  <div class="card-body border-blue">
                      <div class="table-responsive">
                      <%-- 
                      	<img id="merchantP" src='./Resource/JQuery/JMesa/pdf.gif' 
				         	onclick="exportLink('#form1', '<s:property value="%{exportReport}"/>','PDF')" 
				            style="cursor:pointer;">
			            <img id="bankP" src='./Resource/JQuery/JMesa/excel.gif' 
	                   		onclick="exportLink('#form1', '<s:property value="%{exportReport}"/>','XLS')" 
    	               		style="cursor:pointer;">
    	               	<img id="bankP" src='./Resource/JQuery/JMesa/csv.gif' 
	                   		onclick="exportLink('#form1', '<s:property value="%{exportReport}"/>','CSV')" 
    	               		style="cursor:pointer;">
    	               	--%>
                        <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                          <thead>
                            <tr>
                              <th scope="col">Tanggal</th>
                              <th scope="col">Trx ID</th>
                              <th scope="col">Trx Code</th>
                              <th scope="col">Keterangan</th>
                              <th scope="col">Debet</th>
                              <th scope="col">Credit</th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="accStatementVO.listDetail">
                          	<tr>
                              <td><s:text name="u.datetime"><s:param name="value" value="txDateSettle" /></s:text></td>
                              <td><s:property value="txId" /></td>
                              <td><s:property value="txCode" /></td>
                              <td><s:property value="txMsg" /></td>
                              <td><s:text name="u.formatNumber"><s:param name="value" value="debetAmount" /></s:text></td>
                              <td><s:text name="u.formatNumber"><s:param name="value" value="creditAmount" /></s:text></td>
                            </tr>
                          </s:iterator>
                          </tbody>
                        </table>
                      </div>
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Saldo Awal : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:text name="u.formatNumber"><s:param name="value" value="accStatementVO.startBalance" /></s:text>" disabled>
                  </div>
                </div>
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Saldo Akhir : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:text name="u.formatNumber"><s:param name="value" value="accStatementVO.endBalance" /></s:text>" disabled>
                  </div>
                </div>
                      <%-- 
                      <nav aria-label="Page navigation example">
                        <ul class="pagination">
                          <li class="page-item">
                            <a class="page-link" href="#" aria-label="Previous">
                              <span aria-hidden="true">&laquo;</span>
                              <span class="sr-only">Previous</span>
                            </a>
                          </li>
                          <li class="page-item"><a class="page-link" href="#">1</a></li>
                          <li class="page-item"><a class="page-link" href="#">2</a></li>
                          <li class="page-item"><a class="page-link" href="#">3</a></li>
                          <li class="page-item">
                            <a class="page-link" href="#" aria-label="Next">
                              <span aria-hidden="true">&raquo;</span>
                              <span class="sr-only">Next</span>
                            </a>
                          </li>
                        </ul>
                      </nav>
                      --%>
                  </div>
                  </s:if>
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