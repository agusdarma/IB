<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<meta name="decorator" content="content-new">
	<title>Kas Non Tunai - Download</title>
	
	<script type="text/javascript">
    $(document).ready(function()
    {
		alert("Tidak dapat menemukan file <s:property value='f' />");
		window.history.back();
		window.close(); 
    });
  </script>
</head>

<body>
</body>
</html>