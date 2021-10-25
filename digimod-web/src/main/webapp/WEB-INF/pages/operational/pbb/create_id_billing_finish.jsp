<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - Distribution Maker</title>
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
                    Data Distribusi Uang sudah selesai diproses.
                  </div>
                </div>
            
                <div class="row mt-2 ml-3">
                  <div class="col">
                    <s:url id="fileDownload" action="Download" >
                  		<s:param name="f"><s:property value='distMakerVO.fileAssignment' /></s:param>
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
                              <th scope="col">NPWP</th>
                              <th scope="col">NPWP Penyetor</th>
                              <th scope="col">NOP</th>
                              <th scope="col">NO SK</th>
                              <th scope="col">Uraian</th>
                              <th scope="col">Nama</th>
                              <th scope="col">Alamat</th>
                              <th scope="col">Jenis Pajak</th>
                              <th scope="col">Jenis Setoran</th>
                              <th scope="col">Start Date Pajak</th>
                              <th scope="col">End Date Pajak</th>
                              <th scope="col">Tahun Pajak</th>
                              <th scope="col">Jumlah Setor</th>
                              <th scope="col">ID Billing</th>
                              <th scope="col">Masa Aktif Billing</th>
                              <th scope="col">Validasi</th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="distMakerVO.listDetail">
                          <s:if test="!checkSuccess">
                            <tr style="background-color: red; color: white">
                          </s:if>
                          <s:else>
                          	<tr>
                          </s:else>
                              <td><s:property value="index" /></td>
                              <td><s:property value="npwpView" /></td>
                              <td><s:property value="npwpPenyetorView" /></td>
                              <td><s:property value="nopView" /></td>
                              <td><s:property value="noSkView" /></td>
                              <td><s:property value="uraianSsp" /></td>
                              <td><s:property value="nama" /></td>
                              <td><s:property value="alamat" /></td>
                              <td><s:property value="jenisPajak" /></td>
                              <td><s:property value="jenisSetoran" /></td>
                              <td><s:text name="u.date2"><s:param name="value" value="startDatePbb" /></s:text></td>
                              <td><s:text name="u.date2"><s:param name="value" value="endDatePbb" /></s:text></td>
                              <td><s:property value="tahunPajak" /></td>
                              <td><s:text name="u.formatMoney"><s:param name="value" value="jumlahSetor" /></s:text></td>                          
                             <td><s:property value="idBilling" /></td>
                             <td><s:text name="u.date"><s:param name="value" value="masaAktifBillingView" /></s:text></td>
                              <td><s:property value="validationDisplay" /></td>
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
                    <input class="form-control" value="<s:property value='distMakerVO.sracNo' />" disabled>
                  </div>
                </div>
                <div class="form-inline mx-4">
                  <div class="form-group mb-2">
                    <label class="text-black"><b>Total nominal yang akan dibayar : </b></label>
                  </div>
                  <div class="form-group mx-sm-3 mb-2">
                    <input class="form-control" value="<s:text name="u.formatMoney"><s:param name="value" value="distMakerVO.totalAmount" /></s:text>" disabled>
                  </div>
                </div>
                <div class="row mx-sm-3">
                  <div class="col-lg 6">
                    <div class="form-group">
                      <label class="text-black"><b>Remark</b></label>
                      <textarea class="form-control" id="remarks" rows="6" disabled><s:property value='distMakerVO.remarks' /></textarea>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row ml-sm-3 my-4">
                <div class="col-lg-2">
                	<s:a action="CreateIdBilling" cssClass="btn bg-red btn-lg text-white" cssStyle="text-align: left;">
                		<i class="fas fa-window-close mr-2"></i> Selesai
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