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
<%@taglib prefix="subscription" tagdir="/WEB-INF/tags/subscription" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<!DOCTYPE html>
<html xml:lang="en">
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <script src="static/js/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="static/js/custom.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body style="background-color:#424242;">
<c:choose>
    <c:when test="${not empty requestScope.get('lang')}">
        <fmt:setLocale value="${requestScope.get('lang')}"/>
    </c:when>
    <c:otherwise>
        <fmt:setLocale value="${cookie['lang'].value}"/>
    </c:otherwise>
</c:choose>
<fmt:setBundle basename="/i18n/ApplicationMessages" scope="application"/>

<nav>
    <c:set var="url"
           value="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.LIBRARY}&lang="/>
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
                    href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ARTIST_ALBUMS_COMMAND}"><fmt:message
                    key="navbar.home"/></a></li>
            <li class="active"><a
                    href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_USER_LIBRARY_COMMAND}"><fmt:message
                    key="navbar.library"/></a></li>
            <li>
                <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_UPLOAD_PAGE_COMMAND}"><fmt:message
                        key="navbar.upload"/></a>
            </li>
            <c:choose>
                <c:when test="${authorized}">
                    <li><a class="dropdown-trigger" href="#!" data-target="dropdown1">
                        <fmt:message key="navbar.profile"/><i class="material-icons right">more_vert</i></a></li>
                </c:when>
                <c:otherwise>
                    <li><a class="dropdown-trigger" href="#!" data-target="dropdown1">
                        <fmt:message key="navbar.login"/><i class="material-icons right">more_vert</i></a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
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

</nav>
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
        <h5><fmt:message key="home.playlist"/></h5>
    </li>
    <li>
        <div class="divider"></div>
    </li>
    <c:forEach items="${user.userAlbums}" var="album">
        <c:forEach items="${album.trackList}" var="track">
            <track:track album="${album}" track="${track}" user="${user}"/>
        </c:forEach>
    </c:forEach>


</ul>
<div class="row">
    <div class="col s10 offset-l1 white-text">
        <ul class="tabs tabs-transparent grey darken-2 ">
            <li class="tab col s3"><a class="active" href="#albums"><fmt:message key="profile.album"/></a></li>
            <li class="tab col s3"><a href="#tracks"><fmt:message key="profile.track"/></a></li>
            <li class="tab col s3"><a href="#follows"><fmt:message key="profile.follows"/></a></li>
            <li class="tab col s3"><a href="#followers"><fmt:message key="profile.followers"/></a></li>
        </ul>
    </div>
</div>

<div id="albums" class="row">
    <div class="col s10 offset-l1 white-text">
        <div class="col s12 m8 l9 grey darken-2">
            <div class="col s10 offset-l1 section">
                <h3><fmt:message key="library.albums"/></h3>
                <div class="divider"></div>
            </div>
            <p class="col s10 offset-l1 section">
            </p>
            <div class="col s10 offset-l1">
                <c:choose>
                    <c:when test="${not empty user.userAlbums}">
                        <c:forEach items="${user.userAlbums}" var="album">
                            <div class="col s3">
                                <div class="card hoverable">
                                    <div class="card-image">
                                        <img src="data:image/jpg;base64,${album.albumIconBase64}">
                                    </div>
                                    <div class="card-content  grey darken-1">
                                <span class="card-title"><a
                                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}">${user.login}</a></span>
                                        <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALBUM_COMMAND}&albumId=${album.id}">
                                            <p><c:out value="${album.name}"/></p></a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <subscription:subscription_check subscribe="${subscribe}"
                                                         uploadPage="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_UPLOAD_PAGE_COMMAND}"
                                                         profilePage="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"

                        />
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
        <div class="col s12 m4 l3 grey darken-2">
            <ul class="collection with-header" style="border-style:none">
                <li class="collection-header  grey darken-2 white-text">
                    <h6><fmt:message key="home.recommendation.tracks"/></h6>
                </li>
                <c:forEach items="${user.userLikedAlbums}" var="album">
                    <li class="collection-item avatar grey darken-2 white-text">
                        <img src="data:image/jpg;base64,${album.albumIconBase64}"
                             alt="artist_icon" class="circle">
                        <span class="card-title"><a
                                href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}">${album.artistName}</a></span>
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
    </div>
