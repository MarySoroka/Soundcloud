<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@taglib prefix="error" tagdir="/WEB-INF/tags/result_message" %>
<%@page pageEncoding="UTF-8" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.album.AlbumGenre" %>
<%@ page import="com.soundcloud.album.AlbumState" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<jsp:useBean id="securityContext" scope="application" class="com.soundcloud.security.SecurityContext"/>
<!DOCTYPE html>
<html xml:lang="en">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>

<body style="background-color:#424242;">
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
                   class="brand-logo left"><em class="material-icons">soundcloud_icon</em></a>
                <ul class="grey darken-3 right hide-on-med-and-down">
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
                    <li class="active">
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
                        <div id="topbarsearc">
                            <div class="input-field col s6 s12 white-text">
                                <form action="${pageContext.request.contextPath}/" method="get">
                                    <i class="white-text material-icons prefix">search</i>

                                    <input placeholder="" type="hidden"
                                           name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                           value="${CommandType.SEARCH_COMMAND}">
                                    <input type="text" placeholder="Enter artist, album or track"
                                           name="${ApplicationConstants.REQUEST_PARAMETER_SEARCH_DATA}"
                                           id="autocomplete-inpt"
                                           class="autocomplete white-text">
                                </form>
                            </div>
                        </div>
                    </li>
                    <li class="active"><a
                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}">
                        <fmt:message key="navbar.home"/></a></li>
                    <li><a
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
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"><fmt:message
                        key="navbar.profile"/></a></li>
            <li class="divider"></li>
            <li>
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.LOGOUT_USER_COMMAND}">
                    <fmt:message key="navbar.logout"/></a></li>
        </c:when>
        <c:otherwise>
            <li>
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.LOGIN}"><fmt:message
                        key="navbar.login"/></a></li>
        </c:otherwise>
    </c:choose>
</ul>


<div class="row white-text grey darken-2">
    <div class="col s10 offset-l1 section grey darken-2">
        <h3 class="title"><fmt:message key="upload.title"/></h3>
        <p><fmt:message key="upload.text"/></p>
        <div class="divider">
        </div>
    </div>

    <error:error errors="${errors}"/>
    <error:succsess message="${message}"/>
    <c:choose>
        <c:when test="${subscribe}">
            <div class="col s10 offset-l1 white-text">
                <form action="${pageContext.request.contextPath}/" method="post" enctype="multipart/form-data">
                    <div class="row">
                        <div class="input-field col s12">
                            <input id="album_name" name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_NAME}"
                                   type="text"
                                   class="validate" required>
                            <label for="album_name"><fmt:message key="upload.form.album.name"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12 white-text">
                            <select id="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_GENRE}"
                                    name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_GENRE}" required>
                                <option class="white-text" value="" disabled selected><fmt:message
                                        key="upload.form.album.genre.choose"/></option>
                                <c:forEach items="${AlbumGenre.values()}" var="genre">
                                    <option value="${genre}">${genre.getField()}</option>
                                </c:forEach>
                            </select>
                            <label><fmt:message key="upload.form.album.genre"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12 white-text">
                            <select class="white-text" id="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_STATE}"
                                    name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_STATE}" required>
                                <option class="white-text" value="" disabled selected><fmt:message
                                        key="upload.form.album.state.choose"/></option>
                                <c:forEach items="${AlbumState.values()}" var="state">
                                    <option value="${state}">${state.getField()}</option>
                                </c:forEach>
                            </select>
                            <label><fmt:message key="upload.form.album.state"/></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12 white-text">
                            <div class="file-field input-field">
                                <div class="btn">
                                    <span><fmt:message key="upload.form.album.icon"/> </span>
                                    <label for="image"></label>
                                    <input type="file" id="image" name="image" accept="image/*"
                                           placeholder="<fmt:message key="upload.form.album.icon.upload"/>" required>
                                </div>
                                <div class="file-path-wrapper">
                                    <input class="white-text file-path validate" type="text">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12 white-text">
                            <div class="file-field input-field white-text">
                                <div class="btn">
                                    <span><fmt:message key="upload.form.album.tracks"/> </span>
                                    <label for="audio"></label>
                                    <input type="file" id="audio" name="audio" accept="audio/*"
                                           placeholder="<fmt:message key="upload.text"/>" multiple required>
                                </div>
                                <div class="file-path-wrapper">
                                    <input class="white-text file-path validate" type="text">
                                </div>
                            </div>
                        </div>
                    </div>
                    <input type="hidden" name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                           value="${CommandType.UPLOAD_ALBUM_COMMAND}">

                    <div class="row">
                        <div class="input-field col s12 white-text">
                            <button class="btn waves-effect waves-light" type="submit"
                                    name="${CommandType.UPLOAD_ALBUM_COMMAND}">
                                <fmt:message key="upload.submit"/>
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row white-text grey darken-2">
                <div class="col s10 offset-l1 section">
                    <h4><fmt:message key="subscribe.text"/></h4>
                    <i class="material-icons medium white-text right">music_note</i>
                    <p><fmt:message key="upload.subscribe.text"/>
                        <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"><fmt:message
                                key="upload.profile"/></a></p>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

</div>


<footer:footer
        policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
        help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
        page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_UPLOAD_PAGE_COMMAND}"/>
</body>
</html>
