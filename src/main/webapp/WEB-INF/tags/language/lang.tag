<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${not empty pageContext.request.queryString}">
        <c:set value="${pageContext.request.queryString}" var="query"/>
        <c:if test="${query.contains('lang')}">
            <c:set var="startIndex" value="${query.indexOf('lang') - 1}"/>
            <c:set var="query" value="${query.replace('lang='.concat(requestScope['lang']), '')}"/>
        </c:if>
        <c:set var="url"
               value="${pageContext.request.contextPath}?${query}&lang="/>
    </c:when>
    <c:otherwise>
        <c:set var="url" value="${pageContext.request.contextPath}?lang="/>
    </c:otherwise>
</c:choose>