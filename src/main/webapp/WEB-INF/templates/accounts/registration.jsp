<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8"%>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages"/>
<fmt:message key="string.reg.placeholder.email" var="email_place"/>
<fmt:message key="string.reg.placeholder.username" var="username_place"/>
<fmt:message key="string.reg.placeholder.password" var="password_place"/>
<fmt:message key="string.reg.placeholder.passwordConfirm" var="passwordConf_place"/>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registration</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/login.css">
</head>
<body>
<div id="app" class="login-form">
    <form action="${pageContext.request.contextPath}/app/accounts/registration" method="post">
        <h2 class="text-center">
            <fmt:message key="string.reg.title.registration" />
        </h2>
        <div class="form-group">
            <label for="username">${username_place}</label>
            <input name="username" id="username" type="text" class="form-control" placeholder="${username_place}"
                                                 required="required">
        </div>
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
            <label for="confirmPassword">${passwordConf_place}</label>
            <input type="password" name="confirmPassword" id="confirmPassword" class="form-control" placeholder="${passwordConf_place}"
                                                        required="required">
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-block">
                <fmt:message key="string.reg.button.registration" />
            </button>
        </div>
    </form>
    <p class="text-center"><a href="${pageContext.request.contextPath}/app/accounts/login">
        <fmt:message key="string.have.account" />
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