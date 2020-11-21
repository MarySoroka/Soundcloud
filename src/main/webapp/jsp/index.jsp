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

<!DOCTYPE html>
<html>
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
                    </li>
                    <li class="active"><a
                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}"><fmt:message
                            key="navbar.home"/></a></li>
                    <li><a
                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_USER_LIBRARY_COMMAND}"><fmt:message
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
                                <fmt:message key="navbar.login"/><i class="material-icons right">more_vert</i></a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </c:when>
        <c:otherwise>
            <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}"
               class="brand-logo left"><em class="material-icons">soundcloud_icon</em></a>
                <ul class="grey darken-3 right hide-on-med-and-down">
                    <li>
                        <div id="topbarsearh">
                            <div class="input-field col s6 s12 white-text">
                                <form action="${pageContext.request.contextPath}/" method="get">
                                    <i class="white-text material-icons prefix">search</i>

                                    <input placeholder="" type="hidden"
                                           name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                           value="${CommandType.SEARCH_COMMAND}">
                                    <input type="text" placeholder="Enter artist, album or track"
                                           name="${ApplicationConstants.REQUEST_PARAMETER_SEARCH_DATA}"
                                           id="autocomplete-inpu"
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
            <li class="active">
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

<a href="#" data-target="slide-out" class="sidenav-trigger white-text"><em class="material-icons">playlist_play</em></a>
<ul id="slide-out" class="sidenav grey darken-3 white-text">
    <li>
        <div class="background">
            <img id="cover"
                 src="https://images.assetsdelivery.com/compings_v2/alekseyvanin/alekseyvanin1704/alekseyvanin170401312.jpg"
                 class="materialboxed container section" width="200">
        </div>
    </li>
    <li class="player musicplayer col s12 accent-1 ">
        <div class="col s12v center-align">
            <span class="title"></span>
            <span class="right duration"></span>
            <input type="range" id="duration" min="0" value="0"/>
            <span class="left current"></span>
            </form>
        </div>
        <div class="col s12 center controls">
            <a href="#" id="repeat" class="material-icons">repeat</a>
            <a href="#" id="prev" class="material-icons">skip_previous</a>
            <a href="#" id="play" class="material-icons">play_circle_outline</a>
            <a href="#" id="next" class="material-icons">skip_next</a>
            <a href="#" id="volume" class="material-icons">volume_up</a>
        </div>
    </li>

    <li class="collection-header">
        <h5><fmt:message key="home.playlist"/></h5></li>
    <li>
        <div class="divider"></div>
    </li>

    <c:choose>
        <c:when test="${not empty users}">
            <c:forEach items="${users}" var="user">
                <c:forEach items="${user.userAlbums}" var="album">
                    <c:forEach items="${album.trackList}" var="track">
                        <track:track album="${album}" track="${track}" user="${user}"/>
                    </c:forEach>
                </c:forEach>
            </c:forEach>
        </c:when>
    </c:choose>


</ul>

<div class="row ">
    <div class="col s10 offset-l1">
        <div class="col s12 m8 l9 grey darken-2">
            <c:choose>
                <c:when test="${not empty users}">
                    <c:forEach items="${users}" var="user">
                        <c:forEach items="${user.userAlbums}" var="album">
                            <c:forEach items="${album.trackList}" var="track">
                                <div class="col s3">
                                    <div class="card hoverable">
                                        <div class="card-image">
                                            <img src="data:image/jpg;base64,${album.albumIconBase64}">
                                            <a id="audiobutton"
                                               class="btn-floating halfway-fab waves-effect waves-light teal lighten-1"><i
                                                    class="material-icons songPlay" data-link="${track.trackPath}"
                                                    data-name='${user.login} - ${track.name}'
                                                    cover="data:image/jpg;base64,${album.albumIconBase64}">play_arrow</i></a>
                                        </div>
                                        <div class="card-content  grey darken-1">
                                            <span class="card-title"><a
                                                    href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}">
                                                    ${user.login}</a></span>
                                            <a class="teal lighten-2"
                                               href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALBUM_COMMAND}&albumId=${album.id}">
                                                <p>${album.name}</p></a>
                                            <p>${track.name}</p>
                                        </div>
                                    </div>
                                    <audio id="${track.id}" src="${track.trackPath}" preload="auto">
                                    </audio>

                                </div>

                            </c:forEach>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>

        </div>
        <c:choose>
            <c:when test="${authorized and not isAdmin}">
                <div class="col s12 m4 l3 grey darken-2">
                    <ul class="collection with-header" style="border-style:none">
                        <li class="collection-header grey darken-2 white-text">
                            <h6><fmt:message key="home.recommendation.users"/></h6>
                        </li>
                        <c:forEach items="${notFollowUsers}" var="user">
                            <li class="collection-item avatar grey darken-2 white-text">
                                <img src="data:image/jpg;base64,${user.userIcon}"
                                     alt="artist_icon" class="circle">
                                <span class="card-title"><a
                                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${user.artistId}">
                                        <c:out value="${user.login}"/></a></span>
                                <p>
                                    <i class="tiny material-icons ">group</i>
                                    <i><c:out value="${user.userFollowers}"/></i>
                                </p>
                                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.FOLLOW_USER_COMMAND}&${ApplicationConstants.REQUEST_FOLLOW_USER_ID}=${user.id}&${ApplicationConstants.REQUEST_FOLLOWER_USER_AMOUNT}=${user.userFollowers}"
                                   onclick="follow()" class="secondary-content"><i class="material-icons">person_add</i></a>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="collection with-header" style="border-style:none">
                        <li class="collection-header grey darken-2 white-text">
                            <h6><fmt:message key="home.recommendation.tracks"/></h6>
                        </li>
                        <c:forEach items="${user.userLikedAlbums}" var="album">
                            <li class="collection-item avatar grey darken-2 white-text">
                                <img src="data:image/jpg;base64,${album.albumIconBase64}"
                                     alt="artist_icon" class="circle">
                                <span class="card-title"><a
                                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}">
                                        <c:out value="${album.artistName}"/></a></span>
                                <a class="teal lighten-2"
                                   href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALBUM_COMMAND}&albumId=${album.id}">
                                    <p><c:out value="${album.name}"/></p></a>
                                <p>
                                    <i class="tiny material-icons white-text ">favorite</i>
                                    <i><c:out value="${album.likesAmount}"/></i>
                                </p>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </c:when>
        </c:choose>
    </div>
</div>
<script>
    function like(trackId) {
        const myAudio = document.getElementById(trackId);
        if (myAudio.classList.contains('white-text')) {
            myAudio.classList.remove('white-text');
        } else {
            myAudio.classList.add('white-text');
        }
    };

    function follow() {
        M.toast({html: 'User is followed'})
    };
</script>
<footer:footer
        policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
        help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
        page=""/>
</body>
</html>
