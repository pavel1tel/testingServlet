<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<fmt:message key="string.search" var="search_place"/>
<html>
<head>
    <title>Title</title>
    <script src="${pageContext.request.contextPath}/static/js/search.js"></script>
</head>
<body>
<div id="searchForm" class="input-group form-inline mr-auto mb-4 d-flex justify-content-end">
    <div class="input-group-prepend">
            <span class="input-group-text" id="basic-addon1">
                            <fmt:message key="string.name" />
            </span>
    </div>
    <input id="searchString" aria-describedby="basic-addon1" class="form-control mr-sm-2" type="text"
           placeholder="${search_place}" aria-label="${search_place}">
    <a id="searchButton">
        <div class="input-group-append">
            <button type="button"
                    class="btn btn-outline-secondary"
                    onclick="searchSubmit()">
                <fmt:message key="string.search" />
            </button>
        </div>
    </a>
</div>
</body>
</html>
