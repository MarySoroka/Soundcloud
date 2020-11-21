<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:directive.attribute name="album" type="com.soundcloud.album.Album" required="true"
                         description="Command name to submit"/>
<jsp:directive.attribute name="track" type="com.soundcloud.track.Track" required="true"
                         description="track"/>
<jsp:directive.attribute name="user" type="com.soundcloud.user.UserDto" required="true"
                         description="user"/>
<div class="col s3">
    <div class="card hoverable">
        <div class="card-image">
            <img src="data:image/jpg;base64,${album.albumIconBase64}">
            <a href=""${pageContext.request.contextPath}/?${ApplicationConstants.COMMAND_NAME_PARAM}=${CommandType.GET_PAGE_COMMAND}&${ApplicationConstants.REQUEST_PARAMETER_PAGE}=${ApplicationPage.PROFILE}"" id="${track.id}" class="btn-floating waves-effect right waves-light teal lighten-1"
               onclick="like(${track.id})"><i class="material-icons">favorite_border</i></a>
            <a id="audiobutton" class="btn-floating halfway-fab waves-effect waves-light teal lighten-1"><i
                    class="material-icons songPlay" data-link="${track.trackPath}"
                    data-name='${user.login} - ${track.name}' cover="data:image/jpg;base64,${album.albumIconBase64}">play_arrow</i></a>

        </div>
        <div class="card-content  grey darken-1">
            <span class="card-title">${user.login}</span>
            <p>${track.name}</p>
        </div>
    </div>
    <audio id="${track.id}" src="${track.trackPath}" preload="auto">
    </audio>
    <script>
        function like(trackId) {
            const myAudio = document.getElementById(trackId);
            if (myAudio.classList.contains('white-text')) {
               myAudio.classList.remove('white-text');
            } else {
                myAudio.classList.add('white-text');
            }
        };
    </script>
</div>
