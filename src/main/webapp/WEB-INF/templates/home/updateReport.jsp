<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<fmt:message key="string.name" var="name_place"/>
<fmt:message key="string.description" var="desc_place"/>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>
        <fmt:message key="string.update.title"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/userPage.css">


    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<%@ include file="../fragments/navbar.jsp" %>

<div class="content">
    <h3>
        <fmt:message key="string.reason.toggle"/>${requestScope.report.getDeclineReason()}
    </h3>
    <hr/>
    <h1>
        <fmt:message key="string.update.title"/>
    </h1>
    <form method="post" action="${pageContext.request.contextPath}/app/userHome/update/${requestScope.report.getId()}">
        <input id="csrfToken" name="csrfToken" type="hidden" value="${sessionScope.csrfToken}" />
        <div class="form-group">
            <label for="name">
                <fmt:message key="string.name"/>
            </label>
            <input name = "name" type="text" required class="form-control" id="name"
                   aria-describedby="username" placeholder="${name_place}" value="${requestScope.report.getName()}">
        </div>
        <div class="form-group">
            <label for="description">
                <fmt:message key="string.description"/>
            </label>
            <input name="description" type="text" required class="form-control" aria-describedby="description"
                   placeholder="${desc_place}" id="description" value="${requestScope.report.getDescription()}">
        </div>
        <button type="submit" class="btn btn-primary">
            <fmt:message key="string.submit"/>
        </button>
    </form>
</div>

<%@ include file="../fragments/footer.jsp" %>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>
</body>
</html>