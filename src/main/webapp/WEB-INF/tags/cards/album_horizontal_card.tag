<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="command" type="com.soundcloud.command.CommandType" required="true"
                         description="Command name to submit"/>
<jsp:directive.attribute name="user" type="com.soundcloud.user.UserDto" required="true"
                         description="Command name to submit"/>
<jsp:directive.attribute name="album" type="com.soundcloud.album.Album" required="true"
                         description="Command name to submit"/>
<jsp:directive.attribute name="track" type="com.soundcloud.track.Track" required="true"
                         description="Command name to submit"/>


<li class="collection-item avatar grey darken-2 white-text">
    <img src="data:image/jpg;base64,${album.albumIconBase64}"
         alt="artist_icon" class="materialboxed" width="50">
    <span class="card-title" name = "artist_name" id="${user.id}">${user.login}</span>
    <span class="card-title" name = "album_name" id="${track}">${track.name}</span>
    <p>
        <i class="tiny material-icons ">play_arrow</i>
        <i>${track.playsAmount}</i>
    </p>
    <p>
        <i class="tiny material-icons ">favorite_border</i>
        <i>${album.likesAmount}</i>
    </p>
    <a href="${pageContext.request.contextPath}?command=${command}" class="secondary-content"><i class="material-icons">favorite_border</i></a>
</li>
