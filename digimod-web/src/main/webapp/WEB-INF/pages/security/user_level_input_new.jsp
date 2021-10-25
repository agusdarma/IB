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
		<s:url var="processInput" action="UserLevel!processInput" />
		<script type="text/javascript">
		$(document).ready(function() 
		{
			ButtonRequest("#form1", "#btnSave", "#ProgressRequest", "#MessageResult", '<s:property value="%{processInput}"/>');
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
       	<s:hidden name="userLevel.levelId" />
		<s:hidden name="levelId" />					
		<s:hidden name="moduleState" />
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="row mt-4 ml-3">
                  <div class="col">
                    <s:a cssClass="btn bg-green text-white" action="UserLevel" >
                      <i class="fas fa-users"></i> List User Level
                    </s:a>
                  </div>
                </div>
                
                <div class="card-body">
                  <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
                  <div class="row mx-2">
                    <div class="col-lg-4">
                      	<div class="form-group">
                          <label class="text-black"><b><s:text name="l.levelName"/></b></label>
                          <s:if test="userLevel.levelId > 0">
                          	<s:textfield maxlength="32" type="text" id="userName" name="userLevel.levelName" cssClass="form-control form-control-lg" required="true" readOnly="true"/>
                          </s:if>
                          <s:else>
                          	<s:textfield maxlength="32" type="text" id="userName" name="userLevel.levelName" cssClass="form-control form-control-lg" required="true" />
                          </s:else>
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b>Level Type</b></label>
                          <s:select id="levelType" name="userLevel.levelType" cssClass="form-control" list="listLevelType" listKey="lookupValue" listValue="lookupDesc" />
                        </div>
                        <div class="form-group">
                          <label class="text-black"><b><s:text name="l.levelDescription"/></b></label>
                          <s:textarea id="remark" maxlength="255" name="userLevel.levelDesc" cssClass="form-control" />
                        </div>

                    </div>
                  </div>
                  
                  <div class="card shadow mx-4 my-4">
	                  <div class="card-header bg-blue border-blue">
	                    <h6 class="m-0 font-weight-bold text-white">List User Level</h6>
	                  </div>
                  <div class="card-body border-blue">
                      <s:iterator value="listModule">
                        <table class="card shadow mx-4 my-4" id="dataTable" width="30%" cellspacing="0">
                          <thead class="card-header bg-blue border-blue">
                            <tr class="font-weight-bold text-white">
								<th><s:property value="header"/></th>
                            </tr>
                          </thead>
                          <tbody>
	                          <s:iterator value="bodies" status="Index">
									<tr>
										<td>
											<label for="TriggerSubModule<s:property value="menuId"/>"><s:property value="menuText"/></label>
										</td>												
										<td>
											<div class="CheckboxSlide CheckboxSlideForOthers">
											<s:if test="isFullAccess(menuId)">
												<input type="checkbox" id="TriggerAccess<s:property value="menuId"/>" name="modulesAccess[<s:property value="menuId"/>].accessLevel" checked="checked" value="<s:property value="menuId"/>1"/>
											</s:if>	
											<s:else>
												<input type="checkbox" id="TriggerAccess<s:property value="menuId"/>" name="modulesAccess[<s:property value="menuId"/>].accessLevel"  value="<s:property value="menuId"/>1"/>
											</s:else>
												<label for="TriggerAccess<s:property value="menuId"/>"></label>
											</div>
										</td>
									</tr>
								</s:iterator>
                          </tbody>
                        </table>
                        </s:iterator>
                  </div>
                </div> 
                  
                  <div class="row" style="margin-top: 20vh;">
                    <div class="col-lg-12">
                      <s:submit id="btnSave" type="button" cssClass="btn bg-blue btn-lg text-white" style="text-align: left;">
                        <i class="fas fa-sync-alt mr-2"></i> Simpan Data
                      </s:submit>
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