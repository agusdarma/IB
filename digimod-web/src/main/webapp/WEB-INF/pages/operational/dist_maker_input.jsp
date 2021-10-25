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

		<s:form action="DistMaker!process" method="POST" enctype="multipart/form-data">
        <!-- Begin Page Content -->
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="card-body">
                  <div class="card shadow bg-blue text-white mb-4">
                    <div class="card-body">
                      Input Transaksi
                    </div>
                  </div>
            <s:if test="message != null">
			<div class="alert alert-danger" role="alert">
              <s:property value="message" />
            </div>
			</s:if>
                  <div class="row mx-2">
                    <div class="col-lg-4">
                      <label class="text-black"><b>Pilih rekening giro</b></label>
                      <s:iterator value="listSourceAccount">
                      	<div class="form-check mt-2">
                        <input name="sracId" class="form-check-input" type="radio" id="exampleRadios<s:property value='id' />" 
                        	value="<s:property value='id' />">
                        <label class="form-check-label" for="exampleRadios<s:property value='id' />">
                          <s:property value="sracNumber" /> = 
                          <s:if test='"0".equals(balanceRc)'>
                          <s:text name="u.formatMoney"><s:param name="value" value="sracBalance" /></s:text>
                          </s:if>
                          <s:else>
                          	Error <s:property value="balanceRc" />
                          </s:else>
                        </label>
                      </div>
                      </s:iterator>
                    </div>
                    <div class="col-lg-6"> 
					  <div class="card mb-4">
					  	<div class="card-header bg-green ">
					  		<label class="text-white"><b><i class="fas fa-file-csv"></i> Upload CSV Format</b></label>
					  	</div>
					  	<div class="card-body">
					  		<input type="file" class="form-control-file" id="exampleInputFile" aria-describedby="fileHelp" name="fileDist">
					  	</div>
					  </div>                                   
					  <div class="card mb-4">
					  	<div class="card-header bg-orange">
					  		<label class="text-white"><b><i class="fas fa-file-pdf"></i> Upload PDF Penugasan</b></label>
					  	</div>
					  	<div class="card-body">
					  		<s:file name="fileAssignment" cssClass="form-control-file" />
					  	</div>
					  </div>
                    </div>
                  </div>
                  <div class="row ml-2">
                  	<div class="col-lg-12">
	                 	<div class="form-group">
	                      <label class="text-black"><b>Remark</b></label>
	                      <s:textarea cssClass="form-control" name="remarks" rows="4" />
	                    </div>                    	
                  	</div>                	
                  </div>
                  <div class="row mt-4">
                    <div class="col-lg-12">
	                    <s:submit type="button" cssClass="btn bg-blue btn-lg text-white" cssStyle="text-align: left;">
		                    <i class="fas fa-sync-alt mr-2"></i></i> Proses
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
