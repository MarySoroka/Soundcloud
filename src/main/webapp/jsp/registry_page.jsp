<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<%@taglib prefix="lang" tagdir="/WEB-INF/tags/language" %>
<%@taglib prefix="error" tagdir="/WEB-INF/tags/result_message" %>
<jsp:useBean id="securityContext" scope="application" class="com.soundcloud.security.SecurityContext"/>
<html xml:lang="en">
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <%--    <link rel="stylesheet" href="static/style/custom.css">--%>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="static/js/validation.js"></script>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body style="background-color:#424242">
<title>Soundcloud</title>
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
    <form class="col s10 offset-l1" action="${pageContext.request.contextPath}/" method="post">
        <h3><fmt:message key="registry.singup"/></h3>
        <p><fmt:message key="registry.description"/></p>
        <c:choose>
            <c:when test="${not empty errors}">
                <c:forEach items="${errors}" var="error">
                    <div class="row">
                        <div class="col s12" style="color:#ee6e73;">
                            <c:forEach items="${error.errors}" var="err">
                                <span class="helper-text" data-error="wrong"><c:out value="${err}"/></span>
                            </c:forEach>
                            <span class="helper-text" data-error="wrong">in field: <c:out value="${error.field}"/></span>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
        </c:choose>
        <div class="row">
            <div class="input-field col s12">
                <input id="login" name="${ApplicationConstants.REQUEST_PARAMETER_LOGIN}" type="text"
                       class="" onkeyup="validateLength('login')">
                <label for="login"><fmt:message key="registry.login"/></label>
                <span class="helper-text" data-error="The length of the login should be more then 3 and less then 30"></span>

            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <input id="password" name="${ApplicationConstants.REQUEST_PARAMETER_PASSWORD}" type="password"
                       class="" onkeyup="validateLength('password')">
                <label for="password"><fmt:message key="registry.password"/></label>
                <span class="helper-text" data-error="The length of the password should be more then 3 and less then 30"></span>

            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <input id="password-repeat" type="password" class="" onkeyup="isPasswordSame()">
                <label for="password-repeat"><fmt:message key="registry.password.repeat"/></label>
                <span class="helper-text" data-error="Passwords should be the same" data-success="Passwords are the same"></span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <input id="email" name="${ApplicationConstants.REQUEST_PARAMETER_EMAIL}" type="email"
                       class="validate">
                <label for="email"><fmt:message key="registry.email"/></label>
                <span class="helper-text" data-error="Incorrectly email" data-success="Email is correct"></span>
            </div>
        </div>

        <input type="hidden" name="${ApplicationConstants.COMMAND_NAME_PARAM}"
               value="${CommandType.REGISTER_USER_COMMAND}">

        <button class="btn waves-effect waves-light" type="submit" name="${CommandType.LOGIN_USER_COMMAND}">
            <fmt:message key="upload.submit"/>
            <i class="material-icons right">send</i>
        </button>
        <div class="row">
            <p><fmt:message key="registry.question.for.login"/>
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.LOGIN}"><fmt:message
                        key="login.singin"/></a>.
            </p>
        </div>
    </form>
</div>
<footer:footer  policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
                help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
        page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.REGISTRY}"/>
</body>

</html>
