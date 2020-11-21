<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.soundcloud.application.ApplicationPage" %>
<%@taglib prefix="footer" tagdir="/WEB-INF/tags/footer" %>
<%@taglib prefix="card" tagdir="/WEB-INF/tags/cards" %>
<%@taglib prefix="link" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="track" tagdir="/WEB-INF/tags/track" %>
<%@ page import="com.soundcloud.command.CommandType" %>
<%@ page import="com.soundcloud.album.AlbumGenre" %>
<%@ page import="com.soundcloud.album.AlbumState" %>
<%@ page import="com.soundcloud.application.ApplicationConstants" %>
<%@taglib prefix="lang" tagdir="/WEB-INF/tags/language" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
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
    <c:forEach items="${album.trackList}" var="track">
        <track:album_page_playlist_track album="${album}" track="${track}" user="${user}"/>
    </c:forEach>

</ul>

<jsp:include page="navbar.jsp"/>
<a href="#" data-target="slide-out" class="sidenav-trigger white-text"><em class="material-icons">playlist_play</em></a>

<div class="row ">
    <div class="col s10 offset-l1 white-text">
        <div class="col s3">
            <div class="card hoverable">
                <div class="card-image">
                    <img src="data:image/jpg;base64,${album.albumIconBase64}">
                    <c:if test="${authorized}">
                        <c:if test="${not isOwn}">
                            <c:choose>
                                <c:when test="${isLiked}">
                                    <form method="post" action="${pageContext.request.contextPath}/">
                                        <input type="hidden" name="userName" value="${user}">
                                        <input type="hidden" name="albumId" value="${album.id}">
                                        <input type="hidden" name="likesAmount" value="${album.likesAmount}">
                                        <button class="btn-floating halfway-fab waves-effect waves-light teal lighten-1"
                                                type="submit"
                                                name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                                value="${CommandType.DELETE_LIKE_ALBUM_COMMAND}"><i
                                                class="material-icons"
                                                onclick="unlike()">favorite</i>
                                        </button>

                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" action="${pageContext.request.contextPath}/">
                                        <input type="hidden" name="userName" value="${user}">
                                        <input type="hidden" name="albumId" value="${album.id}">
                                        <input type="hidden" name="likesAmount" value="${album.likesAmount}">
                                        <button class="btn-floating halfway-fab waves-effect waves-light teal lighten-1"
                                                type="submit"
                                                name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                                value="${CommandType.SAVE_LIKE_ALBUM_COMMAND}"><i class="material-icons"
                                                                                                  onclick="like()">favorite_border</i>
                                        </button>

                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:if>
                </div>
                <div class="card-content  grey darken-1">
                    <span class="card-title">${album.name}</span>
                    <p><fmt:message key="album.artist"/>${album.artistName}</p>
                    <p><fmt:message key="album.release"/>${album.releaseDate}</p>
                    <p><fmt:message key="album.likes"/>${album.likesAmount}</p>
                    <p><fmt:message key="album.genre"/>${album.albumGenre}</p>
                    <p><fmt:message key="album.state"/>${album.albumState}</p>
                </div>
            </div>
        </div>
        <ul class="collection with-header" style="border: none">
            <li class="collection-header grey darken-2 container left">
                <h5><fmt:message key="upload.form.album.tracks"/></h5></li>
            <li>
                <c:forEach items="${album.trackList}" var="track">
                    <track:album_page_track album="${album}" track="${track}" user="${user}"/>
                </c:forEach>
                <c:if test="${isOwn}">
                    <div>
                        <a class="waves-effect waves-light btn modal-trigger" href="#modal1"><fmt:message
                                key="profile.edit"/> </a>
                    </div>
                    <div id="modal1" class="modal grey darken-2 white-text">
                        <div class="modal-content">
                            <form action="${pageContext.request.contextPath}/" method="post">
                                <h4><fmt:message key="album.information"/></h4>
                                <input type="hidden" name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID}"
                                       value="${album.id}">
                                <button class="btn waves-effect waves-light right" type="submit"
                                        name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                        value="${CommandType.DELETE_ALBUM_COMMAND}"
                                        onclick="deleteAlbum()">
                                    <fmt:message key="album.track.delete"/>
                                    <i class="material-icons right">highlight_off</i>
                                </button>
                            </form>
                            <form action="${pageContext.request.contextPath}/" method="post"
                                  enctype="multipart/form-data">
                                <div class="row">
                                    <div class="input-field col s12">
                                        <input id="album_name"
                                               name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_NAME}"
                                               type="text"
                                               class="validate" value="${album.name}" required>
                                        <label for="album_name"><fmt:message key="upload.form.album.name"/></label>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12 white-text">
                                        <select id="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_GENRE}"
                                                name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_GENRE}" required>
                                            <option class="white-text" value="${album.albumGenre.field}" disabled
                                                    selected>
                                                <fmt:message
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
                                        <select class="white-text"
                                                id="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_STATE}"
                                                name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_STATE}" required>
                                            <option class="white-text" value="${album.albumState.field}" disabled
                                                    selected>
                                                <fmt:message
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
                                                       placeholder="<fmt:message key="upload.form.album.icon.upload"/>"
                                                       value="data:image/jpg;base64,${album.albumIconBase64}">
                                            </div>
                                            <div class="file-path-wrapper">
                                                <input class="white-text file-path validate" type="text">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <input type="hidden" name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID}"
                                       value="${album.id}">
                                <button class="btn waves-effect waves-light" type="submit"
                                        name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                        value="${CommandType.EDIT_ALBUM_COMMAND}">
                                    <fmt:message key="upload.submit"/>
                                    <i class="material-icons right">send</i>
                                </button>
                            </form>
                            <div class="row">
                                <div class="input-field col s12 white-text">
                                    <ul class="collection with-header" style="border-style:none">
                                        <li class="collection-header  grey darken-2 white-text">
                                            <h3><fmt:message key="upload.form.album.tracks"/></h3>
                                        </li>
                                        <c:forEach items="${album.trackList}" var="track">
                                            <form action="${pageContext.request.contextPath}/" method="post">
                                                <input type="hidden"
                                                       name="${ApplicationConstants.REQUEST_PARAMETER_TRACK_ID}"
                                                       value="${track.id}">
                                                <input type="hidden"
                                                       name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID}"
                                                       value="${album.id}">

                                                <li class="collection-item container grey darken-2">
                                                    <div>${track.name}
                                                        <button class="btn waves-effect waves-light right" type="submit"
                                                                name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                                                value="${CommandType.DELETE_TRACK_COMMAND}"
                                                                onclick="deleteTrack()">
                                                            <fmt:message key="album.track.delete"/>
                                                            <i class="material-icons right">highlight_off</i>
                                                        </button>
                                                    </div>
                                                </li>
                                            </form>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s12 white-text">
                                    <div class="file-field input-field white-text">
                                        <form action="${pageContext.request.contextPath}/" method="post"
                                              enctype="multipart/form-data">
                                            <input type="hidden"
                                                   name="${ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID}"
                                                   value="${album.id}">

                                            <div class="btn">
                                                <span><fmt:message key="upload.form.album.new.tracks"/></span>
                                                <label for="audio"></label>
                                                <input type="file" id="audio" name="audio" accept="audio/*"
                                                       placeholder="<fmt:message key="upload.text"/>" multiple required>
                                            </div>
                                            <div class="file-path-wrapper">
                                                <input class="white-text file-path validate" type="text">
                                            </div>
                                            <button class="btn waves-effect waves-light right" type="submit"
                                                    name="${ApplicationConstants.COMMAND_NAME_PARAM}"
                                                    value="${CommandType.UPLOAD_TRACK_COMMAND}"
                                                    onclick="uploadTracks()">
                                                <fmt:message key="upload.submit"/>
                                                <i class="material-icons right">send</i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </li>
        </ul>
    </div>

    </ul>
</div>
</div>
<script>
    function deleteTrack() {
        M.toast({html: 'Track was deleted from album'})
    };
    function uploadTracks() {
        M.toast({html: 'Tracks have been uploaded'})
    };
    function deleteAlbum() {
        M.toast({html: 'Album has been deleted'})
    };

    function unlike() {
        M.toast({html: 'Album was removed from favourites'})
    };

    function like() {
        M.toast({html: 'Album was added to favourites'})
    };
</script>
<footer:footer  policy="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.POLICY}"
                help="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.HELP}"
        page="${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_ALBUM_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_ALBUM_ID}=${album.id}"/>
</body>
</html>

