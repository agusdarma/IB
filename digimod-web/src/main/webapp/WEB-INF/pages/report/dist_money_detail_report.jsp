<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - Laporan Distribusi Uang</title>
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
              <div class="card shadow mb-4">
                <div class="card shadow bg-blue text-white mx-4 my-4">
                  <div class="card-body">
                    Data Distribusi untuk <s:property value="header.sysLogNo" />
                  </div>
                </div>
            
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
                              <th scope="col">No Rekening</th>
                              <th scope="col">Kode Bank</th>
                              <th scope="col">Nama Rekening</th>
                              <th scope="col">No Handphone</th>
                              <th scope="col">Nominal</th>
                              <th scope="col">Status</th>
                              <th scope="col">RC</th>
                              <th scope="col">RRN</th>
                              <th scope="col">Issuer Account Name</th>
                              <th scope="col">Kode Billing</th>
                              <th scope="col">NPWP</th>
                              <th scope="col">Nama Wajib Pajak</th>
                              <th scope="col">NTPN</th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="listDetail">
                          	<tr>
                              <td><s:property value="detailId" /></td>
                              <td><s:property value="accountNo" /></td>
                              <td><s:property value="bankCode" /></td>                              
                              <td><s:property value="accountName" /></td>
                              <td><s:property value="phoneNo" /></td>
                              <td><s:text name="u.formatMoney"><s:param name="value" value="moneyValue" /></s:text></td>
                              <td><s:property value="processStatusDisplay" /></td>
                              <td><s:property value="hostRc" /></td>
                              <td><s:property value="hostRefNo" /></td>
                              <td><s:property value="header.sracName" /></td>
                              <td><s:property value="idBilling" /></td>
                              <td><s:property value="npwp" /></td>
                              <td><s:property value="nama" /></td>
                              <td><s:property value="ntpn" /></td>
                            </tr>
                            <s:if test="hostMessage != null">
                            <tr>
                              <td colspan="7">Message: <s:property value="hostMessage" />
                              <s:if test="processStatus == 21 || processStatus == 22">
                              <br>
                              Remarks: <s:property value="processRemarks" />
                              </s:if>
                              </td>
                            </tr>
                            </s:if>
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
                    <label class="text-black"><b>Status : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:property value='header.statusDisplay' />" disabled>
                  </div>
                </div>
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Maker : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:property value='header.uploadedUserCode' />" disabled>
                    &nbsp; <b> pada </b> &nbsp;
                    <input class="form-control" value="<s:text name='u.datetime2'><s:param name='value' value='header.uploadedOn' /></s:text>" disabled>
                  </div>
                </div>
                <s:if test="header.checkedBy > 0">
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Checker : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:property value='header.checkedUserCode' />" disabled>
                    &nbsp; <b> pada </b> &nbsp;
                    <input class="form-control" value="<s:text name='u.datetime2'><s:param name='value' value='header.checkedOn' /></s:text>" disabled>
                  </div>
                </div>
                </s:if>
                <s:if test="header.approvedBy > 0">
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Approval : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:property value='header.approvedUserCode' />" disabled>
                    &nbsp; <b> pada </b> &nbsp;
                    <input class="form-control" value="<s:text name='u.datetime2'><s:param name='value' value='header.approvedOn' /></s:text>" disabled>
                  </div>
                </div>
                </s:if>
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
                    <label class="text-black"><b>Total nominal yang di-upload : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:text name="u.formatMoney"><s:param name="value" value="header.uploadedValue" /></s:text>" disabled>
                  </div>
                </div>
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Total nominal yang dibayar : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:text name="u.formatMoney"><s:param name="value" value="header.processValue" /></s:text>" disabled>
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
                      <textarea class="form-control" id="remarks" rows="6" disabled><s:property value='header.checkerRemarks' /></textarea>
                    </div>
                  </div>
                </div>
                <div class="row mx-sm-3">
                  <div class="col-lg 6">
                    <div class="form-group">
                      <label class="text-black"><b>Callback Remark</b></label>
                      <textarea class="form-control" id="remarks" rows="6" disabled><s:property value='header.callbackRemarks' /></textarea>
                    </div>
                  </div>
                </div>
                <div class="row mx-sm-3">
                  <div class="col-lg 6">
                    <div class="form-group">
                      <label class="text-black"><b>Approval Remark</b></label>
                      <textarea class="form-control" id="remarks" rows="6" disabled><s:property value='header.approvalRemarks' /></textarea>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row ml-sm-3 my-4">
                <div class="col-lg-2">
                	<s:a action="DistMoneyReport" cssClass="btn bg-green btn-lg text-white" cssStyle="text-align: left;">
                		<i class="fas fa-arrow-left mr-2"></i> Kembali
                	</s:a>
                </div>
                <div class="col-lg-2">
                	<s:url id="printDetail" action="DistMoneyReport!exportReportDetail" >
                  		<s:param name="sysLogNo"><s:property value='header.sysLogNo' /></s:param>
                  	</s:url>
                    <s:a href="%{printDetail}" cssClass="btn bg-blue text-white" target="_blank">
                      <i class="fas fa-print"></i> Cetak
                    </s:a>
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