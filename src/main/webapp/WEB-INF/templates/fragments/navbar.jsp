<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8"%>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="en">
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo03"
            aria-controls="navbarTogglerDemo03" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <a class="navbar-brand" href="${pageContext.request.contextPath}">Testing</a>

    <div class="collapse navbar-collapse" id="navbarTogglerDemo03">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
            <c:if test="${not empty sessionScope.homeUrl}">
            <li class="nav-item active">
                <a class="nav-link" href="${sessionScope.homeUrl}">
                    <fmt:message key="string.home"/>
                </a>
            </li>
            </c:if>
        </ul>
        <div class="form-inline my-2 my-lg-0">
            <c:choose>
                <c:when test="${sessionScope.loggedIn}">
                    <div>
                        <span class="nav-text"><a href="#">
                            ${sessionScope.username}
                        </a></span>
                        <span style = "margin-left : 0.5rem; margin-right : 0.5rem;">|</span>
                        <span class="nav-text"><a href="${pageContext.request.contextPath}/app/accounts/logout">
                           <fmt:message key="string.logout"/>
                        </a></span>
                    </div>
                </c:when>
                <c:when test="${!sessionScope.loggedIn}">
                    <span class="nav-text"><a href="${pageContext.request.contextPath}/app/accounts/login">
                        <fmt:message key="string.login"/>
                    </a></span>
                    <span style = "margin-left : 0.5rem; margin-right : 0.5rem;">|</span>
                    <span class="nav-text"><a href="${pageContext.request.contextPath}/app/accounts/registration">
                        <fmt:message key="string.registration"/>
                    </a></span>
                </c:when>
            </c:choose>
            <div class="nav-item dropdown" style="margin-right: 3.8rem">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown"
                   aria-haspopup="true" aria-expanded="false">
                    <fmt:message key="string.lang"/>
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                    <a class="dropdown-item" href="?lang=ua">
                        <fmt:message key="string.ukr"/>
                    </a>
                    <a class="dropdown-item" href="?lang=en">
                        <fmt:message key="string.eng"/>
                    </a>
                </div>
            </div>
        </div>

    </div>
</nav>

</body>
</html>