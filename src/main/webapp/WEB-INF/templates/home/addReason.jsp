<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<fmt:message key="string.reason" var="reason_place"/>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>
        <fmt:message key="string.add.reason"/>
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
    <h1>
        <fmt:message key="string.decline.reason"/>
    </h1>
    <form method="post" action="${pageContext.request.contextPath}/app/inspHome/${report_id}}">
        <input id="csrfToken" name="csrfToken" type="hidden" value="${sessionScope.csrfToken}" />
        <div class="form-group">
            <label for="declineReason">
                <fmt:message key="string.decline.reason"/>
            </label>
            <input id="declineReason" type="text" required class="form-control" name="declineReason"
                   aria-describedby="reason" placeholder="${reason_place}">
        </div>
        <button type="submit" class="btn btn-primary">
            <fmt:message key="string.submit"/>
        </button>
    </form>
</div>

<%@ include file="../fragments/footer.jsp" %>

</body>
</html>