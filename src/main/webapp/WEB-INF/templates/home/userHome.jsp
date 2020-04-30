<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page pageEncoding="UTF-8"%>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages"/>


<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>
        <fmt:message key="string.home" />
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
            <fmt:message key="string.reports.list" />
        </caption>
        <thead>
        <tr>
            <td>
                <fmt:message key="string.id" />
            </td>
            <td>
                <fmt:message key="string.name" />
            </td>
            <td>
                <fmt:message key="string.status" />
            </td>
            <td>
                <fmt:message key="string.created" />
            </td>
            <td>
                <fmt:message key="string.updated" />
            </td>
            <td></td>
            <td></td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${requestScope.reports}" var = "report">
        <tr class="accordion-toggle" data-toggle="collapse" data-target="#${report.getId()}">
            <th scope="row">
                    ${report.getId()}
            </th>
            <td>
                    ${report.getName()}
            </td>
            <td>
                <c:choose>
                    <c:when test="${report.getStatus().name() == 'ACCEPTED'}">
                        <span class="font-weight-bold" style="color: green">
                            <fmt:message key="string.accepted" />
                        </span>
                    </c:when>
                    <c:when test="${report.getStatus().name() == 'NOT_ACCEPTED'}">
                        <span class="font-weight-bold" style="color : red">
                            <fmt:message key="string.not.accepted" />
                        </span>
                    </c:when>
                    <c:when test="${report.getStatus().name() == 'QUEUE'}">
                        <span class="font-weight-bold" style="color: #ffbf00">
                            <fmt:message key="string.queue" />
                        </span>
                    </c:when>
                </c:choose>
            </td>
            <td>
                    ${report.getCreated()}
            </td>
            <td>
                    ${report.getUpdated()}
            </td>
            <div style="display:inline">
                <c:if test="${report.getStatus().name() == 'NOT_ACCEPTED'}">
                    <td>
                        <form action="${pageContext.request.contextPath}/app/userHome/update/${report.getId()}" method="get">
                            <button type="submit" class="btn btn-warning" onclick="return confirm('You sure?')">
                                <fmt:message key="string.correct" />
                            </button>
                    </form>
                    </td>
                </c:if>
                <c:if test="${report.getStatus().name() == 'NOT_ACCEPTED'}">
                    <td>
                        <form action="'${pageContext.request.contextPath}/app/userHome/change/${report.getId()}" method="post">
                            <input id="csrfToken" name="csrfToken" type="hidden" value="${sessionScope.csrfToken}" />
                            <button type="submit" class="btn btn-danger" onclick="return confirm('You sure?')">
                                <fmt:message key="string.replace" />
                            </button>
                        </form>
                    </td>
                </c:if>
                <c:if test="${report.getStatus().name() != 'NOT_ACCEPTED'}">
                    <td></td>
                    <td></td>
                </c:if>
            </div>
        </tr>
        <tr>
            <td colspan="7" class="hiddenRow">
                <div class="accordian-body collapse" id="${report.getId()}">
                    <div>
                            ${report.getDescription()}
                    </div>
                    <c:if test="${report.getDeclineReason() != null && report.getStatus().name() == 'NOT_ACCEPTED'}">
                        <div style="background-color: #ffdfd4; border-top: 0.5px solid grey;">
                            <fmt:message key="string.reason.toggle" />${report.getDeclineReason()}
                        </div>
                    </c:if>
                </div>
                </td>
        </tr>
    </c:forEach>
        <tbody>
    </table>
    <a href="${pageContext.request.contextPath}/app/userHome/add">
        <button type="button" class="btn btn-outline-success btn-lg">
            <fmt:message key="string.add" />
        </button>
    </a>

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
