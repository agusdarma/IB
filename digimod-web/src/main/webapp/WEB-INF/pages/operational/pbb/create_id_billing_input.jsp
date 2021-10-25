<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<script  src='<s:url value="/Java Script/accounting.js"/>' type="text/javascript"></script>
	<script  src='<s:url value="/Java Script/terbilang.js"/>' type="text/javascript"></script>
	<title>Kas Non Tunai - Distribution Maker</title>
	<script>
	function isNumberKey(evt){
		var charCode = (evt.which) ? evt.which : event.keyCode
		if (charCode > 7 && charCode < 58)
			return true;
		return false;
	}	
	function isNoInput(evt){
		return false;
	}	
	
	function counterForm(form){
	//separator onchange
		var calculateOTR = document.getElementById('calculateOTR');
		$('#terbilang').val(terbilang(calculateOTR.value));
		$('#calculateOTR').val(accounting.formatMoney(calculateOTR.value));
		
	}
	
	$(document).ready(function(){
	//separator onload
		var calculateOTR = document.getElementById('calculateOTR');
		$('#terbilang').val(terbilang(calculateOTR.value));
		$('#calculateOTR').val(accounting.formatMoney(calculateOTR.value));
		

		
	});//end jquery
  </script>
	<sx:head />
	
</head>

<body>
	 
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content" class="bg-gray">

        <!-- Topbar -->
        <%@ include file="/WEB-INF/includes/include_topbar.jsp"%>
        <!-- End of Topbar -->

		<s:form action="CreateIdBilling!process" method="POST" enctype="multipart/form-data">
        <!-- Begin Page Content -->
        <div class="container-fluid">
          <div class="row">
            <div class="col-xl-12 col-lg-12">
              <div class="card shadow">
                <div class="card-body">
                  <div class="card shadow bg-blue text-white mb-4">
                    <div class="card-body">
                      Create ID Billing Pajak
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
					  		<label class="text-white"><b><i class="fas fa-file-csv"></i> Create ID Billing Pajak</b></label>
					  	</div>
					  	<div class="card-body">
					  	<label class="text-black"><b>NPWP SSP</b></label>
					  	<s:textfield type="text" id="npwpSsp" maxLength="15" name="npwpSsp" onchange="javascript:formatNpwpGlobal('npwpSsp');" onKeyPress="return isNumberKey(event)" onfocus="this.select();" cssClass="form-control form-control-lg" required="true" />
					  	<label class="text-black"><b>NPWP Penyetor</b></label>
					  	<s:textfield type="text" id="npwpPenyetor" maxLength="15" name="npwpPenyetor" onchange="javascript:formatNpwpGlobal('npwpPenyetor');" onKeyPress="return isNumberKey(event)" onfocus="this.select();" cssClass="form-control form-control-lg" required="true" />			 					 
					  	<label class="text-black"><b>Jenis Pajak</b></label>					  	
						<div class="col-9">							
							<sx:autocompleter dropdownHeight="500" dropdownWidth="550" searchType="substring" id="jenisPajak" name="jenisPajak" list="listJenisPajak" 
							cssClass="custom-select" listValue="lookupDesc" listKey="lookupValue"/>
						</div>								
						<label class="text-black"><b>Jenis Setoran</b></label>
						<div class="col-9">							
							<sx:autocompleter dropdownHeight="500" dropdownWidth="550" searchType="substring" id="jenisSetoran" name="jenisSetoran" list="listJenisSetoran" 
							cssClass="custom-select" listValue="lookupDesc" listKey="lookupValue"/>
						</div>												  	
					  	<label class="text-black"><b>Masa Pajak</b></label>
						<div class="col-4">
						 <s:textfield width="300" id="startDatePbb" name="startDatePbb" cssClass="form-control" />
						</div>
						<label class="col-1 col-form-label text-center"> - </label>					
						<div class="col-4">
						 <s:textfield width="300" id="endDatePbb" name="endDatePbb" cssClass="form-control" />
						</div>
						<label class="text-black"><b>Tahun Pajak</b></label>
					  	<s:textfield type="text" id="tahunPajak" maxLength="4" name="tahunPajak" cssClass="form-control form-control-lg" required="true" />	
						<label class="text-black"><b>Jumlah Setor</b></label>
					  	<s:textfield type="text" maxLength="12" name="jumlahSetor" onfocus="this.select();" onchange="javascript:counterForm(document.myform);" id="calculateOTR" onKeyPress="return isNumberKey(event)" cssClass="form-control form-control-lg" required="true" />
					  	<label class="text-black"><b>Terbilang</b></label>		  	
					  	<s:textfield readonly="true" type="text" id="terbilang" name="terbilang" cssClass="form-control form-control-lg" />					  						  					
					  	<label class="text-black"><b>No Sk</b></label>
					  	<s:textfield type="text" id="noSk" maxLength="15" onfocus="this.select();" name="noSk" onchange="javascript:formatNoSkGlobal('noSk');" onKeyPress="return isNumberKey(event)" cssClass="form-control form-control-lg" />
					  	<label class="text-black"><b>No Objek Pajak</b></label>
					  	<s:textfield type="text" id="nop" maxLength="18" onfocus="this.select();" name="nop" onchange="javascript:formatNopGlobal('nop');" onKeyPress="return isNumberKey(event)"  cssClass="form-control form-control-lg" />	
					  	<label class="text-black"><b>Uraian</b></label>
					  	<s:textfield type="text" id="uraianSsp" name="uraianSsp" cssClass="form-control form-control-lg" required="true" />					  				
					  	
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
    <script>
	    $('#startDatePbb').datepicker({
	    	format: 'dd/mmm',
	    	uiLibrary: 'bootstrap4'
	    });
	    $('#endDatePbb').datepicker({
	    	format: 'dd/mmm',
	        uiLibrary: 'bootstrap4'
	    });	   
   	</script>
</body>
</html>
