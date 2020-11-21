<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="card" tagdir="/WEB-INF/tags/cards" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="track" tagdir="/WEB-INF/tags/track" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<%@taglib prefix="lang" tagdir="/WEB-INF/tags/language" %>
<%@taglib prefix="payment" tagdir="/WEB-INF/tags/payment" %>
<%@taglib prefix="error" tagdir="/WEB-INF/tags/result_message" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@taglib prefix="subscription" tagdir="/WEB-INF/tags/subscription" %>
<jsp:useBean id="securityContext" scope="application" class="com.soundcloud.security.SecurityContext"/>
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
                    <li><a
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
                            <li class="active"><a class="dropdown-trigger" href="#!" data-target="dropdown1">
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
                        <div id="topbarseach">
                            <div class="input-field col s6 s12 white-text">
                                <form action="${pageContext.request.contextPath}/" method="get">
                                    <i class="white-text material-icons prefix">search</i>

                                    <input placeholder="" type="hidden"
                                           name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                           value="${CommandType.SEARCH_COMMAND}">
                                    <input type="text" placeholder="Enter artist, album or track"
                                           name="${ApplicationConstants.REQUEST_PARAMETER_SEARCH_DATA}"
                                           id="autocompete-input"
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


    <c:forEach items="${user.userAlbums}" var="album">
        <c:forEach items="${album.trackList}" var="track">
            <track:track album="${album}" track="${track}" user="${user}"/>
        </c:forEach>
    </c:forEach>


