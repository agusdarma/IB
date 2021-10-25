<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">

<head>
  <meta name="decorator" content="login">
  <title>NabungDividen - Introducing Broker Login</title>
  
  <script type="text/javascript">
    $(document).ready(function()
    {
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
    });
  </script>
  
</head>

<body class="bg-blue-gradient-2">
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-lg-4 my-5">
        <div class="card bg-blue-gradient my-4">
          <div class="card-body mx-4">
            
			<div class="text-center">
              <h1 class="h5 text-white mb-4">NabungDividen Portal</h1>
            </div>
            <s:if test="message != null">
			<div class="alert alert-danger" role="alert">
              <s:property value="message" />
            </div>
			</s:if>
            
              <div class="form-group">
                <label class="text-white"><b>User Code</b></label>
                <input id="userCode" class="form-control" type="text" autocomplete="off">
              </div>
              <div class="form-group">
                <label class="text-white"><b>Password</b></label>
                <input id="password" class="form-control" type="password" autocomplete="off">
              </div>
              <div class="form-group">
                <label class="text-white"><b>Captcha</b></label>
                <div class="row">
                	<div class="col-6">
                		<input id="captcha" class="form-control" type="text">
                	</div>
                	<div class="col-6">
                		<img src="<s:url action="Captcha" />" border="0" style="height:100%; width: 100%;"/>
                	</div>
                </div>
              </div>
              <a id="ButtonLogin" class="btn bg-golden btn-block text-white">
                <b>Login</b>
              </a>
            
            <br />
            <br />
          </div>
        </div>
      </div>

    </div>

  </div>
  
  <s:form action="Login!process" id="formLogin" method="post" style="display: none;">
    <s:hidden name="loginData.userCode" id="entityUserCode"/>
    <s:hidden name="loginData.password" id="entityPassword" />
    <s:hidden name="j_captcha_response" id="entityCaptcha" />
    <s:hidden name="token" id="token" />
  </s:form>
  
</body>

</html>