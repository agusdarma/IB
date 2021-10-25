<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - Distribution Checker</title>
	
	<script type="text/javascript">
    $(document).ready(function()
    {
		$("#sendOtp").click(function() {
			$.get( "<s:url action='DistChecker!otp' />" );
			$("#sendOtp").attr("disabled", true);
			setTimeout(function(){ 
				$("#sendOtp").removeAttr("disabled");
			}, 60000);
		});
		
		$("#btnProcess").click(function() {
			$("#processType").val("PROCESS");
		});
		$("#btnReject").click(function() {
			$("#processType").val("REJECT");
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

		<s:form action="DistChecker!verify" method="POST">
		<s:hidden id="processType" name="processType" />
        <!-- Begin Page Content -->
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow mb-4">
                <div class="card shadow bg-blue text-white mx-4 my-4">
                  <div class="card-body">
                    Input Transaksi
                  </div>
                </div>
            <s:if test="message != null">
			<div class="alert alert-danger" role="alert">
              <s:property value="message" />
            </div>
			</s:if>
                <div class="row mt-2 ml-3">
                  <div class="col">
                  	<s:url id="fileDownload" action="Download" >
                  		<s:param name="f"><s:property value='header.fileAssignment' /></s:param>
                  	</s:url>
                    <s:a href="%{fileDownload}" cssClass="btn bg-orange text-white" target="_blank">
                      <i class="fas fa-download"></i> Lihat surat penugasan
                    </s:a>
                  </div>
                </div>
                <div class="card shadow mx-4 my-4">
                  <div class="card-header bg-blue border-blue">
                    <h6 class="m-0 font-weight-bold text-white">List Distribusi Uang</h6>
                  </div>
                  <div class="card-body border-blue">
                      <div class="table-responsive">
                        <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                          <thead>
                            <tr>
                              <th scope="col">No.</th>
                              <th scope="col">Kode Bank</th>
                              <th scope="col">No Rekening</th>
                              <th scope="col">Nama Rekening</th>
                              <th scope="col">No Handphone</th>
                              <th scope="col">Nominal</th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="listDetail">
                          	<tr>
                              <td><s:property value="detailId" /></td>
                              <td><s:property value="bankCode" /></td>
                              <td><s:property value="accountNo" /></td>
                              <td><s:property value="accountName" /></td>
                              <td><s:property value="phoneNo" /></td>
                              <td><s:text name="u.formatMoney"><s:param name="value" value="moneyValue" /></s:text></td>
                            </tr>
                          </s:iterator>
                          </tbody>
                        </table>
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
                </div>
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Rekening Giro : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:property value='header.sracNumber' />" disabled>
                  </div>
                </div>
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Total nominal yang akan dibayar : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:text name="u.formatMoney"><s:param name="value" value="header.uploadedValue" /></s:text>" disabled>
                  </div>
                </div>
                <div class="row mx-sm-3">
                  <div class="col-lg 6">
                    <div class="form-group">
                      <label class="text-black"><b>Maker Remark</b></label>
                      <textarea class="form-control" id="remarks" rows="6" disabled><s:property value='header.makerRemarks' /></textarea>
                    </div>
                  </div>
                </div>
                <div class="row mx-sm-3">
                  <div class="col-lg 6">
                    <div class="form-group">
                      <label class="text-black"><b>Checker Remark</b></label>
                      <s:textarea cssClass="form-control" name="remarks" rows="4" />
                    </div>
                  </div>
                  <div class="col-lg 6">
                    <div class="form-group">
                      <label class="text-black"><b>Input Kode OTP</b></label>
                      <s:textfield name="otp" cssClass="form-control form-control-lg" />
                    </div>
                    <button id="sendOtp" type="button" class="btn bg-green text-white" style="text-align: left;">
                      <i class="fas fa-paper-plane mr-2"></i> Kirim OTP
                    </button>
                  </div>
                </div>
              </div>
              <div class="row ml-sm-3 my-4">
                <div class="col-lg-2">
                  <s:submit id="btnProcess" type="button" cssClass="btn bg-blue btn-lg text-white" cssStyle="text-align: left;">
                    <i class="fas fa-sync-alt mr-2"></i> Proses
                  </s:submit>
                </div>
                <div class="col-lg-2">
                  <s:submit id="btnReject" type="button" cssClass="btn bg-red btn-lg text-white" cssStyle="text-align: left;">
                    <i class="fas fa-times-circle mr-2"></i> Tolak
                  </s:submit>
                </div>
                <div class="col-lg-2">
                	<s:a action="DistChecker!back" cssClass="btn bg-red btn-lg text-white" cssStyle="text-align: left;">
                		<i class="fas fa-window-close mr-2"></i> Kembali
                	</s:a>
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