<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="lang" tagdir="/WEB-INF/tags/language" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<jsp:useBean id="securityContext" scope="application" class="com.soundcloud.security.SecurityContext"/>
<html xml:lang="en">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
<script src="../static/js/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script src="../static/js/custom.js"></script>
<script src="../static/js/validation.js"></script>


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
<jsp:include page="navbar.jsp"/>
<div class="row grey darken-2 white-text">
    <div class="container section">
        <form class="col s12" action="${pageContext.request.contextPath}/" method="post">
            <h3 class="title"><fmt:message key="login.singin"/></h3>
            <c:choose>
                <c:when test="${not empty errors}">
                    <c:forEach items="${errors}" var="error">
                        <div class="row">
                            <div class="col s12" style="color:#ee6e73;">
                                <span class="helper-text" data-error="wrong">${error}</span>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:if test="${not empty message}">
                        <div class="row">
                            <div class="col s12">
                                <span class="helper-text" data-success="valid"><fmt:message key="registry.sing"/></span>
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <div class="row">
                <div class="input-field col s12">
                    <input id="login" type="text" name="${ApplicationConstants.REQUEST_PARAMETER_LOGIN}"
                           class="validate">
                    <label for="login"><fmt:message key="login.login"/></label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s12">
                    <input id="password" type="password" name="${ApplicationConstants.REQUEST_PARAMETER_PASSWORD}"
                           class="validate">
                    <label for="password"><fmt:message key="login.password"/></label>
                </div>
            </div>
            <input type="hidden" name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                   value="${CommandType.LOGIN_USER_COMMAND}">
            <button class="btn waves-effect waves-light" type="submit" name="${CommandType.LOGIN_USER_COMMAND}">
                <fmt:message key="login.singin"/>
                <i class="material-icons right">send</i>
            </button>
            <div class="row">
                <p><fmt:message key="login.registry"/>
                    <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.REGISTRY}"><fmt:message
                            key="registry.singup"/></a>.
                </p>
            </div>

        </form>
    </div>
</div>
<footer:footer  policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
                help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
        page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.LOGIN}"/>
</body>
</html>
