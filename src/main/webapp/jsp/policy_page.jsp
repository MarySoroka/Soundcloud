<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="card" tagdir="/WEB-INF/tags/cards" %>
<%@taglib prefix="lang" tagdir="/WEB-INF/tags/language" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<jsp:useBean id="securityContext" scope="application" class="com.soundcloud.security.SecurityContext"/>
<!DOCTYPE html>
<html xml:lang="en">
<link:link>
    <title>Soundcloud</title>
</link:link>
<body style="background-color:#424242;">
<c:choose>
    <c:when test="${('ru').equals(requestScope.lang)}">
        <fmt:setLocale value="ru" scope="application"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en" scope="application"/>
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="/i18n/ApplicationMessages" scope="application"/>

<div class="row grey darken-2 white-text">
    <div class="section">
        <div class="col s12 offset-l1">
            <div class="row">
                <h1 class="title"><fmt:message key="policy.page"/></h1>
            </div>
            <div class="row">
                <div class="col s7">
                    <span><fmt:message key="policy.text"/></span>
                </div>
            </div>
        </div>
    </div>
</div>


<footer:footer  policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
                help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
        page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"/>

</body>
</html>