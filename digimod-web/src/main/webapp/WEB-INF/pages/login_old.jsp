<!DOCTYPE html>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
	<head>
		<meta name="decorator" content="login">
		<title><s:text name="t.login" /></title>
		<script type="text/javascript">
			$(document).ready(function()
			{
				if('<s:property value="message" />'.length  > 0)
				{
					$("#MessageResult").removeClass(MessageState);
					$("#MessageResult").addClass(ResultFailure);
					$("#MessageResult").empty();
					$("#MessageResult").append('<s:property value="message" />');
				}
				else
				{
					$("#MessageResult").removeClass();
					$("#MessageResult").addClass(MessageDefault);
					$("#MessageResult").addClass(MessageState);
				}

				$( "input" ).keypress(function( event ) {
					if ( event.which == 13 ) {
					 $("#ButtonLogin").click();
					}
				});

				$("#ButtonLogin").click(function() {
					var userId = $("#userCode").val();
					var pass = $("#password").val();
					var capcay = $("#captcha").val();
					$("#entityUserCode").val(userId);
					$("#entityPassword").val(pass);
					$("#entityCaptcha").val(capcay);
					$("#formLogin").submit();
				});
				
				/* set autocomplete off ke semua attribute
				for (i=0; i<document.forms.length; i++) {
		            document.forms[i].setAttribute("AutoComplete","off");
		    } */
			});
		</script>
	</head>
	<body>
		<!-- MAIN -->
        <main class="login-content">
                <!-- CONTENT -->
                <section id="MainLogin">
					<div class="card mx-auto shadow-lg rounded" style="background-color:rgba(255,255,255,0.6);">
					  <div class="card-body rounded" style="background-color:rgba(255,255,255,0.8);">
						  <div class="align-items-center">
						    <div class="container-fluid">
						    		<br>
						    		<div class="text-center">
						    			<img class="img-fluid" src="<s:url value='/Resource/Logo/mbanking-logo-black.png'/>" alt="Telkomsel Samba" id="Logo"/>
						    		</div>
						    		<s:form action="Login!process" id="formLogin" method="post" style="display: none;">
							    		<s:hidden name="loginData.userCode" id="entityUserCode"/>
										<s:hidden name="loginData.password" id="entityPassword" />
										<s:hidden name="j_captcha_response" id="entityCaptcha" />
										<s:hidden name="token" id="token" />
						    		</s:form>
				                    <s:form cssClass="form-signin mb-4">
					                    <%-- <hgroup>
							                	<h1><s:text name="t.login"/></h1>
							                	<h2><s:text name="t.login.description"/></h2>
							            		</hgroup> --%>
				                        <%@ include file="/WEB-INF/includes/include_message_result.jsp"%>
				                        <hr style="border-color: #e5131d; border-width: 1px;">
				                        <div class="form-group row">
				                        	<label for="Username" class="sr-only"><s:text name="l.userCode"/></label>
				                        	<div class="col-sm-12">
												<s:textfield type="text" id="userCode" name="userCode" cssClass="form-control" placeholder="%{getText('p.currentUserCode')}" required="true"/>
				                        	</div>
				                        </div>
				                        <div class="form-group row">
				                        	<label for="Userpassword" class="sr-only"><s:text name="l.password"/></label>
				                        	<div class="col-sm-12">
												<s:password type="password" id="password" name="password" cssClass="form-control" placeholder="%{getText('p.oldPassword')}" required="true"/>
				                        	</div>
				                        </div>
				                        <div class="FormCaptcha">
					                        <div class="form-group row align-middle">
					                    		<div class="col-sm-6">
						                    		<s:textfield  name="j_captcha_response" id="captcha"
						                    			maxlength="10" placeholder="Enter captcha" cssClass="form-control" required="true"/>
					                    		</div>
					                    		<div class="col-sm-4">
						                    		<img src="Captcha.jpg" border="0" class="CaptchaPicture rounded" style="height:100%; width: 170%;"/>
					                    		</div>
				                    		</div>
				                   		</div>
				                        <input type="button" id="ButtonLogin" class="btn btn-lg btn-danger btn-block" value="LOGIN"/>
				                        <!-- <s:reset type="reset" id="ButtonReset" cssClass="ButtonReset" value=""/> -->
				                        <hr style="border-color: #e5131d; border-width: 1px;">
				                    </s:form>
						    </div>
						  </div>
					  </div>
					</div>
                </section>
        </main>
	</body>
</html>
