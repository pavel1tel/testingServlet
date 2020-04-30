<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>
        <fmt:message key="string.home"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/userPage.css">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/static/js/tableToggle.js"></script>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<%@ include file="../fragments/navbar.jsp" %>

<div class="content">
    <table class="table-condensed table table-hover" style="border-collapse:collapse;">
        <caption>
            <fmt:message key="string.reports.list"/>
        </caption>
        <thead>
        <tr>
        <tr>
            <td>
                <fmt:message key="string.id"/>
            </td>
            <td>
                <fmt:message key="string.name"/>
            </td>
            <td>
                <fmt:message key="string.created"/>
            </td>
            <td>
                <fmt:message key="string.updated"/>
            </td>
            <td></td>
            <td></td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.reports}" var="report">

        <tr class="accordion-toggle" data-toggle="collapse" data-target="#${report.getId()}">
            <th scope="row">
                    ${report.getId()}
            </th>
            <td>
                    ${report.getName()}
            </td>
            <td>
                    ${report.getCreated()}
            </td>
            <td>
                    ${report.getUpdated()}
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/app/inspHome/accept/${report.getId()}" method="post">
                    <input id="csrfToken" name="csrfToken" type="hidden" value="${sessionScope.csrfToken}" />
                    <button type="submit" class="btn btn-success" onclick="return confirm('You sure?')">
                        <fmt:message key="string.accept" />
                    </button>
                </form>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/app/inspHome/decline/${report.getId()}" method="get">
                    <button type="submit" class="btn btn-danger" onclick="return confirm('You sure?')">
                        <fmt:message key="string.decline" />
                    </button>
                </form>
            </td>
        </tr>
        <tr>
            <td colspan="6" class="hiddenRow">
                <div class="accordian-body collapse" id="${report.getId()}">
                        ${report.getDescription()}
                </div>
            </td>
        </tr>
        </c:forEach>
        <tbody>
    </table>

    <%@ include file="../fragments/paggination.jsp" %>
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
