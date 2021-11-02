<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>NabungDividen - Introducing Broker Main Menu</title>
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
                <div class="row">
                  <div class="col-lg-5 mb-4 mt-4 ml-4">
                    <div class="card shadow bg-blue-gradient-2">
                      <!-- Card Body -->
                      <div class="card-body ml-2 mt-2">
                        <div class="row">
                          <div class="col-6">
                            <i class="fa fa-user ml-4 text-dark-blue" style="font-size:8rem;"></i>
                          </div>
                          <div class="col-6">
                            <div>
                              <label class="text-golden">Kode User</label>
                              </br>
                              <label class="text-white"><s:property value="%{#session.LOGIN_KEY.userCode}" /></label>
                            </div>
                            </br>
                            <div>
                              <label class="text-golden">Nama Level</label>
                              </br>
                              <label class="text-white"><s:property value="%{#session.LOGIN_KEY.levelVO.levelName}" /></label>
                            </div>
                          </div>
                        </div>
                        </br>
                        <div class="row">
                          <div class="col-6">
                            <div>
                              <label class="text-golden">Login terakhir</label>
                              </br>
                              <label class="text-white"><s:property value="%{#session.LOGIN_KEY.lastLoginOn}" /></label>
                            </div>
                            </br>
                            <div>
                              <label class="text-golden">No Handphone</label>
                              </br>
                              <label class="text-white"><s:property value="%{#session.LOGIN_KEY.phoneNo}" /></label>
                            </div>
                          </div>
                          <div class="col-6">
                            <div>
                              <label class="text-golden">Nama User</label>
                              </br>
                              <label class="text-white"><s:property value="%{#session.LOGIN_KEY.userName}" /></label>
                            </div>                            
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="card bg-green text-white shadow mt-2">
                      <div class="card-body">
                      <s:a action="ChangePassword">
                        <div class="row text-white">
                          <div class="ml-4">
                            <i class="fas fa-unlock mr-4" style="font-size: 1.5em"></i>
                          </div>
                          <div>
                            <b>Ubah Password</b>
                          </div>
                        </div>
                      </s:a>
                      </div>
                    </div>
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
                    <div class="card bg-red text-white shadow mt-2">
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
                  <div class="col-lg-6 mb-4 mt-4">
                  <%-- 
                    <div class="card bg-light-blue text-white shadow mb-2">
                      <div class="card-body mt-2 mb-2">
                        <div class="row">
                          <div class="ml-4">
                            <i class="fas fa-bell mr-4" style="font-size: 2em;"></i>
                          </div>
                          <div>
                            <b style="font-size: 1.2em;">Notifikasi</b>
                            <span style="font-size: 1.2em;" class="badge badge-primary mr-2 ml-2 bg-red">10</span>
                          </div>
                          <div class="suffix-icon mr-4 mt-2">
                            <i class="fas fa-arrow-alt-circle-right" style="font-size: 1.5em;"></i>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="card bg-golden text-black shadow mb-4">
                      <div class="card-body mt-2 mb-2">
                        <div class="row">
                          <div class="ml-4">
                            <i class="fas fa-clipboard mr-4" style="font-size: 2em;"></i>
                          </div>
                          <div>
                            <b style="font-size: 1.2em;">Pending</b>
                            <span style="font-size: 1.2em;" class="badge badge-primary mr-2 ml-2 bg-red">10</span>
                          </div>
                          <div class="suffix-icon mr-4 mt-2">
                            <i class="fas fa-arrow-alt-circle-right" style="font-size: 1.5em;"></i>
                          </div>
                        </div>
                      </div>
                    </div>
                    --%>
                    <div class="card shadow mb-4">
                      <div class="card-header bg-blue border-blue">
                        <h6 class="m-0 font-weight-bold text-white">Aktivitas terakhir</h6>
                      </div>
                      <div class="card-body border-blue">
                          <div class="table-responsive">
                            <table class="table table-striped" id="dataTable" width="100%" cellspacing="0">
                              <thead>
                                <tr>
                                  <th scope="col-4">Tanggal</th>
                                  <th scope="col-4">Deskripsi</th>
                                </tr>
                              </thead>
                              <tbody>
                              <s:iterator value="lastUserActivityForUser">
                                <tr>
                                  <td><s:text name="u.datetime"><s:param name="value" value="createdOn" /></s:text></td>
                                  <td><s:property value="description" /></td>
                                </tr>
                              </s:iterator>
                              </tbody>
                            </table>
                          </div>
                          <%-- 
                          <div class="row my-4">
                            <div class="suffix-icon mr-4">
                              <a href="#"><i class="fas fa-long-arrow-alt-right mr-2"></i> Lihat Semua </a>
                            </div>
                          </div>
                          --%>
                      </div>
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