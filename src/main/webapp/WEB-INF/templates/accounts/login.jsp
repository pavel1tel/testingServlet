<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8"%>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages"/>
<fmt:message key="string.reg.placeholder.email" var="email_place"/>
<fmt:message key="string.reg.placeholder.password" var="password_place"/>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/login.css">

</head>
<body>
<div id="app" class="login-form">
    <form action="${pageContext.request.contextPath}/app/accounts/login" method="post">
        <input id="csrfToken" name="csrfToken" type="hidden" value="${sessionScope.csrfToken}" />
        <h2 class="text-center">
            <fmt:message key="string.reg.title.login" />
        </h2>
        <div class="form-group">
            <label for="email">${email_place}</label>
            <input name="email" id="email" type="email" class="form-control" placeholder="${email_place}"
                                              required="required">
        </div>
        <div class="form-group">
            <label for="password">${password_place}</label>
            <input type="password" name="password" id="password" class="form-control" placeholder="${password_place}"
                                                 required="required">
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block">
                <fmt:message key="string.reg.title.login" />
            </button>
        </div>
        <div class="clearfix">
            <label class="pull-left checkbox-inline"><input type="checkbox" name="remember-me">
                <fmt:message key="string.login.remember.me" />
            </label>
            <a href="#" class="pull-right">
                <fmt:message key="string.forgot.password" />
            </a>
        </div>
        <div>
            <c:if test="${param.error}">
                <div class="error text-center">
                    <fmt:message key="string.login.invalid.username.password" />
                </div>
            </c:if>
            <c:if test="${param.logout}">
                <div class="logout text-center">
                    <fmt:message key="string.login.user.been.logged.out" />
                </div>
            </c:if>
        </div>
    </form>
    <p class="text-center"><a href="${pageContext.request.contextPath}/app/accounts/registration">
        <fmt:message key="string.login.button.create.account" />
    </a></p>
    <ul style="display: flex; align-items: center; justify-content: center" id="lang">
        <li><a class="underlineHover" href="?lang=en">
            EN
        </a></li>
        <li><a class="underlineHover" href="?lang=ua">
            UA
        </a></li>
    </ul>
</div>

</body>
</html>