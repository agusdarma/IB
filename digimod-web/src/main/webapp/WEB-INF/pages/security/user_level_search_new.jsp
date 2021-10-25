<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content-new">
		<title>Kas Non Tunai - Level Pengguna</title>
		
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="UserLevel!processSearch"/>
		<script type="text/javascript">	 
			$(document).ready(function()
			{
				MessageResult("<s:property value='message' />", "#MessageResult");	
				DirectSubmit("#formSearch", "#btnSearch", "#ProgressRequest", "divSearch", '<s:property value="%{remoteurl}"/>');
				GenerateTableNoProcessTime("#formSearch", "#ProgressRequest", "divSearch", '<s:property value="%{remoteurl}"/>');
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
        	<s:hidden name="userLevel.levelId" id="userLevelId"/>
            <s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
				<div class="row mt-4 ml-3">
                  <div class="col">
                    <s:a cssClass="btn bg-green text-white" action="UserLevel!gotoInput" >
                      <i class="fas fa-user-plus"></i> Tambah Level
                    </s:a>
                  </div>
                </div>
                
                <div class="card shadow mx-4 my-4">
                  <div class="card-header bg-blue border-blue">
                    <h6 class="m-0 font-weight-bold text-white">List User Level</h6>
                  </div>
                  <div class="card-body border-blue">
                      <div class="table-responsive">
                        <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                          <thead>
                            <tr>
                              <th scope="col">No</th>
                              <th scope="col">Level Name</th>
                              <th scope="col">Level Type</th>
                              <th scope="col">Level Description</th>
                              <th scope="col"></th>
                            </tr>
                          </thead>
                          <tbody>
                          <s:iterator value="listUserAll">
                            <tr>
                              <td><s:property value="rowNum" /></td>
                              <td><s:property value="levelName" /></td>
                              <td><s:property value="levelTypeDisplay" /></td>
                              <td><s:property value="levelDesc" /></td>
                              <td>
                              	<s:url id="urlDetail" action="UserLevel!detail">
                              		<s:param name="levelId" value="%{levelId}" />
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
		</s:form>
		
      </div>
      <!-- End of Main Content -->

    </div>
	</body>
</html>