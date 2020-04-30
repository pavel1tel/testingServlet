<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page pageEncoding="UTF-8"%>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${requestScope.totalPages != 0}">
    <nav style="display: flex; justify-content: center"
         aria-label="Page navigation example">
        <ul class="pagination">
            <c:forEach var = "i" begin = "1" end = "${requestScope.totalPages}">
                <li class="page-item">
                    <a class="page-link" href="?page=${i - 1}">
                            ${i}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</c:if>
</body>
</html>