</ul>
<div class="row">

    <div class="col s10 offset-l1 card horizontal section collection-item avatar grey darken-2  white-text"
         style="box-shadow: none">
        <div class="section">
            <img src="data:image/jpg;base64,${user.userIcon}"
                 alt="artist_icon" class="circle" width="300">
        </div>
        <div class="card-stacked">
            <div class="card-content">
                <h3 class="white-text">${user.login}</h3>
            </div>
            <div class="card-content">
                <h6><fmt:message key="profile.followers"/><a
                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_USER_LIBRARY_COMMAND}#followers"><c:out
                        value="${user.userFollowers}"/></a>
                </h6>
                <h6><fmt:message key="profile.follows"/><a
                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_USER_LIBRARY_COMMAND}#follows"><c:out
                        value="${user.userFollows}"/></a>
                </h6>

            </div>
        </div>
        <div class="fixed-action-btn">
            <a class="btn-floating btn-large">
                <i class="large material-icons">menu</i>
            </a>
            <ul>
                <li><a class="btn-floating red lighten-2 modal-trigger tooltipped" data-position="top"
                       data-tooltip="Edit profile information" href="#edit"><i class="material-icons">mode_edit</i></a>
                </li>
                <c:if test="${not isAdmin}">
                    <li><a class="btn-floating yellow lighten-2 modal-trigger tooltipped" data-position="top"

                           data-tooltip="Subscribe management" href="#subscribe"><i
                            class="material-icons">music_note</i></a></li>
                    <li><a class="btn-floating green lighten-2 modal-trigger tooltipped" data-position="top"
                           data-tooltip="Logout" href="#logout"><i class="material-icons">directions_run</i></a></li>
                </c:if>
            </ul>
        </div>
        <c:if test="${isCurrentUser or isAdmin}">

            <div>
                <error:error errors="${errors}"/>
                <div id="subscribe" class="modal grey darken-2">
                    <div class="modal-content">
                        <div class="col s10 offset-l1">
                            <h3><fmt:message key="profile.payment.title"/></h3>
                            <div class="divider"></div>
                            <c:if test="${subscription!=null}">
                                <p><fmt:message key="profile.subscription.status"/><span
                                        class="teal-text lighten-2"><c:out
                                        value="${subscription.subscriptionStatus}"/></span></p>
                                <p><fmt:message key="profile.subscription.date"/><span
                                        class="teal-text lighten-2"><c:out
                                        value="${subscription.subscriptionDate}"/></span></p>
                                <form action="${pageContext.request.contextPath}/" method="post">
                                    <input type="hidden" name="${ApplicationConstants.REQUEST_PARAMETER_USER_ID}"
                                           value="${user.id}">
                                    <button class="btn waves-effect waves-light row" type="submit"
                                            name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                            value="${CommandType.UNSUBSCRIBE_USER_COMMAND}">
                                        <fmt:message key="profile.unsubscribe"/>
                                    </button>
                                </form>
                            </c:if>
                        </div>

                        <form action="${pageContext.request.contextPath}/" method="post">
                            <c:choose>
                                <c:when test="${subscription==null}">
                                    <div class="col s10 offset-l1 ">
                                        <payment:subscribe_payment_card subscription="${subscription}"/>
                                        <input type="hidden" name="${ApplicationConstants.REQUEST_PARAMETER_USER_ID}"
                                               value="${user.id}">
                                        <button class="btn waves-effect waves-light right white-text" type="submit"
                                                name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                                value="${CommandType.SUBSCRIBE_USER_COMMAND}">
                                            <fmt:message key="profile.subscribe"/>
                                        </button>
                                    </div>

                                </c:when>
                                <c:otherwise>

                                    <div class="col s10 offset-l1 ">
                                        <payment:refill_payment_card subscription="${subscription}"/>
                                        <input type="hidden" name="${ApplicationConstants.REQUEST_PARAMETER_WALLET_ID}"
                                               value="${user.walletId}">
                                        <button class="btn waves-effect waves-light row white-text" type="submit"
                                                name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                                value="${CommandType.REFILL_WALLET_BALANCE_COMMAND}">
                                            <fmt:message key="profile.payment.refill"/>
                                        </button>
                                    </div>

                                </c:otherwise>
                            </c:choose>
                        </form>
                    </div>
                </div>
            </div>


            <c:if test="${not isAdmin}">
                <div id="logout" class="modal grey darken-2 modal-fixed-footer">
                    <div class="modal-content">
                        <h4><fmt:message key="profile.logout.text"/></h4>
                        <p><fmt:message key="profile.logout.description"/></p>
                    </div>
                    <div class="modal-footer grey darken-2 white-text">
                        <form method="post" action="${pageContext.request.contextPath}/">
                            <input type="hidden" name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                   value="${CommandType.LOGOUT_USER_COMMAND}">
                            <button class="left btn waves-effect waves-light btn-flat" type="submit"
                                    name="${CommandType.LOGOUT_USER_COMMAND}">
                                <fmt:message key="navbar.logout"/>
                            </button>
                        </form>
                        <a href="#!" class="btn right modal-close waves-effect waves-light btn-flat"><fmt:message
                                key="profile.logout.no"/></a>
                    </div>
                </div>

            </c:if>
            <div id="edit" class="modal grey darken-2 white-text">
                <div class="modal-content">
                    <form action="${pageContext.request.contextPath}/" method="post">
                        <h4 class="col s10 offset-l1 white-text"><fmt:message key="profile.information"/></h4>
                        <input type="hidden" name="${ApplicationConstants.REQUEST_PARAMETER_USER_ID}"
                               value="${user.id}">
                        <button class="btn waves-effect waves-light right" type="submit"
                                name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                value="${CommandType.DELETE_USER_COMMAND}">
                            <fmt:message key="profile.user.delete"/>
                            <i class="material-icons right">highlight_off</i>
                        </button>
                    </form>
                    <form action="${pageContext.request.contextPath}/" method="post" enctype="multipart/form-data">
                        <div class="col s10 offset-l1 white-text">
                            <input type="hidden" name="${ApplicationConstants.REQUEST_PARAMETER_USER_ID}"
                                   value="${user.id}">
                            <div class="row">
                                <div class="input-field col s12">
                                    <input id="login" name="${ApplicationConstants.REQUEST_PARAMETER_LOGIN}"
                                           type="text" value="${user.login}"
                                           class="validate" required>
                                    <label for="login"><fmt:message key="registry.login"/></label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12 white-text">
                                    <input id="email" name="${ApplicationConstants.REQUEST_PARAMETER_EMAIL}"
                                           type="text" value="${user.email}"
                                           class="validate" required>
                                    <label for="email"><fmt:message key="registry.email"/></label>
                                    <label><fmt:message key="registry.email"/></label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12 white-text">
                                    <div class="file-field input-field">
                                        <div class="btn">
                                            <span><fmt:message key="profile.icon"/> </span>
                                            <label for="image"></label>
                                            <input type="file" id="image" name="image" accept="image/*"
                                                   placeholder="<fmt:message key="profile.icon"/>"
                                            >
                                        </div>
                                        <div class="file-path-wrapper">
                                            <input class="white-text file-path validate" type="text">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <input type="hidden" name="password" value="${user.password}">
                            <div class="row">
                                <button class="btn waves-effect waves-light" type="submit"
                                        name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                        value="${CommandType.EDIT_PROFILE_INFORMATION_COMMAND}" onclick="saveChanges()">
                                    <fmt:message key="upload.submit"/>
                                    <i class="material-icons right">send</i>
                                </button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>

        </c:if>
    </div>

    <div class="row">
        <div class="col s10 offset-l1  white-text">
            <ul class="tabs tabs-transparent grey darken-2">
                <li class="tab"><a class="active"
                                   href="#all">
                    <fmt:message key="profile.all"/> </a>
                </li>
                <li class="tab"><a
                        href="#tracks">
                    <fmt:message key="profile.track"/></a></li>
                <li class="tab"><a
                        href="#albums">
                    <fmt:message key="profile.album"/></a></li>
            </ul>
        </div>
    </div>


    <div id="all" class="row">
        <div class="col s10 offset-l1 white-text">
            <div class="col s12 grey darken-2">
                <div class="col s10 offset-l1 section">
                    <h3><fmt:message key="profile.album"/></h3>
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
                                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}"><c:out
                                        value="${user.login}"/></a></span>
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
                <div class="col s10 offset-l1 section">
                    <h3><fmt:message key="profile.track"/></h3>
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
                                                <a id="audiobutton"
                                                   class="btn-floating halfway-fab waves-effect waves-light teal lighten-1"><i
                                                        class="material-icons songPlay" data-link="${track.trackPath}"
                                                        data-name='${user.login} - ${track.name}'
                                                        cover="data:image/jpg;base64,${album.albumIconBase64}">play_arrow</i></a>
                                            </div>
                                            <div class="card-content  grey darken-1">
                                    <span class="card-title"><a
                                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}"><c:out
                                            value="${user.login}"/></a></span>
                                                <a class="teal lighten-2"
                                                   href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALBUM_COMMAND}&albumId=${album.id}">
                                                    <p><c:out value="${album.name}"/></p></a>
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
                                                             profilePage="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"

                            />
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <div id="tracks" class="row">
        <div class="col s10 offset-l1 white-text">
            <div class="col s12  grey darken-2">
                <div class="col s10 offset-l1 section">
                    <h3><fmt:message key="profile.track"/></h3>
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
                                                <a id="audiobutto"
                                                   class="btn-floating halfway-fab waves-effect waves-light teal lighten-1"><i
                                                        class="material-icons songPlay" data-link="${track.trackPath}"
                                                        data-name='${user.login} - ${track.name}'
                                                        cover="data:image/jpg;base64,${album.albumIconBase64}">play_arrow</i></a>
                                            </div>
                                            <div class="card-content  grey darken-1">
                                    <span class="card-title"><a
                                            href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}"><c:out
                                            value="${user.login}"/></a></span>
                                                <a class="teal lighten-2"
                                                   href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALBUM_COMMAND}&albumId=${album.id}">
                                                    <p><c:out value="${album.name}"/></p></a>
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
                                                             profilePage="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"

                            />
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <div id="albums" class="row">
        <div class="col s10 offset-l1 white-text">
            <div class="col s12 grey darken-2">
                <div class="col s10 offset-l1 section">
                    <h3><fmt:message key="profile.album"/></h3>
                    <div class="divider"></div>
                </div>
                <p class="col s10 offset-l1 section">
                <p/>
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
                                        href="${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&artist_id=${album.artistId}"><c:out
                                        value="${user.login}"/></a></span>
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
        </div>
    </div>

    <footer:footer
            policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
            help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
            page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALL_USER_ALBUMS_COMMAND}&${ApplicationConstants.REQUEST_ARTIST_ID}=${user.artistId}"/>
</div>
<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('.modal');
        var instances = M.Modal.init(elems, options);
    });

    function saveChanges() {
        M.toast({html: 'Changes of profile information have been saved'})
    };
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('.fixed-action-btn');
        var instances = M.FloatingActionButton.init(elems, {
            direction: 'left',
            hoverEnabled: false
        });
    });
</script>

</body>
</html>
