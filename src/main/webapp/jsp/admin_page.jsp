<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="card" tagdir="/WEB-INF/tags/cards" %>
<%@taglib prefix="lang" tagdir="/WEB-INF/tags/language" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="track" tagdir="/WEB-INF/tags/track" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<%@ page import="com.soundcloud.role.RoleType" %>

<%@ page import="com.soundcloud.application.ApplicationPage" %>
<!DOCTYPE html>
<html xml:lang="en">
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <%--    <link rel="stylesheet" href="static/style/custom.css">--%>

    <script src="static/js/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="static/js/custom.js"></script>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
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
<nav class="grey darken-3">

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
</nav>
<div id="albums" class="row">
    <div class="col s10 offset-l1 white-text">
        <div class="col s12 grey darken-2">
            <ul class="collection with-header" style="border-style:none">
                <li class="collection-header  grey darken-2 white-text">
                    <h6><fmt:message key="admin.users"/></h6>
                </li>
                <table>
                    <thead>
                    <tr>
                        <th><fmt:message key="admin.login"/></th>
                        <th><fmt:message key="admin.followers"/></th>
                        <th><fmt:message key="admin.follows"/></th>
                        <th><fmt:message key="admin.subscription"/></th>
                        <th><fmt:message key="admin.albums"/></th>
                        <th></th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td><a
                                    href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}">${user.login}</a>
                            </td>
                            <td><c:out value="${user.userFollowers}"/></td>
                            <td><c:out value="${user.userFollows}"/></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/"
                                      method="post">
                                    <select id="${ApplicationConstants.REQUEST_PARAMETER_USER_ROLE}"
                                            name="${ApplicationConstants.REQUEST_PARAMETER_USER_ROLE}"  multiple required>
                                        <c:forEach items="${RoleType.values()}" var="role">
                                            <c:if test="${not role.type.equals(RoleType.NOT_AUTHORIZED.type)}">
                                                <c:choose>
                                                    <c:when test="${user.haveRole(role.type)}">
                                                        <option
                                                                value="${role.roleTypeId}"
                                                                selected><c:out value="${role.type}"/></option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${role.roleTypeId}"><c:out value="${role.type}"/></option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </c:forEach>
                                    </select>

                                   <input type="hidden"
                                           name="${ApplicationConstants.REQUEST_PARAMETER_USER_ID}"
                                           value="${user.id}">
                                    <button class="btn waves-effect waves-light right"
                                            type="submit"
                                            name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                            value="${CommandType.UPDATE_ROLES_COMMAND}"
                                            onclick="updateRoles()">
                                        <fmt:message key="admin.update.role"/>
                                        <i class="material-icons right">send</i>
                                    </button>
                                </form>
                                <c:if test="${user.subscription!=null}">
                                    <c:out value="${user.subscription.subscriptionStatus}"/>
                                    <c:out value="${user.subscription.subscriptionDate}"/>
                                </c:if>
                            </td>
                            <c:choose>
                                <c:when test="${not empty user.userAlbums}">
                                    <td>
                                        <a class="waves-effect waves-light btn modal-trigger"
                                           href="#modal${user.id}"><fmt:message
                                                key="admin.albums"/></a>
                                        <div id="modal${user.id}" class="modal grey darken-2 white-text">
                                            <div class="modal-content">
                                                <ul class="collection with-header" style="border-style:none">
                                                    <li class="collection-header  grey darken-2 white-text">
                                                        <h6><fmt:message key="admin.albums"/></h6>
                                                    </li>
                                                    <c:forEach items="${user.userAlbums}" var="album">

                                                        <li class="collection-item avatar grey darken-2 white-text">
                                                            <form action="${pageContext.request.contextPath}/"
                                                                  method="post">
                                                                <img src="data:image/jpg;base64,${album.albumIconBase64}"
                                                                     alt="artist_icon" class="circle">
                                                                <span class="card-title"><a
                                                                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"><c:out value="${user.login}"/></a></span><br>
                                                                <span class="card-title"><a
                                                                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALBUM_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID}=${album.id}"><c:out value="${album.name}"/></a></span>
                                                                <input type="hidden"
                                                                       name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID}"
                                                                       value="${album.id}">
                                                                <button class="btn waves-effect waves-light right"
                                                                        type="submit"
                                                                        name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                                                        value="${CommandType.DELETE_ALBUM_COMMAND}"
                                                                        onclick="deleteAlbum()">
                                                                    <fmt:message key="album.track.delete"/>
                                                                    <i class="material-icons right">highlight_off</i>
                                                                </button>
                                                            </form>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </div>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <fmt:message key="admin.no.albums"/>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                            <th>
                                <form action="${pageContext.request.contextPath}/"
                                      method="post">
                                    <input type="hidden" name="userId" value="${user.id}">
                                    <button class="btn waves-effect waves-light right" type="submit"
                                            name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                            value="${CommandType.DELETE_USER_COMMAND}">
                                        <fmt:message key="profile.user.delete"/>
                                        <i class="material-icons right">highlight_off</i>
                                    </button>
                                </form>
                            </th>
                        </tr>

                    </c:forEach>
                    </tbody>
                </table>
            </ul>
        </div>
    </div>
</div>


<footer:footer page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ADMIN_USER_PAGE_COMMAND}"
               policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
               help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"/>
<script>
    function deleteAlbum() {
        M.toast({html: 'Album has been deleted'})
    };
    function updateRoles() {
        M.toast({html: 'User roles have been updated'})
    };

    function deleteTrack() {
        M.toast({html: 'Track has been deleted'})
    };

    function deleteUser() {
        M.toast({html: 'User has been deleted'})
    };

    function changeRole() {
        M.toast({html: 'Roles have been changed'})
    };
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('.collapsible');
        var instances = M.Collapsible.init(elems, options);
    });
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('select');
        var instances = M.FormSelect.init(elems, options);
    });

</script>
</body>
</html>
