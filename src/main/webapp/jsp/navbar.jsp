<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="card" tagdir="/WEB-INF/tags/cards" %>
<%@taglib prefix="track" tagdir="/WEB-INF/tags/track" %>
<%@taglib prefix="lang" tagdir="/WEB-INF/tags/language" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<jsp:useBean id="securityContext" scope="application" class="com.soundcloud.security.SecurityContext"/>


<c:choose>
    <c:when test="${('ru').equals(requestScope.lang)}">
        <fmt:setLocale value="ru" scope="application"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="en" scope="application"/>
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="/i18n/ApplicationMessages" scope="application"/>
<nav>
    <c:choose>
        <c:when test="${not isAdmin}">
            <c:choose>
                <c:when test="${not empty pageContext.request.queryString}">
                    <c:set value="${pageContext.request.queryString}" var="query"/>
                    <c:if test="${query.contains('lang')}">
                        <c:set var="startIndex" value="${query.indexOf('lang') - 1}"/>
                        <c:set var="query" value="${query.replace('lang='.concat(requestScope['lang']), '')}"/>
                    </c:if>
                    <c:set var="url"
                           value="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HOME}&lang="/>
                </c:when>
                <c:otherwise>
                    <c:set var="url"
                           value="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HOME}&lang="/>
                </c:otherwise>
            </c:choose>
            <div class="grey darken-3 z-depth-4  nav-wrapper">
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}"
                   class="brand-logo left"><em class="material-icons">soundcloud_icon</em></a>                <ul class="grey darken-3 right hide-on-med-and-down">
                <li>
                    <div id="topbarsearch">
                        <div class="input-field col s6 s12 white-text">
                            <form action="${pageContext.request.contextPath}/" method="get">
                                <i class="white-text material-icons prefix">search</i>

                                <input placeholder="" type="hidden"
                                       name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                       value="${CommandType.SEARCH_COMMAND}">
                                <input type="text" placeholder="Enter artist, album or track"
                                       name="${ApplicationConstants.REQUEST_PARAMETER_SEARCH_DATA}"
                                       id="autocomplete-input"
                                       class="autocomplete white-text">
                            </form>
                        </div>
                    </div>
                </li>                    <li><a
                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}">
                        <fmt:message key="navbar.home"/></a></li>
                    <li>
                        <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_USER_LIBRARY_COMMAND}"><fmt:message
                                key="navbar.library"/></a></li>
                    <li>
                        <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_UPLOAD_PAGE_COMMAND}"><fmt:message
                                key="navbar.upload"/></a>
                    </li>
                    <c:choose>
                        <c:when test="${authorized}">
                            <li><a class="dropdown-trigger" href="#!" data-target="dropdown1">
                                <fmt:message key="navbar.profile"/><i class="material-icons right">more_vert</i></a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li><a class="dropdown-trigger" href="#!" data-target="dropdown1">
                                <fmt:message key="navbar.login"/><i class="material-icons right">more_vert</i></a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </c:when>
        <c:otherwise>
            <div class="grey darken-3 z-depth-4  nav-wrapper">
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}"
                   class="brand-logo left"><em class="material-icons">soundcloud_icon</em></a>
                <ul class="grey darken-3 right hide-on-med-and-down">
                    <li>
                        <div id="topbarearch">
                            <div class="input-field col s6 s12 white-text">
                                <form action="${pageContext.request.contextPath}/" method="get">
                                    <i class="white-text material-icons prefix">search</i>

                                    <input placeholder="" type="hidden"
                                           name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                           value="${CommandType.SEARCH_COMMAND}">
                                    <input type="text" placeholder="Enter artist, album or track"
                                           name="${ApplicationConstants.REQUEST_PARAMETER_SEARCH_DATA}"
                                           id="autocmplete-input"
                                           class="autocomplete white-text">
                                </form>
                            </div>
                        </div>
                    </li>
                    <li><a
                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}">
                        <fmt:message key="navbar.home"/></a></li>
                    <li class="active"><a
                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ADMIN_USER_PAGE_COMMAND}">
                        <fmt:message key="navbar.admin"/></a></li>
                    <li>
                        <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.LOGOUT_USER_COMMAND}">
                            <fmt:message key="navbar.logout"/></a></li>
                </ul>
            </div>
        </c:otherwise>
    </c:choose>
</nav>

<ul id="dropdown1" class="dropdown-content">
    <c:choose>
        <c:when test="${authorized}">
            <li>
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${artist_id}"><fmt:message
                        key="navbar.profile"/></a></li>
            <li class="divider"></li>
            <li><a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.LOGOUT_USER_COMMAND}">
                <fmt:message key="navbar.logout"/></a></li>
        </c:when>
        <c:otherwise>
            <li>
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.LOGIN}"><fmt:message
                        key="navbar.login"/></a></li>
        </c:otherwise>
    </c:choose>
</ul>
