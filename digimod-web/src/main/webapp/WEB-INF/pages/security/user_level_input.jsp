<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa"%>

<html>
	<head>
		<!-- META DATA -->
		<meta name="decorator" content="content">
		<title><s:text name="t.security" /> | <s:text name="t.userLevel" /></title>
		
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
		<!-- MAIN -->
        <main>
        	<!-- PAGE TITLE -->
        	<hgroup class="ContainerInlineBlock">
            	<h1><s:text name="t.security" /> | <s:text name="t.userLevel" /></h1>
                <h2><s:text name="t.userLevel.description" /></h2>
            </hgroup>
            
              <div class="nav nav-tabs col-6 offset-md-3" id="nav-tab" role="tablist">
                <s:url id="search" action="UserLevel!gotoSearch" includeParams="none"/>
                <s:url id="input" action="UserLevel!gotoInput" includeParams="none"/>
                <s:if test="userLevel.levelId == 0">
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
            <!-- CONTENT -->
            
                <!-- PAPER CONTENT -->
            <s:form id="form1" method="post">
               	<div class="col-6 offset-md-3">
                	<!-- MESSAGE RESULT -->
            		<%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
            		
            		<!-- FORM -->
							<s:hidden name="userLevel.levelId" />
							<s:hidden name="levelId" />					
							<s:hidden name="moduleState" />	

							<div class="form-group row">
		                        <label for="levelName" class="col-sm-4 col-form-label">
		                        	<s:text name="l.levelName"/>
		                        	<font color="red" size="1"><i><b>*)</b></i></font>
		                        </label>
		                        <s:if test="userLevel.levelId > 0">
		                        	<div class="col-sm-8">
										<s:textfield id="levelName" maxlength="32" type="text" name="userLevel.levelName" cssClass="form-control" readOnly="true"/>
									</div>
								</s:if>
								<s:else>
									<div class="col-sm-8">
										<s:textfield id="levelName" maxlength="32" name="userLevel.levelName" cssClass="form-control" required="required"/>							
									</div>
								</s:else>
							</div>
							<div class="form-group row">
								<label for="remark" class="col-sm-4 col-form-label"><s:text name="l.levelDescription"/></label>
								<div class="col-sm-8">
									<s:textarea id="remark" maxlength="255" name="userLevel.levelDesc" cssClass="form-control" />
								</div>
							</div>
					<br>
					
				</div>    
				<!-- GENERATED TABLE -->
					<div class ="col-12 offset-md-0">
						<div class="TableGroup col-sm-12 offset-md-1">	                       
							<s:iterator value="listModule">
								<table>
									<thead>
										<tr>
											<th><s:property value="header"/></th>
											<th>View Only</th>
											<th>Full Previleges</th>
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
													<s:if test="isModulesSelected(menuId)">
														<input type="checkbox" id="TriggerSubModule<s:property value="menuId"/>" name="modulesAccess[<s:property value="menuId"/>].modulesSelected" checked="checked" value="<s:property value="menuId"/>">
													</s:if>	
													<s:else>
														<input type="checkbox" id="TriggerSubModule<s:property value="menuId"/>" name="modulesAccess[<s:property value="menuId"/>].modulesSelected"  value="<s:property value="menuId"/>">
													</s:else>
														<label for="TriggerSubModule<s:property value="menuId"/>"></label>
													</div>
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
													<%-- <input type="radio" name="accessLevel[<s:property value="#Index.index"/>]" id="accessLevel<s:property value="menuId"/>1" value="1">
													<label  for="accessLevel<s:property value="menuId"/>1">
														Full Access
													</label>
													<input type="radio" name="accessLevel[<s:property value="#Index.index"/>]" id="accessLevel<s:property value="menuId"/>2" value="2">
													<label  for="accessLevel<s:property value="menuId"/>2">
														Read Only  
													</label> --%>
												</td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
							</s:iterator>						
						</div>
					</div>
					<br>
					
					<s:if test="checkPrevileges(menuId)">
	                    <!-- BUTTON -->
	                    <div class="col-6 offset-md-6">
	                    	<s:if test="userLevel.levelId > 0">
	                        	<button type="submit" id="btnSave" class="ButtonPrimary btn btn-danger" value="%{getText('b.change')}"><i class="fas fa-save" style="vertical-align: middle;"></i>&nbsp;<s:text name="b.change"/></button>
							</s:if>
							<s:else>
								<button type="submit" id="btnSave" class="ButtonPrimary btn btn-danger" value="%{getText('b.save')}"><i class="fas fa-save" style="vertical-align: middle;"></i>&nbsp;<s:text name="b.save"/></button>
							</s:else>            
	                        <button type="reset" class="ButtonReset btn btn-primary" value="Reset"><i class="fas fa-sync" style="vertical-align: middle;"></i>&nbsp;Reset</button>
	                    </div>                
                    </s:if>
			</s:form>
        </main>
	</body>
</html>