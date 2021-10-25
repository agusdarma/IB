<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - Distribution Approval</title>
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
                    <h6 class="m-0 font-weight-bold text-white">List Checked Distribution</h6>
                  </div>
                  <div class="card-body border-blue">
                      <div class="table-responsive">
                        <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                          <thead>
                            <tr>
                              <th scope="col">Sys Number</th>
                              <th scope="col">Nama File CSV</th>
                              <th scope="col">Rekening</th>
                              <th scope="col">Jumlah</th>
                              <th scope="col">Waktu Checker</th>
                              <th scope="col">User Maker</th>
                              <th scope="col">User Checker</th>
                              <th scope="col">Status</th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="listHeader">
                          	<tr>
                              <td>
                              	<s:url id="approvalDetail" action="CreateIdBillingApproval!detail">
                              		<s:param name="sysLogNo" value="sysLogNo" />
                              	</s:url>
                                <s:a href="%{approvalDetail}">
                                  <s:property value="sysLogNo" />
                                </s:a>
                              </td>
                              <td><s:property value="fileData" /></td>
                              <td><s:property value="sracNumber" /></td>
                              <td><s:text name="u.formatMoney"><s:param name="value" value="uploadedValue" /></s:text></td>
                              <td><s:text name="u.datetime2"><s:param name="value" value="checkedOn" /></s:text></td>
                              <td><s:property value="uploadedUserCode" /></td>
                              <td><s:property value="checkedUserCode" /></td>
                              <td><s:property value="statusDisplay" /></td>
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