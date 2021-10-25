<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.setting" /> | <s:text name="t.advertising" /></title>
		
		<style type="text/css">
			html { height: 100% }
			body { height: 100%; margin: 0; padding: 0 }
			#map_canvas { height: 100% }
			</style>
			<%-- <script type="text/javascript"
				src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA5gOz9zAq75_EIf8kZq2JQijJcEa3ZZe4&sensor=false">
			</script> --%>
			<script type="text/javascript"
				src="https://maps.googleapis.com/maps/api/js?key=<s:property value="googleApiKey" />&sensor=false">
			</script>			
		<!-- JAVA SCRIPT -->
		<s:url var="remoteurl" action="Ads!processInput"/>
		<script type="text/javascript">
		
			function toogleTableBank()
			{
				if($("#bankType").val()==2)
				{
					$("#tableGroup").show();
				}					
				else
				{
					$("#tableGroup").hide();
				}
			}
			
			function adsLocationLabel()
			{
				if($("#adsLocation").val()==1){
					$("#recommendLabel").text(" *Recommended display image size 1025x513 px");						
				}else if($("#adsLocation").val()==2){
					$("#recommendLabel").text(" *Recommended display image size 1025x513 px");						
				}else if($("#adsLocation").val()==3){
					$("#recommendLabel").text("");						
				}else if($("#adsLocation").val()==4){
					$("#recommendLabel").text(" *Recommended display image size 1280x516 px");						
				}
			}
		
			/* BEGIN javascript for google maps */
			var map;			
			var latitude;
			var longitude;
			function getLocation() {
			    if (navigator.geolocation) {
			        navigator.geolocation.getCurrentPosition(successPosition, showError);
			    }
			}
			
			function showError(err) {
				  console.warn(`ERROR(${err.code}): ${err.message}`);
			}
			
			function successPosition(position) {
			    latitude = position.coords.latitude;
			    longitude = position.coords.longitude;
			}
			
	        function initialize(longitude, latitude) {	        	
	        	/* getLocation(); */
	        	if(longitude.length==0 && latitude.length==0)
	        		{
	        			latitude = -6.20675;
	        			longitude = 106.8132684;
	        			$("#lon").val(106.8132684);
	        			$("#lat").val(-6.20675);	        			
	        		}
	            var latlong = new google.maps.LatLng(latitude, longitude);
	            var myOptions = {
	                zoom:14,
	                center: latlong,
	                mapTypeId: google.maps.MapTypeId.ROADMAP
	            }
	            map = new google.maps.Map(document.getElementById("gmap"), myOptions);
	            map.setZoom(14);
	            // marker refers to a global variable
	            var marker = new google.maps.Marker({
	                position: latlong,
	                map: map
	            });
	            
	            var radius = $("#radius").val()*1000;
	            
	            var circle = new google.maps.Circle({
	        		  map: map,
	        		  radius: $("#radius").val()*1000,    // 10 miles in metres
	        		  fillColor: '#AA0000'
	        		});
	            circle.bindTo('center', marker, 'position');
	            
	            google.maps.event.addListener(map, "click", function(event) {
	                // get lat/lon of click
	                var clickLat = event.latLng.lat();
	                var clickLon = event.latLng.lng();
	
	                // show in input box
	                document.getElementById("lat").value = clickLat.toFixed(5);
	                document.getElementById("lon").value = clickLon.toFixed(5);
					
	                marker.setPosition(new google.maps.LatLng(clickLat,clickLon));
	               	circle.setRadius($("#radius").val()*1000);
	            });	          
	           	            
	        }   
	        /* END javascript for google maps */
			$(document).ready(function() 
			{
				initialize('<s:property value="advertising.longitude" />', '<s:property value="advertising.latitude" />');
				
				$("#radius").change(function(){
					initialize($("#lon").val(), $("#lat").val());
				});
				
				$("#bankType").change(function(){
					toogleTableBank();
				});
				
				$("#adsLocation").change(function(){
					adsLocationLabel();
				});
				
				toogleTableBank();
				
				$("#btnSave").click(function()
				{					
					$("#MessageResult").removeClass(MessageState);					
					ProcessStart = Date.now();
					$("#ProgressRequest").show();
					
					var x = document.getElementsByName("selectedBankId");
					var banks="";
					var i;
					for (i = 0; i < x.length; i++) {
					    if (x[i].type == "checkbox") {
					        if(x[i].checked == true)
					        {
					        	if(banks.length==0)
					        	{
					        		banks+=x[i].value;
					        	}
					        	else
					        	{
					        		banks+=","+x[i].value;	
					        	}					        		
					        }
					    }
					}					

					var displayImageFile = $('#displayImageFile').get(0).files[0];
					var fullImageFile = $('#fullImageFile').get(0).files[0];
					var formData = new FormData();
					formData.append('advertising.displayImage', $('#displayImageFile').val());
					formData.append('advertising.fullImage', $('#fullImageFile').val());
					formData.append('advertising.adsName', $("#adsName").val());
					formData.append('advertising.adsLocation', $("#adsLocation").val());
					formData.append('advertising.id', $("#id").val());
					formData.append('advertising.textContent', $("#textContent").val());
					formData.append('advertising.adsUrl', $("#adsUrl").val());
					formData.append('advertising.radiusStr', $("#radius").val());
					formData.append('advertising.periodStart', $("#periodStart").val());
					formData.append('advertising.periodEnd', $("#periodEnd").val());
					formData.append('advertising.longitude', $("#lon").val());
					formData.append('advertising.latitude', $("#lat").val());
					formData.append('advertising.bankType', $("#bankType").val());
					formData.append('advertising.adsPriority', $("#adsPriority").val());					
					formData.append('advertising.displayImageFile', displayImageFile);
					formData.append('advertising.fullImageFile', fullImageFile);
					formData.append('selectedBank', banks);
					
					// PROCESS
					
					$.ajax
					({
						type 		: 'POST', // define the type of HTTP verb we want to use (POST for our form)
						url 		: /* '<s:property value="%{remoteurl}" />' */ '<s:property value="%{remoteurl}"/>', // the url where we want to POST
						data 		: formData, // our data object
						cache: false,
					    contentType: false,
					    processData: false,
					}).done(function(resultJson) 
					{
						if(resultJson.substr(2,7)=="DOCTYPE")
						{
							window.location.href='<s:property value="@id.co.emobile.samba.web.data.WebConstant@LOGIN_PATH"/>';
						}
						$("#ProgressRequest").hide();
						resultObject = JSON.parse(resultJson);
						
						if(resultObject.rc == 0)
						{
							if(resultObject.type > 0)
							{
								window.location.href=resultObject.path;	
							}
							else
							{
								/* $("#form1").trigger("reset"); */
								$("#wordingInd").text("-");
								$("#wordingEng").text("-");
								ProcessEnd = Date.now();
								ProcessTime = (ProcessEnd - ProcessStart) / 1000;
								
								$("#MessageResult").addClass(ResultSuccess);
								$("#MessageResult").empty();
								$("#MessageResult").append(resultObject.message + ", process took " + ProcessTime + " seconds");
								document.location.href = "#form1";
							}
						}
						else
						{
							$("#MessageResult").addClass(ResultFailure);
							$("#MessageResult").empty();
							$("#MessageResult").append(resultObject.message);
							document.location.href = "#form1";
						}
					});
					
					// TIMEOUT
					
					setTimeout(function()
					{
						$("#ProgressRequest").hide();
					}, 
					15000);
					
					setTimeout(function()
					{
						$(LoadingSquareClass).append("Process took longer than usual, press ESC to cancel.");
						
						$(document).keyup(function(e) 
						{
							if (e.keyCode == 27)
							{
								$("#ProgressRequest").hide();
							}
						});
					}, 
					5000);
					
					return false;
				});
			});
	        
			$(document).ready(function()
			{
				MessageFail("<s:property value='message' />", "#MessageResult");
			});
		</script>
		
	<style>
		 div#gmap {
		        width: 100%;
		        height: 400px;
		        border:double;
		 }
    </style>
	</head>

	<body>
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.setting" /> | <s:text name="t.advertising" /></h1>
                <h2><s:text name="t.advertising.description" /></h2>
            </hgroup>
            
            <!-- CONTENT -->
              <div class="nav nav-tabs col-8 offset-md-2" id="nav-tab" role="tablist">
                <s:url id="search" action="Ads!gotoSearch" includeParams="none"/>
                <s:url id="input" action="Ads!gotoInput" includeParams="none"/>
                <s:if test="advertising.id == 0">
                	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
                	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.insert"/></h5></a>
                </s:if>
                <s:else>
                	<s:a href="%{search}" cssClass="ClearHyperlink nav-item nav-link"><h5><s:text name="f.search"/></h5></s:a>
                	<s:a href="%{input}" cssClass="ClearHyperlink nav-item nav-link" id="nav-profile-tab"><h5><s:text name="f.insert"/></h5></s:a>                	
                	<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"><h5><s:text name="f.update"/></h5></a>
				</s:else>                                     
              </div>
				<br>
                
			<div class="col-8 offset-md-2">
                <!-- CONTENT MAIN -->
                <s:form id="form1" action="Ads!processDelete">                	
                	<!-- MESSAGE RESULT -->
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
       
                        <s:actionerror /><s:actionmessage />
                       		<s:hidden name="advertising.id" id="id" />
							<s:if test="advertising.id==0">                       		
	                       		<div class="form-group row">
	                        		<label for="adsName" class="col-sm-4 col-form-label">
	                        			<s:text name="l.adsName" />
	                        			<font color="red" size="1"><i><b>*)</b></i></font>	
	                        		</label>
	                        		<div class="col-sm-8">
										<s:textfield id="adsName" name="advertising.adsName" cssClass="form-control" 
											required="true" onkeypress="return fileNameForbiddenChar(event)" maxlength="160"/>
									</div>
								</div>
							</s:if>
							<s:else>
								<div class="form-group row">
	                        		<label for="adsName" class="col-sm-4 col-form-label">
	                        			<s:text name="l.adsName" />
		                        		<font color="red" size="1"><i><b>*)</b></i></font>	
	                        		</label>	                        		
	                        		<div class="col-sm-8">
										<s:textfield id="adsName" name="advertising.adsName" cssClass="form-control" 
											readOnly="true" onkeypress="return fileNameForbiddenChar(event)" maxlength="160"/>
									</div>
								</div>
								<div class="form-group row">
	                        		<label for="hitCount" class="col-sm-4 col-form-label">
	                        			<s:text name="l.hitCount" />
	                        		</label>	                        		
	                        		<div class="col-sm-8">
										<s:property value="advertising.hitCount"/>
									</div>
								</div>
							</s:else>
							
							<br>							
							<div class="form-group row">
	                        	<label for="showOn" class="col-sm-4 col-form-label"><s:text name="l.showOn" /></label>
	                        	<div class="col-sm-8">
									<s:select id="adsLocation" name="advertising.adsLocation" cssClass="form-control"
										list="listShowOn" listKey="lookupValue" listValue="lookupDesc" />
								</div>
							</div>
							
							<s:if test="advertising.id>0 and advertising.adsLocation==3">
								<div class="form-group row">
		                        	<label for="pushStatus" class="col-sm-4 col-form-label"><s:text name="l.pushStatus" /></label>
		                        	<div class="col-sm-8">
										<s:property value="pushStatusDisplay" />
									</div>
								</div>
							</s:if>
							
							<s:if test="advertising.id>0">
								<br>
								<div class="form-group row">
									<label for="'photo'" class="col-sm-4 col-form-label"><s:text name="l.existingDisplayImage" /></label>
									<div class="col-sm-8">
										<img id='photo' src="<s:url value="%{photoLink}" />" width='200' height='200' style="z-index: 2;"/>
									</div>
								</div>
							</s:if>
                        	<div class="form-group row">
	                        	<label for="displayImage" class="col-sm-4 col-form-label">
	                        		<s:text name="l.displayImage" />
		                        	<font color="red" size="1"><i><b>*)</b></i></font>
	                        	</label>
	                        	<div class="col-sm-8">
		                        	<input id="displayImageFile" type="file" name="advertising.displayImageFile" cssClass="form-control" />
	                        	</div>
	                        	<div class="col-sm-4">
	                        	</div>
	                        	<div class="col-sm-6">
	                        		<font size="1" color="red" id="recommendLabel"> *Recommended display image size 1025x513 px</font>
	                        	</div>
							</div>
							
							<s:if test="advertising.id>0 and advertising.fullImage.length > 0">
								<br>
								<div class="form-group row">
									<label for="'photo'" class="col-sm-4 col-form-label"><s:text name="l.existingFullImage" /></label>
									<div class="col-sm-8">
										<img id='photo' src="<s:url value="%{photoLinkFull}" />" width='200' height='200' style="z-index: 2;"/>
									</div>
								</div>
							</s:if>
                        	<div class="form-group row">
	                        	<label for="fullImage" class="col-sm-4 col-form-label"><s:text name="l.fullImage" /></label>
								<div class="col-sm-8">
									<input id="fullImageFile" type="file" name="advertising.fullImageFile" cssClass="form-control" />
								</div>
							</div>
                        	<div class="form-group row">
	                        	<label for="textContent" class="col-sm-4 col-form-label">
	                        		<s:text name="l.textContent" />
	                        		<font color="red" size="1"><i><b>*)</b></i></font>
	                        	</label>
								<div class="col-sm-8">
									<s:textfield id="textContent" maxlength="300" name="advertising.textContent" cssClass="form-control" required="true"/>
								</div>
                        	</div>
                        	<div class="form-group row">
	                        	<label for="textContent" class="col-sm-4 col-form-label">
	                        		<s:text name="l.adsPriority" />
	                        	</label>
								<div class="col-sm-8">
									<s:textfield id="adsPriority" name="advertising.adsPriority" cssClass="form-control" 
										onkeypress="return isNumberOnly(event)" required="true"/>
								</div>
                        	</div>
                        	<div class="form-group row">
	                        	<label for="adsUrl" class="col-sm-4 col-form-label">
	                        		<s:text name="l.adsUrl" />
	                        		<font color="red" size="1"><i><b>*)</b></i></font>	
	                        	</label>
								<div class="col-sm-8">
									<s:textfield id="adsUrl" name="advertising.adsUrl" maxlength="300" cssClass="form-control" required="true"/>
								</div>
                        	</div>
                        	<div class="form-group row">
	                        	<label for="radius" class="col-sm-4 col-form-label">
	                        		<s:text name="l.radius" />
	                        		<font color="red" size="1"><i><b>*)</b></i></font>	
	                        	</label>
								<div class="col-sm-6">
									<s:textfield id="radius" name="advertising.radius" onkeypress="return isFloatFormat(event)"
										cssClass="form-control" required="true"/>
								</div>
								<div class="col-sm-1">Km</div>
                        	</div>
                        	<div class="form-group row">
	                            <label for="periodStart" class="col-sm-4 col-form-label">
	                            	<s:text name="l.period"/>
	                            	<font color="red" size="1"><i><b>*)</b></i></font>	
	                            </label>
	                            <div class="col-sm-4">
		                            <s:textfield type="text" id="periodStart" name="advertising.periodStart" required="true"
		                            	cssClass="form-control" value="%{getText('u.datetime',{advertising.periodStart})}"/>
	                            </div>
								<div class="text-center" style="margin-left: -1%; margin-right: -1%;">-</div>
								<div class="col-sm-4">
		                            <s:textfield type="text" id="periodEnd" name="advertising.periodEnd" required="true"
		                            	cssClass="form-control" value="%{getText('u.datetime',{advertising.periodEnd})}"/>
	                            </div>
                        	</div>
                        	<div class="form-group row">
	                        	<label for="advertising" class="col-sm-4 col-form-label"><s:text name="l.longlat" /><font color="red" size="1"><i><b>*)</b></i></font></label>
								<div class="col-sm-4">
									<s:textfield id="lon" name="advertising.longitude" onkeypress="return isFloatFormat(event)"
										cssClass="form-control" required="true"/>
								</div>
								<div class="text-center" style="margin-left: -1%; margin-right: -1%;">-</div>
								<div class="col-sm-4">
									<s:textfield id="lat" name="advertising.latitude" onkeypress="return isFloatFormat(event)"
										cssClass="form-control" required="true"/>
								</div>
							</div>
							
							<br>
							<div id="gmap"></div>
                        	<br>
                        	<br>
                        	<div class="form-group row">
	                        	<label for="bankType" class="col-sm-4 col-form-label"><s:text name="l.bankType" /></label>
								<div class="col-sm-8">
									<s:select id="bankType" name="advertising.bankType" cssClass="form-control"
										list="listBankType" listKey="lookupValue" listValue="lookupDesc" />
								</div>
							</div>
					
					<div class="TableGroupAds" id="tableGroup">	                       
						<table>	
							<tr>
								<td colspan="10"><b>LIST EXISTING BANKS</b></td>
							</tr>															
							<tr>
								<s:iterator value="listBank" status="stat">
								<td>
									<table>
										<tr>
											<td>
												<label for="TriggerSubModule<s:property value="id"/>"><s:property value="bankName"/></label>
											</td>
											<td style="padding-right: 40px; vertical-align: center;">
											<label for="TriggerSubModule<s:property value="id"/>"></label>										
											<s:if test="isBankSelected(id)">
												<input type="checkbox" id="TriggerSubModule<s:property value="id"/>" name="selectedBankId" checked="checked" value="<s:property value="id"/>">
											</s:if>	
											<s:else> 
												<input type="checkbox" id="TriggerSubModule<s:property value="id"/>" name="selectedBankId"  value="<s:property value="id"/>">
											</s:else> 
											</td>
										</tr>
									</table>
								</td>
									<s:if test="#stat.last || #stat.index % 5 == 4">
										</tr><tr>
									</s:if>
								</s:iterator>										
							</tr>
						</table>					
					</div>
					<br>
					<s:if test="checkPrevileges(menuId)">
	                    <!-- BUTTON -->
	                    <div class="col-12 offset-md-3">
	                        <button type="input" id="btnSave" class="ButtonPrimary btn btn-danger" value="%{getText('b.save')}"><i class="fas fa-save" style="vertical-align: middle;"></i>&nbsp;<s:text name="b.save"/></button>
	                        <button type="reset" class="ButtonReset btn btn-primary" value="Reset"><i class="fas fa-sync" style="vertical-align: middle;"></i>&nbsp;Reset</button>
	                       <s:if test="advertising.hitCount==0 and advertising.id>0 ">	                       
	                       		<button type="submit" id="btnDelete" class="ButtonPrimary btn btn-danger" value="%{getText('b.delete')}"><i class="fas fa-save" style="vertical-align: middle;"></i>&nbsp;<s:text name="b.delete"/></button>	                        
	                       </s:if>
	                    </div>
                    </s:if>
										
				</s:form>
				<br><br>
            </div>
            <script>
			    $('#periodStart').datetimepicker({
			    	format: 'dd/mm/yyyy HH:MM',
			    	uiLibrary: 'bootstrap4',
			    });

			    $('#periodEnd').datetimepicker({
			    	format: 'dd/mm/yyyy HH:MM',
			    	uiLibrary: 'bootstrap4',
			    });
	    	</script>
        </main>
	</body>
</html>