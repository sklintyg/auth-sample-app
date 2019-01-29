<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="sv">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex"/>
<title>Auth Sample App</title>
<link rel="stylesheet" href="/css/styles.css">
<style>
  h1 {
    font-family: 'Helvetica Neue', helvetica, arial, sans-serif;
    font-size: 24px;
    font-weight: bold;
    color: #008391;
  }
</style>

</head>
<body>
  <div class="container">
    <div class="row">
      <div class="content">
        <c:choose>

          <c:when test="${param.reason eq \"login.failed\"}">
            <h1 class="page-header">Auth sample application</h1>
            <div class="alert alert-danger">Inloggningen misslyckades. Gå tillbaka till <a href="/">startsidan</a>.</div>
          </c:when>

          <c:otherwise>
            <h1 class="page-header">Auth sample application</h1>
            <div class="alert alert-danger">Ett tekniskt fel har uppstått. Gå tillbaka till <a href="/">startsidan</a>.</div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</body>
</html>
