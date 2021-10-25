<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>List IB - NabungDividen</title>
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="UserData!processInput"/>
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
        <s:form id="form1">
        	<s:hidden name="userData.id" id="userDataId"/>
            <s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <s:a cssClass="btn bg-green text-white" action="UserData!gotoInput" >
                      <i class="fas fa-user-plus"></i> Tambah User
                    </s:a>
                  </div>
                </div>
                
                <div class="card shadow mx-4 my-4">
                  <div class="card-header bg-blue border-blue">
                    <h6 class="m-0 font-weight-bold text-white">List User</h6>
                  </div>
                  <div class="card-body border-blue">
                      <div class="table-responsive">
                        <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                          <thead>
                            <tr>
                              <th scope="col">No</th>
                              <th scope="col">User Code</th>
                              <th scope="col">User Name</th>
                              <th scope="col">Email</th>
                              <th scope="col">Level</th>
                              <th scope="col">Phone No</th>
                              <th scope="col"></th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="listUserAll">
                            <tr>
                              <td><s:property value="rowNum" /></td>
                              <td><s:property value="userCode" /></td>
                              <td><s:property value="userName" /></td>
                              <td><s:property value="email" /></td>
                              <td><s:property value="userLevelDisplay" /></td>
                              <td><s:property value="phoneNo" /></td>
                              <td>
                              	<s:url id="urlDetail" action="UserData!detail">
                              		<s:param name="userId" value="%{id}" />
                              	</s:url>
                                <s:a href="%{urlDetail}"><i class="fas fa-edit mr-4"></i> Edit 
                                </s:a>
                              </td>
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
		</s:form>
		
      </div>
      <!-- End of Main Content -->

    </div>
   	
</body>
</html>