<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <!-- FAVICON -->
  <link href="<s:url value='/img/favicon.ico'/>" rel="shortcut icon" type="image/png"/>
  <title><decorator:title default="NabungDividen - Introducing Broker" /></title>

  <!-- Custom fonts for this template-->
  <link href="<s:url value='/css/fontawesome.all.min.css'/>" rel="stylesheet" type="text/css">
  <%-- 
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">
  --%>
  
  <!-- Custom styles for this template-->
  <link href="<s:url value='/css/sb-admin-2.css'/>" rel="stylesheet">
  <link href="<s:url value='/css/personal.css'/>" rel="stylesheet">
  
  <!-- Bootstrap core JavaScript-->
  <script src="<s:url value='/js/jquery.min.js'/>"></script>
  <script src="<s:url value='/js/bootstrap.bundle.min.js'/>"></script>

  <!-- Core plugin JavaScript-->
  <script src="<s:url value='/js/jquery.easing.min.js'/>"></script>
  <script type="text/javascript" src="<s:url value='/Java Script/emobile.js'/>"></script>

  <!-- Custom scripts for all pages-->
  <script src="<s:url value='/js/sb-admin-2.min.js'/>"></script>
  
  <!-- DataTable -->
  <link rel="stylesheet" type="text/css" href="<s:url value='/css/jquery.dataTables.min.css'/>"/>
 
  <script type="text/javascript" src="<s:url value='/js/jquery.dataTables.min.js'/>"></script>
  
  <script type="text/javascript" src="<s:url value='/js/gijgo.min.js'/>"></script>
  <link href="<s:url value='/css/gijgo.css'/>" rel="stylesheet" type="text/css">
  		
  <decorator:head />
  <%@ include file="/WEB-INF/includes/include_script_initialization.jsp"%>
  <%@ include file="/WEB-INF/includes/include_script_table_generator.jsp"%>
</head>

<body id="page-top">

  <!-- Page Wrapper -->
  <div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-light accordion" id="accordionSidebar">

      <!-- Sidebar - Brand -->
      <a class="sidebar-brand d-flex align-items-center justify-content-center" href="#">        
        <div class="sidebar-brand-text mx-3">NabungDividen Portal</div>
      </a>
      <br />
      <li class="nav-item active">
        <s:a class="nav-link" action="MainMenu">
          <span>Dashboard</span>
        </s:a>
      </li>
      <s:set name="levelVO" value="%{#session.LOGIN_KEY.levelVO}" />
      <s:iterator value="#levelVO.listMenu" status="Counter">
      	<s:if test="hasChild()">
      	<li class="nav-item">
          <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapse<s:property value='menuId' />"
             aria-expanded="true" aria-controls="collapse<s:property value='menuId' />">
             <span><s:property value="menuText" /></span>
          </a>
            <div id="collapse<s:property value='menuId' />" class="collapse" aria-labelledby="headingUtilities" data-parent="#accordionSidebar">
              <div class="py-2 collapse-inner rounded">
              <s:iterator value="childs">
                <s:a class="collapse-item" action="%{menuUrl}"><s:property value="menuText" /></s:a>
              </s:iterator>
              </div>
            </div>
        </li>
      	</s:if>
      	<s:else>
      	<li class="nav-item">
          <s:a class="nav-link" action="%{menuUrl}">
            <span><s:property value="menuText" /></span>
          </s:a>
        </li>
      	</s:else>
      </s:iterator>
      
    </ul>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <decorator:body />
    <!-- End of Content Wrapper -->

  </div>
  <!-- End of Page Wrapper -->

</body>

</html>