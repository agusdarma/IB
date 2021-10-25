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
				$("#btnReload").click(function() {
					$.ajax({
					     type: "GET",
					     url: "<s:url action='SystemSetting!reload' />",
					     success: function(response){
					    	 respJson = JSON.parse(response);
					         alert(respJson.message);
					     }
					});
				});
				
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
       
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <button id="btnReload" class="btn bg-green text-white">
                      <i class="fas fa-sync-alt"></i> Reload System Setting
                    </button>
                  </div>
                </div>
                <s:if test="message != null">
                <div class="alert alert-success" role="alert">
	              <s:property value="message" />
	            </div>
                </s:if>
                <div class="card shadow mx-4 my-4">
                  <div class="card-header bg-blue border-blue">
                    <h6 class="m-0 font-weight-bold text-white">List System Setting</h6>
                  </div>
                  <div class="card-body border-blue">
                      <div class="table-responsive">
                        <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                          <thead>
                            <tr>
                              <th scope="col">No</th>
                              <th scope="col">Nama Setting</th>
                              <th scope="col">Keterangan Setting</th>
                              <th scope="col">Data Setting</th>
                              <th scope="col"></th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="listSystemSettingAll">
                            <tr>
                              <td><s:property value="rowNum" /></td>
                              <td><s:property value="settingName" /></td>
                              <td><s:property value="settingDesc" /></td>
                              <td><s:property value="settingValue" /></td>
                              <td>
                              	<s:url id="urlDetail" action="SystemSetting!detail">
                              		<s:param name="settingId" value="%{id}" />
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
		
      </div>
      <!-- End of Main Content -->

    </div>
   	
</body>
</html>