</div>
<div id="tracks" class="row">
    <div class="col s10 offset-l1 white-text">
        <div class="col s12 m8 l9 grey darken-2">
            <div class="col s10 offset-l1 section">

                <h3><fmt:message key="library.tracks"/></h3>
                <div class="divider"></div>
            </div>
            <p class="col s10 offset-l1 section">
            </p>
            <div class="col s10 offset-l1">

                <c:choose>
                    <c:when test="${not empty user.userAlbums}">
                        <c:forEach items="${user.userAlbums}" var="album">
                            <c:forEach items="${album.trackList}" var="track">
                                <div class="col s3">
                                    <div class="card hoverable">
                                        <div class="card-image">
                                            <img src="data:image/jpg;base64,${album.albumIconBase64}">
                                            <a id="audiobut"
                                               class="btn-floating halfway-fab waves-effect waves-light teal lighten-1"><i
                                                    class="material-icons songPlay"
                                                    data-link="${track.trackPath}"
                                                    data-name='${user.login} - ${track.name}'
                                                    cover="data:image/jpg;base64,${album.albumIconBase64}">play_arrow</i></a>
                                        </div>
                                        <div class="card-content  grey darken-1">
                                    <span class="card-title"><a
                                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}">${user.login}</a></span>
                                            <p><c:out value="${track.name}"/></p>
                                        </div>
                                    </div>
                                    <audio id="${track.id}" src="${track.trackPath}" preload="auto">
                                    </audio>
                                </div>
                            </c:forEach>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <subscription:subscription_check subscribe="${subscribe}"
                                                         uploadPage="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_UPLOAD_PAGE_COMMAND}"
                                                         profilePage="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"/>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="col s12 m4 l3 grey darken-2">
            <ul class="collection with-header" style="border-style:none">
                <li class="collection-header  grey darken-2 white-text">
                    <h6><fmt:message key="home.recommendation.tracks"/></h6>
                </li>
                <c:forEach items="${user.userLikedAlbums}" var="album">
                    <li class="collection-item avatar grey darken-2 white-text">
                        <img src="data:image/jpg;base64,${album.albumIconBase64}"
                             alt="artist_icon" class="circle">
                        <span class="card-title"><a
                                href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}"><c:out
                                value="${album.artistName}"/></a></span>
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
    </div>
</div>
<div id="followers" class="row">
    <div class="col s10 offset-l1 white-text">
        <div class="col s12 m8 l9 grey darken-2">
            <div class="row">
                <ul class="collection with-header" style="border-style:none">
                    <li class="collection-header  grey darken-2 white-text">
                        <h3><fmt:message key="profile.followers"/></h3>
                    </li>
                    <c:forEach items="${user.followersUser}" var="follower">
                        <li class="collection-item avatar grey darken-2 white-text">
                            <img src="data:image/jpg;base64,${follower.userIcon}"
                                 alt="artist_icon" class="circle" width="150">
                            <span class="card-title"><a
                                    href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${follower.artistId}"><c:out
                                    value="${follower.login}"/></a></span>
                            <p>
                                <i class="tiny material-icons ">group</i>
                                <i><c:out value="${follower.userFollowers}"/></i>
                            </p>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="col s12 m4 l3 grey darken-2">
            <ul class="collection with-header" style="border-style:none">
                <li class="collection-header  grey darken-2 white-text">
                    <h6><fmt:message key="home.recommendation.tracks"/></h6>
                </li>
                <c:forEach items="${user.userLikedAlbums}" var="album">
                    <li class="collection-item avatar grey darken-2 white-text">
                        <img src="data:image/jpg;base64,${album.albumIconBase64}"
                             alt="artist_icon" class="circle">
                        <span class="card-title"><a
                                href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}"><c:out
                                value="${album.artistName}"/></a></span>
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
    </div>
</div>
<div id="follows" class="row">
    <div class="col s10 offset-l1 white-text">
        <div class="col s12 m8 l9 grey darken-2">
            <div class="row">
                <ul class="collection with-header" style="border-style:none">
                    <li class="collection-header  grey darken-2 white-text">
                        <h3><fmt:message key="profile.follows"/></h3>
                    </li>
                    <c:forEach items="${user.followsUser}" var="follow">
                        <li class="collection-item avatar grey darken-2 white-text">
                            <img src="data:image/jpg;base64,${follow.userIcon}"
                                 alt="artist_icon" class="circle">
                            <span class="card-title"><a
                                    href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${follow.artistId}"><c:out
                                    value="${follow.login}"/></a></span>
                            <p>
                                <i class="tiny material-icons ">group</i>
                                <i><c:out value="${follow.userFollowers}"/></i>
                            </p>
                            <a href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.UNFOLLOW_USER_COMMAND}&${ApplicationConstants.REQUEST_FOLLOW_USER_ID}=${follow.id}&${ApplicationConstants.REQUEST_FOLLOWER_USER_AMOUNT}=${follow.userFollowers}"
                               onclick="unfollow()" class="secondary-content"><i
                                    class="material-icons">highlight_off</i></a>

                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div class="col s12 m4 l3 grey darken-2">
            <ul class="collection with-header" style="border-style:none">
                <li class="collection-header  grey darken-2 white-text">
                    <h6><fmt:message key="home.recommendation.tracks"/></h6>
                </li>
                <c:forEach items="${user.userLikedAlbums}" var="album">
                    <li class="collection-item avatar grey darken-2 white-text">
                        <img src="data:image/jpg;base64,${album.albumIconBase64}"
                             alt="artist_icon" class="circle">
                        <span class="card-title"><a
                                href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}"><c:out
                                value="${album.artistName}"/></a> </span>
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
    </div>

</div>

<footer:footer
        policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
        help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
        page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_USER_LIBRARY_COMMAND}"/>
<script>
    function unfollow() {
        M.toast({html: 'User was removed from following'})
    };
</script>
</body>
</html>
